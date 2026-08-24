package com.lionsgates.notes.presentation.add_edit_note.components.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily

@Composable
fun TransparentHintTextFields(
    modifier: Modifier = Modifier,
    text: String,
    hint: String,
    textTags: String = "",
    isHintVisible: Boolean = true,
    singleLine: Boolean = false,
    textStyle: TextStyle = TextStyle(),
    fontFamily: FontFamily,
    onValueChange: (String) -> Unit,
    onFocusChange: (FocusState) -> Unit
) {
    Box(modifier = modifier) {
        BasicTextField(
            value = text,
            onValueChange = onValueChange,
            textStyle = textStyle,
            singleLine = singleLine,
            modifier = Modifier
                .fillMaxWidth()
                .testTag(textTags)
                .onFocusChanged {
                    onFocusChange(it)
                }
        )
        if (isHintVisible) {
            Text(
                text = hint,
                color = Color.DarkGray,
                style = textStyle,
                fontFamily = fontFamily
            )
        }
    }
}