package com.shino72.wallet.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Plan")
data class OttDB(
   @PrimaryKey(autoGenerate = true)
   val uid : Int = 0,
   var platform : String,
   var korean : String,
   var name : String,
   var price : Int,
   var duedate : Int,
   var memo : String?
): java.io.Serializable
