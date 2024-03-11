package presentation.input

import domain.Result
import presentation.model.OutputModel

interface LoginCommandsHandler {
    fun start(): Result
    fun logIn(): Result
    fun signUp(): Result
}
