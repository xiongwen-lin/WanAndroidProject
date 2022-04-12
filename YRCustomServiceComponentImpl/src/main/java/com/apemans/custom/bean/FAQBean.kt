package com.apemans.custom.bean

class FAQBean {
    var title: String? = null
    var content: String? = null
    var children: List<FAQBean>? = null
    var isExpand = false

    fun toggleExpand() {
        isExpand = !isExpand
    }
}