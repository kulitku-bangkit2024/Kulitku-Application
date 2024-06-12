package com.dicoding.kulitku.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class UserViewModel(private val preference: UserPreferences) : ViewModel() {
    fun getLogin(): LiveData<Boolean> {
        return preference.getLoginSession().asLiveData()
    }

    fun saveLogin(login: Boolean) {
        viewModelScope.launch {
            preference.saveLoginSession(login)
        }
    }


    fun getToken(): LiveData<String> {
        return preference.getToken().asLiveData()
    }

    fun saveToken(token: String) {
        viewModelScope.launch {
            preference.saveToken(token)
        }
    }

    fun getName():LiveData<String> {
        return preference.getName().asLiveData()
    }

    fun saveName(name: String) {
        viewModelScope.launch {
            preference.saveName(name)
        }
    }

    fun logout() {
        viewModelScope.launch {
            preference.logout()
        }
    }
}
