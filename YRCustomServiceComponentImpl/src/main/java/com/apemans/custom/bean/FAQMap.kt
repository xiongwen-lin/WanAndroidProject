package com.apemans.custom.bean

class FAQMap {
    private var data: Map<String, List<FAQBean>>? = null

    fun getData(): Map<String, List<FAQBean>>? {
        return data
    }

    fun setData(data: Map<String, List<FAQBean>>?) {
        this.data = data
    }
}