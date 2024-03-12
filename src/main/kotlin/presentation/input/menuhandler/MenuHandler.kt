package presentation.input.menuhandler

import data.Session
import data.user.Role
import domain.Error
import domain.Success
import presentation.input.commands.CommandsHandler
import presentation.input.menu.AdminMenuState
import presentation.input.menu.Menu
import presentation.input.menu.UserMenuState
import presentation.model.OutputModel

enum class Sections {
    Registration, Main, Meals, Orders, Statistics, Feedback
}

interface MenuHandler {
    fun handleMenu()
    fun getMenu(): Menu
    fun getCommandsHandler(): CommandsHandler
    fun start()
    var finish: Boolean
    var current: Sections
}

class MenuHandlerImpl(
    private val commandsHandler: CommandsHandler,
    private val menu: Menu,
) : MenuHandler {
    override var finish = false
    override var current = Sections.Registration
    private val commands = mutableMapOf<Sections, () -> Unit>()
    private var menuHandlerState: MenuHandlerState = UserMenuHandlerState(this)

    init {
        commands[Sections.Registration] = ::registrationCommand
        commands[Sections.Main] = ::mainCommand
        commands[Sections.Meals] = ::mealsCommand
        commands[Sections.Orders] = ::ordersCommand
        commands[Sections.Statistics] = ::statisticsCommand
        commands[Sections.Feedback] = ::feedbackCommand
    }

    override fun handleMenu() {
        commands[current]?.invoke()
    }

    override fun getMenu() = menu

    override fun getCommandsHandler() = commandsHandler

    override fun start() {
        val res = commandsHandler.start()
        if (res is Error) {
            println(res.outputModel.message)
        }
    }

    private fun mainCommand() {
        menuHandlerState.mainCommand()
    }

    private fun registrationCommand() {
        println(menu.showRegistrationMenu().message)
        val input = readln()
        when (input) {
            "log in" -> {
                when (val res = commandsHandler.logIn()) {
                    is Success -> {
                        menuHandlerState = when (Session.currentUserRole) {
                            Role.Admin -> {
                                menu.changeState(AdminMenuState(menu))
                                AdminMenuHandlerState(this)
                            }

                            Role.User -> {
                                menu.changeState(UserMenuState(menu))
                                UserMenuHandlerState(this)
                            }
                        }
                        current = Sections.Main
                    }

                    is Error -> println(res.outputModel.message)
                }
            }

            "sign up" -> {
                when (val res = commandsHandler.signUp()) {
                    is Success -> {
                        current = Sections.Main
                        menuHandlerState = when (Session.currentUserRole) {
                            Role.Admin -> {
                                menu.changeState(AdminMenuState(menu))
                                AdminMenuHandlerState(this)
                            }

                            Role.User -> {
                                menu.changeState(UserMenuState(menu))
                                UserMenuHandlerState(this)
                            }
                        }
                    }

                    is Error -> println(res.outputModel.message)
                }
            }

            "exit" -> finish = true
            else -> {
                println(OutputModel("Incorrect command").message)
            }
        }
    }

    private fun mealsCommand() {
        menuHandlerState.mealsCommand()
    }

    private fun ordersCommand() {
        menuHandlerState.ordersCommand()
    }

    private fun statisticsCommand() {
        menuHandlerState.statisticsCommand()
    }

    private fun feedbackCommand() {
        menuHandlerState.feedbackCommand()
    }
}