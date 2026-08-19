package com.yaroslavsdev.nutriscan.data.remote

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.time.Instant

class InstantAdapter : TypeAdapter<Instant>() {

    override fun write(out: JsonWriter, value: Instant?) {
        out.value(value?.toString())
    }

    override fun read(reader: JsonReader): Instant {
        val text = reader.nextString()
        return Instant.parse(text)
    }
}