package presentation.input

import presentation.model.OutputModel

interface Menu {
    fun showMainMenu(): OutputModel
    fun showRegistrationMenu(): OutputModel
    fun showMealsMenu(): OutputModel
    fun showOrdersMenu(): OutputModel
    fun showStatisticsMenu(): OutputModel
    fun showFeedbackMenu(): OutputModel
}

class MenuImpl: Menu {
    override fun showMainMenu(): OutputModel {
        TODO("Not yet implemented")
    }

    override fun showRegistrationMenu(): OutputModel {
        TODO("Not yet implemented")
    }

    override fun showMealsMenu(): OutputModel {
        TODO("Not yet implemented")
    }

    override fun showOrdersMenu(): OutputModel {
        TODO("Not yet implemented")
    }

    override fun showStatisticsMenu(): OutputModel {
        TODO("Not yet implemented")
    }

    override fun showFeedbackMenu(): OutputModel {
        TODO("Not yet implemented")
    }

}