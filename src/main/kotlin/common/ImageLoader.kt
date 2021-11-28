package common

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import io.ktor.client.*
import io.ktor.client.request.*
import org.jetbrains.skija.Image
import org.koin.java.KoinJavaComponent.inject
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.layout.ContentScale
import io.ktor.client.engine.cio.*

suspend fun loadPicture(url: String): Result<ImageBitmap> {
    val client: HttpClient by inject(HttpClient::class.java)
    val image = client.use {
        it.get<ByteArray>(url)
    }
    return try {
        Result.success(Image.makeFromEncoded(image).asImageBitmap())
    } catch (e: Exception) {
        Result.failure(e)
    }
}

@Composable
fun RemoteUrlImage(
    url: String?,
    contentDescription: String? = null,
    OnImageLoading: @Composable () -> Unit,
    OnImageLoadFailed: @Composable () -> Unit,
    modifier: Modifier
) {
    var isLoading by remember { mutableStateOf(true) }
    var hasFail by remember { mutableStateOf(false) }
    var imageBitmap: ImageBitmap? by remember { mutableStateOf(null) }

    LaunchedEffect(url) {
        isLoading = true
        if (url != null) {
            loadPicture(url)
                .onSuccess {
                    hasFail = false
                    imageBitmap = it
                }.onFailure {
                    hasFail = true
                }
        } else {
            hasFail = true
        }
        isLoading = false
    }

    when {
        isLoading -> OnImageLoading()
        hasFail -> OnImageLoadFailed()
        else -> Image(
            bitmap = requireNotNull(imageBitmap),
            contentDescription = contentDescription,
            modifier = modifier,
            contentScale = ContentScale.Crop
        )
    }
}