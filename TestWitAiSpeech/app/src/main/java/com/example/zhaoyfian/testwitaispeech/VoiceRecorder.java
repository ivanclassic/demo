package com.example.zhaoyfian.testwitaispeech;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.support.annotation.NonNull;
import android.util.Log;


/**
 * Continuously records audio and notifies the {@link VoiceRecorder.Callback} when voice (or any
 * sound) is heard.
 * <p>
 * <p>The recorded audio format is always {@link AudioFormat#ENCODING_PCM_16BIT} and
 * {@link AudioFormat#CHANNEL_IN_MONO}. This class will automatically pick the right sample rate
 * for the device. Use {@link #getSampleRate()} to get the selected value.</p>
 */
public class VoiceRecorder {
    private static final String TAG = "VoiceRecorder";

    private static final int[] SAMPLE_RATE_CANDIDATES = new int[]{16000, 8000};

    private static final int CHANNEL = AudioFormat.CHANNEL_IN_MONO;
    private static final int ENCODING = AudioFormat.ENCODING_PCM_16BIT;

    private static final int AMPLITUDE_THRESHOLD = 1500;
    private static final int MAX_SPEECH_SECONDS = 15;

    private final Callback mCallback;
    private AudioRecord mAudioRecord;
    private Thread mThread;
    private byte[] mBuffer;
    private int mSampleRate = SAMPLE_RATE_CANDIDATES[0];

    public VoiceRecorder(@NonNull Callback callback) {
        mCallback = callback;
    }

    /**
     * Starts recording audio.
     * <p>
     * <p>The caller is responsible for calling {@link #stop()} later.</p>
     */
    public boolean start() {
        // Stop recording if it is currently ongoing.
        if (mThread != null)
            stop();

        // Try to create a new recording session.
        mAudioRecord = createAudioRecord();
        if (mAudioRecord == null)
            return false;

        // Start recording.
        mAudioRecord.startRecording();
        // Start processing the captured audio.
        mThread = new Thread(new ProcessVoice());
        mThread.start();
        return true;
    }

    /**
     * Stops recording audio.
     */
    public void stop() {
        if (mThread != null) {
            mThread.interrupt();
            try {
                mThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mThread = null;
        }
        if (mAudioRecord != null) {
            try {
                mAudioRecord.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
            mAudioRecord.release();
            mAudioRecord = null;
        }
        mBuffer = null;
    }

    /**
     * Retrieves the sample rate currently used to record audio.
     *
     * @return The sample rate of recorded audio.
     */
    public int getSampleRate() {
        return mAudioRecord != null ? mAudioRecord.getSampleRate() : mSampleRate;
    }

    /**
     * Creates a new {@link AudioRecord}.
     *
     * @return A newly created {@link AudioRecord}, or null if it cannot be created (missing
     * permissions?).
     */
    private AudioRecord createAudioRecord() {
        for (int sampleRate : SAMPLE_RATE_CANDIDATES) {
            final int sizeInBytes = AudioRecord.getMinBufferSize(sampleRate, CHANNEL, ENCODING);
            if (sizeInBytes == AudioRecord.ERROR_BAD_VALUE) {
                continue;
            }
            final AudioRecord audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                    sampleRate, CHANNEL, ENCODING, sizeInBytes);
            if (audioRecord.getState() == AudioRecord.STATE_INITIALIZED) {
                mSampleRate = sampleRate;
                mBuffer = new byte[sizeInBytes];
                Log.i(TAG, "createAudioRecord, [sampleRate: " + sampleRate + ", sizeInBytes: " + sizeInBytes + "].");
                return audioRecord;
            } else {
                audioRecord.release();
            }
        }
        return null;
    }

    public interface Callback {

        /**
         * Called when the recorder starts hearing voice.
         */
        void onVoiceStart();

        /**
         * Called when the recorder is hearing voice.
         *
         * @param data The audio data in {@link AudioFormat#ENCODING_PCM_16BIT}.
         * @param size The size of the actual data in {@code data}.
         */
        void onVoice(byte[] data, int size);

        /**
         * Called when the recorder stops hearing voice.
         */
        void onVoiceEnd();
    }

    /**
     * Continuously processes the captured audio and notifies {@link #mCallback} of corresponding
     * events.
     */
    private class ProcessVoice implements Runnable {

        @Override
        public void run() {
            int bytesPerSecond = mSampleRate * 2; // 1 Channel, LINEAR16
            int totalBytes = 0;
            boolean started = false;

            for (;;) {
                final int size = mAudioRecord.read(mBuffer, 0, mBuffer.length);
                if (isHearingVoice(mBuffer, size)) {
                    if (!started) {
                        started = true;
                        mCallback.onVoiceStart();
                    }
                } else if (Thread.currentThread().isInterrupted()) {
                    Log.d(TAG, "Record thread exit");
                    break;
                }
                if (started) {
                    totalBytes += size;
                    if (totalBytes >= MAX_SPEECH_SECONDS * bytesPerSecond) {
                        Log.d(TAG, "Reach max length");
                        break;
                    }
                    mCallback.onVoice(mBuffer, size);
                }
            }
            if (started)
                mCallback.onVoiceEnd();
        }
    }

    public static boolean isHearingVoice(byte[] buffer, int size) {
        if (buffer == null) return false;
        for (int i = 0; i < size - 1; i += 2) {
            // The buffer has LINEAR16 in little endian.
            int s = buffer[i + 1];
            if (s < 0) s *= -1;
            s <<= 8;
            s += Math.abs(buffer[i]);
            if (s > AMPLITUDE_THRESHOLD) {
                return true;
            }
        }
        return false;
    }
}
