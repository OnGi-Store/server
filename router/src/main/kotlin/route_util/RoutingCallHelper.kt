package route_util

import io.ktor.server.plugins.*
import io.ktor.server.routing.*
import java.util.*

internal object RoutingCallHelper {
    fun RoutingCall.getId(paramName: String): UUID {
        val param: String = parameters[paramName] ?: throw BadRequestException("유효한 ${paramName}를 입력해 주세요.")
        return runCatching { UUID.fromString(param) }
            .fold(
                onSuccess = { it },
                onFailure = { throw BadRequestException("유효한 ${paramName}를 입력해 주세요.") }
            )
    }

    fun RoutingCall.getDoubleParam(
        name: String,
        defaultValue: Double
    ): Double = getParam(
        name = name,
        defaultValue = defaultValue,
        convert = { value: String? -> value?.toDoubleOrNull() }
    )

    fun RoutingCall.getIntParam(
        name: String,
        defaultValue: Int
    ): Int = getParam(
        name = name,
        defaultValue = defaultValue,
        convert = { value: String? -> value?.toIntOrNull() }
    )

    inline fun <reified T : Enum<T>> RoutingCall.getEnumParam(
        name: String,
        defaultValue: T
    ): T = getParam(
        name = name,
        defaultValue = defaultValue,
        convert = { value: String? ->
            if (value == null) return@getParam defaultValue
            runCatching { enumValueOf<T>(name = value) }.getOrNull()
        }
    )

    inline fun <reified T : Enum<T>> RoutingCall.getEnumParamOrNull(
        name: String
    ): T? = getParamOrNull(
        name = name,
        convert = { value: String? ->
            if (value == null) return@getParamOrNull null
            runCatching { enumValueOf<T>(name = value) }.getOrNull()
        }
    )

    fun <T> RoutingCall.getParamOrNull(name: String, convert: (String?) -> T?): T? {
        val value: String? = request.queryParameters[name]
        return value?.let { convert(it) }
    }


    fun <T> RoutingCall.getParam(name: String, defaultValue: T, convert: (String?) -> T?): T {
        val value: String? = request.queryParameters[name]
        return value?.let { convert(it) } ?: defaultValue
    }
}
