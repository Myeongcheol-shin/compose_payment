package com.shino72.wallet.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class InfoViewModel @Inject constructor(
    state : SavedStateHandle
) : ViewModel() {
    val ott : String?
    private val _name = MutableStateFlow("")
    val name = _name.asStateFlow()
    fun setName(v : String){_name.value = v}

    private val _price = MutableStateFlow("")
    val price = _price.asStateFlow()
    fun setPrice(v: String) {_price.value = v}

    private val _duedate = MutableStateFlow("")
    val duedate = _duedate.asStateFlow()
    fun setDuedata(v : String){_duedate.value = v}

    private val _memo = MutableStateFlow("")
    val memo = _memo.asStateFlow()
    fun setMemo(v : String) {_memo.value = v}

    init {
        ott = state.get<String>("ott")
    }
}