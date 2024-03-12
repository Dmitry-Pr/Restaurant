package presentation.input.menu

import presentation.model.OutputModel

abstract class MenuState(
    val menu: Menu,
) {
    abstract fun showMainMenu(): OutputModel
    abstract fun showRegistrationMenu(): OutputModel
    abstract fun showMealsMenu(): OutputModel
    abstract fun showOrdersMenu(): OutputModel
    abstract fun showStatisticsMenu(): OutputModel
    abstract fun showFeedbackMenu(): OutputModel
}