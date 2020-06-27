package it.polito.tdp.crimes.model;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.EventsDAO;

public class Model {

	private EventsDAO dao ;
	private List<String> categorie;
	private List<String> date;
	
	//grafo semplice, pesato, non orientato
	private Graph<String, DefaultWeightedEdge> grafo;
	private List<String> tipologie;
	private List<Collegamento> collegamenti;
	
	public Model() {
		this.dao = new EventsDAO();
		this.categorie = this.dao.prendiCategorie();
		this.date = this.dao.prendiDate();
		
		this.tipologie = new ArrayList<>();
		this.collegamenti = new ArrayList<>();
	}
	
	public List<String> prendiCategorie(){
		return this.categorie;
	}
	
	public List<String> prendiDate(){
		return this.date;
	}
	
	public void creaGrafo(String categoria, String data) {
		
		this.tipologie = dao.prendiTipoDaCatData(categoria, data);
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		//VERTICI
		Graphs.addAllVertices(this.grafo, this.tipologie);
		
		//ARCHI
		this.collegamenti = this.dao.prendiCollegamenti(categoria, data);
		
		for(Collegamento c: this.collegamenti) {
			
			if(c.getTipo1() != null && c.getTipo2() != null && c.getPeso() != 0) {
				Graphs.addEdge(this.grafo, c.getTipo1(), c.getTipo2(), c.getPeso());
			}
			
		}
		
	}
	
	public String inferioriAllaMediana(){
		String stampa = "";
		float mediana = this.pesoMediano();
		
		for(DefaultWeightedEdge e: this.grafo.edgeSet()) {
			if(this.grafo.getEdgeWeight(e)<=mediana) {
				stampa += this.grafo.getEdgeSource(e)+" --- "+this.grafo.getEdgeTarget(e)+ " "+this.grafo.getEdgeWeight(e)+"\n";
			}
		}
		
		return stampa;
	}
	
	public int numeroVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int numeroArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public float pesoMediano() {
		int max = 0;
		int min = Integer.MAX_VALUE;
		
		for(DefaultWeightedEdge e: this.grafo.edgeSet()) {
			if(this.grafo.getEdgeWeight(e) > max) {
				max = (int) this.grafo.getEdgeWeight(e);
			}
			if(this.grafo.getEdgeWeight(e)<min) {
				min = (int) this.grafo.getEdgeWeight(e);
			}
		}
		
		return (max+min)/2;
	}
	
	
	//-------PUNTO 2-------
	public List<Collegamento> elencoArchi(){
		
		List<Collegamento> lista = new ArrayList<>();
		
		for(DefaultWeightedEdge e: this.grafo.edgeSet()) {
			lista.add(new Collegamento(this.grafo.getEdgeSource(e), this.grafo.getEdgeTarget(e), (int)this.grafo.getEdgeWeight(e)));
		}
		
		return lista;
	}
	
	private List<String> soluzione;
	private int max;
	private Collegamento scelto;
	
	public String cammino(Collegamento scelto) {
		
		this.scelto = scelto;
		String partenza = scelto.getTipo1();
		
		this.soluzione = new ArrayList<>();
		this.max = 0;
		List<String> parziale = new ArrayList<>();
		parziale.add(partenza);
		
		ricorsione(parziale, 0);
		
		String stampa = "";
		for(String s: this.soluzione) {
			stampa += s +"\n";
		}
		
		return stampa;
	}
	
	public void ricorsione(List<String> parziale, int livello) {
		
		String ultimoAggiunto = parziale.get(parziale.size()-1);
		
		List<String> vicini = Graphs.neighborListOf(this.grafo, ultimoAggiunto);
		
		//caso finale
		if(vicini.size()==0) {
			if(this.scelto.getTipo2().equals(ultimoAggiunto) && this.calcolaPesoMassimo(parziale)>max) {
				max = this.calcolaPesoMassimo(parziale);
				this.soluzione = new ArrayList<>(parziale);
			}
		}
		
		//caso intermedio
		for(String s: vicini) {
			parziale.add(s);
			ricorsione(parziale, livello+1);
			parziale.remove(s);
		}
	}
	
	public int calcolaPesoMassimo(List<String> parziale) {
		int conta = 0;
		
		for(String prima : parziale) {
			for(String dopo: parziale) {
				for(Collegamento c: this.collegamenti) {
					if(c.getTipo1().equals(prima) && c.getTipo2().equals(dopo)) {
						conta+=c.getPeso();
					}
				}
			}
		}
		
		return conta;
	}
	
}
