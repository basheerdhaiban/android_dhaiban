package com.semicolon.data.repository.remote.model.product

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class Pagination(
    @SerializedName("current_page") val currentPage: Int?,
    @SerializedName("last_page") val lastPage: Int?,
    @SerializedName("next_page_url") val nextPageUrl: String?,
    @SerializedName("per_page") val perPage: Int?,
    @SerializedName("prev_page_url") val previousPageUrl: String?,
    val total: Int?
)