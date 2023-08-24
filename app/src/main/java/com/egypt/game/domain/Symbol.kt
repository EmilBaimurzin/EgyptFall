package com.egypt.game.domain

import com.egypt.game.core.library.XY

data class Symbol(
    override var x: Float,
    override var y: Float,
    val value: Int
) : XY