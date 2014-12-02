package DM;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

//Students have IDs, names, degrees, semester + year, availible courses, preffered days?
public class Student {

	private String ID;
	private String name;
	private String degree;
	private String year;
	private String times;
	private String days;
	private String term;
	private ArrayList courses;
	
		
	Student(String id){				//populate with data from the other table
	Statement stmt = null;
   	String queryid = "SELECT * FROM Student_Form WHERE id = " + id;   
	Connection conn = ConnectDB.getConn();
	try{			
			stmt = conn.createStatement();	
			ResultSet rs = stmt.executeQuery(queryid);
			while (rs.next()) {
				setYear(rs.getString("year"));
				setTerm(rs.getString("term"));
				setDays(rs.getString("day"));
				setTimes(rs.getString("time"));				
			}
			
	}
	catch (SQLException e ) {
        e.printStackTrace();
    } finally {
        if (stmt != null) { try {
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
				e.printStackTrace();
				} 
        	}
    	}
	}
	
	public String getID(){
		return(ID);
	}
	
	public void setID(String studentid){
		ID = studentid;
	}
	
	public String getName(){
		return(name);
	}
	
	public void setName(String x){
		name = x;
	}
	
	public String getDegree(){
		return(degree);
	}
	
	public void setDegree(String x){
		degree = x;
	}
	
	public String getYear(){
		return(year);
	}
	
	public void setYear(String x){
		year = x;
	}
	
	public void setTimes(String x){
		times = x;
	}
	public String getTimes(){
		return(times);
	}
	
	public void setDays(String x){
		days = x;
	}
	
	public String getDays(){
		return(days);
	}
	
	public void setTerm(String x){
		term = x;
	}
	
	public String getTerm(){
		return(term);
	}
	
}
