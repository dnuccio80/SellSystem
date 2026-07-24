package org.example.project.ui.screens.productvariants

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ProductVariantsViewModel: ViewModel() {

    private val _variantsList = MutableStateFlow(listOf(""))
    val variants = _variantsList.asStateFlow()

    private val _variantTitle = MutableStateFlow("")
    val variantTitle = _variantTitle.asStateFlow()

    fun addField() {
        _variantsList.update {
            it + ""
        }
    }

    fun removeVariant(index: Int) {
        _variantsList.update { current ->
            current.toMutableList().apply {
                removeAt(index)
            }
        }
    }

    fun updateTitle(newValue:String) {
        _variantTitle.update { newValue }
    }

    fun updateValue(index: Int, value: String) {
        _variantsList.update { current ->
            current.toMutableList().apply {
                this[index] = value
            }
        }
    }

    fun cleanData() {
        _variantsList.value = listOf("")
        _variantTitle.value = ""
    }
}