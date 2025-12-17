package local_util

import org.jetbrains.exposed.sql.ColumnType
import java.sql.Timestamp
import java.time.ZoneOffset
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
internal class KotlinTimeInstantColumnType : ColumnType<Instant>() {
    override fun sqlType() = "TIMESTAMP"

    override fun valueToDB(value: Instant?): Any? {
        return value?.let {
            val epochMilli: Long = it.toEpochMilliseconds()
            val javaInstant: java.time.Instant = java.time.Instant.ofEpochMilli(epochMilli)
            Timestamp.from(javaInstant)
        }
    }

    override fun valueFromDB(value: Any): Instant {
        return when (value) {
            is Instant -> value

            is Timestamp -> {
                Instant.fromEpochMilliseconds(epochMilliseconds = value.time)
            }

            is java.time.LocalDateTime -> {
                val javaInstant: java.time.Instant = value.atZone(ZoneOffset.UTC).toInstant()
                Instant.fromEpochMilliseconds(epochMilliseconds = javaInstant.toEpochMilli())
            }

            is java.time.Instant -> {
                Instant.fromEpochMilliseconds(epochMilliseconds = value.toEpochMilli())
            }

            else -> error("Instant 타입에 대해 예상치 못한 값이 들어왔습니다: $value of type ${value::class}")
        }
    }
}
