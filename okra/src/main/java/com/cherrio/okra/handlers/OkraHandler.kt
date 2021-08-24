package com.cherrio.okra.handlers

import com.cherrio.okra.utils.successmodel.OkraResponse
import java.io.Serializable

data class OkraHandler(
    var data: String = "",
    var isSuccessful: Boolean = false,
    var hasError: Boolean = false,
    var isDone: Boolean = false,
    var response: OkraResponse? = null
)