package khome.extending.entities.sensors.binary

import khome.KhomeApplication
import khome.entities.Attributes
import khome.entities.devices.Sensor
import khome.extending.entities.SwitchableState
import java.time.Instant

typealias MotionSensor = Sensor<SwitchableState, MotionSensorAttributes>

@Suppress("FunctionName")
fun KhomeApplication.MotionSensor(objectId: String): MotionSensor = BinarySensor(objectId)

data class MotionSensorAttributes(
    override val userId: String?,
    override val friendlyName: String,
    override val lastChanged: Instant,
    override val lastUpdated: Instant
) : Attributes
