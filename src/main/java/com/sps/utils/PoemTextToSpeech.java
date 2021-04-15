package com.sps.utils;

import java.io.IOException;

import com.google.cloud.texttospeech.v1.AudioConfig;
import com.google.cloud.texttospeech.v1.AudioEncoding;
import com.google.cloud.texttospeech.v1.SsmlVoiceGender;
import com.google.cloud.texttospeech.v1.SynthesisInput;
import com.google.cloud.texttospeech.v1.SynthesizeSpeechResponse;
import com.google.cloud.texttospeech.v1.TextToSpeechClient;
import com.google.cloud.texttospeech.v1.VoiceSelectionParams;

public class PoemTextToSpeech {
  public byte[] speakPoem(String poemText) throws IOException {
    TextToSpeechClient textToSpeechClient = TextToSpeechClient.create();
    // Build speech synthesis input using poem text
    SynthesisInput input = SynthesisInput.newBuilder().setText(poemText).build();
    VoiceSelectionParams voice = VoiceSelectionParams.newBuilder().setLanguageCode("en-US")
        .setSsmlGender(SsmlVoiceGender.MALE).build();

    // Set audio type to MP3 and return speech utterance
    AudioConfig audioConfig = AudioConfig.newBuilder().setAudioEncoding(AudioEncoding.MP3).build();
    SynthesizeSpeechResponse response = textToSpeechClient.synthesizeSpeech(input, voice, audioConfig);
    return response.getAudioContent().toByteArray();
  }
}
