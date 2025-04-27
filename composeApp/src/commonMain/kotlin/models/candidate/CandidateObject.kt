package com.trivaris.votechain.models.candidate

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import kotlinx.serialization.Serializable
import org.mongodb.kbson.ObjectId

class CandidateObject : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var affiliation: String = ""
    var firstname: String = ""
    var lastname: String = ""
    var age: Int = -1
    var profession: String = ""
    var slogan: String = ""

    fun getFullName(): String {
        return "${firstname.replaceFirstChar { it.uppercase() }} ${lastname.replaceFirstChar { it.uppercase() }}"
    }

    fun getId(): String {
        return _id.toString()
    }
}