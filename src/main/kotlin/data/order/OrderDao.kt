package data.order

import kotlinx.datetime.LocalDateTime
import kotlin.time.Duration


interface OrderDao {
    fun add(userId: Int, duration: Duration, meals: MutableMap<Int, Int>, totalPrice: Int, startedOn: LocalDateTime?, state: OrderState)
    fun getAll(): List<OrderEntity>
    fun get(id: Int): OrderEntity?
    fun remove(id: Int)
    fun update(vararg listorder: OrderEntity)
    fun load(orders: List<OrderEntity>)
}

class RuntimeOrderDao : OrderDao {
    private val orders = mutableMapOf<Int, OrderEntity>()
    private var counter = 0
    override fun add(
        userId: Int,
        duration: Duration,
        meals: MutableMap<Int, Int>,
        totalPrice: Int,
        startedOn: LocalDateTime?,
        state: OrderState
    ) {
        val order = OrderEntity(
            id = counter,
            userId = userId,
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
    override fun remove(id: Int) {
        orders.remove(id)
    }

    override fun update(vararg listorder: OrderEntity) =
        listorder.forEach { meal -> orders[meal.id] = meal }

    override fun load(orders: List<OrderEntity>) {
        orders.forEach { add(it.userId, it.duration, it.meals, it.totalPrice, it.startedOn, it.state) }
    }

}
