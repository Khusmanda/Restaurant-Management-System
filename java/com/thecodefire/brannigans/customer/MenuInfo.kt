package com.thecodefire.brannigans.customer

class MenuInfo {
    var menuId: String? = null
    var menuName: String? = null
    var menuPrice: String? = null
    var menuType: String? = null

    constructor(){}

    constructor(menuId: String?, menuName: String?, menuPrice: String?, menuType: String?) {
        this.menuId = menuId
        this.menuName = menuName
        this.menuPrice = menuPrice
        this.menuType = menuType
    }
}