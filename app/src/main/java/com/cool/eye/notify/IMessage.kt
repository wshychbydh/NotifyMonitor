package com.cool.eye.notify

interface IMessage {

    fun other()

    /**
     * 电话
     */
    fun phone()

    /**
     * 短信
     */
    fun shortMessage()

    /**
     * 微信
     */
    fun wxMessage()

    /**
     * qq
     */
    fun qqMessage()
}