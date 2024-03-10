package data


interface FeedbackDao {
    fun add(mealId: Int, userId: Int, rating: Int, comment: String)
    fun getAll(): List<FeedbackEntity>
    fun get(id: Int): FeedbackEntity?
    fun update(vararg listfeedback: FeedbackEntity)
    fun load(feedback: List<FeedbackEntity>)
}

class RuntimeFeedbackDao : FeedbackDao {
    private val feedback = mutableMapOf<Int, FeedbackEntity>()
    private var counter = 0
    override fun add(mealId: Int, userId: Int, rating: Int, comment: String) {
        val response = FeedbackEntity(
            id = counter,
            mealId = mealId,
            userId = userId,
            rating = rating,
            comment = comment
        )
        feedback[counter] = response
        counter++
    }

    override fun getAll(): List<FeedbackEntity> = feedback.values.toList()

    override fun get(id: Int): FeedbackEntity? = feedback[id]

    override fun update(vararg listfeedback: FeedbackEntity) =
        listfeedback.forEach { response -> feedback[response.id] = response }

    override fun load(feedback: List<FeedbackEntity>) {
        feedback.forEach { add(it.mealId, it.userId, it.rating, it.comment) }
    }

}
