
package it.polito.tdp.borders;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.jgrapht.Graph;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;

import it.polito.tdp.borders.db.BordersDAO;
import it.polito.tdp.borders.model.Country;
import it.polito.tdp.borders.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

	private Model model;
	private Map<String, Country> idMapNomi;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;
    
    @FXML // fx:id="cmbBox"
    private ComboBox<String> cmbStati; // Value injected by FXMLLoader

    @FXML // fx:id="txtAnno"
    private TextField txtAnno; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCalcolaConfini(ActionEvent event) {
    	txtResult.clear();
    	int anno;
    	try {
			anno=Integer.parseInt(txtAnno.getText());
		} catch (Exception e) {
			txtResult.setText("Oh no! Qualcosa è andato storto!");
			return;
		}
    	if(anno<1816) {
    		txtResult.setText("Inserire un anno successivo al 1816 (1816 compreso)");
    		return;
    	}
    	if(anno>2022) {
    		txtResult.setText("Non prevedo il futuro");
    		return;
    	}
    	
    	Graph<Country, DefaultEdge> grafo=model.calcolaConfini(anno);
    	ConnectivityInspector<Country , DefaultEdge> ispettore=new ConnectivityInspector<>(grafo);
    	
    	List<Country> lista=new ArrayList<Country>(grafo.vertexSet());
    	Collections.sort(lista, new ordinaCountry());
    	
    	txtResult.setText("Il grafo contiene "+lista.size()+" stati e "+grafo.edgeSet().size()+" confini.\n");
    	txtResult.appendText("Nel grafo sono presenti "+ispettore.connectedSets().size()+" componenti connesse.\n");
    	txtResult.appendText("Elenco stati: \n");
    	for(Country country : lista) {
    		int n=grafo.degreeOf(country);
    		txtResult.appendText(country.getAbb()+" "+country.getNome()+" confina con "+n+" stati\n");
    	}
     }
    
    @FXML
    void doStatiRaggiungibili(ActionEvent event) {
    	txtResult.clear();
    	int anno=0;
    	try {
			anno=Integer.parseInt(txtAnno.getText());
			if(anno<1816 || anno>2022)
				throw new Exception();
    	} catch (NumberFormatException nfe) {
    		txtResult.setText("Formato data errato o data mancante!\n");
    		return;
		} catch (Exception e) {
			txtResult.setText("Inserire data compresa tra 1816 e 2022 (estremi compresi)\n");
			return;
		}
    	String stato;
    	int id;
    	try {
    		stato=cmbStati.getValue();
    		id=idMapNomi.get(stato).getCod(); 
		} catch (Exception e) {
			txtResult.appendText("Inserire uno stato\n");
			return;
		}
    	//fine controllo dell'input
    	
    	model.creaGrafo(anno);
    	List<Country> listaStatiRaggiungibi=model.statiRagiungibili(id);
    	if(listaStatiRaggiungibi==null) {
    		txtResult.appendText("Nel "+anno+" partendo da "+stato+" non si poteva raggiungere nesssun altro stato\n");
    	} else {
	    	Collections.sort(listaStatiRaggiungibi, new ordinaCountry());
	    	
	    	txtResult.appendText("Nel "+anno+" partendo dallo stato "+stato+" si potevano raggiungere "+listaStatiRaggiungibi.size()+" stati\n");
	    	txtResult.appendText("Elenco stati raggiungibili partendo da "+stato+":\n");
	    	for(Country c : listaStatiRaggiungibi) {
	    		txtResult.appendText(c.getNome()+"\n");
	    	}
    	}
    }
    

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtAnno != null : "fx:id=\"txtAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
    }
    
    public void setModel(Model model) {
    	this.model = model;
    	
    	idMapNomi=new HashMap<String, Country>();
    	cmbStati.getItems().clear();
    	List<Country> lista=model.loadAllCountries();
    	Collections.sort(lista, new ordinaCountry());
    	for(Country c : lista) {
    		cmbStati.getItems().add(c.getNome());
    		idMapNomi.put(c.getNome(), c);
    	}
    		
    }
}
