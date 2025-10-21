package controller;

import java.util.List;

import au.edu.uts.ap.javafx.Controller;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.application.Player;
import model.application.League;
import model.application.Team;
import model.exception.FillException;
import model.exception.InvalidSigningException;

import java.io.IOException;
import java.util.ArrayList;

public class TeamDashboardController extends Controller<Team> {

    @FXML private Label teamNameLbl;
    @FXML private TextField signTf;
    @FXML private Button signBtn;
    @FXML private Button unsignBtn;
    @FXML private TableView<Player> playerTv;
    @FXML private TableColumn<Player, String> nameCol;
    @FXML private TableColumn<Player, String> positionCol;
    @FXML private ImageView slot0Iv;
    @FXML private ImageView slot1Iv;
    @FXML private ImageView slot2Iv;
    @FXML private ImageView slot3Iv;
    @FXML private ImageView slot4Iv;

    private List<ImageView> activeJerseySlots; 
    private String teamJerseyPath;

    @FXML
    private void initialize() {
        activeJerseySlots = new ArrayList<>(Team.REQUIRED_TEAM_SIZE);
        activeJerseySlots.add(slot0Iv);
        activeJerseySlots.add(slot1Iv);
        activeJerseySlots.add(slot2Iv);
        activeJerseySlots.add(slot3Iv);
        activeJerseySlots.add(slot4Iv);

        teamNameLbl.setText(model.toString());
        teamJerseyPath = "/view/image/" + model.getTeamName().toLowerCase() + ".png";
        
        playerTv.setItems(model.getAllPlayers().getPlayers());
        nameCol.setCellValueFactory(cellData -> cellData.getValue().fullNameProperty());
        positionCol.setCellValueFactory(cellData -> cellData.getValue().positionProperty());
        
        signBtn.disableProperty().bind(signTf.textProperty().isEmpty());
        unsignBtn.disableProperty().bind(playerTv.getSelectionModel().selectedItemProperty().isNull());

        setupActiveTeamSlots();
        
    }


    private void setupActiveTeamSlots() {
        Player[] currentTeam = model.getCurrentTeam();
        String noneJerseyPath = "/view/image/none.png";

        for (int i = 0; i < currentTeam.length; i++) {
            ImageView slot = activeJerseySlots.get(i);
            Player player = currentTeam[i];

            String imagePath = (player != null) ? teamJerseyPath : noneJerseyPath;
            
            slot.setImage(new Image(getClass().getResourceAsStream(imagePath)));
            
            Tooltip tooltip = new Tooltip((player != null) ? player.getFullName() : "Unallocated");
            Tooltip.install(slot, tooltip);
        }
    }
    
    private void showError(String exceptionName, String message) {
        try {
            ErrorController.showErrorStage(exceptionName, message, new Stage());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void handleSign() {
        String fullName = signTf.getText().trim();
        if (fullName.isEmpty()) return;

        try {
            Player player = League.getInstance().getPlayers().player(fullName);    
            model.signPlayer(player);
            
            signTf.clear();
        } catch (InvalidSigningException e) {
            showError(e.getClass().getSimpleName(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleUnsign() {
        Player selectedPlayer = playerTv.getSelectionModel().getSelectedItem();
        if (selectedPlayer == null) return;
        
        try {
            model.unsignPlayer(selectedPlayer);
        } catch (InvalidSigningException e) {
            showError(e.getClass().getSimpleName(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    @FXML
    private void handleSlotClick(MouseEvent event) {
        ImageView clickedSlot = (ImageView) event.getSource();
        int slotIndex = activeJerseySlots.indexOf(clickedSlot);
        
        Player selectedPlayer = playerTv.getSelectionModel().getSelectedItem();
        
        if (selectedPlayer != null) {
            try {
                model.assignPlayerToSlot(selectedPlayer, slotIndex);
            } catch (FillException e) {
                showError(e.getClass().getSimpleName(), e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            model.removePlayerFromSlot(slotIndex);
        }
        
        setupActiveTeamSlots(); 
        playerTv.getSelectionModel().clearSelection(); 
    }
    
    @FXML
    private void handleClose() {
        this.stage.close();
    }
}
