package ir.miare.androidcodechallenge.data.util

import kotlinx.serialization.json.Json

internal inline fun <reified T> ClassLoader?.toSuccessResponse(path: String): T {
    return Json.decodeFromString<T>(readResourceAsText(path))
}

internal fun ClassLoader?.readResourceAsText(path: String): String {
    return this?.getResourceAsStream(path)
        ?.bufferedReader(Charsets.UTF_8)
        ?.readText()
        ?: throw IllegalAccessException("Resource not found: $path")
}
