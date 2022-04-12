package com.apemans.tuya.component.constants

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
const val KEY_DEVICE_ID = "device_id"
const val KEY_GROUP_ID = "group_id"
const val KEY_TYPE = "type"
const val KEY_SSID = "ssid"
const val KEY_PSD = "psd"
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

//device model
const val SMART_PLUG_PRODUCTID = "octeoqhuayzof69q" //teckin美规单插  SP10

const val SMART_PLUG_PRODUCTID_NEW = "iqgfsxokdkzzehmj" //teckin美规上线单插  SP10

const val SMART_PLUG_PRODUCTID_OLD = "4bVOiYN0zdh6vTYq" //teckin美规单插  SP10

const val SMART_PLUG_US_PRODUCTID_TWO = "dok3rzi3pnnqu6ju" //teckin美规规插头 SP20

const val SMART_PLUG_US_PRODUCTID_THREE = "viv1giuyu2tk4kt4" //teckin美规规插头 SP20

const val SMART_PLUG_EU_PRODUCTID = "cya3zxfd38g4qp8d" //teckin欧规插头 SP21

const val SMART_PLUG_UK_PRODUCTID = "5bvnmoqjth5nd4de" //teckin英规插头 SP23/SP27

const val SMART_PLUG_UK_PRODUCTID_TWO = "sOhGq6u1M2JwB5d8" //teckin英规插头 SP23/SP27

const val SMART_PLUG_UK_PRODUCTID_THREE = "twezq8g8ykoaggey" //teckin英规插头 SP23/SP27

const val SMART_LAMP_PRODUCTID = "isehgkqn5uqlrorl" //teckin美规灯 SB53

const val SMART_LAMP_PRODUCTID_TWO = "fnxgcsysunpyxkou" //teckin美规灯 SB50L

const val SMART_LAMP_PRODUCTID_THREE = "ttrn0dlxota0rrav" //teckin美规灯 SB50H

const val SMART_LAMP_PRODUCTID_FOUR = "hdnoe1sqimwad9f4" //teckin美规灯 SB60

const val SMART_LAMP_PRODUCTID_FIVE = "gswrpjab2vfawful" //teckin新dp灯 SB50

const val SMART_LAMP_PRODUCTID_SIX = "5abhrka6ejfr0hvx" //teckin新dp灯 SB50

const val SMART_PLUG_EU_PRODUCTID_TWO = "fbvia0apnlnattcy" //teckin欧规插头 SP22

const val SMART_PLUG_EU_PRODUCTID_THREE = "vnya2spfopsh9lro" //teckin欧规插头 SP22

const val SMART_STRIP_PRODUCTID = "01wjigkru2tgixxp" //teckin排插 SS36

const val SMART_STRIP_PRODUCTID_NEW = "omwxkdvwpxtyjans" //teckin排插 SS36

const val SMART_STRIP_PRODUCTID_TWO = "EQD8hAQw543vzh6O" //teckin排插 SS31

const val SMART_STRIP_PRODUCTID_THREE = "iGuAESc6917owGUr" //teckin排插 SS33

const val SMART_STRIP_PRODUCTID_FOUR = "j7ewsefbjxaprlqy" //teckin排插 SS30N

const val SMART_SWITCH_PRODUCTID = "pJnpT0XcM5FTRjOd" //SR41

const val SMART_SWITCH_PRODUCTID_TWO = "bYdRrWx5iLCyAfPs" //SR42

const val SMART_FLOOR_LAMP_PRODUCTID = "nscnnpeguv620u4f" //落地灯 FL41

const val SMART_LIGHT_MODULATOR_PRODUCTID = "1ogqu4bxwzrjxu8v" //调光器 SR46

const val SMART_LIGHT_STRIP_PRODUCTID = "gmjdt6mvy1mntvqn" //灯带 SL02

const val SMART_LIGHT_STRIP_PRODUCTID_TWO = "g56afofns8lpko6v" //灯带 SL02/SL07

const val SMART_LIGHT_STRIP_PRODUCTID_THREE = "behqxmx1m8e4sr08" //灯带 SL12

const val SMART_LIGHT_STRIP_PRODUCTID_FOUR = "b6vjkghax6amtwdf" //灯带 SL08

const val SMART_PLUG_JP_PRODUCTID = "A6bBfm2fmKKRfIxU" //teckin日规单插

const val SMART_PLUG_JP_PRODUCTID_ONE = "8jkyyvxsep3yr5ql" //teckin日规单插SP11

const val SMART_SWITCH_PRODUCTID_THREE = "ycccdik7krsxuybg" //teckin墙壁开关SR40

const val SMART_SWITCH_PRODUCTID_FOUR = "J4b9HONUUjrBxJXK" //teckin墙壁开关带USB SR43
const val SMART_PLUG_EU_PRODUCTID_EIGHT = "xajto1x7xm4w3x0s"


var Bundle.ssid: String?
    get() = getString("ssid")
    set(value) = putString("ssid", value)

var Bundle.pwd: String?
    get() = getString("pwd")
    set(value) = putString("pwd", value)

