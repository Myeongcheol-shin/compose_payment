package com.shino72.wallet.repo

import com.shino72.wallet.data.Status
import com.shino72.wallet.db.dao.OttDao
import com.shino72.wallet.db.entity.OttDB
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject

class DBRepository@Inject constructor(private val ottDao : OttDao) {
    fun getAllOttData(): Flow<Status<List<OttDB>>> = flow {
        emit(Status.Loading())
        try{
            val ott = ottDao.getAll()
            emit(Status.Success(ott))
        }
        catch (e : Exception){
            emit(Status.Error(e.localizedMessage ?: ""))
        }
    }

    suspend fun insertDB(ott: OttDB) : Status<Unit> {
        return try {
            ottDao.insertDB(ott)
            Status.Success(Unit)
        }
        catch (e : Exception){
            Status.Error(e.localizedMessage ?: "")
        }

    }

}