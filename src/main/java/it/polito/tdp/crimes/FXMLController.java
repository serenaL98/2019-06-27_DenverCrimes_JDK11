package it.polito.tdp.crimes;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.crimes.model.Collegamento;
import it.polito.tdp.crimes.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

//controller turno C --> switchare al branch master_turnoA o master_turnoB per turno A o B

public class FXMLController {
	
	private Model model;

	@FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<String> boxCategoria;

    @FXML
    private ComboBox<String> boxGiorno;

    @FXML
    private Button btnAnalisi;

    @FXML
    private ComboBox<Collegamento> boxArco;

    @FXML
    private Button btnPercorso;

    @FXML
    private TextArea txtResult;

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	
    	//dato il grafo precedente calcola percorso
    	txtResult.clear();
    	
    	Collegamento arco = this.boxArco.getValue();
    	if(arco == null) {
    		txtResult.setText("Selezionare un arco.");
    		return;
    	}
    	
    	txtResult.setText("Cammino calcolato:\n"+this.model.cammino(arco));
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {

    	txtResult.clear();
    	
    	String categoria = this.boxCategoria.getValue();
    	if(categoria == null) {
    		txtResult.setText("Selezionare una categoria.");
    		return;
    	}
    	
    	String data = this.boxGiorno.getValue();
    	if(data == null) {
    		txtResult.setText("Selezionare una data.");
    		return;
    	}
    	
    	this.model.creaGrafo(categoria, data);
    	
    	txtResult.setText("Crea grafo...");
    	txtResult.appendText("\n\n#VERTICI: "+this.model.numeroVertici());
    	txtResult.appendText("\n#ARCHI: "+this.model.numeroArchi());
    	
    	if(this.model.numeroArchi()!=0) {
    		txtResult.appendText("\n\nArchi con peso inferiore alla mediana:\n"+this.model.inferioriAllaMediana());
    	}else
    		txtResult.appendText("\n\nNon ci sono archi: non è possibile determinare il peso medio.La tendina sarà vuota.");
    	
    	if(this.model.elencoArchi() != null) {
    		this.boxArco.getItems().addAll(this.model.elencoArchi());
    	}else
    		txtResult.setText("\n\nNon ci sono archi: non è possibile riempire la tendina.");
    	
    	
    }

    @FXML
    void initialize() {
        assert boxCategoria != null : "fx:id=\"boxCategoria\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert boxGiorno != null : "fx:id=\"boxGiorno\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert boxArco != null : "fx:id=\"boxArco\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert btnPercorso != null : "fx:id=\"btnPercorso\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Crimes.fxml'.";

    }

	public void setModel(Model model) {
		this.model = model;
		this.boxCategoria.getItems().addAll(this.model.prendiCategorie());
		this.boxGiorno.getItems().addAll(this.model.prendiDate());
	}
}
