package khome.core.entities.deviceTracker

import khome.calling.ServiceInterface
import khome.core.entities.AbstractEntity

abstract class AbstractDeviceTrackerEntity(deviceEntityName: String) :
    AbstractEntity("device_tracker", deviceEntityName) {
    val isHome = getStateValue<String>() == "home"
    val isAway = getStateValue<String>() == "not_home"
    abstract val notifyIosService: ServiceInterface
}