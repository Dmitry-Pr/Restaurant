package presentation.input

interface Menu {
    fun showMainMenu(): String
    fun showRegistrationMenu(): String
    fun showMealsMenu(): String
    fun showOrdersMenu(): String
    fun showStatisticsMenu(): String
    fun showFeedbackMenu(): String

}