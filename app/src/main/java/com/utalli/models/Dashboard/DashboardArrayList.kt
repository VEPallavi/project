package com.utalli.models.Dashboard

class DashboardArrayList {

    private var populardestinations: List<PopulardestinationList>? = null
    private var data: List<GuidListData>? = null

    fun getPopulardestinations(): List<PopulardestinationList>? {
        return populardestinations
    }

    fun setPopulardestinations(populardestinations: List<PopulardestinationList>) {
        this.populardestinations = populardestinations
    }

    fun getData(): List<GuidListData>? {
        return data
    }

    fun setData(data: List<GuidListData>) {
        this.data = data
    }
}