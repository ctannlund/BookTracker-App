package com.ctannlund.booklog

import android.app.Application

class BookLogApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        BookRepository.initialize(this)
    }

}