package data

import kotlinx.datetime.LocalDateTime
import kotlin.time.Duration


interface OrderDao {
    fun add(duration: Duration, meals: MutableList<Int>, totalPrice: Int, startedOn: LocalDateTime, state: OrderState)
    fun getAll(): List<OrderEntity>
    fun get(id: Int): OrderEntity?
    fun update(vararg listorder: OrderEntity)
    fun load(orders: List<OrderEntity>)
}

class RuntimeOrderDao : OrderDao {
    private val orders = mutableMapOf<Int, OrderEntity>()
    private var counter = 0
    override fun add(
        duration: Duration,
        meals: MutableList<Int>,
        totalPrice: Int,
        startedOn: LocalDateTime,
        state: OrderState
    ) {
        val order = OrderEntity(
            id = counter,
            duration = duration,
            meals = meals,
            totalPrice = totalPrice,
            startedOn = startedOn,
            state = state
        )
        orders[counter] = order
        counter++
    }

    override fun getAll(): List<OrderEntity> = orders.values.toList()

    override fun get(id: Int): OrderEntity? = orders[id]

    override fun update(vararg listorder: OrderEntity) =
        listorder.forEach { meal -> orders[meal.id] = meal }

    override fun load(orders: List<OrderEntity>) {
        orders.forEach { add(it.duration, it.meals, it.totalPrice, it.startedOn, it.state) }
    }

}
