package com.guru.app.ui.screens.ambient

import androidx.lifecycle.ViewModel
import com.guru.app.core.utils.SoundEngine
import com.guru.app.domain.model.AmbientSound
import com.guru.app.domain.model.SoundType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AmbientViewModel @Inject constructor(
    private val soundEngine: SoundEngine
) : ViewModel() {

    private val defaultSounds = listOf(
        AmbientSound("1", "Gentle Rain", SoundType.RAIN, "🌧️"),
        AmbientSound("2", "Pine Forest", SoundType.FOREST, "🌲"),
        AmbientSound("3", "Ocean Waves", SoundType.OCEAN, "🌊"),
        AmbientSound("4", "Cozy Cafe", SoundType.CAFE, "☕"),
        AmbientSound("5", "White Noise", SoundType.WHITE_NOISE, "📻"),
        AmbientSound("6", "Brown Noise", SoundType.BROWN_NOISE, "🎧"),
        AmbientSound("7", "Pink Noise", SoundType.PINK_NOISE, "🎵")
    )

    private val _soundList = MutableStateFlow(defaultSounds)
    val soundList: StateFlow<List<AmbientSound>> = _soundList.asStateFlow()

    fun toggleSound(sound: AmbientSound) {
        val updated = _soundList.value.map { item ->
            if (item.id == sound.id) {
                val newPlaying = !item.isPlaying
                if (newPlaying) {
                    soundEngine.playSound(item.type, item.volume)
                } else {
                    soundEngine.stopSound(item.type)
                }
                item.copy(isPlaying = newPlaying)
            } else item
        }
        _soundList.value = updated
    }

    fun updateVolume(sound: AmbientSound, newVolume: Float) {
        val updated = _soundList.value.map { item ->
            if (item.id == sound.id) {
                if (item.isPlaying) {
                    soundEngine.setVolume(item.type, newVolume)
                }
                item.copy(volume = newVolume)
            } else item
        }
        _soundList.value = updated
    }

    fun stopAllSounds() {
        soundEngine.stopAll()
        _soundList.value = _soundList.value.map { it.copy(isPlaying = false) }
    }

    override fun onCleared() {
        super.onCleared()
        soundEngine.stopAll()
    }
}
