package domain.order

import presentation.model.OutputModel

class CreatedState(
    orderController: OrderController
) : State(orderController) {
    override fun addOrder(duration: Int, meals: MutableList<Int>, totalPrice: Int): OutputModel {
        TODO("Not yet implemented")
    }

    override fun getOrder(id: Int): OutputModel {
        TODO("Not yet implemented")
    }

    override fun addMeal(id: Int, mealId: Int): OutputModel {
        TODO("Not yet implemented")
    }

    override fun removeMeal(id: Int, mealId: Int): OutputModel {
        TODO("Not yet implemented")
    }

    override fun getDuration(id: Int): OutputModel {
        TODO("Not yet implemented")
    }

    override fun removeOrder(id: Int): OutputModel {
        TODO("Not yet implemented")
    }

    override fun isPaid(id: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun pay(id: Int): OutputModel {
        TODO("Not yet implemented")
    }

    override fun startCooking(id: Int): OutputModel {
        TODO("Not yet implemented")
    }

    override fun isReady(id: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun isCooking(id: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun getAllOrders(): OutputModel {
        TODO("Not yet implemented")
    }
}