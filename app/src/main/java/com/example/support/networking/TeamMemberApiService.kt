/*
 * Copyright 2019, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.example.support.networking

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

enum class TeamMemberApiFilter(val value: Boolean) {
    SHOW_AVAILABLE(true),
    SHOW_BLOCK(false),
    SHOW_ALL(true || false) }

private const val BASE_URL = "https://5350b64d-d615-4eb4-90b7-18097a93554f.mock.pstmn.io/"



private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()



private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi).asLenient())
        .baseUrl(BASE_URL)
        .build()

interface TeamMemberApiService {
    @GET("profiles")
    suspend fun getProperties(): List<TeamMember>
}

object MembersApi {
    val retrofitService : TeamMemberApiService by lazy { retrofit.create(TeamMemberApiService::class.java) }
}

