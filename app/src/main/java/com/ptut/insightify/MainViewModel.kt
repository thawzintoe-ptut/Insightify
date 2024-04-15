package com.ptut.insightify

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ptut.insightify.domain.user.usecase.UserLoggedInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
	private val userLoggedInUseCase: UserLoggedInUseCase
): ViewModel() {
	val isLoggedIn = userLoggedInUseCase()
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(500L),
			initialValue = false
		)
}