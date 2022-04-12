/*
 * * * Copyright (c) 2021 海龙 Inc. All rights reserved.
 */

package com.apemans.base.utils

class CollectionUtil {
    companion object {
        @JvmStatic
        fun isEmpty(collection: Collection<*>?): Boolean {
            return null == collection || collection.isEmpty()
        }

        @JvmStatic
        fun isNotEmpty(collection: Collection<*>?): Boolean {
            return null != collection && !collection.isEmpty()
        }

        @JvmStatic
        fun isIndexSafe(index: Int, size: Int): Boolean {
            return index >= 0 && index < size
        }

        @JvmStatic
        fun emptyList(): List<*> {
            return emptyList<Any>()
        }

        @JvmStatic
        fun <T> size(other: List<T>?): Int {
            return other?.size ?: 0
        }

        @JvmStatic
        fun <T> safeFor(other: List<T>?): List<T> {
            if (other == null) {
                return mutableListOf()
            } else {
                return other
            }
        }
    }

}

