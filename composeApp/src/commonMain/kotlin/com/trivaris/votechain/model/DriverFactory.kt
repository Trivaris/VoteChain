package com.trivaris.votechain.model

import app.cash.sqldelight.db.SqlDriver

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class DriverFactory {
    fun createDriver(): SqlDriver
}