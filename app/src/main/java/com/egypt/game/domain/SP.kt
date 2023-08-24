package com.egypt.game.domain

import android.content.Context

class SP(context: Context) {
    private val sp = context.getSharedPreferences("SP", Context.MODE_PRIVATE)

    fun getBest(): Int = sp.getInt("BEST", 0)
    fun setBest(best: Int) = sp.edit().putInt("BEST", best).apply()

    fun getBg(): Int = sp.getInt("BG", 1)
    fun setBg(bg: Int) = sp.edit().putInt("BG", bg).apply()
}