package org.example.project.data.storage

interface ImageStorage {

    suspend fun saveImage(
        image: ByteArray,
        extension: String
    ): String

    suspend fun deleteImage(
        path: String
    )
}