package com.trivaris.votechain.model.block

import com.trivaris.votechain.BlockDatabase
import com.trivaris.votechain.common.Config
import com.trivaris.votechain.common.Logger
import com.trivaris.votechain.model.DriverFactory
import com.trivaris.votechain.model.Repository

class BlockRepository(
    driverFactory: DriverFactory
) : Repository<BlockObject> {
    override var database: BlockDatabase

    override fun insert(obj: BlockObject) {
        database.blockQueries.insert(
            obj.hash,
            Config.json.encodeToString(obj.votes),
            obj.previousHash,
            obj.timestamp,
            obj.nonce
        )
        ChainInteraction.updateCurrentVotes(obj.votes)
    }

    override fun getAll(): MutableList<BlockObject> {
        return database.blockQueries.getAll()
            .executeAsList()
            .map { row -> BlockObject(row) }.toMutableList()
    }

    override fun clear() {
        Logger.DEBUG.log("Clearing Database")
        database.blockQueries.clear()
    }

    init {
        val driver = driverFactory.createDriver()
        database = BlockDatabase(driver)

        database.blockQueries.clear()
    }

}