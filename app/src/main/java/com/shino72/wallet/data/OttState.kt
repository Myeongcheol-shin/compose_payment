package com.shino72.wallet.data

import com.shino72.wallet.db.entity.OttDB

data class OttState (
    val db : List<OttDB>?  = null,
    val error: String = "",
    val isLoading: Boolean = false
)