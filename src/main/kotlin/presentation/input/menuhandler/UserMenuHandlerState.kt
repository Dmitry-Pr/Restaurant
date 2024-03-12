package presentation.input.menuhandler

import presentation.model.OutputModel
import domain.Error
import domain.Success


class UserMenuHandlerState(
    menuHandler: MenuHandler,
) : MenuHandlerState(menuHandler) {
    override fun mainCommand() {
        println(menuHandler.getMenu().showMainMenu().message)
        val input = readln()
        when (input) {
            "meals" -> menuHandler.current = Sections.Meals
            "orders" -> menuHandler.current = Sections.Orders
            "statistics" -> menuHandler.current = Sections.Statistics
            "log out" -> menuHandler.current = Sections.Registration
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
            "show all" -> menuHandler.getCommandsHandler().getUserOrders()
            "make" -> menuHandler.getCommandsHandler().addOrder()
            "add" -> menuHandler.getCommandsHandler().addMealToOrder()
            "remove" -> menuHandler.getCommandsHandler().removeMealFromOrder()
            "show" -> menuHandler.getCommandsHandler().getOrder()
            "cancel" -> menuHandler.getCommandsHandler().removeOrder()
            "start cooking" -> menuHandler.getCommandsHandler().startCooking()
            "pay" -> {
                when (val paid = menuHandler.getCommandsHandler().payForOrder()) {
                    is Success -> {
                        menuHandler.current = Sections.Feedback
                        paid.outputModel
                    }

                    is Error -> paid.outputModel
                }
            }

            "duration" -> menuHandler.getCommandsHandler().getOrderDuration()
            "stop cooking" -> menuHandler.getCommandsHandler().stopCooking()
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
        menuHandler.current = Sections.Main
    }

    override fun feedbackCommand() {
        println(menuHandler.getMenu().showFeedbackMenu().message)
        val input = readln()
        val res = when (input) {
            "add" -> menuHandler.getCommandsHandler().addFeedback()
            "show my" -> menuHandler.getCommandsHandler().getFeedbackByCurrentUser()
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