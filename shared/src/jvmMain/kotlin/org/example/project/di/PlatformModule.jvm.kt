package org.example.project.di

import org.example.project.data.database.getDataBase
import org.example.project.data.storage.ImagePicker
import org.example.project.data.storage.ImageStorage
import org.example.project.data.storage.JvmImagePicker
import org.example.project.data.storage.JvmImageStorage
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule(): Module {
    return module {
        single { getDataBase() }
        single<ImageStorage> {
            JvmImageStorage()
        }
        single<ImagePicker> {
            JvmImagePicker()
        }
    }

}