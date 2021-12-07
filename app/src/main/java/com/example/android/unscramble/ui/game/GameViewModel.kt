package com.example.android.unscramble.ui.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.ArrayDeque

class GameViewModel : ViewModel() {
    private val _score = MutableLiveData(0)
    val score: LiveData<Int> get() = _score

    private val _currentWordCount = MutableLiveData(0)
    val currentWordCount: LiveData<Int> get() = _currentWordCount

    private val _currentScrambledWord = MutableLiveData<String>()
    val currentScrambledWord: LiveData<String> get() = _currentScrambledWord

    private val randomIndexes = ArrayDeque(getRandomIndexes())
    private lateinit var currentWord: String

    init {
        getNextWord()
    }

    fun nextWord(): Boolean {
        return if (_currentWordCount.value!! < MAX_NO_OF_WORDS) {
            getNextWord()
            true
        } else false
    }

    private fun getNextWord() {
        currentWord = allWordsList[randomIndexes.pop()]
        scrambleWord()
        _currentWordCount.value = (_currentWordCount.value)?.inc()
    }

    private fun getRandomIndexes() = (0..allWordsList.lastIndex).shuffled()

    private fun scrambleWord() {
        val scramble = currentWord.toCharArray()

        do {
            scramble.shuffle()
        } while (String(scramble) == currentWord)

        _currentScrambledWord.value = String(scramble)
    }

    private fun increaseScore() {
        _score.value = (_score.value)?.plus(SCORE_INCREASE)
    }

    fun isUserWordCorrect(playerWord: String): Boolean {
        return if (playerWord.equals(currentWord, true)) {
            increaseScore()
            true
        } else false
    }

    fun reinitializeData() {
        _score.value = 0
        _currentWordCount.value = 0
        if (randomIndexes.size < MAX_NO_OF_WORDS) {
            randomIndexes.clear()
            randomIndexes.addAll(getRandomIndexes())
        }
        getNextWord()
    }
}