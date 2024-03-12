package domain.order

import kotlinx.datetime.toKotlinLocalDateTime
import presentation.model.OutputModel
import domain.Result
import domain.Error

class CreatedState(
    orderController: OrderController
) : State(orderController) {

    override fun getOrder(id: Int): OutputModel {
        return orderController.getOrderById(id)?.let {
            OutputModel(
                "Order id: ${it.id}\n" +
                        "Meals: ${it.meals}\n" +
                        "Duration: ${it.duration}\n" +
                        "Total price: ${it.totalPrice}\n" +
                        "State: ${it.state}"
            )
        } ?: OutputModel("Order with id $id does not exist")
    }

    override fun addMeal(id: Int, mealId: Int, amount: Int): OutputModel {
        return orderController.addMealById(id, mealId, amount)
    }

    override fun removeMeal(id: Int, mealId: Int, amount: Int): OutputModel {
        return orderController.removeMealById(id, mealId, amount)
    }

    override fun getDuration(id: Int): OutputModel {
        return orderController.getOrderById(id)?.let {
            OutputModel("Duration: ${it.duration}")
        } ?: OutputModel("Order with id $id does not exist")
    }

    override fun removeOrder(id: Int): OutputModel {
        return orderController.removeOrderById(id)
    }

    override fun isPaid(id: Int): Boolean {
        return false
    }

    override fun pay(id: Int): Result {
        return Error(OutputModel("The order with id $id is not prepared yet, you can ask to start cooking"))
    }

    override fun startCooking(id: Int): OutputModel {
        val order = orderController.getOrderById(id) ?: return OutputModel("Order with id $id does not exist")
        if (order.meals.isEmpty()) {
            return OutputModel("The order with id $id does not contain any meal")
        }
        orderController.setTimeStart(id, java.time.LocalDateTime.now().toKotlinLocalDateTime())
        orderController.changeState(InProgressState(orderController))
        orderController.prepare(id)
        return OutputModel("The order with id $id is being prepared")
    }

    override fun stopCooking(id: Int): OutputModel {
        return OutputModel("The order with id $id is not being prepared")
    }

    override fun isReady(id: Int): Boolean {
        return false
    }

    override fun isCooking(id: Int): Boolean {
        return false
    }


}