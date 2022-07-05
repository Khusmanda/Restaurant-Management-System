package com.thecodefire.brannigans.customer

class MenuItems {
    // not in use
    var menuId: String? = null
    var menuName: String? = null
    var menuPrice: String? = null
    var menuType: String? = null
    var quantity: String? = null
    var status: String? = null
    var time: String? = null

    constructor(){}

    constructor(
        menuId: String?,
        menuName: String?,
        menuPrice: String?,
        menuType: String?,
        quantity: String?,
        status: String?,
        time: String?
    ) {
        this.menuId = menuId
        this.menuName = menuName
        this.menuPrice = menuPrice
        this.menuType = menuType
        this.quantity = quantity
        this.status = status
        this.time = time
    }
}