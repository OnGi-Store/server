package remote_data.geocode.response

import kotlinx.serialization.Serializable

@Serializable
internal data class Refined(
    val text: String,
    val structure: Structure
)

@Serializable
internal data class Structure(
    val level0: String,
    val level1: String,
    val level2: String,
    val level3: String,
    val level4L: String,
    val level4LC: String,
    val level4A: String,
    val level4AC: String,
    val level5: String,
    val detail: String
)
