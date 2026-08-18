package com.guru.app.core.utils

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import com.guru.app.domain.model.SoundType
import kotlinx.coroutines.*
import java.util.Random
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SoundEngine @Inject constructor() {

    private val activeTrackMap = mutableMapOf<SoundType, ActiveAudioTrack>()
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    private data class ActiveAudioTrack(
        val soundType: SoundType,
        val audioTrack: AudioTrack,
        var volume: Float,
        var isPlaying: Boolean,
        val job: Job
    )

    fun playSound(soundType: SoundType, volume: Float = 0.7f) {
        stopSound(soundType)

        val sampleRate = 44100
        val bufferSize = AudioTrack.getMinBufferSize(
            sampleRate,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )

        val audioTrack = AudioTrack.Builder()
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
            .setAudioFormat(
                AudioFormat.Builder()
                    .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                    .setSampleRate(sampleRate)
                    .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                    .build()
            )
            .setBufferSizeInBytes(bufferSize)
            .setTransferMode(AudioTrack.MODE_STREAM)
            .build()

        audioTrack.setVolume(volume)
        audioTrack.play()

        val job = scope.launch {
            val random = Random()
            val buffer = ShortArray(bufferSize)
            var b0 = 0.0
            var b1 = 0.0
            var b2 = 0.0
            var b3 = 0.0
            var b4 = 0.0
            var b5 = 0.0
            var b6 = 0.0
            var lastValue = 0.0

            while (isActive) {
                for (i in buffer.indices) {
                    val white = random.nextDouble() * 2.0 - 1.0
                    val sampleValue: Double = when (soundType) {
                        SoundType.WHITE_NOISE -> white
                        SoundType.BROWN_NOISE -> {
                            lastValue = (lastValue + (0.02 * white)) / 1.02
                            lastValue * 3.5
                        }
                        SoundType.PINK_NOISE -> {
                            b0 = 0.99886 * b0 + white * 0.0555179
                            b1 = 0.99332 * b1 + white * 0.0750759
                            b2 = 0.96900 * b2 + white * 0.1538520
                            b3 = 0.86650 * b3 + white * 0.3104856
                            b4 = 0.55000 * b4 + white * 0.5329522
                            b5 = -0.7616 * b5 - white * 0.0168980
                            val pink = b0 + b1 + b2 + b3 + b4 + b5 + b6 + white * 0.5362
                            b6 = white * 0.115926
                            pink * 0.11
                        }
                        SoundType.RAIN -> {
                            // Rain synthesis: low brown noise base with occasional sharp droplets
                            lastValue = (lastValue + (0.04 * white)) / 1.04
                            val drop = if (random.nextDouble() > 0.994) (random.nextDouble() * 0.8) else 0.0
                            (lastValue * 2.0) + drop
                        }
                        SoundType.FOREST -> {
                            // Gentle wind rustle with soft frequency shifts
                            b0 = 0.99 * b0 + white * 0.02
                            b0 * 1.8
                        }
                        SoundType.OCEAN -> {
                            // Waves modulation
                            val waveMod = Math.sin(System.currentTimeMillis() / 1500.0) * 0.5 + 0.5
                            lastValue = (lastValue + (0.015 * white)) / 1.015
                            lastValue * 2.5 * waveMod
                        }
                        SoundType.CAFE -> {
                            // Warm ambient chatter noise
                            b1 = 0.97 * b1 + white * 0.08
                            b1 * 0.8
                        }
                    }

                    val clamped = sampleValue.coerceIn(-1.0, 1.0)
                    buffer[i] = (clamped * Short.MAX_VALUE).toInt().toShort()
                }
                audioTrack.write(buffer, 0, buffer.size)
            }
        }

        activeTrackMap[soundType] = ActiveAudioTrack(soundType, audioTrack, volume, true, job)
    }

    fun setVolume(soundType: SoundType, volume: Float) {
        activeTrackMap[soundType]?.let { track ->
            track.volume = volume
            track.audioTrack.setVolume(volume)
        }
    }

    fun stopSound(soundType: SoundType) {
        activeTrackMap[soundType]?.let { track ->
            track.job.cancel()
            try {
                track.audioTrack.stop()
                track.audioTrack.release()
            } catch (e: Exception) {
                // Ignore cleanup errors
            }
        }
        activeTrackMap.remove(soundType)
    }

    fun stopAll() {
        val keys = activeTrackMap.keys.toList()
        keys.forEach { stopSound(it) }
    }
}
