package com.arslan.taskmaster.interfaces

interface UploadResultCallback {
    fun onSuccess()
    fun onFailure(error: Exception)
}