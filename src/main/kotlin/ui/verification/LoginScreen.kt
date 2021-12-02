package ui.verification

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
fun LoginScreen(
    onSignUpNewUserSelected: () -> Unit,
    onLoggedIn: () -> Unit,
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
        LoginWithCredentialsSection(
            onLoggedInAsGuestSelected = onLoggedIn,
            modifier = Modifier.fillMaxWidth(0.4f)
        )
    }
}

@Composable
fun LoginWithCredentialsSection(
    onLoggedInAsGuestSelected: () -> Unit,
    modifier: Modifier = Modifier
) {

    var stayLoggedInChecked by remember { mutableStateOf(false) }
    Column(modifier = modifier) {
        Text(
            text = "Log in to your account to play chess",
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
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = stayLoggedInChecked,
                onCheckedChange = {
                    stayLoggedInChecked = it
                },
                modifier = Modifier.padding(end = 2.dp)
            )
            Text("Stay logged in to your account")
        }

        Button(
            onClick = {

            },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        ) {
            Text("Log in with Credentials")
        }

        Divider(
            thickness = 2.dp,
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )
        Button(
            onClick = onLoggedInAsGuestSelected,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Log in as Guest")
        }
    }
}