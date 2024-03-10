package domain.order

import presentation.model.OutputModel

const val ORDER_JSON_PATH = "data/orders.json"

interface OrderController {
    fun addOrder(duration: Int, meals: MutableList<Int>, totalPrice: Int): OutputModel
    fun getOrder(id: Int): OutputModel
    fun addMeal(id: Int, mealId: Int): OutputModel
    fun removeMeal(id: Int, mealId: Int): OutputModel
    fun getDuration(id: Int): OutputModel
    fun removeOrder(id: Int): OutputModel
    fun isPaid(id: Int): Boolean
    fun pay(id: Int): OutputModel
    fun startCooking(id: Int): OutputModel
    fun isReady(id: Int): Boolean
    fun isCooking(id: Int): Boolean
    fun getAllOrders(): OutputModel
}