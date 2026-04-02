package com.atiq.quickwhatsapp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atiq.quickwhatsapp.data.local.countriesList
import com.atiq.quickwhatsapp.data.repository.AppRepository
import com.atiq.quickwhatsapp.model.Country
import com.atiq.quickwhatsapp.model.MessageTemplate
import com.atiq.quickwhatsapp.model.RecentNumber
import com.atiq.quickwhatsapp.utils.LaunchResult
import com.atiq.quickwhatsapp.utils.PhoneNumberUtils
import com.atiq.quickwhatsapp.utils.WhatsAppLauncher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val selectedCountry: Country = countriesList.first(),
    val phoneInput: String = "",
    val isBusinessMode: Boolean = false,
    val selectedTemplate: MessageTemplate? = null,
    val templates: List<MessageTemplate> = emptyList(),
    val recents: List<RecentNumber> = emptyList(),
    val error: HomeError? = null
)

sealed class HomeError {
    object InvalidNumber : HomeError()
    object WhatsAppNotInstalled : HomeError()
    object BusinessNotInstalled : HomeError()
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = combine(
        _uiState,
        repository.recentNumbers,
        repository.templates
    ) { state, recents, templates ->
        state.copy(recents = recents, templates = templates)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HomeUiState())

    fun onCountrySelected(country: Country) {
        _uiState.update { it.copy(selectedCountry = country, error = null) }
    }

    fun onPhoneInputChanged(input: String) {
        _uiState.update { it.copy(phoneInput = input, error = null) }
    }

    fun onBusinessModeToggled(enabled: Boolean) {
        _uiState.update { it.copy(isBusinessMode = enabled) }
    }

    fun onTemplateSelected(template: MessageTemplate?) {
        _uiState.update { it.copy(selectedTemplate = template) }
    }

    fun onOpenChat(context: Context) {
        val state = _uiState.value
        if (!PhoneNumberUtils.isValid(state.selectedCountry.dialCode, state.phoneInput)) {
            _uiState.update { it.copy(error = HomeError.InvalidNumber) }
            return
        }

        val url = PhoneNumberUtils.buildWaUrl(
            state.selectedCountry.dialCode,
            state.phoneInput
        ).let { baseUrl ->
            state.selectedTemplate?.let { "$baseUrl?text=${it.text}" } ?: baseUrl
        }

        when (WhatsAppLauncher.open(context, url, state.isBusinessMode)) {
            LaunchResult.Success -> {
                _uiState.update { it.copy(error = null) }
                viewModelScope.launch {
                    repository.saveRecent(
                        RecentNumber(
                            dialCode = state.selectedCountry.dialCode,
                            number = state.phoneInput
                        )
                    )
                }
            }
            LaunchResult.WhatsAppNotInstalled ->
                _uiState.update { it.copy(error = HomeError.WhatsAppNotInstalled) }
            LaunchResult.BusinessNotInstalled ->
                _uiState.update { it.copy(error = HomeError.BusinessNotInstalled) }
        }
    }

    fun onDeleteRecent(id: Int) {
        viewModelScope.launch { repository.deleteRecent(id) }
    }

    fun onDeleteTemplate(template: MessageTemplate) {
        viewModelScope.launch { repository.deleteTemplate(template) }
    }

    fun onDismissError() {
        _uiState.update { it.copy(error = null) }
    }
}