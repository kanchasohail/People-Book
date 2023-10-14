package com.social.people_book.views.settings_screen

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.social.people_book.ui.layout.MyText
import com.social.people_book.ui.theme.RobotoFontFamily


@Composable
fun AboutUsCard(modifier: Modifier = Modifier , textColor: Color) {
    val context = LocalContext.current

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ), elevation = CardDefaults.cardElevation(
            8.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 12.dp, start = 14.dp, end = 8.dp)
        ) {
            MyText(text = "About Us", fontSize = 22.sp, color = textColor)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, top = 8.dp, end = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val annotatedString = buildAnnotatedString {
                    pushStringAnnotation(tag = "policy", annotation = "https://shimul-riley.github.io/The-Ordinary-Android-Dev/#privacy")
                    append("Privacy Policy\n")
                    pop()

                    withStyle(
                        style = SpanStyle(
                            color = Color.Gray,
                            fontSize = 16.sp,
                            fontFamily = RobotoFontFamily,
                        )
                    ) {
                        append(" &\n")
                    }

                    pushStringAnnotation(tag = "terms", annotation = "https://shimul-riley.github.io/The-Ordinary-Android-Dev/#terms")
                    append("Terms and Conditions")
                    pop()
                }

                ClickableText(
                    text = annotatedString,
                    onClick = { offset ->
                        annotatedString.getStringAnnotations(tag = "policy", start = offset, end = offset)
                            .firstOrNull()?.let {
                                val urlIntent = Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse(it.item)
                                )
                                context.startActivity(urlIntent)
                            }

                        annotatedString.getStringAnnotations(tag = "terms", start = offset, end = offset)
                            .firstOrNull()?.let {
                                val urlIntent = Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse(it.item)
                                )
                                context.startActivity(urlIntent)
                            }
                    },
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontFamily = RobotoFontFamily,
                        textAlign = TextAlign.Center
                    ),
                )
            }
        }
    }
}