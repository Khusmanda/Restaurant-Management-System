package com.thecodefire.brannigans.manager

class TableBookingInfo {
    var customerId: String? = null
    var capacity: String? = null
    var status: String? = null
    var tableNo: String? = null

    constructor(){}

    constructor(customerId: String?, capacity: String?, status: String?, tableNo: String?) {
        this.customerId = customerId
        this.capacity = capacity
        this.status = status
        this.tableNo = tableNo
    }
}