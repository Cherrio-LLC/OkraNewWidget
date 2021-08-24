package com.cherrio.okra.utils

import java.io.Serializable

data class DeviceInfo(
    var deviceName: String? = null,
    var deviceModel: String? = null,
    var longitude: Float = 0f,
    var latitude: Float = 0f,
    var platform: String? = null
) : Serializable {
}