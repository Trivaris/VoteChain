package com.trivaris.votechain.model

import app.cash.sqldelight.Transacter

interface Repository<T: Object> {
    val database: Transacter

    fun insert(obj: T)
    fun getAll(): List<T>
    fun clear()
    fun repopulate(objList: List<T>) {
        clear()
        objList.forEach { obj ->
            insert(obj)
        }
    }
}