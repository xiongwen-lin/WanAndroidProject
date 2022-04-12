package com.apemans.tuya.module.api.exception

/**
 * @author Dylan Cai
 */
class TuyaException(
    val code: String,
    message: String
) : Exception(message)