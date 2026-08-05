package org.example.project.data.storage

import java.io.File
import java.util.UUID

class JvmImageStorage : ImageStorage {
    private val imageDirectory = File(
        System.getProperty("user.home"),
        ".yourApp/product-images"
    )

    override suspend fun saveImage(
        image: ByteArray,
        extension: String
    ): String {

        imageDirectory.mkdirs()

        val fileName = "${UUID.randomUUID()}.$extension"
        val file = File(imageDirectory, fileName)

        file.writeBytes(image)

        return file.absolutePath
    }

    override suspend fun deleteImage(path: String) {
        File(path).delete()
    }
}