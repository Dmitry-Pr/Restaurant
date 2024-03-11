package presentation.input

import data.Role
import data.Session
import domain.Result
import domain.Success
import domain.Error
import domain.feedback.FeedbackController
import domain.meal.MealController
import domain.order.OrderController
import domain.user.UserController
import presentation.model.OutputModel

interface CommandsHandler : LoginCommandsHandler, OrderCommandsHandler, MealCommandsHandler, FeedbackCommandsHandler
class CommandsHandlerImpl(
    private val userController: UserController,
    private val orderController: OrderController,
    private val mealController: MealController,
    private val feedbackController: FeedbackController,
    private val session: Session,
) : CommandsHandler {
    override fun addFeedback(): OutputModel {
        println(OutputModel("Enter the feedback in format: mealId; rating; text").message)
        val input = readln().split("; ")
        if (input.size < 3) {
            return OutputModel("Incorrect number of arguments")
        }
        val mealId = input[0].toIntOrNull() ?: return OutputModel("Incorrect mealId format")
        val rating = input[1].toIntOrNull() ?: return OutputModel("Incorrect rating format")
        return feedbackController.addFeedback(session.currentUserId, mealId, rating, input[2])
    }

    override fun getAllFeedback(): OutputModel {
        return feedbackController.getAllFeedback()
    }

    override fun getFeedbackByMeal(): OutputModel {
        println(OutputModel("Enter mealId").message)
        val id = readln().toIntOrNull() ?: return OutputModel("Incorrect mealId format")
        return feedbackController.getFeedbackByMealId(id)
    }

    override fun getFeedbackByUser(): OutputModel {
        println(OutputModel("Enter userId").message)
        val id = readln().toIntOrNull() ?: return OutputModel("Incorrect userId format")
        return feedbackController.getFeedbackByUserId(id)
    }

    override fun getAllFeedbackByRating(): OutputModel {
        return feedbackController.getAllFeedbackByRating()
    }

    override fun getMeanRatingByMeal(): OutputModel {
        println(OutputModel("Enter mealId").message)
        val id = readln().toIntOrNull() ?: return OutputModel("Incorrect mealId format")
        return feedbackController.getMeanRatingByMealId(id)
    }

    override fun start(): Result {
        val resultUsers = userController.deserialize()
        val resultMeals = mealController.deserialize()
        val resultOrders = if (resultMeals is Success) orderController.deserialize()
        else Error(OutputModel("Orders data is invalid now"))
        val resultFeedbacks = if (resultMeals is Success && resultUsers is Success) feedbackController.deserialize()
        else Error(OutputModel("Feedbacks data is invalid now"))
        val results = listOf(resultUsers, resultMeals, resultOrders, resultFeedbacks)
        if (results.all { it is Success }) {
            return Success(OutputModel("All data is valid now"))
        }
        var result = ""
        results.forEach {
            result += if (it is Error) it.outputModel.message + "\n" else ""
        }
        return Error(OutputModel(result))
    }

    override fun logIn(): Result {
        println(OutputModel("Enter login and password in format: login; password").message)
        val input = readln().split("; ")
        if (input.size < 2) {
            return Error(OutputModel("Incorrect number of arguments"))
        }
        return userController.loginUser(input[0], input[1])
    }

    override fun signUp(): Result {
        println(OutputModel("Enter admin if you want to sign up as admin or user if you want to sign up as user").message)
        val inputRole = readln()
        var role: Role = when (inputRole) {
            "admin" -> {
                Role.Admin
            }

            "user" -> {
                Role.User
            }

            else -> {
                return Error(OutputModel("Incorrect role"))
            }
        }
        println(OutputModel("Enter user info in format: name; surname; login; password").message)
        val input = readln().split("; ")
        if (input.size < 4) {
            return Error(OutputModel("Incorrect number of arguments"))
        }
        return userController.addUser(input[0], input[1], input[2], input[3], role)
    }

    override fun addMeal(): OutputModel {
        println(
            OutputModel(
                "Enter the meal in format: name; amount; price; duration;. "
                        + "Duration is in format: nnHnnM"
            ).message
        )
        val input = readln().split("; ")
        if (input.size < 4) {
            return OutputModel("Incorrect number of arguments")
        }
        val name = input[0]
        val amount = input[1].toIntOrNull() ?: return OutputModel("Incorrect amount format")
        val price = input[2].toIntOrNull() ?: return OutputModel("Incorrect price format")
        val duration = "PT" + input[3]
        return mealController.addMeal(name, amount, price, duration)
    }

    override fun changeMealName(): OutputModel {
        println(OutputModel("Enter mealId and newName in format: mealId; newName").message)
        val input = readln().split("; ")
        if (input.size < 2) {
            return OutputModel("Incorrect number of arguments")
        }
        val id = input[0].toIntOrNull() ?: return OutputModel("Incorrect mealId format")
        return mealController.changeName(id, input[1])
    }

    override fun changeMealPrice(): OutputModel {
        println(OutputModel("Enter mealId and newPrice in format: mealId; newPrice").message)
        val input = readln().split("; ")
        if (input.size < 2) {
            return OutputModel("Incorrect number of arguments")
        }
        val id = input[0].toIntOrNull() ?: return OutputModel("Incorrect mealId format")
        val price = input[1].toIntOrNull() ?: return OutputModel("Incorrect price format")
        return mealController.changePrice(id, price)
    }

    override fun changeMealDuration(): OutputModel {
        println(
            OutputModel(
                "Enter mealId and newDuration in format: mealId; duration. "
                        + "Duration is in format: nnHnnM"
            ).message
        )
        val input = readln().split("; ")
        if (input.size < 2) {
            return OutputModel("Incorrect number of arguments")
        }
        val id = input[0].toIntOrNull() ?: return OutputModel("Incorrect mealId format")
        return mealController.changeDuration(id, "PT" + input[1])
    }

    override fun changeMealAmount(): OutputModel {
        println(OutputModel("Enter mealId and newAmount in format: mealId; newAmount").message)
        val input = readln().split("; ")
        if (input.size < 2) {
            return OutputModel("Incorrect number of arguments")
        }
        val id = input[0].toIntOrNull() ?: return OutputModel("Incorrect mealId format")
        val amount = input[1].toIntOrNull() ?: return OutputModel("Incorrect amount format")
        return mealController.changeAmount(id, amount)
    }

    override fun showAllMeals(): OutputModel {
        return mealController.getAllMeals()
    }

    override fun addOrder(): OutputModel {
        println(OutputModel("Enter the order in format: meal1Id, meal1Amount; mealId2, ...").message)
        val input = readln().split("; ")
        val meals = mutableMapOf<Int, Int>()
        for (meal in input) {
            val mealInfo = meal.split(", ")
            if (mealInfo.size < 2) {
                return OutputModel("Incorrect number of arguments")
            }
            val mealId = mealInfo[0].toIntOrNull() ?: return OutputModel("Incorrect mealId format")
            val amount = mealInfo[1].toIntOrNull() ?: return OutputModel("Incorrect amount format")
            meals[mealId] = amount
        }
        return orderController.addOrder(session.currentUserId, meals)
    }

    override fun removeOrder(): OutputModel {
        println(OutputModel("Enter orderId").message)
        val id = readln().toIntOrNull() ?: return OutputModel("Incorrect orderId format")
        return orderController.removeOrder(id)
    }

    override fun getOrder(): OutputModel {
        println(OutputModel("Enter orderId").message)
        val id = readln().toIntOrNull() ?: return OutputModel("Incorrect orderId format")
        return orderController.getOrder(id)
    }

    override fun getOrderByUser(): OutputModel {
        return orderController.getOrdersByUserId(session.currentUserId)
    }
    override fun addMealToOrder(): OutputModel {
        println(OutputModel("Enter orderId, mealId and amount in format: orderId; mealId; amount").message)
        val input = readln().split("; ")
        if (input.size < 3) {
            return OutputModel("Incorrect number of arguments")
        }
        val id = input[0].toIntOrNull() ?: return OutputModel("Incorrect orderId format")
        val mealId = input[1].toIntOrNull() ?: return OutputModel("Incorrect mealId format")
        val amount = input[2].toIntOrNull() ?: return OutputModel("Incorrect amount format")
        return orderController.addMeal(id, mealId, amount)
    }

    override fun removeMealFromOrder(): OutputModel {
        println(OutputModel("Enter orderId, mealId and amount in format: orderId; mealId; amount").message)
        val input = readln().split("; ")
        if (input.size < 3) {
            return OutputModel("Incorrect number of arguments")
        }
        val id = input[0].toIntOrNull() ?: return OutputModel("Incorrect orderId format")
        val mealId = input[1].toIntOrNull() ?: return OutputModel("Incorrect mealId format")
        val amount = input[2].toIntOrNull() ?: return OutputModel("Incorrect amount format")
        return orderController.removeMeal(id, mealId, amount)
    }

    override fun getOrderDuration(): OutputModel {
        println(OutputModel("Enter orderId").message)
        val id = readln().toIntOrNull() ?: return OutputModel("Incorrect orderId format")
        return orderController.getDuration(id)
    }

    override fun payForOrder(): OutputModel {
        println(OutputModel("Enter orderId").message)
        val id = readln().toIntOrNull() ?: return OutputModel("Incorrect orderId format")
        return orderController.pay(id)
    }

    override fun startCooking(): OutputModel {
        println(OutputModel("Enter orderId").message)
        val id = readln().toIntOrNull() ?: return OutputModel("Incorrect orderId format")
        return orderController.startCooking(id)
    }

    override fun stopCooking(): OutputModel {
        TODO("Not yet implemented")
    }

    override fun getAllOrders(): OutputModel {
        return orderController.getAllOrders()
    }

}