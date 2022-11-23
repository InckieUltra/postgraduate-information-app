package com.example.informationapplication.ui.middle

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MiddleViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is middle Fragment"
    }
    val text: LiveData<String> = _text
}