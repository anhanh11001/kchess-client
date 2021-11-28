package ui.users

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.width
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import common.RemoteUrlImage

@Composable
fun Avatar(
    imageUrl: String?,
    modifier: Modifier = Modifier
) {
    RemoteUrlImage(
        url = imageUrl,
        contentDescription = "Player avatar",
        OnImageLoading = {
            Box(
                modifier = Modifier.aspectRatio(1f),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        },
        OnImageLoadFailed = { DefaultAvatar(modifier = modifier) },
        modifier = modifier.aspectRatio(1f),
    )
}

@Composable
fun DefaultAvatar(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource("images/default-avatar.png"),
        contentDescription = "Default Avatar",
        modifier = modifier.aspectRatio(1f)
    )
}

@Preview
@Composable
fun MockAvatar() {
    Row {
        Avatar(
            imageUrl = "https://scontent-amt2-1.xx.fbcdn.net/v/t1.6435-9/80389858_3120199038206234_208919943855472640_n.jpg?_nc_cat=106&ccb=1-5&_nc_sid=09cbfe&_nc_ohc=iGADh6cRtdEAX-4xzpA&_nc_ht=scontent-amt2-1.xx&oh=592f472cc009a9286610e167f0b6077f&oe=61C77C44",
            modifier = Modifier.width(300.dp)
        )

        Avatar(
            imageUrl = null,
            modifier = Modifier.width(300.dp)
        )
    }
}