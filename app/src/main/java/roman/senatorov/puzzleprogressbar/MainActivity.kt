package roman.senatorov.puzzleprogressbar

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import roman.senatorov.puzzleprogress.PuzzleProgressView
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var puzzleProgressView: PuzzleProgressView

    private var isAnimate: Boolean = false
    private var timer: Timer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        puzzleProgressView = findViewById(R.id.puzzle_progress_view)
        puzzleProgressView.setHollowStyle(false)
        puzzleProgressView.alpha = 0.70f
        puzzleProgressView.setSquareColor(Color.WHITE)
        puzzleProgressView.setNumberOfSquaresOnMinSide(15)
        puzzleProgressView.setFadeInDuration(250)
        puzzleProgressView.setFadeOutDuration(250)
        puzzleProgressView.useFadeInFadeOutAnimation(true)
        //puzzleProgressView.startAnimation()

        timer = Timer()
        timer?.schedule(object : TimerTask() {
            override fun run() {
                if(isAnimate){
                    runBlocking {
                        withContext(Dispatchers.Main) {
                            isAnimate = false
                            puzzleProgressView.stopAnimation()
                        }
                    }
                }
                else {
                    runBlocking {
                        withContext(Dispatchers.Main) {
                            isAnimate = true
                            puzzleProgressView.startAnimation()
                        }
                    }
                }
            }
        },
        1000L,2000L )
    }

}