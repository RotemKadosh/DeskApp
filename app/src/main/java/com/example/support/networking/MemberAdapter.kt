package com.example.support.networking

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonReader

class MemberAdapter {
    @FromJson
    fun fromJsno(reader: JsonReader){
        val res :MutableList<TeamMember>
        reader.beginObject()
        while (reader.hasNext())
        {
            reader.beginArray()

        }
    }

}