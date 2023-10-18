package com.arslan.taskmaster.interfaces

interface GenericCallbackAdapter {
    fun <T> getClickedObject(clickedObj: T, position: T, callingID: String = "")
}