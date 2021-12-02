package ui.verification

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun SignupScreen(
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize().padding(32.dp)
    ) {
        Image(
            painter = painterResource("icons/chess_game_icon.svg"),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.fillMaxWidth(0.4f)
        )
        SignupSection(
            modifier = Modifier.fillMaxWidth(0.4f)
        )
    }
}

@Composable
fun SignupSection(
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "Sign up to create a new account",
            modifier = Modifier.padding(bottom = 8.dp).align(Alignment.CenterHorizontally)
        )

        TextField(
            value = "",
            onValueChange = {

            },
            label = { Text("Username") },
            modifier = Modifier.padding(bottom = 8.dp).fillMaxWidth()
        )
        TextField(
            value = "",
            onValueChange = {

            },
            label = { Text("Password") },
            modifier = Modifier.padding(bottom = 8.dp).fillMaxWidth()
        )

        Button(
            onClick = {

            },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        ) {
            Text("Sign up")
        }
    }
}