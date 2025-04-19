package com.trivaris.votechain.data

import com.trivaris.votechain.models.CandidateObject
import com.trivaris.votechain.models.RequestState
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class MongoDB {
    private var realm: Realm? = null

    init {
        configureRealm()
    }

    private fun configureRealm() {
        if (realm == null || realm!!.isClosed()) {
            val config = RealmConfiguration.Builder(
                schema = setOf(CandidateObject::class)
            )
                .compactOnLaunch()
                .build()
            realm = Realm.open(config)
        }
    }

    fun readCandidates(): Flow<RequestState<List<CandidateObject>>> {
        return realm?.query<CandidateObject>(query = "")
            ?.asFlow()
            ?.map { result ->
                RequestState.Success(
                    data = result.list.sortedByDescending { candidate -> candidate.lastname }
                )
            } ?: flow { RequestState.Error(message = "Realm is not available.") }
    }

}