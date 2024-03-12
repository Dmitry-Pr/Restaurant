package presentation.input.menu

import presentation.model.OutputModel

class AdminMenuState(
    menu: Menu
) : MenuState(menu) {
    override fun showMainMenu(): OutputModel {
        val res =
            """Main menu. Enter
            |"meals" To go to meals menu
            |"orders" To go to orders menu
            |"feedback" To go to feedback menu
            |"statistics" To go to statistics menu
            |"log out" In order to log out
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
            |"add" In order to add a meal
            |"change name" In order to change a meal name
            |"change price" In order to change a meal price
            |"change duration" In order to change a meal duration
            |"change amount" In order to change a meal amount
            |"show all" In order to see the list of meals
            |"exit" In order to exit to main menu
        """.trimMargin()
        return OutputModel(res)
    }

    override fun showOrdersMenu(): OutputModel {
        val res =
            """Orders menu. Enter
            |"show all" In order to see the list of orders
            |"exit" In order to exit to main menu
        """.trimMargin()
        return OutputModel(res)
    }

    override fun showStatisticsMenu(): OutputModel {
        val res =
            """Statistics menu. Enter
            |"show all meals" In order to see the list of meals
            |"show all orders" In order to see the list of orders
            |"show all feedback" In order to see the list of feedback
            |"get feedback by rating" In order to get feedback by rating
            |"get mean rating by meal" In order to get mean rating by meal
            |"exit" In order to exit to main menu
        """.trimMargin()
        return OutputModel(res)
    }

    override fun showFeedbackMenu(): OutputModel {
        val res =
            """Feedback menu. Enter
            |"show all" In order to see the list of feedback
            |"show by meal" In order to see feedback by meal
            |"show by user" In order to see feedback by user
            |"exit" In order to exit to main menu
        """.trimMargin()
        return OutputModel(res)
    }
}