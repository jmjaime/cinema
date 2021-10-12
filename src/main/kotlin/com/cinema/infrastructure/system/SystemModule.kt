package com.cinema.infrastructure.system

import org.koin.dsl.module
import java.time.Clock

val systemModule = module {
    single { Clock.systemUTC() }
}