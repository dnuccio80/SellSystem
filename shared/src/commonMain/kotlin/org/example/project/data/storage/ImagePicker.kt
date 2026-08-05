package org.example.project.data.storage

import org.example.project.domain.models.ImageData

interface ImagePicker {
    suspend fun pickImage(): ImageData?
}