/* This is free and unencumbered software released into the public domain. */

package app.conreality.plugins.headset;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import java.util.UUID;

/** HeadsetService */
public final class HeadsetService extends Service implements TextToSpeech.OnInitListener {
  private static final String TAG = "ConrealityHeadset";
  private static final String TTS_ENGINE = "com.google.android.tts";

  public final class LocalBinder extends Binder {
    HeadsetService getService() {
      return HeadsetService.this;
    }
  }

  private final IBinder binder = new LocalBinder();
  private TextToSpeech ttsEngine;
  private Bundle ttsParams;

  /** Implements Service#onBind(). */
  @Override
  public IBinder onBind(final Intent intent) {
    return this.binder;
  }

  /** Implements Service#onCreate(). */
  @Override
  public void onCreate() {
    Log.i(TAG, "Created the bound service.");
  }

  /** Implements Service#onDestroy(). */
  @Override
  public void onDestroy() {
    Log.d(TAG, "Terminating the bound service...");
    if (this.ttsEngine != null) {
      this.ttsEngine.shutdown();
      this.ttsEngine = null;
      this.ttsParams = null;
    }
    Log.i(TAG, "Terminated the bound service.");
  }

  /** Implements Service#onStartCommand(). */
  @Override
  public int onStartCommand(final Intent intent, final int flags, final int startId) {
    Log.d(TAG, String.format("HeadsetService.onStartCommand: intent=%s flags=%d startId=%d", intent, flags, startId));
    return START_REDELIVER_INTENT;
  }

  public void onConnection(final Context context) {
    this.ttsEngine = new TextToSpeech(context, this, TTS_ENGINE);
    this.ttsParams = new Bundle();
  }

  /** Implements TextToSpeech.OnInitListener#onInit(). */
  @Override
  public void onInit(final int status) {
    if (status == TextToSpeech.SUCCESS) {
      if (Log.isLoggable(TAG, Log.DEBUG)) {
        Log.d(TAG, "Initialized the speech synthesis engine.");
      }
      //this.ttsEngine.setOnUtteranceProgressListener(this); // TODO
    }
    else {
      this.ttsEngine = null;
      this.ttsParams = null;
      Log.e(TAG, "Failed to initialize the speech synthesis engine.");
    }
  }

  public boolean canSpeak() {
    return (this.ttsEngine != null);
  }

  public boolean speak(final String message) {
    if (this.ttsEngine == null) return false;
    final String utteranceID = UUID.randomUUID().toString();
    return this.ttsEngine.speak(message, TextToSpeech.QUEUE_FLUSH, this.ttsParams, utteranceID) == TextToSpeech.SUCCESS;
  }

  public boolean stopSpeaking() {
    if (this.ttsEngine == null) return false;
    return this.ttsEngine.stop() == TextToSpeech.SUCCESS;
  }
}
