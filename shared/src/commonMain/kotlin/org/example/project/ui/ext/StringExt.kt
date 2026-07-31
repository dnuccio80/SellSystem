package org.example.project.ui.ext

fun String.capitalizeWords(): String =
    split(" ").joinToString(" ") { word ->
        word.replaceFirstChar { char ->
            if (char.isLowerCase()) char.titlecase()
            else char.toString()
        }
    }

//fun String.toPrice(): String {
//    val number = this.toLongOrNull()?:0L
//        number.reversed()
//        .chunked(3)
//        .joinToString(".")
//        .reversed()
//    return "$ $number"
//}

fun String.capitalizeSentences(): String {
    val result = StringBuilder()
    var capitalizeNext = true

    for (char in this) {
        when {
            capitalizeNext && char.isLetter() -> {
                result.append(char.titlecaseChar())
                capitalizeNext = false
            }
            else -> result.append(char)
        }

        if (char == '.' || char == '!' || char == '?') {
            capitalizeNext = true
        }
    }

    return result.toString()
}