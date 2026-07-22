package org.example.project.di

import org.example.project.data.database.getDataBase
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule(): Module {
    return module {
        single { getDataBase() }
    }
}