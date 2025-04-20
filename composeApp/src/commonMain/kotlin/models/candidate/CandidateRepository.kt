package com.trivaris.votechain.models.candidate

import com.trivaris.votechain.models.RequestState
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.RealmResults
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

expect fun getRealmDirectory(): String

class CandidateRepository {
    private var realm: Realm? = null

    private val realmInstance: Realm
        get() {
            if (realm == null || realm!!.isClosed())
                configureRealm()
            return realm!!
        }

    private fun configureRealm() {
        val config = RealmConfiguration.Builder(
            schema = setOf(CandidateObject::class)
        )
            .compactOnLaunch()
            .directory(getRealmDirectory())
            .build()
        realm = Realm.open(config)
    }

    fun readCandidates(): Flow<RequestState<List<CandidateObject>>> {
        return realmInstance.query<CandidateObject>()
            .asFlow()
            .map { result ->
                RequestState.Success(
                    data = result.list.sortedBy { candidate -> candidate.lastname }
                )
            }
    }

}