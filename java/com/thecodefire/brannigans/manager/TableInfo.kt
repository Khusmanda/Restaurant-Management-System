package com.thecodefire.brannigans.manager

class TableInfo {
    var tableNo: String? = null
    var tableStatus: String? = null
    var customerId: String? = null
    var tableCapacity: String? = null

    constructor(){}

    constructor(tableNo: String?, tableStatus: String?, customerId: String?, tableCapacity: String) {
        this.tableNo = tableNo
        this.tableStatus = tableStatus
        this.customerId = customerId
        this.tableCapacity = tableCapacity
    }
}