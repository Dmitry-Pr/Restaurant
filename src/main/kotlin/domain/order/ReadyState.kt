package domain.order

import data.Session
import domain.Error
import domain.Result
import domain.Success
import presentation.model.OutputModel

class ReadyState(
    orderController: OrderController
) : State(
    orderController
) {
    override fun getOrder(id: Int): OutputModel {
        return orderController.getOrderById(id)?.let {
            OutputModel(
                "Order id: ${it.id}\n" +
                        "Meals: ${it.meals}\n" +
                        "Total price: ${it.totalPrice}\n" +
                        "State: ${it.state}"
            )
        } ?: OutputModel("Order with id $id does not exist")
    }

    override fun addMeal(id: Int, mealId: Int, amount: Int): OutputModel {
        return OutputModel("You can't add a meal to the order that is ready")
    }

    override fun removeMeal(id: Int, mealId: Int, amount: Int): OutputModel {
        return OutputModel("You can't remove a meal from the order that is ready")
    }

    override fun getDuration(id: Int): OutputModel {
        return OutputModel("The order is prepared")
    }

    override fun removeOrder(id: Int): OutputModel {
        return OutputModel("You can't remove the order that is prepared")
    }

    override fun isPaid(id: Int): Boolean {
        return false
    }

    override fun pay(id: Int): Result {
        orderController.changeState(id, PaidState(orderController))
        Session.currentOrderId = id
        return Success(OutputModel("The order with id $id is paid"))
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