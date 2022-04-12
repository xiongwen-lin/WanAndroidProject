package com.apemans.dmcomponent.contants

import android.os.Bundle

/**
 * @author Dylan Cai
 */


const val KEY_HOME_ID = "home_id"
const val KEY_MEMBER_ID = "member_id"
const val KEY_NICKNAME = "nickname"
const val KEY_ACCOUNT = "account"
const val KEY_IS_OWNER = "is_owner"
const val KEY_EMAIL = "email"
const val KEY_URL = "url"
const val KEY_TYPE = "type"
const val KEY_SSID = "ssid"
const val KEY_PWD = "pwd"
const val KEY_ICON = "icon"
const val KEY_DESCRIPTION = "description"

const val KEY_CLOUD_PACK_PARAM_UUID = "uuid";
const val KEY_CLOUD_PACK_PARAM_MODEL = "model";
const val KEY_CLOUD_PACK_PARAM_ENTER_MARK = "enter_mark";
const val KEY_CLOUD_PACK_PARAM_ORIGIN = "origin";

const val INTENT_KEY_DETECTION_TYPE = "INTENT_KEY_DETECTION_TYPE"
const val INTENT_KEY_DEVICE_ID = "INTENT_KEY_DEVICE_ID"
const val INTENT_KEY_MODEL = "INTENT_KEY_MODEL"

const val DETECTION_TYPE_MOTION = 1
const val DETECTION_TYPE_SOUND = 2
const val DETECTION_TYPE_HUMAN = 3

var Bundle.ssid: String?
    get() = getString("ssid")
    set(value) = putString("ssid", value)

var Bundle.pwd: String?
    get() = getString("pwd")
    set(value) = putString("pwd", value)

var Bundle.name: String?
    get() = getString("name")
    set(value) = putString("name", value)

var Bundle.result: String?
    get() = getString("result")
    set(value) = putString("result", value)

var Bundle.deviceId: String?
    get() = getString("deviceId")
    set(value) = putString("deviceId", value)
