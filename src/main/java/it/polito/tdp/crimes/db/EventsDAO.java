package it.polito.tdp.crimes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.crimes.model.Collegamento;
import it.polito.tdp.crimes.model.Event;


public class EventsDAO {
	
	public List<Event> listAllEvents(){
		String sql = "SELECT * FROM events" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Event> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Event(res.getLong("incident_id"),
							res.getInt("offense_code"),
							res.getInt("offense_code_extension"), 
							res.getString("offense_type_id"), 
							res.getString("offense_category_id"),
							res.getTimestamp("reported_date").toLocalDateTime(),
							res.getString("incident_address"),
							res.getDouble("geo_lon"),
							res.getDouble("geo_lat"),
							res.getInt("district_id"),
							res.getInt("precinct_id"), 
							res.getString("neighborhood_id"),
							res.getInt("is_crime"),
							res.getInt("is_traffic")));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<String> prendiCategorie(){
		String sql = "SELECT DISTINCT e.offense_category_id cat" + 
				" FROM `events` e" + 
				" ORDER BY e.offense_category_id";
		List<String>lista = new ArrayList<>();
		
		try {
			Connection con = DBConnect.getConnection();
			PreparedStatement st = con.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			
			while(res.next()) {
				String in = res.getString("cat");
				lista.add(in);
			}
			
			con.close();
			
		}catch(SQLException e) {
			throw new RuntimeException("ERRORE DB: impossibile prendere le categorie.\n", e);
		}
		
		return lista;
	}

	public List<String> prendiDate(){
		String sql = "SELECT DISTINCT date(e.reported_date) dat" + 
				" FROM `events` e" + 
				" ORDER BY e.reported_date";
		List<String>lista = new ArrayList<>();
		
		try {
			Connection con = DBConnect.getConnection();
			PreparedStatement st = con.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			
			while(res.next()) {
				String in = res.getString("dat");
				lista.add(in);
			}
			
			con.close();
			
		}catch(SQLException e) {
			throw new RuntimeException("ERRORE DB: impossibile prendere le date.\n", e);
		}
		
		return lista;
	}
	
	public List<String> prendiTipoDaCatData(String categoria, String data){
		String sql = "SELECT DISTINCT e.offense_type_id tipo" + 
				" FROM `events` e" + 
				" WHERE e.offense_category_id = ? AND date(e.reported_date) = ? ";
		List<String>lista = new ArrayList<>();
		
		try {
			Connection con = DBConnect.getConnection();
			PreparedStatement st = con.prepareStatement(sql);
			st.setString(1, categoria);
			st.setString(2, data);
			ResultSet res = st.executeQuery();
			
			while(res.next()) {
				String in = res.getString("tipo");
				lista.add(in);
			}
			
			con.close();
			
		}catch(SQLException e) {
			throw new RuntimeException("ERRORE DB: impossibile prendere le tipologie.\n", e);
		}
		
		return lista;
	}
	
	public List<Collegamento> prendiCollegamenti(String cat, String data){
		String sql = "SELECT e.offense_type_id tipo1, e1.offense_type_id tipo2, count(e.precinct_id) peso" + 
				" FROM `events` e, `events` e1" + 
				" WHERE e.offense_category_id = ? AND date(e.reported_date) = ? " + 
				"		AND e.offense_category_id = e1.offense_category_id AND date(e.reported_date) = date(e1.reported_date)" + 
				"		AND e.offense_type_id< e1.offense_type_id" + 
				"		AND e.precinct_id = e1.precinct_id";
		List<Collegamento>lista = new ArrayList<>();
		
		try {
			Connection con = DBConnect.getConnection();
			PreparedStatement st = con.prepareStatement(sql);
			st.setString(1, cat);
			st.setString(2, data);
			ResultSet res = st.executeQuery();
			
			while(res.next()) {
				
				String t1 = res.getString("tipo1");
				String t2 = res.getString("tipo2");
				Integer pe = res.getInt("peso");
				
				if(t1!=null && t2!=null && pe != 0) {
					Collegamento c = new Collegamento(t1, t2, pe);
					
					lista.add(c);
				}
				
			}
			
			con.close();
			
		}catch(SQLException e) {
			throw new RuntimeException("ERRORE DB: impossibile prendere le tipologie e le stazioni distinte.\n", e);
		}
		
		return lista;
	}
}
