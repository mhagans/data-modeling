package DM;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

//Student menu; gets list of students, displays students allows a student to be selected, allows a student to be created
//try to only run this once
public class StudentMenu {
	private String userName="teama1dm2f14";//oracle account user name goes here
    private String password="team1bcchlrt";//password goes here
    private String url="jdbc:oracle:thin:@olympia.unfcsd.unf.edu:1521:dworcl";
    private Connection conn;
    private Hashtable<String, student> studenttable;
    
    private void openDBcon(){	 
    	try{
    		DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
    		//Can use either one need to test which will work. The one below was the one we used in SE
    		//Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
    		conn=DriverManager.getConnection(url, userName, password);
    		System.out.println("Connected to DB");
         
    	}
    	catch (Exception ie) {
    		if (ie.getMessage().equals("IO Error: The Network Adapter could not establish the connection")) {
    			System.out.println("Error The network adapter could not establish the connection\n"
    					+ "Make sure you are connected to the unf campus internet either locally or through a VPN"
    					+ "\nSee the unf support page for info on VPNing into the campus internet");
         } 
    		else {
    			System.out.println(ie.getMessage());
         }
    	}
	}
    
    private void closeDBcon(){
    	try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    //Constructor builds and populates linked list of student objects
    StudentMenu(){
    	Statement stmt = null;
    	String query = "SELECT * FROM id WHERE Permission = 0";
    	
    	LinkIterator iterator = new LinkIterator();
    	
    	try{
    		openDBcon();						//Open the DB connection
    		stmt = conn.createStatement();	
    		 ResultSet rs = stmt.executeQuery(query);
    		 
    		studenttable = new Hashtable<String, Student>();
    		 while (rs.next()) {
    			 Student student = new Student();					//make a student
    			 studentid = rs.getString("id");					//get attributes and put them in student
    			 student.setID(studentid);
    			 student.setName(rs.getString("name"));    			 
    			 student.setDegree(rs.getString("degree"));    			 
    			 studenttable(studentid, student);					//put student object in hashtable		 
    			 
    			
    		 }
    		 closeDBcon();					//close DB connection
    		 
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
    		System.out.println("Student Preffered Days: " stud.getDays() + "\n");
    		System.out.println("Student Preffered Times: " + stud.getTimes() + "\n");
    		//a thing for classes
    	}
    	
    	else{
    		System.out.println("student not found, create new student? \n");		//go to create student method if not found
    		
    	}
    	
    }
   private createStudent(){
	   //put a student in the db
   }
}
