package movie.metropolis.app.screen.profile.component

import android.app.Activity.RESULT_OK
import android.content.Context
import androidx.activity.compose.LocalActivityResultRegistryOwner
import androidx.activity.result.ActivityResultRegistryOwner
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.*
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.PasswordRequestOptions
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInCredential
import kotlinx.coroutines.tasks.await
import java.net.PasswordAuthentication

@Composable
fun requestOneTapAsState(
    context: Context = LocalContext.current,
    owner: ActivityResultRegistryOwner? = LocalActivityResultRegistryOwner.current
): State<PasswordAuthentication?> {
    val state = remember { mutableStateOf(null as PasswordAuthentication?) }
    val request = remember {
        val options = PasswordRequestOptions.builder()
            .setSupported(true)
            .build()
        BeginSignInRequest.builder()
            .setPasswordRequestOptions(options)
            .setAutoSelectEnabled(true)
            .build()
    }
    val client = remember(context) { Identity.getSignInClient(context) }
    LaunchedEffect(request) {
        val intent = client.beginSignIn(request).runCatching { await() }
        if (!intent.isSuccess || owner == null) {
            return@LaunchedEffect
        }
        val result = intent.getOrThrow()
        val contract = ActivityResultContracts.StartIntentSenderForResult()
        val launcher = owner.activityResultRegistry.register("one-tap", contract) {
            state.value = when (it.resultCode) {
                RESULT_OK -> client
                    .getSignInCredentialFromIntent(it.data)
                    .asPasswordAuthentication()

                else -> null
            }
        }
        val senderRequest = IntentSenderRequest.Builder(result.pendingIntent)
            .build()
        launcher.launch(senderRequest)
    }

    return state
}

private fun SignInCredential.asPasswordAuthentication(): PasswordAuthentication {
    checkNotNull(password)
    return PasswordAuthentication(id, password?.toCharArray())
}