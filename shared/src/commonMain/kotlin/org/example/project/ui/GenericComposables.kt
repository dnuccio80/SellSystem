package org.example.project.ui

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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
                    onValueChange = { }
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                IconButton(onClick = { }) {
                    Icon(Icons.Default.Notifications, contentDescription = "notificaciones", tint = Color.White)
                }
                Card(shape = CircleShape, elevation = CardDefaults.cardElevation(4.dp)) {
                    Image(painterResource(Res.drawable.woman_img), contentDescription = null,  modifier = Modifier.size(40.dp))
                }
            }
        }
        HorizontalDivider(Modifier.fillMaxWidth(), thickness = 1.dp, color = WhiteText)
    }

}

@Composable
fun SearchTextField(value:String, onValueChange:(String) -> Unit) {
    TextField(
        value = value,
        onValueChange = { onValueChange(it) },
        placeholder = { Text("Buscar...") },
        trailingIcon = {
            Icon(
                Icons.Filled.Search, contentDescription = null
            )
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

@Composable
fun GenericTextField(value: String, labelText: String, onValueChange: (String) -> Unit) {
    TextField(
        value = value,
        modifier = Modifier.fillMaxWidth(),
        onValueChange = { onValueChange(it) },
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
        )
    )

}

@Composable
fun GenericHeaderWithButtonAndSearch(title:String, description:String, buttonText:String, hasSearch: Boolean = true, onButtonClick: () -> Unit) {
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
            if(hasSearch) {
                SearchTextField("", onValueChange = { })
            }
        }
        GenericButton(
            text = buttonText
        ) {
            onButtonClick()
        }
    }

}

@Composable
fun SimpleGenericHeader(title:String, description:String) {
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
fun GenericButton(text: String, icon: ImageVector? = null, color: Color = SecondaryCardBackground, onClick: () -> Unit) {
    Button(onClick = { onClick() }, shape = RoundedCornerShape(4.dp), colors = ButtonDefaults.buttonColors(containerColor = color)) {
        if(icon != null) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text)
                Icon(icon, contentDescription = null)
            }
        }else {
            Text(text)
        }
    }
}

@Composable
fun AcceptDeclineButtons(onDismiss: () -> Unit, onAccept: () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            GenericButton("Cancelar", color = GrayText) { onDismiss() }
            GenericButton("Aceptar") { onAccept() }
        }
    }
}

@Composable
fun GenericScreenTitleHeaderWithButtons(mainTitle:String, description:String, firstButtonText:String, secondButtonText:String, buttonIcon: ImageVector? = null, onFirstButtonClick:() -> Unit, onSecondButtonClick:() -> Unit) {
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
