package presentation.input.menuhandler

abstract class MenuHandlerState(
    val menuHandler: MenuHandler
) {
    abstract fun mainCommand()
    abstract fun mealsCommand()
    abstract fun ordersCommand()
    abstract fun statisticsCommand()
    abstract fun feedbackCommand()
}