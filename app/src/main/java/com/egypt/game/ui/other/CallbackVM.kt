package com.egypt.game.ui.other

import androidx.lifecycle.ViewModel

class CallbackVM : ViewModel() {
    var callback: (() -> Unit)? = null
}