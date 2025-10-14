package controller;

import au.edu.uts.ap.javafx.*;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.application.*;
import model.exception.UnauthorisedAccessException;


public class LoginController extends Controller<League> {

    @FXML private TextField managerIdTf;

    @FXML
    private void initialize() {
    }

    @FXML
    private void handleLogin() {
        String idString = managerIdTf.getText().trim();
        try {
            int managerId = Integer.parseInt(idString);
            Manager manager = model.validateManager(managerId); 
            model.setLoggedInManager(manager);
            this.stage.close(); 
            ViewLoader.showStage(model, "/view/ManagerDashboardView.fxml", "Manager Dashboard", new Stage());
            



        } catch (NumberFormatException e) {
            String message = "Incorrect format for manager id"; 
            showError("UnauthorisedAccessException", message);
            
        } catch (UnauthorisedAccessException e) {
            showError(e.getClass().getSimpleName(), e.getMessage());
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showError(String exceptionName, String message) {
        try {
            ErrorController.showErrorStage(exceptionName, message, new Stage());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void handleExit() {
        this.stage.close();
    }

}
