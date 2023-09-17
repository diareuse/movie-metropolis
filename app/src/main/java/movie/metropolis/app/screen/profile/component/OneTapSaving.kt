package movie.metropolis.app.screen.profile.component

import android.content.Context
import androidx.activity.compose.LocalActivityResultRegistryOwner
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.ActivityResultRegistryOwner
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.*
import com.google.android.gms.auth.api.identity.CredentialSavingClient
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SavePasswordRequest
import com.google.android.gms.auth.api.identity.SignInPassword
import kotlinx.coroutines.tasks.await

@Composable
fun rememberOneTapSaving(
    context: Context = LocalContext.current,
    owner: ActivityResultRegistryOwner? = LocalActivityResultRegistryOwner.current
): OneTapSaving {
    return remember(context, owner) {
        val client = Identity.getCredentialSavingClient(context)
        OneTapSaving(client, owner?.activityResultRegistry)
    }
}

class OneTapSaving(
    private val client: CredentialSavingClient,
    private val registry: ActivityResultRegistry?
) {

    suspend fun save(email: String, password: String) {
        val registry = registry ?: return
        val contract = ActivityResultContracts.StartIntentSenderForResult()
        val signInPassword = SignInPassword(email, password)
        val passwordRequest = SavePasswordRequest.builder()
            .setSignInPassword(signInPassword)
            .build()
        val result = client.savePassword(passwordRequest)
            .runCatching { await() }.getOrNull() ?: return

        val launcher = registry.register("one-save", contract) {
            // don't care
        }
        val senderRequest = IntentSenderRequest.Builder(result.pendingIntent).build()
        launcher.launch(senderRequest)
    }

}