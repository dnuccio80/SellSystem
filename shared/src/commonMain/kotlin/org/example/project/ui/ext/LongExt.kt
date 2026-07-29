package org.example.project.ui.ext

fun Long.toPrice(): String {
    val number = this.toString().reversed()
        .chunked(3)
        .joinToString(".")
        .reversed()
    return "$ $number"
}

fun Long.toPercentOff():String {
    return "$this% off"
}

fun Long.toPercentAdd():String {
    return "+$this%"
}