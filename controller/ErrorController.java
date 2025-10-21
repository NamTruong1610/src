package controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.exception.UnauthorisedAccessException;


public class ErrorController extends au.edu.uts.ap.javafx.Controller<UnauthorisedAccessException> {

    @FXML private Label exceptionNameLbl;
    @FXML private Label errorMessageLbl;
    @FXML private Button closeBtn;

    private String exceptionName;
    private String errorMessage;

    public void setErrorDetails(String exceptionName, String errorMessage) {
        this.exceptionName = exceptionName;
        this.errorMessage = errorMessage;
    }

    @FXML
    private void initialize() {
    }

    public void setupUI() {
        exceptionNameLbl.setText(exceptionName);
        errorMessageLbl.setText(errorMessage);    
    }

    public static void showErrorStage(String exceptionName, String errorMessage, Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(ErrorController.class.getResource("/view/ErrorView.fxml"));
        Scene scene = new Scene(loader.load());
        
        ErrorController controller = loader.getController();
        controller.setErrorDetails(exceptionName, errorMessage);
        
        stage.setTitle("Error");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.setResizable(false);
        
        controller.setupUI(); 
        stage.show();
    }

    @FXML
    private void handleExit() {
        Stage stage = (Stage) closeBtn.getScene().getWindow();
        stage.close();
    }
}
