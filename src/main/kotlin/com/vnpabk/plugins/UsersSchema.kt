package com.vnpabk.plugins

import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import kotlinx.serialization.Serializable
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*

@Serializable
data class Car(
    val id: Int? = null,
    val no: Int? = null,
    val name: String? = null,
    val vehicleCategory: String? = null,
    val driverName: String? = null,
    val numberPlate: String? = null,
    val currentStatus: Boolean? = null,
    val usageStatus: String? = null,
    val usageYear: String? = null
)

class CarService(private val database: Database) {
    object Cars : Table() {
        val id = integer("id").autoIncrement()
        val no = integer("no").default(0).nullable()
        val name = varchar("name", length = 50).nullable()
        val vehicleCategory = varchar("vehicleCategory", length = 50).nullable()
        val driverName = varchar("driverName", length = 50).nullable()
        val numberPlate = varchar("numberPlate", length = 20).nullable()
        val currentStatus = bool("currentStatus").default(false).nullable()
        val usageStatus = text("usageStatus").default("Thông số kỹ thuật tốt").nullable()
        val usageYear = varchar("usageYear", length = 25).default("2024").nullable()
        override val primaryKey = PrimaryKey(id)
    }

    init {
        transaction(database) {
            SchemaUtils.create(Cars)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    suspend fun create(car: Car): Int = dbQuery {
        Cars.insert {
            it[name] = car.name
            it[no] = car.no
            it[vehicleCategory] = car.vehicleCategory
            it[driverName] = car.driverName
            it[numberPlate] = car.numberPlate
            it[currentStatus] = car.currentStatus
            it[usageStatus] = car.usageStatus
            it[usageYear] = car.usageYear
        }[Cars.id]
    }

    suspend fun read(id: Int): Car? {
        return dbQuery {
            Cars.select { Cars.id eq id }
                .map {
                    Car(
                        it[Cars.id],
                        it[Cars.no],
                        it[Cars.name],
                        it[Cars.vehicleCategory],
                        it[Cars.driverName],
                        it[Cars.numberPlate],
                        it[Cars.currentStatus],
                        it[Cars.usageStatus],
                        it[Cars.usageYear]
                    )
                }
                .singleOrNull()
        }
    }

    suspend fun all(): List<Car>{
        return dbQuery { Cars.selectAll().map { Car(
            it[Cars.id],
            it[Cars.no],
            it[Cars.name],
            it[Cars.vehicleCategory],
            it[Cars.driverName],
            it[Cars.numberPlate],
            it[Cars.currentStatus],
            it[Cars.usageStatus],
            it[Cars.usageYear]
        ) } }
    }

    suspend fun update(id: Int, car: Car): Boolean {
        return dbQuery {
            Cars.update({ Cars.id eq id }) { upCar ->
                car.name?.let { upCar[name] = car.name }
                car.no?.let { upCar[no] = car.no }
                car.vehicleCategory?.let { upCar[vehicleCategory] = car.vehicleCategory }
                car.driverName?.let { upCar[driverName] = car.driverName }
                car.numberPlate?.let { upCar[numberPlate] = car.numberPlate }
                car.currentStatus?.let { upCar[currentStatus] = car.currentStatus }
                car.usageStatus?.let { upCar[usageStatus] = car.usageStatus }
                car.usageYear?.let { upCar[usageYear] = car.usageYear }
            } > 0
        }
    }

    suspend fun delete(id: Int) {
        dbQuery {
            Cars.deleteWhere { Cars.id.eq(id) }
        }
    }
}
