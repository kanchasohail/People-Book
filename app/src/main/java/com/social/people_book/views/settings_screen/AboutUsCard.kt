package com.social.people_book.views.settings_screen

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.social.people_book.R
import com.social.people_book.ui.layout.MyText
import com.social.people_book.ui.theme.OutfitFontFamily
import com.social.people_book.ui.theme.RobotoFontFamily


@Composable
fun AboutUsCard(modifier: Modifier = Modifier, textColor: Color, viewModel: SettingsViewModel) {
    val context = LocalContext.current

    val privacyPolicyUrl = context.getString(R.string.privacy_policy)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ), elevation = CardDefaults.cardElevation(
            4.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 12.dp, start = 14.dp, end = 8.dp)
        ) {
            MyText(text = "About", fontSize = 20.sp, color = textColor)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp, top = 4.dp, end = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

//                val annotatedString = buildAnnotatedString {
//                    append("We are committed to protect all the information you put in the app. No one, not even the developers have access to your information. Check our ")
//                    pushStringAnnotation(
//                        tag = "policy",
//                        annotation = "https://shimul-riley.github.io/The-Ordinary-Android-Dev/#privacy"
//                    )
//                    withStyle(
//                        style = SpanStyle(
//                            fontSize = 16.sp,
//                            color = MaterialTheme.colorScheme.primary,
//                            fontFamily = OutfitFontFamily
//                        )
//                    ) {
//                        append("policy ")
//                    }
//                    pop()
//                    append("to learn how seriously we take your privacy")
//                }
//
//                ClickableText(
//                    text = annotatedString,
//                    onClick = { offset ->
//                        annotatedString.getStringAnnotations(
//                            tag = "policy",
//                            start = offset,
//                            end = offset
//                        )
//                            .firstOrNull()?.let {
//                                val urlIntent = Intent(
//                                    Intent.ACTION_VIEW,
//                                    Uri.parse(it.item)
//                                )
//                                context.startActivity(urlIntent)
//                            }
//                    },
//                    style = TextStyle(
//                        fontSize = 16.sp,
//                        color = textColor.copy(.8f),
//                        fontFamily = OutfitFontFamily,
//                        lineHeight = 24.sp
//                    ),
//                )

                MyText(
                    text = "We are committed to protect all the information you put in the app. No one, not even the developers have access to your information. Check our policy to learn how seriously we take your privacy",
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = textColor.copy(.8f),
                        fontFamily = OutfitFontFamily,
                        lineHeight = 24.sp
                    ),
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = {
                        val urlIntent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(privacyPolicyUrl)
                        )
                        context.startActivity(urlIntent)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    MyText(text = "Privacy Policy")
                    Spacer(modifier = Modifier.width(3.dp))
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = "Privacy Policy",
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))
                OutlinedButton(
                    onClick = {
                        viewModel.contactUsEmail(context)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    MyText(text = "Contact Us")
                    Spacer(modifier = Modifier.width(3.dp))
                    Icon(
                        imageVector = Icons.Outlined.MailOutline,
                        contentDescription = "Contact Us",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}