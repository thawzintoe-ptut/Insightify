package com.ptut.insightify.common

import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME

@Qualifier
@Retention(RUNTIME)
annotation class Dispatcher(val insightifyDispatchers: InsightifyDispatchers)

enum class InsightifyDispatchers {
    Default,
    IO,
    Main,
}
