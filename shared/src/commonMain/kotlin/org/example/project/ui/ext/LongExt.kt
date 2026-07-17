package org.example.project.ui.ext

fun Long.toPrice(): String {
    val number = this.toString().reversed()
        .chunked(3)
        .joinToString(".")
        .reversed()
    return "$ $number"
}