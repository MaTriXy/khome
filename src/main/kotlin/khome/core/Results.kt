package khome.core

import com.google.gson.annotations.SerializedName
import khome.core.exceptions.InvalidAttributeValueTypeException
import khome.entities.EntityId
import java.time.OffsetDateTime

typealias StateAttributes = Map<String, Any>

data class ResolverResponse(val id: Int, val type: ResponseType) : MessageInterface
data class StateChangedResponse(val id: Int, val type: ResponseType, val event: EventData) : MessageInterface
data class EventData(
    override val eventType: String,
    val data: Data,
    override val timeFired: OffsetDateTime,
    override val origin: String
) :
    MessageInterface, EventDtoInterface

data class Data(val entityId: String, val oldState: StateResponse?, val newState: StateResponse?) : MessageInterface

interface EventDtoInterface {
    val eventType: String
    val timeFired: OffsetDateTime
    val origin: String
}

data class StateResponse(
    val entityId: EntityId,
    val lastChanged: OffsetDateTime,
    val state: Any,
    val attributes: StateAttributes,
    val lastUpdated: OffsetDateTime
)

data class State<T>(
    val lastChanged: OffsetDateTime,
    val value: T,
    val attributes: StateAttributes,
    val lastUpdated: OffsetDateTime
)

inline fun <reified T> StateAttributes.safeGet(key: String): T {
    return get(key) as? T ?: throw InvalidAttributeValueTypeException(
        "Attribute value for $key is of type: ${(get(key)
            ?: error("Key not valid"))::class}."
    )
}

data class ResultResponse(
    val id: Int,
    val type: String,
    val success: Boolean,
    val error: ErrorResult?,
    val result: Any?
) : MessageInterface

enum class ResponseType {
    @SerializedName("event")
    EVENT,

    @SerializedName("result")
    RESULT
}
