package khome.calling

import io.ktor.util.KtorExperimentalAPI
import khome.calling.errors.DomainNotFoundException
import khome.calling.errors.ServiceNotFoundException
import khome.core.ServiceCallInterface
import khome.core.koin.KhomeKoinComponent
import khome.core.boot.servicestore.ServiceStoreInterface
import kotlinx.coroutines.ObsoleteCoroutinesApi
import org.koin.core.get

internal typealias ServiceCallMutator<T> = T.() -> Unit

@KtorExperimentalAPI
@ObsoleteCoroutinesApi
abstract class EntityIdOnlyServiceCall(
    domain: DomainInterface,
    service: ServiceInterface
) : EntityBasedServiceCall(domain, service) {

    fun entityId(id: String) {
        serviceData.apply {
            entityId = id
        }
    }

    override val serviceData: EntityBasedServiceDataInterface =
        EntityIdOnlyServiceData()
}

class EntityIdOnlyServiceData : EntityBasedServiceDataInterface {
    override var entityId: String? = ""
}

/**
 * The base class to build the payload for home-assistant websocket api calls.
 * @see callService
 *
 * @property domain One of the from Khome supported domains [Domain].
 * @property service One of the services that are available for the given [domain].
 */
@KtorExperimentalAPI
@ObsoleteCoroutinesApi
abstract class ServiceCall(
    val domain: DomainInterface,
    val service: ServiceInterface
) : KhomeKoinComponent, ServiceCallInterface {
    override var id: Int = 0
    private val type: String = "call_service"

    @Transient
    private val serviceStore: ServiceStoreInterface = get()

    @Transient
    private val _domain = domain.toString().toLowerCase()

    @Transient
    private val _service = service.toString().toLowerCase()

    init {
        if (_domain !in serviceStore)
            throw DomainNotFoundException("ServiceDomain: \"$_domain\" not found in homeassistant Services")
        if (!serviceStore[_domain]!!.contains(_service))
            throw ServiceNotFoundException("Service: \"${_service}service\" not found under domain: \"${_domain}\"in homeassistant Services")
    }
}

abstract class EntityBasedServiceCall(
    domain: DomainInterface,
    service: ServiceInterface
) : ServiceCall(domain, service) {
    abstract val serviceData: EntityBasedServiceDataInterface
}

/**
 * Main entry point to create new domain enum classes
 */
interface DomainInterface

/**
 * Main entry point to create new service enum classes
 */
interface ServiceInterface

/**
 * Main entry point to create own service data classes
 */
interface ServiceDataInterface

abstract class EntityBasedServiceData : EntityBasedServiceDataInterface {
    override var entityId: String? = null
}

interface EntityBasedServiceDataInterface {
    var entityId: String?
}

/**
 * Domains that are supported from Khome
 */
enum class Domain : DomainInterface {
    SENSOR, SUN, COVER, LIGHT, HOMEASSISTANT, MEDIA_PLAYER, NOTIFY, PERSISTENT_NOTIFICATION, INPUT_BOOLEAN, INPUT_NUMBER
}
