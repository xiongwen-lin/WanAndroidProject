package com.apemans.quickui.preference

import java.io.Serializable

/***********************************************************
 * @Author : caro
 * @Date   : 2020/11/11
 * @Func:
 *
 * @Description:
 * 设置项Item
 *
 * @param name 设置项名称
 *
 ***********************************************************/
class PreferenceBean(
    val name:String,
    @PreferenceViewType
    val itemUIType: Int = -1,
) : Serializable {
    //设置项图标
    var iconDrawable: Int?=null
    //子标题
    var subTile:String?=null
    //value 设置项当前生效值
    var value:String?=null

    //设置项参数列表
    var params: List<Param> = emptyList()

    //设置项是否可用/可点击
    var enable: Boolean = true

    /**
     * 参数列表
     */
    class Param(
        //参数value
        var value: String,
        //参数图标
        var iconDrawable: String = "",
    ) : Serializable {

        //多级参数列表，当有多级时，请添加到levelParam中
        var levelParam: List<Param>? = null

        //当前参数是否是生效值
        var isAlive = false

        override fun toString(): String {
            return "\n Param :  \n value:$value \n  levelParam:$levelParam \n iconDrawable:$iconDrawable\n isAlive:$isAlive"
        }

        override fun hashCode(): Int {
            var result = value.hashCode()
            result = 31 * result + iconDrawable.hashCode()
            result = 31 * result + (levelParam?.hashCode() ?: 0)
            result = 31 * result + isAlive.hashCode()
            return result
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Param

            if (value != other.value) return false
            if (iconDrawable != other.iconDrawable) return false
            if (levelParam != other.levelParam) return false
            if (isAlive != other.isAlive) return false

            return true
        }
    }

    override fun toString(): String {
        return "\n PreferenceItem:  " +
                "\n value:$value " +
                "\n params:$params"
    }

    override fun hashCode(): Int {
        var result = value.hashCode()
        result = 31 * result + itemUIType
        result = 31 * result + name.hashCode()
        result = 31 * result + iconDrawable.hashCode()
        result = 31 * result + value.hashCode()
        result = 31 * result + params.hashCode()
        result = 31 * result + enable.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PreferenceBean

        if (itemUIType != other.itemUIType) return false
        if (name != other.name) return false
        if (iconDrawable != other.iconDrawable) return false
        if (value != other.value) return false
        if (params != other.params) return false
        if (enable != other.enable) return false

        return true
    }

}

