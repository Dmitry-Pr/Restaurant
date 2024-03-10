package data

import org.example.data.MealEntity
import kotlin.time.Duration


interface MealDao {
    fun add(name: String, amount: Int, price: Int, duration: Duration)
    fun getAll(): List<MealEntity>
    fun get(id: Int): MealEntity?
    fun update(vararg listmeal: MealEntity)
    fun load(meals: List<MealEntity>)
}

class RuntimeMealDao : MealDao {
    private val meals = mutableMapOf<Int, MealEntity>()
    private var counter = 0
    override fun add(name: String, amount: Int, price: Int, duration: Duration) {
        val meal = MealEntity(
            id = counter,
            name = name,
            amount = amount,
            price = price,
            duration = duration
        )
        meals[counter] = meal
        counter++
    }

    override fun getAll(): List<MealEntity> = meals.values.toList()

    override fun get(id: Int): MealEntity? = meals[id]

    override fun update(vararg listmeal: MealEntity) =
        listmeal.forEach { meal -> meals[meal.id] = meal }

    override fun load(meals: List<MealEntity>) {
        meals.forEach { add(it.name, it.amount, it.price, it.duration) }
    }

}
