package domain.order

import presentation.model.OutputModel

class PaidState(
    orderController: OrderController
) : State(
    orderController
) {
    override fun getOrder(id: Int): OutputModel {
        return orderController.getOrderById(id)?.let {
            OutputModel(
                "Order id: ${it.id}\n" +
                        "Meals: ${it.meals}\n" +
                        "State: ${it.state}"
            )
        } ?: OutputModel("Order with id $id does not exist")
    }

    override fun addMeal(id: Int, mealId: Int, amount: Int): OutputModel {
        return OutputModel("You can't add a meal to the paid order")
    }

    override fun removeMeal(id: Int, mealId: Int, amount: Int): OutputModel {
        return OutputModel("You can't remove a meal from the paid order")
    }

    override fun getDuration(id: Int): OutputModel {
        return OutputModel("The order is prepared")
    }

    override fun removeOrder(id: Int): OutputModel {
        return OutputModel("You can't remove the paid order")
    }

    override fun isPaid(id: Int): Boolean {
        return true
    }

    override fun pay(id: Int): OutputModel {
        return OutputModel("The order with id $id is already paid")
    }

    override fun startCooking(id: Int): OutputModel {
        return OutputModel("The order with id $id is already prepared")
    }

    override fun stopCooking(id: Int): OutputModel {
        return OutputModel("The order with id $id is already prepared")
    }

    override fun isReady(id: Int): Boolean {
        return true
    }

    override fun isCooking(id: Int): Boolean {
        return false
    }
}