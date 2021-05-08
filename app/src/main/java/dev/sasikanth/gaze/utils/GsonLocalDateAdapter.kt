package dev.sasikanth.gaze.utils

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type
import java.text.ParseException
import java.time.LocalDate

class GsonLocalDateAdapter : JsonDeserializer<LocalDate> {

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): LocalDate {
        try {
            return LocalDate.parse(json!!.asString)
        } catch (e: ParseException) {
            throw JsonParseException(e)
        }
    }
}
