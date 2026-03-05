package com.TFG_JCF.fittrack.data.model

import com.TFG_JCF.fittrack.data.database.entities.User_Bonus.ActivityLevel
import com.TFG_JCF.fittrack.data.database.entities.User_Bonus.Gender
import com.TFG_JCF.fittrack.data.database.entities.User_Bonus.GoalType

data class SignUpData(

    var uid: String? = null,
    var email: String? = null,
    var password: String? = null,
    var name: String? = null,

    var goalType: GoalType? = null,
    var activityLevel: ActivityLevel? = null,

    var gender: Gender? = null,
    var height: Int? = null,
    var age: Int? = null,
    var weight: Float? = null
)