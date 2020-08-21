package UI;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class VentanaHandler implements Initializable {
    @FXML
    private ImageView displayImage;
    @FXML
    private Button initPauseBtn;
    @FXML
    private Button reiniciarBtn;
    @FXML
    private Button exitBtn;
    @FXML
    private Label displayText;

    private Backend backend;
    private ExecutorService pool;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //
        pool = Executors.newFixedThreadPool(3);
        pool.execute((backend = new Backend(this)));
    }

    protected void cambiarImagen(String imagen) {
        displayImage.setImage(new Image("/images/"+imagen+".jpg"));
    }

    protected void cambiarTexto(String texto){
        displayText.setText(texto);
    }

    public void iniciar(){
        if(initPauseBtn.getText().equalsIgnoreCase("Pausar")){
            //Se va a pausar :v
            backend.pausarJuego();
            initPauseBtn.setText("Reanudar");
        }else if(initPauseBtn.getText().equalsIgnoreCase("Reanudar")){
            backend.pausarJuego();
            initPauseBtn.setText("Pausar");
        }else{
            initPauseBtn.setText("Pausar");
            backend.pausarJuego();
        }
    }

    public void reiniciar(){
        backend.reiniciarJuego();
    }

    public void salir(){
        pool.shutdown();
        try{
            if(!pool.awaitTermination(1, TimeUnit.SECONDS)) {
                pool.shutdownNow();
            }
            if(pool.isShutdown()){
                System.exit(0);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
