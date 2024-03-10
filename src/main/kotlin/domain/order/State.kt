package domain.order

abstract class State(
    protected val orderController: OrderController
) : OrderController

