package domain.order

import data.*
import data.meal.MealDao
import data.meal.MealEntity
import data.order.OrderDao
import data.order.OrderEntity
import data.order.OrderState
import domain.Error
import domain.Result
import domain.Success
import domain.meal.MealController
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import presentation.model.OutputModel
import java.io.File
import java.io.FileNotFoundException
import java.lang.Exception
import java.lang.Thread.sleep
import kotlin.time.DurationUnit
import kotlin.time.toDuration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val ORDERS_JSON_PATH = "data/orders.json"

interface OrderController {
    fun addOrder(userId: Int, meals: MutableMap<Int, Int>): OutputModel
    fun getOrder(id: Int): OutputModel
    fun getOrderById(id: Int): OrderEntity?
    fun getOrdersByUserId(userId: Int): OutputModel
    fun addMeal(id: Int, mealId: Int, amount: Int): OutputModel
    fun addMealById(id: Int, mealId: Int, amount: Int): OutputModel
    fun removeMeal(id: Int, mealId: Int, amount: Int): OutputModel
    fun removeMealById(id: Int, mealId: Int, amount: Int): OutputModel
    fun getDuration(id: Int): OutputModel
    fun removeOrder(id: Int): OutputModel
    fun removeOrderById(id: Int): OutputModel
    fun isPaid(id: Int): Boolean
    fun pay(id: Int): Result
    fun startCooking(id: Int): OutputModel
    fun stopCooking(id: Int): OutputModel
    fun isReady(id: Int): Boolean
    fun isCooking(id: Int): Boolean
    fun getAllOrders(): OutputModel
    fun changeState(state: State)
    fun setTimeStart(id: Int, time: LocalDateTime?)
    fun prepare(id: Int)
    fun stopJob(id: Int)
    fun deserialize(): Result
}

