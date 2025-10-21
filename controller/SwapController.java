package controller;

import au.edu.uts.ap.javafx.Controller;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.application.League;
import model.application.Manager;
import model.application.Team;

public class SwapController extends Controller<League> {

    @FXML private ListView<Team> teamLv;
    @FXML private Button swapBtn;
    @FXML private Button closeBtn;
    

    @FXML
    private void initialize() {
        teamLv.setItems(model.getManageableTeams().getTeams());
        swapBtn.disableProperty().bind(teamLv.getSelectionModel().selectedItemProperty().isNull());
        
    }

    @FXML
    private void handleSwap() {
        Team selectedTeam = teamLv.getSelectionModel().getSelectedItem();
        Manager manager = model.getLoggedInManager();
        
        if (selectedTeam != null && manager != null) {
            model.setManagerForTeam(manager, selectedTeam);
            
        }
    }
    
    @FXML
    private void handleClose() {
        stage.close();
    }
}
