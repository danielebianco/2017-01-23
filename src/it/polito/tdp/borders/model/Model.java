package it.polito.tdp.borders.model;

import java.util.*;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.borders.db.BordersDAO;

public class Model {
	
	private Graph<Country, DefaultEdge> graph;
	private List<Country> countries;
	private Map<Integer,Country> countriesMap ;
	
	public Model() {
		
	}
	
	public void creaGrafo(int anno) {
		BordersDAO dao = new BordersDAO();
		this.graph = new SimpleGraph<>(DefaultEdge.class);
		// vertici
		this.countries = dao.getCountriesFromYear(anno);
		this.countriesMap = new HashMap<>();
		for(Country c : countries) {
			this.countriesMap.put(c.getcCode(), c);
		}
		Graphs.addAllVertices(graph, this.countries);
		
		
		// archi
		List<CoppiaNoStati> archi = dao.getCoppieAdiacenti(anno);
		for(CoppiaNoStati c : archi) {
			graph.addEdge(this.countriesMap.get(c.getState1no()),
					this.countriesMap.get(c.getState2no()));
		}
		
//		System.out.format("Grafo creato con %d vertici e %d archi\n",
//				this.graph.vertexSet().size(), this.graph.edgeSet().size());
	}
	
	public List<CountryAndNumber> getCountryAndNumber() {
		List<CountryAndNumber> list = new ArrayList<>();
		for(Country c : graph.vertexSet()) {
			list.add(new CountryAndNumber(c,graph.degreeOf(c)));
		}
		Collections.sort(list);
		return list;
	}
	
}
