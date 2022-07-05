package com.thecodefire.brannigans.manager

import android.util.Log

class MenuInfo {
    var menuLog: String? = null
    var menuId: String? = null
    var menuName: String? = null
    var menuPrice: String? = null
    var menuType: String? = null

    constructor(){}

    constructor(menuId: String?, menuName: String?, menuPrice: String?, menuType: String?, menuLog: String?) {
        this.menuId = menuId
        this.menuName = menuName
        this.menuPrice = menuPrice
        this.menuType = menuType
        this.menuLog = menuLog
    }
}