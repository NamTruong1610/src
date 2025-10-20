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
        Manager manager = model.getLoggedInManager();
        if (manager != null) {
            
            teamNameLbl.textProperty().bind(
                Bindings.when(manager.teamProperty().isNull())
                .then("No team")
                .otherwise(manager.teamProperty().asString())
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
        Image jersey = new Image(getClass().getResourceAsStream("/view/image/" + imageFileName));
        jerseyImage.setImage(jersey);
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
        Team team = model.getLoggedInManager().getTeam();
        if (team != null) {
            ViewLoader.showStage(team, "/view/TeamDashboardView.fxml", "Team Dashboard", new Stage());
        }
    }

    @FXML
    private void handleSwapTeam() {
        ViewLoader.showStage(model, "/view/SwapView.fxml", "Swap", new Stage());
    }

    @FXML
    private void handleExit() {
        this.stage.close();
    }
}
