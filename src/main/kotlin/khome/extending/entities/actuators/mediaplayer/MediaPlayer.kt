package khome.extending.entities.actuators.mediaplayer

import khome.KhomeApplication
import khome.communicating.ServiceCommandResolver
import khome.entities.Attributes
import khome.entities.EntityId
import khome.entities.State
import khome.entities.devices.Actuator
import khome.extending.entities.Actuator

typealias MediaPlayer<reified S, reified A> = Actuator<S, A>

@Suppress("FunctionName")
inline fun <reified S : State<*>, reified A : Attributes> KhomeApplication.MediaPlayer(
    objectId: String,
    serviceCommandResolver: ServiceCommandResolver<S>
): MediaPlayer<S, A> = Actuator(EntityId(domain = "media_player", objectId = objectId), serviceCommandResolver)
