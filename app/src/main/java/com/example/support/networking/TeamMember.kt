package com.example.support.networking

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize


@Parcelize
data class TeamMember(
    @Json(name = "firstName") val firstName: String = "rotem",
    @Json(name = "lastName") val lastName: String = "kadosh",
    @Json(name = "available") val available: Boolean = false,
    @Json(name = "phone") val phone: String = "0548166800",
    @Json(name = "email") val email: String = "rotemkadosh27@gmail.com",
    @Json(name = "image") var _imgSrcUrl: String?
) : Parcelable {

    var imgSrcUrl: String = ""
        get() {
            if (_imgSrcUrl.isNullOrEmpty()) return "https://images.unsplash.com/photo-1528660493888-ab6f4761e036?ixlib=rb-1.2.1&amp;ixid=eyJhcHBfaWQiOjEyMDd9&amp;auto=format&amp;fit=crop&amp;w=1800&amp;q=80"
            return _imgSrcUrl as String
        }
}





