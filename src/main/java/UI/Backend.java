package UI;

import Audio.AudioPlayer;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.stage.StageStyle;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class Backend implements Runnable{
    private VentanaHandler ventanaHandler;
    private long delay = 5000; // 6 sec
    private AudioPlayer audioPlayer;
    private AtomicBoolean isRunning;
    private AtomicBoolean isPaused;
    private List<Integer> quemadas; // Array del bucle de fichas.
    private List<String> loteria; // Array de respaldo para no leer el archivo cada que se reinicie.

    public Backend(VentanaHandler ventanaHandler){
        this.ventanaHandler = ventanaHandler;
        audioPlayer = new AudioPlayer();
        isRunning = new AtomicBoolean();
        isPaused = new AtomicBoolean();
        isPaused.set(true);
        isRunning.set(true);
        setupLists();
    }

    public void terminarJuego(){
        isRunning.getAndSet(false);
        Platform.exit();
    }

    public void pausarJuego(){
        if(isPaused.get()){
            isPaused.set(false);
            //informe("Juego reanudado");
        }else{
            isPaused.set(true);
            //informe("Juego pausado");
        }
    }

    public void reiniciarJuego(){
        isPaused.set(true);
        quemadas.clear();
        informe("Juego reiniciado, presiona Reanudar.");
    }

    private int obtenerAleatorio(List<String> lista){
        Random r = new Random();
        int random = r.nextInt(lista.size());
        if(quemadas.size()<90){
            while(quemadas.contains(random)){
                random = r.nextInt(lista.size());
            }
        }else{
            informe("No hay mas fichas para cantar.");
        }
        return random;
    }


    private void setupLists(){
        String linea;
        loteria = new ArrayList<>();
        quemadas = new ArrayList<>();
        try{
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("lista.txt")));
            while ((linea = bufferedReader.readLine()) != null){
                loteria.add(linea);
            }
            bufferedReader.close();
            // Una vez cerrado el bucle
            System.out.println("Las listas han sido creadas/copiadas con exito");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void informe(String texto){
        Alert alerta = new Alert(Alert.AlertType.NONE, "Info", ButtonType.OK);
        alerta.setTitle("Informacion");
        alerta.setContentText(texto);
        alerta.show();
    }

    @Override
    public void run() {
        while(isRunning.get()){
            if(!isPaused.get()){
                int valor = obtenerAleatorio(loteria);
                // Para que no se crashee
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        ventanaHandler.cambiarImagen(Integer.toString((valor+1)));
                        ventanaHandler.cambiarTexto(loteria.get(valor));
                    }
                });

                audioPlayer.hablar(loteria.get(valor));
                try {
                    Thread.sleep(delay); // Si es interrumpido puede arrojar un sleep interrupted.
                } catch (InterruptedException e) {
                   // e.printStackTrace();
                }
            }
        }
    }
}
