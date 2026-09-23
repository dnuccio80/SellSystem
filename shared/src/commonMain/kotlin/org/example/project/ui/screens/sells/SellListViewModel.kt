package org.example.project.ui.screens.sells

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.project.data.db.daos.SellRepository
import org.example.project.domain.models.sell.Sell
import org.example.project.domain.usecases.sells.GetSells
import org.example.project.domain.usecases.sells.SellFilterLabel
import org.koin.core.qualifier._q
import kotlin.collections.emptyList
import kotlin.time.Duration.Companion.milliseconds
@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)

class SellListViewModel(getSells: GetSells, private val sellRepository: SellRepository) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    private val _labelSelected = MutableStateFlow(SellFilterLabel.ALL)
    val labelSelected = _labelSelected.asStateFlow()

    private val _sellData = MutableStateFlow<Sell?>(null)
    val sellData = _sellData.asStateFlow()

    private val _sellsList: StateFlow<List<Sell>> = combine(
        _query,
        _labelSelected,
    ) { query, labelSelected ->
        query to labelSelected
    }.debounce(300.milliseconds).flatMapLatest { (query, filter) ->
        getSells(query, filter)
    }.catch { e -> }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val sellsList = _sellsList

    fun updateQuery(newValue: String) {
        _query.update { newValue }
    }

    fun updateLabelSelected(newValue: SellFilterLabel) {
        _labelSelected.update { newValue }
    }

    fun getSell(id:Int, onDone:() -> Unit) {
        viewModelScope.launch {
            async { _sellData.update { sellRepository.getSellById(id) } }.await()
            onDone()
        }
    }

    fun deleteSell() {
        viewModelScope.launch {
            sellRepository.deleteSellById(sellData.value!!.id)
            clearSellData()
        }
    }

    fun clearSellData() {
        _sellData.update { null }
    }



}