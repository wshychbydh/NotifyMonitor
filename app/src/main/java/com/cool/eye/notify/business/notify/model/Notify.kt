package com.cool.eye.notify.business.notify.model

import java.io.Serializable

/**
 *Created by cool on 2018/5/29
 */
class Notify : Serializable {

    var name: String? = null
    var message: String? = null
    var time: Long = 0
    var platform: String? = null // QQ,WX,MSG...
}