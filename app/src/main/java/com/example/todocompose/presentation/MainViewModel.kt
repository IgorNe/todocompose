package com.example.todocompose.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todocompose.db.MainDb
import com.example.todocompose.model.TodoItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val mainDb: MainDb
):ViewModel(){
    val mainList = mutableStateOf(emptyList<TodoItem>())
    init {
        viewModelScope.launch {
            getAllItemsByCategory("%")
        }
    }

    fun getAllItemsByCategory(category: String) = viewModelScope.launch{
        mainList.value = mainDb.dao.getAllItems(category)
    }

    fun insertItem(item:TodoItem) = viewModelScope.launch{
        mainDb.dao.insertItem(item)
        getAllItemsByCategory("%")
    }

    fun deletetItem(item:TodoItem) = viewModelScope.launch{
        mainDb.dao.delete(item)
        getAllItemsByCategory("%")
    }

}