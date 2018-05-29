package it.polito.tdp.borders.db;

import it.polito.tdp.borders.model.CoppiaNoStati;
import it.polito.tdp.borders.model.Country;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BordersDAO {
	
	public List<Country> loadAllCountries() {
		
		String sql = 
				"SELECT ccode,StateAbb,StateNme " +
				"FROM country " +
				"ORDER BY StateAbb " ;

		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet rs = st.executeQuery() ;
			
			List<Country> list = new LinkedList<Country>() ;
			
			while( rs.next() ) {
				
				Country c = new Country(
						rs.getInt("ccode"),
						rs.getString("StateAbb"), 
						rs.getString("StateNme")) ;
				
				list.add(c) ;
			}
			
			conn.close() ;
			
			return list ;
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null ;
	}
	
	public List<Country> getCountriesFromYear(int anno) {
		String sql = "SELECT * FROM country " +
				"WHERE CCode IN ( " +
				"SELECT state1no " +
				"FROM contiguity " +
				"WHERE year<=? " +
				"AND conttype=1)";
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, anno);
			ResultSet rs = st.executeQuery() ;
			
			List<Country> list = new LinkedList<Country>() ;
			
			while( rs.next() ) {
				
				Country c = new Country(
						rs.getInt("ccode"),
						rs.getString("StateAbb"), 
						rs.getString("StateNme")) ;
				
				list.add(c) ;
			}
			
			conn.close() ;
			
			return list ;
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}
	
	public List<CoppiaNoStati> getCoppieAdiacenti(int anno) {
		String sql="SELECT state1no, state2no "+
					"FROM contiguity " +
					"WHERE year<=? " +
					"AND conttype=1 " +
					"AND state1no < state2no";
		
		List<CoppiaNoStati> result = new ArrayList<>() ;
		
		try {
			
			Connection conn = DBConnect.getConnection() ;
	
			PreparedStatement st = conn.prepareStatement(sql) ;
			
			st.setInt(1, anno);
			
			ResultSet rs = st.executeQuery() ;
						
			while( rs.next() ) {
				
				result.add(new CoppiaNoStati(rs.getInt("state1no"),rs.getInt("state2no")));
			}
			
			conn.close() ;
			
			return result ;
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	
	}
	
	public static void main(String[] args) {
		List<Country> list ;
		BordersDAO dao = new BordersDAO() ;
		list = dao.loadAllCountries() ;
		for(Country c: list) {
			System.out.println(c);
		}
	}
	
	
}
