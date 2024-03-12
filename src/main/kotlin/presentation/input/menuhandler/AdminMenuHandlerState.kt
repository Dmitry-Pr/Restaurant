package presentation.input.menuhandler

import presentation.model.OutputModel
import domain.Error
import domain.Success

class AdminMenuHandlerState(
    menuHandler: MenuHandler,
) : MenuHandlerState(menuHandler) {
    override fun mainCommand() {
        println(menuHandler.getMenu().showMainMenu().message)
        val input = readln()
        when (input) {
            "meals" -> menuHandler.current = Sections.Meals
            "orders" -> menuHandler.current = Sections.Orders
            "feedback" -> menuHandler.current = Sections.Feedback
            "statistics" -> menuHandler.current = Sections.Statistics
            "exit" -> menuHandler.finish = true
            else -> {
                println(OutputModel("Incorrect command").message)
            }
        }
    }

    override fun mealsCommand() {
        println(menuHandler.getMenu().showMealsMenu().message)
        val input = readln()
        val res = when (input) {
            "add" -> menuHandler.getCommandsHandler().addMeal()
            "change name" -> menuHandler.getCommandsHandler().changeMealName()
            "change price" -> menuHandler.getCommandsHandler().changeMealPrice()
            "change duration" -> menuHandler.getCommandsHandler().changeMealDuration()
            "change amount" -> menuHandler.getCommandsHandler().changeMealAmount()
            "show all" -> menuHandler.getCommandsHandler().showAllMeals()
            "exit" -> {
                menuHandler.current = Sections.Main
                return
            }
            else -> {
                OutputModel("Incorrect command")
            }
        }
        println(res.message)
    }

    override fun ordersCommand() {
        println(menuHandler.getMenu().showOrdersMenu().message)
        val input = readln()
        val res = when (input) {
            "show all" -> menuHandler.getCommandsHandler().getAllOrders()
            "exit" -> {
                menuHandler.current = Sections.Main
                return
            }

            else -> {
                OutputModel("Incorrect command")
            }
        }
        println(res.message)
    }

    override fun statisticsCommand() {
        println(menuHandler.getMenu().showStatisticsMenu().message)
        val input = readln()
        val res = when (input) {
            "show all meals" -> menuHandler.getCommandsHandler().showAllMeals()
            "show all orders" -> menuHandler.getCommandsHandler().getAllOrders()
            "show all feedback" -> menuHandler.getCommandsHandler().getAllFeedback()
            "get feedback by rating" -> menuHandler.getCommandsHandler().getAllFeedbackByRating()
            "get mean rating by meal" -> menuHandler.getCommandsHandler().getMeanRatingByMeal()
            "exit" -> {
                menuHandler.current = Sections.Main
                return
            }
            else -> {
                OutputModel("Incorrect command")
            }
        }
        println(res.message)
    }

    override fun feedbackCommand() {
        println(menuHandler.getMenu().showFeedbackMenu().message)
        val input = readln()
        val res = when (input) {
            "show all" -> menuHandler.getCommandsHandler().getAllFeedback()
            "show by meal" -> menuHandler.getCommandsHandler().getFeedbackByMeal()
            "show by user" -> menuHandler.getCommandsHandler().getFeedbackByUser()
            "exit" -> {
                menuHandler.current = Sections.Main
                return
            }
            else -> {
                OutputModel("Incorrect command")
            }
        }
        println(res.message)
    }

}