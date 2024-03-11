package presentation.input

import presentation.model.OutputModel

interface OrderCommandsHandler {
    fun addOrder(): OutputModel
    fun removeOrder(): OutputModel
    fun getOrder(): OutputModel
    fun getOrderByUser(): OutputModel
    fun addMealToOrder(): OutputModel
    fun removeMealFromOrder(): OutputModel
    fun getOrderDuration(): OutputModel
    fun payForOrder(): OutputModel
    fun startCooking(): OutputModel
    fun getAllOrders(): OutputModel
}