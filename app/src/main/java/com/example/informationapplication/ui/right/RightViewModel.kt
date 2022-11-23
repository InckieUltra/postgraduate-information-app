package com.example.informationapplication.ui.right

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RightViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is right Fragment"
    }
    val text: LiveData<String> = _text
}