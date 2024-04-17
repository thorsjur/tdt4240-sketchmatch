package com.groupfive.sketchmatch.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.groupfive.sketchmatch.Difficulty
import com.groupfive.sketchmatch.Player
import com.groupfive.sketchmatch.WordRepository
import com.groupfive.sketchmatch.communication.MessageClient
import com.groupfive.sketchmatch.navigator.Screen
import io.ak1.drawbox.DrawController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DrawViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    companion object {
        const val MAX_ROUNDS = 5
    }

    private val roomId: String = checkNotNull(savedStateHandle["roomId"])

    private val _showWordDialog = mutableStateOf(true)
    val showWordDialog: State<Boolean> = _showWordDialog

    private val _currentWord = mutableStateOf("")
    val currentWord: State<String> = _currentWord

    private val _currentGuess = mutableStateOf("")
    val currentGuess: State<String> = _currentGuess

    private val _currentRound = mutableIntStateOf(3)
    val currentRound: State<Int> = _currentRound

    private val _timeCount = mutableIntStateOf(59)
    val timeCount: State<Int> = _timeCount

    private val _isTimerRunning = mutableStateOf(false)
    private var _isColorBarVisible = mutableStateOf(false)
    val isColorBarVisible: State<Boolean> = _isColorBarVisible

    private var _isSizePickerVisible = mutableStateOf(false)
    val isSizePickerVisible: State<Boolean> = _isSizePickerVisible

    private val _currentColor = mutableStateOf(Color.Black)

    private val _players = mutableStateOf(List(5) { Player(it, false) })
    val players: State<List<Player>> = _players

    private val _easyWord = mutableStateOf("")
    val easyWord: State<String> = _easyWord

    private val _mediumWord = mutableStateOf("")
    val mediumWord: State<String> = _mediumWord

    private val _hardWord = mutableStateOf("")
    val hardWord: State<String> = _hardWord

    private val _isDrawing = mutableStateOf(true)
    val isDrawing: State<Boolean> = _isDrawing

    private val client = MessageClient.getInstance()

    // Load initial words
    init {
        generateWords()
    }

    fun submitGuess() {
        val guess = currentGuess.value
        // TODO: Add the required functionality to the server for handling guesses.
    }

    fun subscribeToRoom(controller: DrawController) = client.subscribeToRoom(
        roomId = roomId.toInt()
    ) { controller.importPath(it) }

    fun unsubscribeFromRoom() = client.unsubscribeFromRoom(roomId.toInt())

    fun publishFullDrawBoxPayload(controller: DrawController) {
        val payload = controller.exportPath()
        client.publishFullDrawBoxPayload(roomId.toInt(), payload)
    }

    private suspend fun handleTimer() {
        while (_timeCount.intValue > 0 && _isTimerRunning.value) {
            delay(1000)
            _timeCount.value--
        }
        _players.value = _players.value.map { it.copy(isComplete = true) }
    }

    fun onWordChosen(word: String) {
        viewModelScope.launch {
            _currentWord.value = word
            _showWordDialog.value = false
            toggleTimerRunning()
        }
    }

    private fun toggleTimerRunning() {
        viewModelScope.launch {
            _isTimerRunning.value = !_isTimerRunning.value
            handleTimer()
        }
    }

    fun toggleIsDrawing() {
        _isDrawing.value = !_isDrawing.value
    }

    fun toggleColorBarVisibility() {
        if (_isSizePickerVisible.value) {
            _isSizePickerVisible.value = false
        }
        _isColorBarVisible.value = !_isColorBarVisible.value
    }

    fun toggleSizePickerVisibility() {
        if (_isColorBarVisible.value) {
            _isColorBarVisible.value = false
        }
        _isSizePickerVisible.value = !_isSizePickerVisible.value
    }

    fun hideToolbars() {
        _isColorBarVisible.value = false
        _isSizePickerVisible.value = false
    }

    fun changeColor(color: Color) {
        _currentColor.value = color
    }

    fun dismissWordDialog() {
        _showWordDialog.value = false
    }

    fun setCurrentGuess(guess: String) {
        _currentGuess.value = guess
    }

    fun generateWords() {
        viewModelScope.launch {
            _easyWord.value = WordRepository.getRandomWord(Difficulty.EASY)
            _mediumWord.value = WordRepository.getRandomWord(Difficulty.MEDIUM)
            _hardWord.value = WordRepository.getRandomWord(Difficulty.HARD)
        }
    }

    fun goBackToMainMenu(navController: NavController) {
        dismissWordDialog()
        navController.navigate(Screen.MainMenu.route)
    }

    fun formatTime(timeCount: Int): String {
        // Calculate minutes and seconds from timeCount
        val minutes = timeCount / 60
        val seconds = timeCount % 60

        // Format minutes and seconds to include a leading zero if they are less than 10
        val formattedMinutes = minutes.toString().padStart(2, '0')
        val formattedSeconds = seconds.toString().padStart(2, '0')

        // Combine minutes and seconds into a "MM:SS" format
        val formattedTime = "$formattedMinutes:$formattedSeconds"
        return formattedTime
    }
}
