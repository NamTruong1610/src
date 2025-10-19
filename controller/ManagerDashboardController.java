package controller;


import au.edu.uts.ap.javafx.Controller;
import au.edu.uts.ap.javafx.ViewLoader;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.application.*;

public class ManagerDashboardController extends Controller<League> {

    @FXML private Label teamNameLbl;
    @FXML private ImageView jerseyImage;
    @FXML private Button withdrawBtn; 
    @FXML private Button manageBtn; 
    @FXML private Button swapTeamBtn; 

    @FXML
    private void initialize() {
        Manager manager = model.getLoggedInManager();;
        if (manager != null) {
            teamNameLbl.textProperty().bind(
                Bindings.createStringBinding(
                    () -> {
                        Team team = manager.getTeam();
                        return (team != null) ? team.toString() : "No team";
                    },
                    manager.teamProperty() 
                )
            );

            manager.teamProperty().addListener((observable, oldTeam, newTeam) -> {
                updateJerseyImage(newTeam);
            });

            withdrawBtn.disableProperty().bind(manager.teamProperty().isNull());
            manageBtn.disableProperty().bind(manager.teamProperty().isNull());
            
            updateJerseyImage(manager.getTeam());
        }
    }

    private void updateJerseyImage(Team team) {
        String imageFileName = "none.png";
        if (team != null) {
            String teamNameLower = team.getTeamName().toLowerCase();
            imageFileName = teamNameLower + ".png";
        } 

        try {
            Image jersey = new Image(getClass().getResourceAsStream("/view/image/" + imageFileName));
            jerseyImage.setImage(jersey);
        } catch (Exception e) {
            System.err.println("Could not find jersey image: " + imageFileName);
            try {
                Image errorImage = new Image(getClass().getResourceAsStream("/view/image/error.png"));
                jerseyImage.setImage(errorImage);
            } catch (Exception ex) {
                System.err.println("Could not load fallback image.");
            }
        }
    }

    @FXML
    private void handleWithdraw() {
        Team currentTeam = model.getLoggedInManager().getTeam();
        if (currentTeam != null) {
            model.withdrawManagerFromTeam(model.getLoggedInManager());
        }
    }

    @FXML
    private void handleManage() {
        this.stage.close(); 
        
        try {
            Team team = model.getLoggedInManager().getTeam();
            if (team != null) {
                ViewLoader.showStage(team, "/view/TeamDashboardView.fxml", "Team Dashboard", new Stage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSwapTeam() {
        try {
            ViewLoader.showStage(model, "/view/SwapView.fxml", "Swap Team", new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleExit() {
        this.stage.close();
    }
}
