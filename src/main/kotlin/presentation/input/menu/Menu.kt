package presentation.input.menu

import presentation.model.OutputModel

interface Menu {
    fun showMainMenu(): OutputModel
    fun showRegistrationMenu(): OutputModel
    fun showMealsMenu(): OutputModel
    fun showOrdersMenu(): OutputModel
    fun showStatisticsMenu(): OutputModel
    fun showFeedbackMenu(): OutputModel
    fun changeState(state: MenuState)
}

class MenuImpl: Menu{
    private var menuState: MenuState = UserMenuState(this)
    override fun showMainMenu(): OutputModel {
        return menuState.showMainMenu()
    }

    override fun showRegistrationMenu(): OutputModel {
        val res =
            """Registration menu. Enter
            |"log in" If you already have an account
            |"sign up" In order to make an account
            |"exit" In order to finish program
        """.trimMargin()
        return OutputModel(res)
    }

    override fun showMealsMenu(): OutputModel {
        return menuState.showMealsMenu()
    }

    override fun showOrdersMenu(): OutputModel {
        return menuState.showOrdersMenu()
    }

    override fun showStatisticsMenu(): OutputModel {
        return menuState.showStatisticsMenu()
    }

    override fun showFeedbackMenu(): OutputModel {
        return menuState.showFeedbackMenu()
    }

    override fun changeState(state: MenuState) {
        this.menuState = state
    }
}
