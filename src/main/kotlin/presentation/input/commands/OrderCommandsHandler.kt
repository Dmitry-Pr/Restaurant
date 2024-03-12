package presentation.input.commands

import domain.Result
import presentation.model.OutputModel

interface OrderCommandsHandler {
    fun addOrder(): OutputModel
    fun removeOrder(): OutputModel
    fun getOrder(): OutputModel
    fun getOrderByUser(): OutputModel
    fun addMealToOrder(): OutputModel
    fun removeMealFromOrder(): OutputModel
    fun getOrderDuration(): OutputModel
    fun payForOrder(): Result
    fun startCooking(): OutputModel
    fun stopCooking(): OutputModel
    fun getAllOrders(): OutputModel
}