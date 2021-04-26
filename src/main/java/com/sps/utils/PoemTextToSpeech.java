package com.sps.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.texttospeech.v1.AudioConfig;
import com.google.cloud.texttospeech.v1.AudioEncoding;
import com.google.cloud.texttospeech.v1.SsmlVoiceGender;
import com.google.cloud.texttospeech.v1.SynthesisInput;
import com.google.cloud.texttospeech.v1.SynthesizeSpeechResponse;
import com.google.cloud.texttospeech.v1.TextToSpeechClient;
import com.google.cloud.texttospeech.v1.TextToSpeechSettings;
import com.google.cloud.texttospeech.v1.VoiceSelectionParams;

public class PoemTextToSpeech {
  public byte[] speakPoem(String poemText) throws IOException {
    InputStream jsonPath = this.getClass().getResourceAsStream("/spring21-sps-4-credentials.json");
    GoogleCredentials credentials = GoogleCredentials.fromStream(jsonPath);
    TextToSpeechSettings textToSpeechSettings = TextToSpeechSettings.newBuilder()
        .setCredentialsProvider(FixedCredentialsProvider.create(credentials)).build();
    TextToSpeechClient textToSpeechClient = TextToSpeechClient.create(textToSpeechSettings);
    // Build speech synthesis input using poem text
    SynthesisInput input = SynthesisInput.newBuilder().setText(poemText).build();
    VoiceSelectionParams voice = VoiceSelectionParams.newBuilder().setLanguageCode("en-US")
        .setSsmlGender(SsmlVoiceGender.MALE).build();

    // Set audio type to MP3 and return speech utterance
    AudioConfig audioConfig = AudioConfig.newBuilder().setAudioEncoding(AudioEncoding.MP3).build();
    SynthesizeSpeechResponse response = textToSpeechClient.synthesizeSpeech(input, voice, audioConfig);
    try {
      textToSpeechClient.shutdown();
      textToSpeechClient.awaitTermination(30, TimeUnit.SECONDS);
      textToSpeechClient.close();
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return response.getAudioContent().toByteArray();
  }
}
