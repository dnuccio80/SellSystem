package org.example.project.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.example.project.ui.Capitalization.*
import org.example.project.ui.ext.capitalizeSentences
import org.example.project.ui.ext.capitalizeWords
import org.example.project.ui.ext.toPercentAdd
import org.example.project.ui.ext.toPercentOff
import org.example.project.ui.ext.toPrice
import org.example.project.ui.utils.AccentColor
import org.example.project.ui.utils.GrayText
import org.example.project.ui.utils.GreenText
import org.example.project.ui.utils.PrimaryBackground
import org.example.project.ui.utils.PrimaryCardBackground
import org.example.project.ui.utils.SecondaryCardBackground
import org.example.project.ui.utils.WhiteText
import org.jetbrains.compose.resources.painterResource
import sellsystem.shared.generated.resources.Res
import sellsystem.shared.generated.resources.woman_img

@Composable
fun MainHeader() {
    Column(modifier = Modifier.safeContentPadding().fillMaxWidth()) {
        HorizontalDivider(Modifier.fillMaxWidth(), thickness = 1.dp, color = WhiteText)
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    "INVENTORY",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White
                )
                SearchTextField(
                    value = "",
                    onDelete = { },
                    onValueChange = { }
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                IconButton(onClick = { }) {
                    Icon(
                        Icons.Default.Notifications,
                        contentDescription = "notificaciones",
                        tint = Color.White
                    )
                }
                Card(shape = CircleShape, elevation = CardDefaults.cardElevation(4.dp)) {
                    Image(
                        painterResource(Res.drawable.woman_img),
                        contentDescription = null,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
        }
        HorizontalDivider(Modifier.fillMaxWidth(), thickness = 1.dp, color = WhiteText)
    }

}

@Composable
fun SearchTextField(value: String,capitalization: Capitalization = SENTENCES, onDelete: () -> Unit, onValueChange: (String) -> Unit) {

    val capitalizedValue = when(capitalization) {
        WORDS -> value.capitalizeWords()
        SENTENCES -> value.capitalizeSentences()
        NONE -> value
    }

    TextField(
        value = capitalizedValue,
        onValueChange = { onValueChange(it) },
        placeholder = { Text("Buscar...") },
        trailingIcon = {
            if (value.isBlank()) {
                Icon(
                    Icons.Filled.Search, contentDescription = null
                )
            } else {
                Icon(
                    Icons.Filled.Close,
                    contentDescription = null,
                    modifier = Modifier.clickable { onDelete() }.pointerHoverIcon(
                        PointerIcon.Hand
                    )
                )
            }

        },
        shape = RoundedCornerShape(4.dp),
        colors = TextFieldDefaults.colors(
            unfocusedTextColor = Color.White,
            focusedTextColor = Color.White,
            focusedPlaceholderColor = WhiteText,
            unfocusedPlaceholderColor = WhiteText,
            focusedTrailingIconColor = WhiteText,
            unfocusedTrailingIconColor = WhiteText,
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
            focusedIndicatorColor = GreenText,
            unfocusedIndicatorColor = GrayText,
            cursorColor = GreenText
        )
    )
}

enum class Capitalization {
    WORDS, SENTENCES, NONE
}

@Composable
fun GenericTextField(
    value: String,
    labelText: String,
    onlyNumbers: Boolean = false,
    isPrice: Boolean = false,
    isPercentOff: Boolean = false,
    isPercentAdd: Boolean = false,
    modifier: Modifier = Modifier.fillMaxWidth(),
    capitalizationMethod: Capitalization = SENTENCES,
    trailingIcon:@Composable (() -> Unit)? = null,
    onValueChange: (String) -> Unit,
) {

    val interactionSource = remember { MutableInteractionSource() }
    val isFocused = interactionSource.collectIsFocusedAsState()

    TextField(
        value = when {
            isPrice && !isFocused.value && value.isNotBlank() -> value.toLong().toPrice()
            isPercentOff && !isFocused.value && value.isNotBlank() -> value.toLong().toPercentOff()
            isPercentAdd && !isFocused.value && value.isNotBlank() -> value.toLong().toPercentAdd()
            else -> value
        },
        modifier = modifier,
        onValueChange = { valueChange ->
            if (onlyNumbers) {
                val newVal = valueChange.filter { it.isDigit() }
                onValueChange(newVal)
            } else {
                val capitalized = when (capitalizationMethod) {
                    WORDS -> valueChange.capitalizeWords()
                    SENTENCES -> valueChange.capitalizeSentences()
                    NONE -> valueChange
                }
                onValueChange(capitalized)
            }
        },
        interactionSource = interactionSource,
        label = { Text(labelText) },
        shape = RoundedCornerShape(4.dp),
        colors = TextFieldDefaults.colors(
            unfocusedTextColor = Color.White,
            focusedTextColor = Color.White,
            focusedPlaceholderColor = WhiteText,
            unfocusedPlaceholderColor = WhiteText,
            focusedTrailingIconColor = WhiteText,
            unfocusedTrailingIconColor = WhiteText,
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
            focusedIndicatorColor = GreenText,
            unfocusedIndicatorColor = GrayText,
            cursorColor = GreenText,
            focusedLabelColor = GreenText,
            unfocusedLabelColor = GrayText
        ),
        trailingIcon = trailingIcon,
        singleLine = true,
        maxLines = 1
    )

}

@Composable
fun GenericHeaderWithButtonAndSearch(
    title: String,
    description: String,
    buttonText: String,
    searchValue: String = "",
    onSearchValueChange: (String) -> Unit = {},
    hasSearch: Boolean = true,
    buttonColor: Color = SecondaryCardBackground,
    querySearchCapitalization: Capitalization = SENTENCES,
    onDeleteQuerySearch: () -> Unit = {},
    onButtonClick: () -> Unit,
) {
    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column {
                Text(
                    title,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White
                )
                Text(
                    description,
                    color = WhiteText,
                    style = MaterialTheme.typography.labelMedium
                )
            }
            if (hasSearch) {
                SearchTextField(
                    searchValue,
                    onDelete = { onDeleteQuerySearch() },
                    onValueChange = { onSearchValueChange(it) },
                    capitalization = querySearchCapitalization
                )
            }
        }
        GenericButton(
            text = buttonText,
            color = buttonColor
        ) {
            onButtonClick()
        }
    }

}

