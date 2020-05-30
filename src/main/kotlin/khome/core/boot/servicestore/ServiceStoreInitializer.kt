package khome.core.boot.servicestore

import khome.KhomeSession
import khome.core.boot.StartSequenceStep
import khome.communicating.CALLER_ID
import mu.KotlinLogging

internal class ServiceStoreInitializer (
    override val khomeSession: KhomeSession,
    private val serviceStore: ServiceStoreInterface
) : StartSequenceStep {
    private val logger = KotlinLogging.logger { }
    private val servicesRequest =
        ServicesRequest(CALLER_ID.incrementAndGet())

    override suspend fun runStartSequenceStep() {
        sendServicesRequest()
        logger.info { "Requested registered homeassistant services" }
        storeServices(consumeServicesResponse())
    }

    private suspend fun consumeServicesResponse() =
        khomeSession.consumeSingleMessage<ServicesResponse>()

    private suspend fun sendServicesRequest() =
        khomeSession.callWebSocketApi(servicesRequest)

    private fun storeServices(servicesResponse: ServicesResponse) =
        servicesResponse.let { response ->
            when (response.success) {
                false -> logger.error { "Could not fetch services from homeassistant" }
                true -> {
                    response.result.forEach { (domain, services) ->
                        val serviceList = mutableListOf<String>()
                        services.forEach { (name, _) ->
                            serviceList += name
                            logger.debug { "Fetched service: $name in domain: $domain from homeassistant" }
                        }
                        serviceStore[domain] = serviceList
                    }
                }
            }
            logger.info { "Stored homeassistant services in local service store" }
        }
}
