package org.example.project.ui.screens.suppliers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.project.domain.models.supplier.SupplierError
import org.example.project.domain.repositories.SupplierRepository
import org.example.project.domain.usecases.suppliers.AddSupplier
import org.example.project.domain.usecases.suppliers.GetSuppliers
import kotlin.time.Duration.Companion.milliseconds


enum class SupplierAction {
    NAME, MAIL, PHONE, ADDRESS, WEBPAGE, PRODUCTS, DISMISS, ACCEPT, DELETE_CONFIRM
}

class SuppliersViewModel(
    getSuppliers: GetSuppliers,
    private val repository: SupplierRepository,
    private val addSupplier: AddSupplier,
) : ViewModel() {


    private val _query = MutableStateFlow("")
    private val _supplierData = MutableStateFlow(ClearSupplier().getClearSupplier())

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    private val _suppliers = _query.debounce(300.milliseconds).flatMapLatest { query ->
        getSuppliers(query)
    }.catch { e -> }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _uiState =
        combine(_query, _supplierData, _suppliers) { query, supplierData, suppliers ->
            SuppliersUiState.Success(
                query = query,
                supplierData = supplierData,
                supplierList = suppliers
            ) as SuppliersUiState
        }.catch { e -> SuppliersUiState.Error(e) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SuppliersUiState.Loading)
    val uiState = _uiState

    private val _events = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val events = _events.asSharedFlow()
    fun updateSupplierData(action: SupplierAction, value: String) {
        when (action) {
            SupplierAction.NAME -> {
                _supplierData.update { it.copy(name = value) }
            }

            SupplierAction.MAIL -> {
                _supplierData.update { it.copy(mail = value) }
            }

            SupplierAction.PHONE -> {
                val inLong = value.toLongOrNull() ?: 0L
                _supplierData.update { it.copy(phoneNumber = inLong) }
            }

            SupplierAction.ADDRESS -> {
                _supplierData.update { it.copy(address = value) }
            }

            SupplierAction.PRODUCTS -> {
                _supplierData.update { it.copy(productsOffered = value) }
            }

            SupplierAction.WEBPAGE -> {
                _supplierData.update { it.copy(webpage = value) }
            }
            SupplierAction.DISMISS -> {}
            SupplierAction.ACCEPT -> {}
            SupplierAction.DELETE_CONFIRM -> {}
        }
    }

    fun getSupplier(id: Int, onDone: () -> Unit) {
        viewModelScope.launch {
            val supplier = async { repository.getSupplierById(id) }.await()
            _supplierData.update { supplier }
            onDone()
        }
    }

    fun tryAddSupplier(onDone: () -> Unit) {
        viewModelScope.launch {
            try {
                addSupplier(_supplierData.value)
                cleanSupplierData()
                onDone()
            } catch (e: SupplierError) {
                _events.emit(e.msg)
            }
        }
    }

    fun deleteSupplier() {
        viewModelScope.launch {
            async { repository.deleteSupplierById(_supplierData.value.id) }.await()
            cleanSupplierData()
        }
    }

    fun cleanSupplierData() {
        _supplierData.update { ClearSupplier().getClearSupplier() }
    }

}