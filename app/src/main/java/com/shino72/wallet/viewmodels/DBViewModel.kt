package com.shino72.wallet.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shino72.wallet.data.OttState
import com.shino72.wallet.data.Status
import com.shino72.wallet.db.entity.OttDB
import com.shino72.wallet.repo.DBRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DBViewModel@Inject constructor(private val dbRepository: DBRepository) : ViewModel() {

    private var _dbStatus = MutableStateFlow(OttState())
    val dbState : StateFlow<OttState> = _dbStatus

    fun getAllDBData() {
        _dbStatus.value = OttState()
        dbRepository.getAllOttData().onEach {
            when(it)
            {
                is Status.Loading -> {
                    _dbStatus.value = OttState(isLoading = true)
                }
                is Status.Error -> {
                    _dbStatus.value = OttState(error = it.message ?: "")
                }
                is Status.Success -> {
                    _dbStatus.value = OttState(it.data)
                }
            }
        }.launchIn(viewModelScope)
    }
    fun insertDB(ott: OttDB) {
        viewModelScope.launch(Dispatchers.IO) {
            dbRepository.insertDB(ott)
        }
    }
    fun deleteDB(ott: OttDB){
        viewModelScope.launch(Dispatchers.IO) {
            dbRepository.deleteDB(ott)
        }
    }
    fun updateDB(ott: OttDB){
        viewModelScope.launch(Dispatchers.IO){
            dbRepository.updateDB(ott)
        }
    }
}