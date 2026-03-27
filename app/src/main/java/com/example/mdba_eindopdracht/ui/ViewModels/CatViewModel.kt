package com.example.mdba_eindopdracht.ui.ViewModels

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mdba_eindopdracht.data.CatData
import com.example.mdba_eindopdracht.data.CatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CatViewModel(application: Application) : AndroidViewModel(application) {
    private val _selectedCat = MutableStateFlow<CatData?>(null)
    val selectedCat: StateFlow<CatData?> = _selectedCat.asStateFlow()

    private val _catImage = MutableStateFlow<String?>(null)
    val catImage: StateFlow<String?> = _catImage.asStateFlow()

    private val prefs: SharedPreferences = application.getSharedPreferences("favourite_cats", Context.MODE_PRIVATE)
    private val FAVORITES_KEY = "favorites"

    private val favorites = mutableSetOf<String>()

    init {
        loadFavoritesFromPrefs()
    }

    private fun loadFavoritesFromPrefs() {
        val savedFavorites = prefs.getStringSet(FAVORITES_KEY, emptySet()) ?: emptySet()
        favorites.clear()
        favorites.addAll(savedFavorites)
    }

    private fun saveFavoritesToPrefs() {
        prefs.edit().putStringSet(FAVORITES_KEY, favorites).apply()
    }

    fun selectCat(cat: CatData, repository: CatRepository) {
        cat.isFavourite = favorites.contains(cat.id)
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

    fun restoreFavoritesToList(cats: List<CatData>) {
        cats.forEach { cat ->
            cat.isFavourite = favorites.contains(cat.id)
        }
    }

    fun updateCat(block: (CatData) -> CatData) {
        _selectedCat.value = _selectedCat.value?.let(block)
    }

    fun toggleFavourite() {
        _selectedCat.value = _selectedCat.value?.let { cat ->
            val newFavouriteState = !cat.isFavourite
            if (newFavouriteState) {
                favorites.add(cat.id)
            } else {
                favorites.remove(cat.id)
            }
            saveFavoritesToPrefs()  // Save to SharedPreferences
            cat.copy(isFavourite = newFavouriteState)
        }
    }
}