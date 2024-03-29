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
import kotlin.time.Duration

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
    fun changeState(id: Int, state: State)
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
        val orderMealsAmount = mutableListOf<Int>()
        for (meal in meals) {
            if (mealDao.get(meal.key) == null) {
                answer += "Meal with id ${meal.key} does not exist\n"
            } else if (mealController.checkEnoughMeal(meal.key, meal.value) is Error) {
                answer += "Not enough meal with id ${meal.key} in storage\n"
            } else {
                orderMeals.add(mealDao.get(meal.key)!!)
                orderMealsAmount.add(meal.value)
            }
        }
        if (orderMeals.isEmpty()) {
            return OutputModel("$answer\nCan not create order without meals")
        }
//        val mealsMap = orderMeals.associate { it.id to it.amount }.toMutableMap()
        val mealsMap = mutableMapOf<Int, Int>()
        var totalPrice = 0
        for (i in orderMeals.indices) {
            mealsMap[orderMeals[i].id] = orderMealsAmount[i]
            totalPrice += orderMeals[i].price * orderMealsAmount[i]
        }

        orderDao.add(
            userId = userId,
            duration = orderMeals.sumOf { it.duration.inWholeMinutes }.toDuration(DurationUnit.MINUTES),
            meals = mealsMap,
            totalPrice = totalPrice,
            state = OrderState.Created,
            startedOn = null
        )
        return OutputModel("Order created " + serialize().message)
    }

    override fun getOrder(id: Int): OutputModel {
        updateState(id)
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
        updateState(id)
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
        return OutputModel("Meal added to the order " + serialize().message)
    }

    override fun removeMeal(id: Int, mealId: Int, amount: Int): OutputModel {
        updateState(id)
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
        return OutputModel("Meal removed from the order " + serialize().message)
    }

    override fun getDuration(id: Int): OutputModel {
        updateState(id)
        return state.getDuration(id)
    }

    override fun removeOrder(id: Int): OutputModel {
        updateState(id)
        return state.removeOrder(id)
    }

    override fun removeOrderById(id: Int): OutputModel {
        val order = orderDao.get(id) ?: return OutputModel("Order with id $id does not exist")
        if (order.userId != Session.currentUserId) {
            return OutputModel("You can not remove another user order")
        }
        orderDao.remove(id)
        return OutputModel("Order removed " + serialize().message)
    }

    override fun isPaid(id: Int): Boolean {
        updateState(id)
        return state.isPaid(id)
    }

    override fun pay(id: Int): Result {
        val order = orderDao.get(id) ?: return Error(OutputModel("Order with id $id does not exist"))
        if (order.userId != Session.currentUserId) {
            return Error(OutputModel("You can not pay for another user order"))
        }
        updateState(id)
        return state.pay(id)
    }

    override fun startCooking(id: Int): OutputModel {
        val order = orderDao.get(id) ?: return OutputModel("Order with id $id does not exist")
        if (order.userId != Session.currentUserId) {
            return OutputModel("You can not start cooking another user order")
        }
        updateState(id)
        return state.startCooking(id)
    }

    override fun stopCooking(id: Int): OutputModel {
        val order = orderDao.get(id) ?: return OutputModel("Order with id $id does not exist")
        if (order.userId != Session.currentUserId) {
            return OutputModel("You can not stop cooking another user order")
        }
        updateState(id)
        return state.stopCooking(id)
    }

    override fun isReady(id: Int): Boolean {
        updateState(id)
        return state.isReady(id)
    }

    override fun isCooking(id: Int): Boolean {
        updateState(id)
        return state.isCooking(id)
    }

    override fun getAllOrders(): OutputModel {
        val orders = orderDao.getAll().joinToString("\n")
        return OutputModel(orders).takeIf { it.message.isNotEmpty() } ?: OutputModel("List of orders is empty")
    }

    override fun changeState(id: Int, state: State) {
        this.state = state
        val orderState = when (state) {
            is CreatedState -> OrderState.Created
            is InProgressState -> OrderState.InProgress
            is ReadyState -> OrderState.Ready
            is PaidState -> OrderState.Paid
            else -> OrderState.Created
        }
        serialize()
        orderDao.update(orderDao.get(id)!!.copy(state = orderState))
    }

    override fun setTimeStart(id: Int, time: LocalDateTime?) {
        val order = orderDao.get(id) ?: return
        val newOrder = order.copy(startedOn = java.time.LocalDateTime.now().toKotlinLocalDateTime())
        orderDao.update(newOrder)

    }

    override fun prepare(id: Int) {
        val order = orderDao.get(id)!!
        var flag = false
        val incorrectMeals = order.meals.filter { mealController.decreaseAmount(it.key, it.value) is Error }
        for (meal in incorrectMeals) {
            order.meals.remove(meal.key)
            flag = true
        }
        if (flag) {
            updateOrder(id)
        }
        val obj = this
        val job = CoroutineScope(Dispatchers.Default).launch {
            sleep(order.duration.inWholeMilliseconds)
            changeState(id, ReadyState(obj))
        }
        Jobs.addJob(id, job)
    }

    override fun stopJob(id: Int) {
        Jobs.stopJob(id)
        val order = orderDao.get(id)!!
        for (meal in order.meals) {
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

    private fun updateOrder(id: Int) {
        val order = orderDao.get(id)!!
        var totalPrice = 0
        var duration: Duration = 0.toDuration(DurationUnit.MINUTES)
        for (meal in order.meals) {
            val mealEntity = mealDao.get(meal.key)!!
            totalPrice += mealEntity.price * meal.value
            duration += mealEntity.duration
        }
        val newOrder = order.copy(totalPrice = totalPrice, duration = duration)
        orderDao.update(newOrder)
    }
    private fun updateState(id: Int){
        val order = orderDao.get(id)?: return
        state = when (order.state) {
            OrderState.Created -> CreatedState(this)
            OrderState.InProgress -> InProgressState(this)
            OrderState.Ready -> ReadyState(this)
            OrderState.Paid -> PaidState(this)
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