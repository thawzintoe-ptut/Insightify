package com.ptut.insightify.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ptut.insightify.common.error.DataError
import com.ptut.insightify.common.util.handleNetworkError
import com.ptut.insightify.domain.survey.model.Survey
import com.ptut.insightify.domain.survey.usecase.GetSurveysUseCase
import com.ptut.insightify.domain.survey.usecase.RefreshSurveysUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
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
            UiEvent.Scroll -> loadSurveys()
        }
    }

    private fun loadSurveys() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            getSurveysUseCase.invoke()
                .catch { exception ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            hasError = true,
                            errorType = handleNetworkError(exception),
                        )
                    }
                }
                .collectLatest { profileWithSurveys ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            hasError = false,
                            currentDate = profileWithSurveys.profile.currentDate,
                            name = profileWithSurveys.profile.name,
                            profileImageUrl = profileWithSurveys.profile.profileImageUrl,
                            surveys = flowOf(profileWithSurveys.surveys)
                                .cachedIn(viewModelScope),
                        )
                    }
                }
        }
    }

    private fun refreshSurveys() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            refreshSurveysUseCase()
        }
    }

    sealed interface UiEvent {
        data object RefreshSurveyList : UiEvent
        data object OnErrorRetryClicked : UiEvent
        data object Scroll : UiEvent
    }

    data class UiState(
        val isLoading: Boolean = true, // Initially loading
        val hasError: Boolean = false,
        val errorType: DataError.Network? = null,
        val currentDate: String = "",
        val name: String = "",
        val profileImageUrl: String = "",
        val surveys: Flow<PagingData<Survey>> = MutableStateFlow(PagingData.empty()),
    )
}
