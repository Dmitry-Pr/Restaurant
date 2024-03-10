package domain

import presentation.model.OutputModel

sealed class Result

class Success(val outputModel: OutputModel) : Result()
class Error(val outputModel: OutputModel) : Result()