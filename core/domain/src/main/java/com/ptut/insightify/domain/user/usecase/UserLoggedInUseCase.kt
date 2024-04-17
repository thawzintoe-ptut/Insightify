package com.ptut.insightify.domain.user.usecase

import com.ptut.insightify.domain.user.repository.UserTokenProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserLoggedInUseCase @Inject constructor(
	private val userTokenProvider: UserTokenProvider
) {
	operator fun invoke(): Flow<Boolean> = flow {
		emit(userTokenProvider.getAccessToken().isNotBlank())
	}
}