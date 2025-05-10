package com.example.nodrah_project

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TalkAndType(
    val app: Application
) : RecognitionListener {
    private val _state = MutableStateFlow(TalkAndTypeParserState())
    val state = _state.asStateFlow()
    val recognizer = SpeechRecognizer.createSpeechRecognizer(app)

    fun startListening(languageCode: String) {
        _state.update { TalkAndTypeParserState() }
        if (!SpeechRecognizer.isRecognitionAvailable(app)) {
            _state.update {
                it.copy(error = "Recognition is not available")
            }
            Log.e("TalkAndType", "Recognition not available")
            return
        }
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, languageCode)
        }
        recognizer.setRecognitionListener(this)
        recognizer.startListening(intent)
        _state.update {
            it.copy(isSpeaking = true)
        }
        Log.d("TalkAndType", "Start listening")
    }

    fun stopListening() {
        _state.update {
            it.copy(isSpeaking = false)
        }
        recognizer.stopListening()
        Log.d("TalkAndType", "Stop listening")
    }

    override fun onReadyForSpeech(params: Bundle?) {
        _state.update { it.copy(error = null) }
        Log.d("TalkAndType", "onReadyForSpeech")
    }

    override fun onBeginningOfSpeech() {
        Log.d("TalkAndType", "onBeginningOfSpeech")
    }

    override fun onRmsChanged(rmsdB: Float) = Unit

    override fun onBufferReceived(buffer: ByteArray?) = Unit

    override fun onEndOfSpeech() {
        _state.update { it.copy(isSpeaking = false) }
        Log.d("TalkAndType", "onEndOfSpeech")
    }

    override fun onError(error: Int) {
        if (error == SpeechRecognizer.ERROR_CLIENT) {
            return
        }
        _state.update {
            it.copy(error = "Error: $error")
        }
        Log.e("TalkAndType", "Error: $error")
    }

    override fun onResults(results: Bundle?) {
        results
            ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            ?.getOrNull(0)
            ?.let { result ->
                _state.update {
                    it.copy(spokenText = result)
                }
                Log.d("TalkAndType", "onResults: $result")
            }
    }

    override fun onPartialResults(partialResults: Bundle?) = Unit
    override fun onEvent(eventType: Int, params: Bundle?) = Unit
}

data class TalkAndTypeParserState(
    val spokenText: String = "",
    val isSpeaking: Boolean = false,
    val error: String? = null
)