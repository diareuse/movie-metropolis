package movie.metropolis.app

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import movie.metropolis.app.screen.Navigation
import movie.metropolis.app.theme.Theme

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Theme {
                Navigation()
            }
        }
    }

}