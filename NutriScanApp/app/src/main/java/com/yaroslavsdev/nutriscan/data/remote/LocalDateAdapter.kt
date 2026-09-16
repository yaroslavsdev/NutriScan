package com.yaroslavsdev.nutriscan.data.remote

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.time.LocalDate

class LocalDateAdapter : TypeAdapter<LocalDate>() {
    override fun write(out: JsonWriter, value: LocalDate?) {
        out.value(value?.toString())
    }

    override fun read(reader: JsonReader): LocalDate {
        val text = reader.nextString()
        return LocalDate.parse(text)
    }
}