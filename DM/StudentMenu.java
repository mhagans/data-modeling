package DM;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;

//Student menu; gets list of students, displays students allows a student to be selected, allows a student to be created
//try to only run this once
public class StudentMenu {
	private String userName="teama1dm2f14";//oracle account user name goes here
    private String password="team1bcchlrt";//password goes here
    private String url="jdbc:oracle:thin:@olympia.unfcsd.unf.edu:1521:dworcl";
    private Hashtable<String, Student> studenttable;
    
       
    //Constructor builds and populates linked list of student objects
    StudentMenu(){
    	Statement stmt = null;
    	String queryid = "SELECT * FROM id WHERE Permission = 0";   	   	
    	try{
    	Connection conn = ConnectDB.getConn();
    	stmt = conn.createStatement();	
		ResultSet rs = stmt.executeQuery(queryid);    		 
    		studenttable = new Hashtable();
    		 while (rs.next()) {
    			 
    			 String studentid = rs.getString("id");					//grab id
    			 Student student = new Student(studentid);					//make a student
    			 student.setID(studentid);
    			 student.setName(rs.getString("name"));    			 
    			 student.setDegree(rs.getString("degree"));    			 
    			 studenttable.put(studentid, student);					//put student object in hashtable	   			
    		 }    		 
    		 displayStudentMenu();			//display stuff  		
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
    public void displayStudentMenu(){
    	System.out.println("Hello, please select a student ID: \n");
    	Scanner reader = new Scanner(System.in);    	
    	Student stud = studenttable.get(reader.next());			//pull student from table    	
    	if(stud != null){										//if it was found display attributes
    		System.out.println("Student ID: " + stud.getID() + "\n");													
    		System.out.println("Student Name: " + stud.getName() + "\n");
    		System.out.println("Student Degree: " + stud.getDegree() + "\n");
    		System.out.println("Student Year: " + stud.getYear() + "\n");
    		System.out.println("Student Term: " + stud.getTerm() + "\n");
    		System.out.println("Student Preffered Days: " + stud.getDays() + "\n");
    		System.out.println("Student Preffered Times: " + stud.getTimes() + "\n");
    		//a thing for classes
    	}
    	
    	else{
    		System.out.println("student not found, create new student? \n");		//go to create student method if not found    		
    	}    	
    }
   private void createStudent(){
	   Scanner reader = new Scanner(System.in);	   	   
	   System.out.println("Student ID: ");
	   String id = reader.next();
	   while(studenttable.get(id) != null){
		   System.out.println("\nThat ID is taken, select a different ID: ");
		   id = reader.next();
	   }	   
	   	System.out.println("\nStudent Name: ");
	   	String name = reader.next();
		System.out.println("\nStudent Degree: ");
		String degree = reader.next();
		System.out.println("\nStudent Year: ");
		int year = reader.nextInt();
		System.out.println("\nStudent Term: ");
		String term = reader.next();
		System.out.println("\nStudent Prefered Days (MW, TR, or MWF: ");
		String prefdays = reader.next();
		System.out.println("\nStudent Prefered Times (morning, afternoon, evening): ");
		String preftimes = reader.next();		
		Statement stmt = null;		
		 String queryid = String.format("INSERT INTO id (id, name, degree, Permission) values %s %s %s  %i;",
                 id, name, degree, 0);
		 String querystud = String.format("INSERT INTO Student_Form (id, year, term, day, time) values %s %s %s %s %s;",
                 id, year, term, prefdays, preftimes);		 
		 try{
		    	Connection conn = ConnectDB.getConn();
		    	stmt = conn.createStatement();	
				stmt.executeQuery(queryid);
				stmt.executeQuery(querystud);
				System.out.println("\nStudent added");
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
}
