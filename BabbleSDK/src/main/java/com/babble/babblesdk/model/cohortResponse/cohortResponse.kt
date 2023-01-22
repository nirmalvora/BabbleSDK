package com.babble.babblesdk.model.cohortResponse

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CohortResponse(

    @SerializedName("document") var document: CohortDocument? = CohortDocument(),
    @SerializedName("readTime") var readTime: String? = null

) : Serializable

data class CohortDocument(

    @SerializedName("name") var name: String? = null,
    @SerializedName("fields") var fields: CohortFields? = CohortFields(),
    @SerializedName("createTime") var createTime: String? = null,
    @SerializedName("updateTime") var updateTime: String? = null

) : Serializable

data class CohortFields(

    @SerializedName("name") var name: CohortString? = CohortString(),
    @SerializedName("customer_ids") var customerIds: CustomerIds? = CustomerIds(),
    @SerializedName("source") var source: CohortString? = CohortString(),
    @SerializedName("created_on") var createdOn: CohortTimestamp? = CohortTimestamp(),
    @SerializedName("user_id") var userId: CohortString? = CohortString(),
    @SerializedName("updated_on") var updatedOn: CohortTimestamp? = CohortTimestamp()

) : Serializable

data class CohortString(

    @SerializedName("stringValue") var stringValue: String? = null

) : Serializable

data class ArrayValue(

    @SerializedName("values") var values: ArrayList<CohortString> = arrayListOf()

) : Serializable

data class CustomerIds(

    @SerializedName("arrayValue") var arrayValue: ArrayValue? = ArrayValue()

) : Serializable

data class CohortTimestamp(

    @SerializedName("timestampValue") var timestampValue: String? = null

) : Serializable