package com.semicolon.data.repository.remote.model.productdetails

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class Photo(
    @SerializedName("file_path") val photoUrl: String?,
    val id: Int?
)