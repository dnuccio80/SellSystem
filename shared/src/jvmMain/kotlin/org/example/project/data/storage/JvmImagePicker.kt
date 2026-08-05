package org.example.project.data.storage

import org.example.project.domain.models.ImageData
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter

class JvmImagePicker : ImagePicker {

    override suspend fun pickImage(): ImageData? {
        val chooser = JFileChooser()

        chooser.dialogTitle = "Seleccionar imagen"
        chooser.fileFilter = FileNameExtensionFilter(
            "Imágenes",
            "jpg",
            "jpeg",
            "png",
            "webp"
        )

        val result = chooser.showOpenDialog(null)

        if (result != JFileChooser.APPROVE_OPTION) {
            return null
        }

        val file = chooser.selectedFile

        return ImageData(
            bytes = file.readBytes(),
            extension = file.extension
        )
    }
}