@Composable
fun CheckBoxItem(name: String, checked: Boolean, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable {
            onClick()
        }) {
        Checkbox(
            checked = checked,
            onCheckedChange = { onClick() },
            colors = CheckboxDefaults.colors(
                checkedColor = GreenText,
                uncheckedColor = GrayText
            )
        )
        Text(name, color = Color.White, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun SimpleGenericHeader(title: String, description: String) {
    Column {
        Text(
            title,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineSmall,
            color = Color.White
        )
        Text(
            description,
            color = WhiteText,
            style = MaterialTheme.typography.labelMedium
        )
    }
}

@Composable
fun RadioButtonRowWithText(name: String, selected: String, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable { onClick() }.pointerHoverIcon(
            PointerIcon.Hand
        )
    ) {
        RadioButton(
            selected == name, onClick = { onClick() }, colors = RadioButtonDefaults.colors(
                selectedColor = GreenText,
                unselectedColor = GrayText
            )
        )
        Text(
            name,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            style = MaterialTheme.typography.titleSmall
        )
    }
}

@Composable
fun CardTitleCentered(title: String) {
    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Text(
            title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

@Composable
fun GenericButton(
    text: String,
    icon: ImageVector? = null,
    color: Color = SecondaryCardBackground,
    onClick: () -> Unit,
) {
    Button(
        onClick = { onClick() },
        shape = RoundedCornerShape(4.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color)
    ) {
        if (icon != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text)
                Icon(icon, contentDescription = null)
            }
        } else {
            Text(text)
        }
    }
}

@Composable
fun AcceptDeclineButtons(
    acceptText: String = "Aceptar",
    declineText: String = "Cancelar",
    acceptColor: Color = GreenText,
    declineColor: Color = GrayText,
    onDismiss: () -> Unit,
    onAccept: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            GenericButton(declineText, color = declineColor) { onDismiss() }
            GenericButton(acceptText, color = acceptColor) { onAccept() }
        }
    }
}

@Composable
fun GenericScreenTitleHeaderWithButtons(
    mainTitle: String,
    description: String,
    firstButtonText: String,
    secondButtonText: String,
    buttonIcon: ImageVector? = null,
    onFirstButtonClick: () -> Unit,
    onSecondButtonClick: () -> Unit,
) {
    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                mainTitle,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White
            )
            Text(
                description,
                color = WhiteText,
                style = MaterialTheme.typography.labelMedium
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            GenericButton(firstButtonText, buttonIcon) { onFirstButtonClick() }
            GenericButton(secondButtonText) { onSecondButtonClick() }
        }
    }
}

@Composable
fun RowWithMidBodyAndDescription(title: String, description:String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = Color.White)
        Text(description, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = GreenText)
    }
}

@Composable
fun RowWithMidTitleAndDescription(title: String, description:String, modifier: Modifier = Modifier) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp), modifier = modifier) {
        Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = Color.White)
        Text(description, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = GreenText)
    }
}

@Composable
fun SummaryCard(
    icon: ImageVector,
    title: String,
    description: String,
    amount: Long,
    buttonText: String,
    modifier: Modifier,
    onClick: () -> Unit,
) {

    val amountColor = when {
        amount == 0L -> WhiteText
        amount > 0L -> GreenText
        else -> AccentColor
    }
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = PrimaryCardBackground),
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            SummaryCardHeader(icon, title, description)
            Text(
                amount.toPrice(),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineSmall,
                color = amountColor
            )
            Button(
                onClick = { onClick() },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues(horizontal = 0.dp),
                shape = RoundedCornerShape(4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(buttonText)
                    Icon(
                        Icons.AutoMirrored.Default.ArrowForwardIos,
                        contentDescription = null,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun SummaryCardHeader(icon: ImageVector, title: String, description: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier.size(50.dp),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = SecondaryCardBackground,
                contentColor = Color.White
            )
        ) {
            Icon(
                icon,
                contentDescription = null,
                modifier = Modifier.fillMaxSize().padding(4.dp)
            )
        }
        Column {
            Text(
                title,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
            Text(description, style = MaterialTheme.typography.labelLarge, color = WhiteText)
        }
    }
}


@Composable
fun ScreenContainer(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxSize(),
        shape = RectangleShape,
        colors = CardDefaults.cardColors(containerColor = PrimaryBackground)
    ) {
        content()
    }
}
