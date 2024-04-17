package com.ptut.insightify.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.ptut.insightify.common.error.DataError
import com.ptut.insightify.common.error.Result
import com.ptut.insightify.common.util.handleNetworkError
import com.ptut.insightify.domain.survey.model.ProfileWithSurveys
import com.ptut.insightify.domain.survey.model.Survey
import com.ptut.insightify.domain.survey.usecase.GetSurveysUseCase
import com.ptut.insightify.domain.survey.usecase.RefreshSurveysUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getSurveysUseCase: GetSurveysUseCase,
    private val refreshSurveysUseCase: RefreshSurveysUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        loadSurveys()
    }

    fun onUiEvent(uiEvent: UiEvent) {
        when (uiEvent) {
            is UiEvent.RefreshSurveyList -> refreshSurveys()
            is UiEvent.OnErrorRetryClicked -> loadSurveys()
        }
    }

    private fun loadSurveys() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            getSurveysUseCase()
                .catch { exception ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            hasError = true,
                            errorType = handleNetworkError(exception),
                        )
                    }
                }
                .collectLatest { result ->
                    handleResult(result)
                }
        }
    }

    private fun handleResult(result: Result<ProfileWithSurveys, DataError.Network>) {
        when (result) {
            is Result.Success -> _uiState.value = _uiState.value.copy(
                isLoading = false,
                profileImageUrl = result.data.profile.profileImageUrl,
                surveys = flowOf(result.data.surveys),
                currentDate = result.data.profile.currentDate,
            )

            is Result.Error -> _uiState.value = _uiState.value.copy(
                isLoading = false,
                hasError = true,
                errorType = result.error,
            )
        }
    }

    private fun refreshSurveys() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            refreshSurveysUseCase()
        }
    }

    sealed class UiEvent {
        data object RefreshSurveyList : UiEvent()
        data object OnErrorRetryClicked : UiEvent()
    }

    data class UiState(
        val isLoading: Boolean = true, // Initially loading
        val hasError: Boolean = false,
        val errorType: DataError.Network? = null,
        val currentDate: String = "",
        val profileImageUrl: String = "",
        val surveys: Flow<PagingData<Survey>> = emptyFlow(),
    )
}
