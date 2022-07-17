package application;

import java.io.IOException;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.time.Instant;

import javax.swing.JTable;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Controller {
	
	@FXML
	private AnchorPane Ancre;

    @FXML
    private ComboBox combDate;

    @FXML
    private   ComboBox combFiliere;

    @FXML
    private ComboBox combPeriode;

    @FXML
    private Button validButton;
    
    @FXML
    private Button absentButton;
    
    @FXML
    private Label warning;
    
    @FXML
    private TitledPane TitleFiche;
    
    @FXML
    private Button startButton;
    
    @FXML
    private Button stopButton;
    
    @FXML
    private BorderPane borderAbs;
    
	@FXML
	public ListView<String> logList;
	
	@FXML
	private TableView<Etudiant> tablePresence;
	
	@FXML
	private TableView<EtudiantAbs> tableAbscence;
	
	@FXML
	private TableColumn<Etudiant, String> columnMatricule;
	
	@FXML
	private TableColumn<EtudiantAbs, String> columnMatriculeAbs;
	
	@FXML
	private TableColumn<Etudiant, String>  columnNom;
	
	@FXML
	private TableColumn<EtudiantAbs, String>  columnNomAbs;
	
	@FXML
	private TableColumn<Etudiant, String>  columnPrenom;
	
	@FXML
	private TableColumn<EtudiantAbs, String>  columnPrenomAbs;
	
	@FXML
	private TableColumn<Etudiant, String>  columnFiliere;
	
	@FXML
	private TableColumn<EtudiantAbs, String>  columnFiliereAbs;
	
	
	@FXML
	private TableColumn<Etudiant, String>  columnNomCours;
	
	
	public static ObservableList<String> event = FXCollections.observableArrayList();
	public  ObservableList<Etudiant> etudiantsPresent = FXCollections.observableArrayList();
	public  ObservableList<EtudiantAbs> etudiantsAbsent = FXCollections.observableArrayList();
	
    
    private String filiere="";
    private String date="";
    private String periode="";
    
    ObservableList<String> listFil = FXCollections.observableArrayList("L1-GENIE_INFO","L2-ISR","L2-GENIE_LOGICIEL","L3-QSIR","L3-CDRI");
    ObservableList<String> listDate = FXCollections.observableArrayList();
    ObservableList<String> listPeriode = FXCollections.observableArrayList();
    
    Database database = new Database();
    public boolean isDBready = false;
    
	public void putOnLog(String data) {

		Instant now = Instant.now();

		String logs = now.toString() + ":\n" + data;

		event.add(logs);

		logList.setItems(event);
	}
    
    

    @FXML 
    public void start() throws SQLException{
    	
    	if (!database.init()) {

			putOnLog("Error: Database Connection Failed ! ");

		} else {
			isDBready = true;
			putOnLog("Success: Database Connection Succesful ! ");
		}
    	
    	combFiliere.getSelectionModel().clearSelection();
    	combFiliere.setItems(listFil);
    	
    	combDate.getSelectionModel().clearSelection();
    	combPeriode.getSelectionModel().clearSelection();
    	combDate.setItems(listDate);
    	combPeriode.setItems(listPeriode);

    	
    	TitleFiche.setDisable(false);
    	startButton.setDisable(true);
    	startButton.setVisible(false);
    	
    	stopButton.setDisable(false);
    	stopButton.setVisible(true);
    	
    	
    	columnMatricule.setCellValueFactory(new PropertyValueFactory<Etudiant, String>("matricule"));
    	columnNom.setCellValueFactory(new PropertyValueFactory<Etudiant, String>("nom"));
    	columnPrenom.setCellValueFactory(new PropertyValueFactory<Etudiant, String>("prenom"));
    	columnFiliere.setCellValueFactory(new PropertyValueFactory<Etudiant, String>("filiere"));
    	columnNomCours.setCellValueFactory(new PropertyValueFactory<Etudiant, String>("Nomcours"));
    	
    	
    	columnMatriculeAbs.setCellValueFactory(new PropertyValueFactory<EtudiantAbs, String>("matricule"));
    	columnNomAbs.setCellValueFactory(new PropertyValueFactory<EtudiantAbs, String>("nom"));
    	columnPrenomAbs.setCellValueFactory(new PropertyValueFactory<EtudiantAbs, String>("prenom"));
    	columnFiliereAbs.setCellValueFactory(new PropertyValueFactory<EtudiantAbs, String>("filiere"));
    	

    	
    	if(etudiantsPresent.size()!=0) {
    		etudiantsPresent.clear();
        	tablePresence.getItems().clear();
    	}
    	
    	if(etudiantsAbsent.size()!=0) {
    		etudiantsAbsent.clear();
        	tableAbscence.getItems().clear();
    	}
    	
    
    }
    
    @FXML
    public void stop() throws SQLException {
    	
    	
		database.db_close();
		putOnLog("Database Connection Closed");
		isDBready=false;
    	
    	TitleFiche.setDisable(true);
    	startButton.setDisable(false);
    	startButton.setVisible(true);
    	
    	stopButton.setDisable(true);
    	stopButton.setVisible(false);
    	
    	listDate.clear();
    	listPeriode.clear();
    	
    	absentButton.setVisible(false);
		absentButton.setDisable(true);

    }
    
    @FXML
    public void Selectionner() {
    	try 
    	{
    		
    	filiere = combFiliere.getSelectionModel().getSelectedItem().toString();
    	
    	listDate = database.getDate(filiere);
    	
    	if(listDate.size()==0) {
    		putOnLog("Aucun emploi de temps trouvé pour cette filiere");
    		combDate.getSelectionModel().clearSelection();
    		listDate.clear();
    		combDate.setItems(listDate);
    		date="";
    		listPeriode.clear();
    		combPeriode.setItems(listPeriode);
    		periode="";
    	}
    	else {
    		combDate.getSelectionModel().clearSelection();
    		combDate.setItems(listDate);
    		putOnLog("Emploi de temps trouvé !!!");
    	}
    	}catch(Exception e){
    		filiere = "";		
    	}
    	System.out.println("filiere sélectionné: "+filiere);
    	
    }
    
    @FXML
    public void SelectionnerDate() {
    	
     	try 
    	{
    		
    	date = combDate.getSelectionModel().getSelectedItem().toString();
    	
    	listPeriode = database.getPeriode(date);
    	
    	if(listPeriode.size()==0) {
    		putOnLog("Aucune periode trouvé pour cette date");
    		combPeriode.getSelectionModel().clearSelection();
    		listPeriode.clear();
    		combPeriode.setItems(listPeriode);
    	}
    	else {
    		combPeriode.getSelectionModel().clearSelection();
    		combPeriode.setItems(listPeriode);
    		putOnLog("période trouvé !!!");
    	}
    	}catch(Exception e){
    	
    		date = "";
    	}
    	System.out.println("date sélectionné: "+date);
    	
    }
    
    @FXML
    public void SelectionnerPeriode() {
    	try {
    		
    		periode = combPeriode.getSelectionModel().getSelectedItem().toString();
    		
    		
    	}catch(Exception e) {
    		periode = "";
    		System.out.println(e.getMessage());
    	}
    	
    	System.out.println("Periode selectionné: "+periode);
    	
    }
    
    @FXML
    public void valider() 
    {
    	
    	if(filiere.equals("") || periode.equals("") || date.equals("")) 
    	{
			
			new Thread(() -> {

				try {
					warning.setVisible(true);

					Thread.sleep(4000);

					warning.setVisible(false);

				} catch (InterruptedException ex) {
				}

			}).start();
			
		}
    	else 
    	{
    		etudiantsPresent = database.getPresence(filiere, periode, date);
    		
    		if(etudiantsPresent.size()==0) {
    			putOnLog("Aucun Etudiant trouvé");
    		} else 
    		{
    			tablePresence.setVisible(true);
    			tablePresence.setItems(etudiantsPresent);
    			absentButton.setVisible(true);
    			absentButton.setDisable(false);
    			borderAbs.setVisible(false);
    		}
    			
    	}
    	
    }
    
    @FXML
    public void absent() throws IOException {

    	etudiantsAbsent.clear();
    	tableAbscence.getItems().clear();
    	//borderAbs.setDisable(false);
    	borderAbs.setVisible(true);
    	tablePresence.setVisible(false);
    	etudiantsAbsent = database.getAbscence(filiere, periode, date);
    	
    	if(etudiantsAbsent.size()==0) {
    		
			putOnLog("Aucun Absent trouvé");
		} else tableAbscence.setItems(etudiantsAbsent);
    	
    }
    
    
    
}
