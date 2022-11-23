package com.example.informationapplication.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LeftViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is left Fragment"
    }
    val text: LiveData<String> = _text
}