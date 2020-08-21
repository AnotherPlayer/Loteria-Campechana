package Audio;

import javazoom.jl.player.Player;

import java.io.*;


public class AudioPlayer {
    // Valores de Fabrica segun la Documentacion Oficial de Google.
    private String voz = "es-ES-Standard-A";
    private String idioma = "es-ES";
    private double pitch = 0.0;
    private double speakingRate = 1;
    private TTS tts;

    public AudioPlayer(){
        tts = new TTS();
        tts.initialConfiguration();
    }

    public void hablar(String texto){
        File sample = tts.generarAudio(idioma,voz,texto,pitch,speakingRate);
        if(sample!=null){
            try{
                Player player = new Player(new FileInputStream(sample));
                player.play();
                sample.deleteOnExit();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
