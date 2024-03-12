package presentation.input.menu

import presentation.model.OutputModel

class UserMenuState(
    menu: Menu,
) : MenuState(menu) {
    override fun showMainMenu(): OutputModel {
        val res =
            """Main menu. Enter
            |"meals" To go to meals menu
            |"orders" To go to orders menu
            |"exit" In order to finish program
        """.trimMargin()
        return OutputModel(res)
    }

    override fun showRegistrationMenu(): OutputModel {
        return menu.showRegistrationMenu()
    }

    override fun showMealsMenu(): OutputModel {
        val res =
            """Meals menu. Enter
            |"show all" In order to see the list of meals
            |"exit" In order to exit to main menu
        """.trimMargin()
        return OutputModel(res)
    }

    override fun showOrdersMenu(): OutputModel {
        val res =
            """Orders menu. Enter
            |"show all" In order to see the list of orders
            |"make" In order to make an order
            |"add" In order to add a meal to an order
            |"remove" In order to remove a meal from an order
            |"show" In order to see an order details
            |"cancel" In order to cancel an order
            |"start cooking" In order to start cooking an order
            |"pay" In order to pay for an order
            |"duration" In order to see the duration of an order
            |"stop cooking" In order to stop cooking an order
            |"exit" In order to exit to main menu
        """.trimMargin()
        return OutputModel(res)
    }

    override fun showStatisticsMenu(): OutputModel {
        val res = "Only admin can access this menu"
        return OutputModel(res)
    }

    override fun showFeedbackMenu(): OutputModel {
        val res = """Feedback menu. Enter
            |"add" In order to add a feedback
            |"show my" In order to see your feedback
            |"exit" In order to exit to main menu
        """.trimMargin()
        return OutputModel(res)
    }
}