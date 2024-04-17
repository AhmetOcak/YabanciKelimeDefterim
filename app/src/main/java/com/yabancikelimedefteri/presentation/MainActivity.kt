package com.yabancikelimedefteri.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import com.yabancikelimedefteri.core.navigation.MyVocabularyApp
import com.yabancikelimedefteri.core.ui.theme.YabanciKelimeDefteriTheme
import com.yabancikelimedefteri.presentation.game.games.quiz.QuizGameScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            //MyVocabularyApp()

            YabanciKelimeDefteriTheme {
                Surface {
                    QuizGameScreen(upPress = { /*TODO*/ }, onReturnGamesScreenClick = {})
                }
            }
        }
    }
}