package com.egypt.game.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SettingsViewModel: ViewModel() {
    private val _currentBg = MutableLiveData(-1)
    val currentBg: LiveData<Int> = _currentBg

    fun left() {
        if (_currentBg.value!! - 1 >= 1) {
            _currentBg.postValue(_currentBg.value!! - 1)
        }
    }

    fun right() {
        if (_currentBg.value!! + 1 <= 6) {
            _currentBg.postValue(_currentBg.value!! + 1)
        }
    }

    fun initBg(value: Int) {
        _currentBg.postValue(value)
    }
}