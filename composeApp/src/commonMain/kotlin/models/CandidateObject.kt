package com.trivaris.votechain.models

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
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
}