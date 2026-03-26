package com.example.mdba_eindopdracht.ui.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mdba_eindopdracht.data.CatData
import com.example.mdba_eindopdracht.data.CatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CatViewModel : ViewModel() {
    private val _selectedCat = MutableStateFlow<CatData?>(null)
    val selectedCat: StateFlow<CatData?> = _selectedCat.asStateFlow()

    private val _catImage = MutableStateFlow<String?>(null)
    val catImage: StateFlow<String?> = _catImage.asStateFlow()

    fun selectCat(cat: CatData, repository: CatRepository) {
        _selectedCat.value = cat
        _catImage.value = null
        viewModelScope.launch {
            try {
                _catImage.value = repository.fetchCatImageUrl(cat.id)
            } catch (e: Exception) {
                println("Error loading image: $e")
            }
        }
    }
}