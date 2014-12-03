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
	private int year;
	private String times;
	private String days;
	private String term;
	public ArrayList<StudentForm> forms;
		
	Student(String id){				//populate with data from the other table
	Statement stmt = null;
   	String queryid = "SELECT * FROM Student_Form WHERE id = " + id;   
	Connection conn = ConnectDB.getConn();
	forms = new ArrayList();
	try{			
			stmt = conn.createStatement();	
			ResultSet rs = stmt.executeQuery(queryid);
			
			while (rs.next()) {
				StudentForm sform = new StudentForm();
				sform.setYear(rs.getInt("year"));
				sform.setDays(rs.getString("day"));
				sform.setTerm(rs.getString("term"));
				sform.setTimes(rs.getString("day"));
				sform.setID(id);
				forms.add(sform);
			}
			  stmt.close();
			  rs.close();
			forms.trimToSize();
			
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
	
	public void addStudentForm(StudentForm s){
		forms.add(s);
		forms.trimToSize();
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
	
	public int getYear(){
		return(year);
	}
	
	public void setYear(int x){
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
