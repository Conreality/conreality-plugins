/* This is free and unencumbered software released into the public domain. */

package app.conreality.plugins.headset;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Process;
import android.util.Log;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedByInterruptException;

public final class AudioRecordingThread extends Thread {
  private static final String TAG = "ConrealityHeadset";
  private static final int AUDIO_SOURCE = MediaRecorder.AudioSource.DEFAULT;
  private static final int SAMPLE_RATE = 44100; // Hz
  private static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO;
  private static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;

  ByteBuffer buffer;
  AudioRecord recorder;

  AudioRecordingThread() {
    super("AudioRecordingThread");
    final int bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT);
    this.buffer = ByteBuffer.allocateDirect(bufferSize);
    this.recorder = new AudioRecord(AUDIO_SOURCE, SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT, bufferSize);
    if (Log.isLoggable(TAG, Log.DEBUG)) {
      Log.w(TAG, "AudioRecordingThread: bufferSize=" + bufferSize);
    }
  }

  @Override
  public void run() {
    Log.d(TAG, "AudioRecordingThread.start");
    Process.setThreadPriority(Process.THREAD_PRIORITY_AUDIO);
    this.recorder.startRecording();
    try {
      while (!this.isInterrupted()) {
        this.buffer.clear();
        final int len = this.recorder.read(this.buffer, this.buffer.capacity());
        if (len >= 0 && len <= this.buffer.capacity()) {
          //Log.d(TAG, "Expected length returned: " + len); // TODO
        }
        else {
          Log.w(TAG, "Unexpected audio sample length returned: " + len);
        }
      }
    }
    //catch (final IOException error) {}
    //catch (final InterruptedException error) {}
    //catch (final ClosedByInterruptException error) {}
    finally {
      Log.d(TAG, "AudioRecordingThread.stop");
      try {
        this.recorder.stop();
      }
      catch (final IllegalStateException error) {
        Log.e(TAG, "Failed to stop audio recording.", error);
      }
      this.recorder.release();
      this.recorder = null;
      this.buffer = null;
    }
  }
}
