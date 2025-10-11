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
            // 1. Check for non-integer ID
            int managerId = Integer.parseInt(idString);

            // 2. Check for integer but invalid ID (delegated to League model)
            Manager manager = model.validateManager(managerId); // model is League instance

            // If no exception is thrown, login is successful
            model.setLoggedInManager(manager);
            System.out.println("Login successful for Manager ID: " + managerId);
            this.stage.close(); // Close login window on success
            // TODO: Load the next view here (e.g., the Manager panel)

        } catch (NumberFormatException e) {
            // If input is not an integer (Non-integer ID -> First photo error)
            String message = "Incorrect format for manager id"; // Matches the first photo's smaller text
            // The first photo's error text looks like a generic "Invalid login credentials" under the exception.
            showError("UnauthorisedAccessException", message);
            
        } catch (UnauthorisedAccessException e) {
            // If ID is an integer but invalid (Invalid integer ID -> Second photo error)
            // The exception message is set by League.validateManager: "Invalid login credentials"
            showError(e.getClass().getSimpleName(), e.getMessage());
            
        } catch (Exception e) {
            // Catch any unexpected exceptions
            e.printStackTrace();
        }
    }

    private void showError(String exceptionName, String message) {
        try {
            // We use the static helper in ErrorController to load and setup the stage
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
