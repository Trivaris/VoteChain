package com.trivaris.votechain.store

interface Repository<T, O> {
    val database: T

    fun insert(item: O)
    fun getAll(): List<O>
    fun repopulate(blocks: List<O>)
    fun clear()
    fun init(driverFactory: DriverFactory)
}