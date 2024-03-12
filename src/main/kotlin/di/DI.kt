package di

import data.feedback.FeedbackDao
import data.feedback.RuntimeFeedbackDao
import data.meal.MealDao
import data.meal.RuntimeMealDao
import data.order.OrderDao
import data.order.RuntimeOrderDao
import data.user.RuntimeUserDao
import data.user.UserDao
import domain.feedback.FeedbackController
import domain.feedback.FeedbackControllerImpl
import domain.feedback.FeedbackValidator
import domain.feedback.FeedbackValidatorImpl
import domain.meal.MealController
import domain.meal.MealControllerImpl
import domain.meal.MealValidator
import domain.meal.MealValidatorImpl
import domain.order.OrderController
import domain.order.OrderControllerImpl
import domain.user.*
import presentation.input.commands.CommandsHandler
import presentation.input.commands.CommandsHandlerImpl
import presentation.input.menu.Menu
import presentation.input.menu.MenuImpl
import presentation.input.menuhandler.MenuHandler
import presentation.input.menuhandler.MenuHandlerImpl

object DI {
    private val menu: Menu
        get() = MenuImpl()
    private val mealDao: MealDao by lazy {
        RuntimeMealDao()
    }
    private val userDao: UserDao by lazy {
        RuntimeUserDao()
    }
    private val orderDao: OrderDao by lazy {
        RuntimeOrderDao()
    }
    private val feedbackDao: FeedbackDao by lazy {
        RuntimeFeedbackDao()
    }
    private val loginValidator: UserValidator
        get() {
            val fieldsValidator = UserFieldsValidator()
            val loginValidator = UserLoginValidator(userDao)
            fieldsValidator.link(fieldsValidator, loginValidator)
            return fieldsValidator
        }

    private val registrationValidator: UserValidator
        get() {
            val fieldsValidator = UserFieldsValidator()
            val doesNotExistsValidator = UserDoesNotExistsValidator(userDao)
            val passwordValidator = UserPasswordValidator()
            fieldsValidator.link(fieldsValidator, doesNotExistsValidator, passwordValidator)
            return fieldsValidator
        }
    private val mealValidator: MealValidator
        get() = MealValidatorImpl()
    private val feedbackValidator: FeedbackValidator
        get() = FeedbackValidatorImpl()
    private val mealController: MealController
        get() = MealControllerImpl(
            mealDao = mealDao,
            mealValidator = mealValidator
        )
    private val userController: UserController
        get() = UserControllerImpl(
            userDao = userDao,
            loginValidator = loginValidator,
            registrationValidator = registrationValidator
        )
    private val orderController: OrderController
        get() = OrderControllerImpl(
            orderDao = orderDao,
            mealDao = mealDao,
            mealController = mealController,
        )
    private val feedbackController: FeedbackController
        get() = FeedbackControllerImpl(
            feedbackDao = feedbackDao,
            mealDao = mealDao,
            orderDao = orderDao,
            feedbackValidator = feedbackValidator
        )
    private val commandsHandler: CommandsHandler
        get() = CommandsHandlerImpl(
            userController = userController,
            mealController = mealController,
            orderController = orderController,
            feedbackController = feedbackController,
        )
    val menuHandler: MenuHandler
        get() = MenuHandlerImpl(
            commandsHandler = commandsHandler,
            menu = menu
        )
}