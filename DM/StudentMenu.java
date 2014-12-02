package DM;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Scanner;

//Student menu; gets list of students, displays students allows a student to be selected, allows a student to be created
//try to only run this once
public class StudentMenu {	
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
    	System.out.println("Listed Students: ");
    	Enumeration e = studenttable.keys();
    	while(e.hasMoreElements()){
    		System.out.println(e.nextElement());
    		System.out.println("\n");
    	}    	
    	System.out.println("Please select a student ID: \n");
    	Scanner reader = new Scanner(System.in);    	
    	Student stud = studenttable.get(reader.next());			//pull student from table    	
    	if(stud != null){										//if it was found display attributes
    		System.out.println("Student ID: " + stud.getID() + "\n");													
    		System.out.println("Student Name: " + stud.getName() + "\n");
    		System.out.println("Student Degree: " + stud.getDegree() + "\n");
    		System.out.println("Student Year: " + stud.getYear() + "\n");
    		System.out.println("Student Term: " + stud.getTerm() + "\n");
    		System.out.println("Student Prefered Days: " + stud.getDays() + "\n");
    		System.out.println("Student Prefered Times: " + stud.getTimes() + "\n");
    		//a thing for classes
    		//show edit course listing        	
        	System.out.println("Edit/View courses for this student? (y/n)\n");
            String input = reader.next();
            if((input.toLowerCase() == "y") || (input.toLowerCase() == "yes")){
            	//go to dispclassmenu method
            	displayStudentClasses(stud);
            }
    	}
    	
    	
    	
    	else{
    		System.out.println("student not found, create new student? \n");		//go to create student method if not found   
    		createStudent();
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
   private void displayStudentClasses(Student stud){
	  System.out.println("Student class menu for " + stud.getName() + "\n");
	  System.out.println("1. View enrolled courses\n");
	  System.out.println("2. See available courses\n");
	  System.out.println("3. Enroll in a course\n");
	  System.out.println("4. Exit");
	  Scanner reader = new Scanner(System.in);
	  int selection = reader.nextInt();
	  
	  switch (selection){
	  case 1:	viewEnrolled(stud); 
		  break;
	  case 2: 	showAvailable(stud);
		  break;
	  case 3: 	showEnroll(stud);
		  break;
	  case 4: break;
	  }
	  
	  
	  }
	  
	  private void viewEnrolled(Student s){
		  Statement stmt = null;
	    	String query = "SELECT * FROM Form_Course WHERE id = " + s.getID();   	   	
	    	try{    		
	    	Connection conn = ConnectDB.getConn();    	
	    	stmt = conn.createStatement();	
			ResultSet rs = stmt.executeQuery(query); 
			int n = 1;
	    		 while (rs.next()) {    			 
	    			System.out.println(n + ". " + rs.getString("course_number") + "\n");
	    			n++;
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
	  
	  private void showAvailable(Student s){
		  Statement stmt = null;
	    	String query = "SELECT * FROM Course WHERE term = " + s.getTerm();   	   	
	    	try{    		
	    	Connection conn = ConnectDB.getConn();    	
	    	stmt = conn.createStatement();	
			ResultSet rs = stmt.executeQuery(query); 
			int n = 1;
	    		 while (rs.next()) {    			 
	    			System.out.println(n + ". " + rs.getString("course_number") + " " + rs.getString("name") + "\n");
	    			n++;
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
	  
	  private void showEnroll(Student s){
		  Statement stmt = null;
	    	String query = "SELECT * FROM Course WHERE term = " + s.getTerm();   	   	
	    	try{    		
	    	Connection conn = ConnectDB.getConn();    	
	    	stmt = conn.createStatement();	
			ResultSet rs = stmt.executeQuery(query); 
			int n = 1;
	    		 while (rs.next()) {    			 
	    			 System.out.println(n + ". " + rs.getString("course_number") + " " + rs.getString("name") + "\n");
	    			n++;
	    		 }
	    	System.out.println("Type in a CRN to enroll in that course or exit to exit: \n");
	    	Scanner reader = new Scanner(System.in);
	        while(reader.next().toLowerCase() != "exit"){
	        	
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
	  
 }

