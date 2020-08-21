package Audio;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.texttospeech.v1.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class TTS
{
    private TextToSpeechClient textToSpeechClient;

    public void initialConfiguration(){
        // Recibe las credenciales para configurar el text-to-speech.
       try {
            ServiceAccountCredentials serviceAccountCredentials = ServiceAccountCredentials.fromStream(getClass().getClassLoader().getResourceAsStream("credentials.json"));
            TextToSpeechSettings settings = TextToSpeechSettings.newBuilder()
                    .setCredentialsProvider(FixedCredentialsProvider
                            .create(serviceAccountCredentials)).build();
            textToSpeechClient = TextToSpeechClient.create(settings);
           System.out.println("Conectado a la API de Google.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public File generarAudio(String idioma, String voz, String texto, double pitch, double speakingRate){
        SynthesisInput synthesisInput = SynthesisInput.newBuilder().setText(texto).build();
        //Configuracion de Voz
        VoiceSelectionParams voiceSelectionParams = VoiceSelectionParams.newBuilder().setLanguageCode(idioma)
                .setName(voz).build();

        //Configuracion del Audio.
        AudioConfig audioConfig = AudioConfig.newBuilder().setPitch(pitch).setSpeakingRate(speakingRate)
                .setAudioEncoding(AudioEncoding.MP3).build();

        //Sintetizamos una respuesta y la retornamos.
        SynthesizeSpeechResponse synthesizeSpeechResponse = this.textToSpeechClient
                .synthesizeSpeech(synthesisInput,voiceSelectionParams,audioConfig);
        byte[] response =  synthesizeSpeechResponse.getAudioContent().toByteArray();

        try{
            File temp = File.createTempFile("audio",".mp3");
            FileOutputStream fileOutputStream = new FileOutputStream(temp);
            fileOutputStream.write(response);
            fileOutputStream.close();
            temp.deleteOnExit();
            return temp;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
