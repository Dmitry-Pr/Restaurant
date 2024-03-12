package domain.order

import domain.Error
import domain.Result
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import presentation.model.OutputModel
import kotlin.time.Duration
import kotlin.time.toKotlinDuration

class InProgressState(
    orderController: OrderController
) : State(orderController) {

    override fun getOrder(id: Int): OutputModel {
        return orderController.getOrderById(id)?.let {
            OutputModel(
                "Order id: ${it.id}\n" +
                        "Meals: ${it.meals}\n" +
                        "Total price: ${it.totalPrice}\n" +
                        "State: ${it.state}" +
                        "StartCookingTime: ${it.startedOn}\n" +
                        "TimeLeft: ${
                            it.duration - java.time.Duration.between(
                                java.time.LocalDateTime.now(),
                                it.startedOn!!.toJavaLocalDateTime()
                            ).toKotlinDuration()
                        }"
            )
        } ?: OutputModel("Order with id $id does not exist")
    }

    override fun addMeal(id: Int, mealId: Int, amount: Int): OutputModel {
        return OutputModel("You can't add a meal to the order that is being prepared")
    }

    override fun removeMeal(id: Int, mealId: Int, amount: Int): OutputModel {
        return OutputModel("You can't remove a meal from the order that is being prepared")
    }

    override fun getDuration(id: Int): OutputModel {
        return orderController.getOrderById(id)?.let {
            OutputModel(
                "Time left: ${
                    it.duration - java.time.Duration.between(
                        java.time.LocalDateTime.now(),
                        it.startedOn!!.toJavaLocalDateTime()
                    ).toKotlinDuration()
                }"
            )
        } ?: OutputModel("Order with id $id does not exist")
    }

    override fun removeOrder(id: Int): OutputModel {
        return OutputModel("You can't remove the order that is being prepared")
    }

    override fun isPaid(id: Int): Boolean {
        return false
    }

    override fun pay(id: Int): Result {
        return Error(OutputModel("You can't pay for the order that is being prepared, wait until it's ready"))
    }

    override fun startCooking(id: Int): OutputModel {
        return OutputModel("The order with id $id is already being prepared")
    }

    override fun stopCooking(id: Int): OutputModel {
        val order = orderController.getOrderById(id) ?: return OutputModel("Order with id $id does not exist")
        orderController.setTimeStart(id, null)
        orderController.stopJob(id)
        orderController.changeState(CreatedState(orderController))
        return OutputModel("The order with id $id is not being prepared anymore")
    }

    override fun isReady(id: Int): Boolean {
        return false
    }

    override fun isCooking(id: Int): Boolean {
        return true
    }

}