package it.polito.tdp.borders.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

import it.polito.tdp.borders.db.BordersDAO;

public class Model {

	private BordersDAO dao=new BordersDAO();
	private Graph<Country,DefaultEdge> grafo;
	private Map<Integer, Country> idMap;
	
	public Model() {
		dao=new BordersDAO();
		idMap=new HashMap<Integer, Country>();
		
		for(Country c : dao.loadAllCountries()) {  //assumo che non cambino nel tempo gli stati
			idMap.put(c.getCod(), c);
		}
	}
	
	public List<Country> loadAllCountries() {
		return dao.loadAllCountries();
	}

	public void creaGrafo(int anno) {
		grafo=new SimpleGraph<Country,DefaultEdge>(DefaultEdge.class);
		
		//aggiungo i vertici (solo quelli che hanno dei confini) e gli archi
		for(Border b : dao.getCountryPairs(anno)) {
			if(!grafo.containsVertex(idMap.get(b.getCountry1())))
				grafo.addVertex(idMap.get(b.getCountry1()));
			if( !grafo.containsVertex( idMap.get(b.getCountry2()) ) )
				grafo.addVertex(idMap.get(b.getCountry2()));
			
			grafo.addEdge(idMap.get(b.getCountry1()), idMap.get(b.getCountry2()));
		}
	}

	public Graph<Country, DefaultEdge> calcolaConfini(int anno) {
			creaGrafo(anno);
		return this.grafo;
	}

	public List<Country> statiRagiungibili(int id) {
			List<Country> result=new ArrayList<Country>();
			
			try {
				GraphIterator<Country, DefaultEdge> visita=new BreadthFirstIterator<Country, DefaultEdge>(this.grafo, idMap.get(id));
				
				while(visita.hasNext()) {
					result.add(visita.next());
				}
				return result;
			} catch (Exception e) {
				return null;
			}
	}
	
	
}