class OrderControllerImpl(
    private val orderDao: OrderDao,
    private val mealDao: MealDao,
    private val mealController: MealController,
) : OrderController {
    private var state: State = CreatedState(this)

    override fun addOrder(userId: Int, meals: MutableMap<Int, Int>): OutputModel {
        var answer = ""
        val orderMeals = mutableListOf<MealEntity>()
        for (meal in meals) {
            if (mealDao.get(meal.key) == null) {
                answer += "Meal with id ${meal.key} does not exist\n"
            } else {
                orderMeals.add(mealDao.get(meal.key)!!)
            }
        }
        if (orderMeals.isEmpty()) {
            return OutputModel("$answer\nCan not create order without meals")
        }
        val mealsMap = orderMeals.associate { it.id to it.amount }.toMutableMap()
        orderDao.add(
            userId = userId,
            duration = orderMeals.sumOf { it.duration.inWholeMinutes }.toDuration(DurationUnit.MINUTES),
            meals = mealsMap,
            totalPrice = orderMeals.sumOf { it.price },
            state = OrderState.Created,
            startedOn = null
        )
        return OutputModel("Order created" + serialize().message)
    }

    override fun getOrder(id: Int): OutputModel {
        return state.getOrder(id)
    }

    override fun getOrderById(id: Int): OrderEntity? {
        return when (orderDao.get(id)) {
            null -> null
            else -> if (orderDao.get(id)!!.userId != Session.currentUserId) null else orderDao.get(id)
        }
    }

    override fun getOrdersByUserId(userId: Int): OutputModel {
        val orders = orderDao.getAll().filter { it.userId == userId }.joinToString("\n")
        return OutputModel(orders).takeIf { it.message.isNotEmpty() } ?: OutputModel("List of orders is empty")
    }

    override fun addMeal(id: Int, mealId: Int, amount: Int): OutputModel {
        return state.addMeal(id, mealId, amount)
    }

    override fun addMealById(id: Int, mealId: Int, amount: Int): OutputModel {
        val order = orderDao.get(id) ?: return OutputModel("Order with id $id does not exist")
        if (order.userId != Session.currentUserId) {
            return OutputModel("You can not add meal to another user order")
        }
        val meal = mealDao.get(mealId) ?: return OutputModel("Meal with id $mealId does not exist")
        if (meal.amount < amount) {
            return OutputModel("It is only ${meal.amount} ${meal.name} left in the storage")
        }
        val meals = order.meals
        if (meals.contains(mealId)) {
            meals[mealId] = meals[mealId]!! + amount
        } else {
            meals[mealId] = amount
        }
        val newOrder = order.copy(
            meals = meals,
            totalPrice = order.totalPrice + meal.price,
            duration = order.duration + meal.duration
        )
        orderDao.update(newOrder)
        return OutputModel("Meal added to the order" + serialize().message)
    }

    override fun removeMeal(id: Int, mealId: Int, amount: Int): OutputModel {
        return state.removeMeal(id, mealId, amount)
    }

    override fun removeMealById(id: Int, mealId: Int, amount: Int): OutputModel {
        val order = orderDao.get(id) ?: return OutputModel("Order with id $id does not exist")
        if (order.userId != Session.currentUserId) {
            return OutputModel("You can not remove meal from another user order")
        }
        val meal = mealDao.get(mealId) ?: return OutputModel("Meal with id $mealId does not exist")
        if (!orderDao.get(id)!!.meals.contains(mealId)) {
            return OutputModel("The meal with id $mealId is not in the order with id $id")
        }
        if (order.meals[mealId]!! < amount) {
            return OutputModel("The order with id $id does not contain $amount ${meal.name}")
        }
        val meals = order.meals
        if (meals.contains(mealId)) {
            meals[mealId] = meals[mealId]!! - amount
        }
        if (meals[mealId] == 0) {
            meals.remove(mealId)
        }
        val newOrder = order.copy(
            meals = meals,
            totalPrice = order.totalPrice - meal.price,
            duration = order.duration - meal.duration
        )
        orderDao.update(newOrder)
        return OutputModel("Meal removed from the order" + serialize().message)
    }

    override fun getDuration(id: Int): OutputModel {
        return state.getDuration(id)
    }

    override fun removeOrder(id: Int): OutputModel {
        return state.removeOrder(id)
    }

    override fun removeOrderById(id: Int): OutputModel {
        val order = orderDao.get(id) ?: return OutputModel("Order with id $id does not exist")
        if (order.userId != Session.currentUserId) {
            return OutputModel("You can not remove another user order")
        }
        orderDao.remove(id)
        return OutputModel("Order removed" + serialize().message)
    }

    override fun isPaid(id: Int): Boolean {
        return state.isPaid(id)
    }

    override fun pay(id: Int): Result {
        val order = orderDao.get(id) ?: return Error(OutputModel("Order with id $id does not exist"))
        if (order.userId != Session.currentUserId) {
            return Error(OutputModel("You can not pay for another user order"))
        }
        return state.pay(id)
    }

    override fun startCooking(id: Int): OutputModel {
        val order = orderDao.get(id) ?: return OutputModel("Order with id $id does not exist")
        if (order.userId != Session.currentUserId) {
            return OutputModel("You can not start cooking another user order")
        }
        return state.startCooking(id)
    }

    override fun stopCooking(id: Int): OutputModel {
        val order = orderDao.get(id) ?: return OutputModel("Order with id $id does not exist")
        if (order.userId != Session.currentUserId) {
            return OutputModel("You can not stop cooking another user order")
        }
        return state.stopCooking(id)
    }

    override fun isReady(id: Int): Boolean {
        return state.isReady(id)
    }

    override fun isCooking(id: Int): Boolean {
        return state.isCooking(id)
    }

    override fun getAllOrders(): OutputModel {
        val orders = orderDao.getAll().joinToString("\n")
        return OutputModel(orders).takeIf { it.message.isNotEmpty() } ?: OutputModel("List of orders is empty")
    }

    override fun changeState(state: State) {
        this.state = state
    }

    override fun setTimeStart(id: Int, time: LocalDateTime?) {
        val order = orderDao.get(id) ?: return
        val newOrder = order.copy(startedOn = java.time.LocalDateTime.now().toKotlinLocalDateTime())
        orderDao.update(newOrder)

    }

    override fun prepare(id: Int) {
        val order = orderDao.get(id)!!
        for(meal in order.meals) {
            mealController.decreaseAmount(meal.key, meal.value)
        }
        val obj = this
        val job = CoroutineScope(Dispatchers.Default).launch {
            sleep(order.duration.inWholeMilliseconds)
            changeState(ReadyState(obj))
        }
        Jobs.addJob(id, job)
    }

    override fun stopJob(id: Int) {
        Jobs.stopJob(id)
        val order = orderDao.get(id)!!
        for(meal in order.meals) {
            mealController.increaseAmount(meal.key, meal.value)
        }
    }

    override fun deserialize(): Result {
        return try {
            val file = File(ORDERS_JSON_PATH)
            val jsonString = file.readText()
            val orders = Json.decodeFromString<List<OrderEntity>>(jsonString)
            orderDao.load(orders)
            Success(OutputModel("Successfully loaded orders data"))
        } catch (ex: FileNotFoundException) {
            Error(OutputModel("The file with orders is not found"))
        } catch (ex: Exception) {
            Error(OutputModel("Unpredicted problem with orders file"))
        }
    }

    private fun serialize(): OutputModel {
        return try {
            val file = File(ORDERS_JSON_PATH)
            val jsonString = Json.encodeToString(orderDao.getAll())
            file.writeText(jsonString)
            OutputModel("Successfully saved orders data")
        } catch (ex: FileNotFoundException) {
            OutputModel("\nThe changes are not saved, orders file is not found")
        } catch (ex: Exception) {
            OutputModel("\nThe changes are not saved, unpredicted problem with saving orders file")
        }
    }

}