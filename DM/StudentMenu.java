package DM;


//create a new student form, students have forms
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
        	System.out.println("Edit/View courses and forms for this student? (y/n)\n");
            String input = reader.next();
            if((input.toLowerCase().equals("y")) || (input.toLowerCase().equals("yes"))){
            	//go to dispclassmenu method
            	displayStudentClasses(stud);
            }
            else if((input.toLowerCase().equals("n")) || (input.toLowerCase().equals("no"))){
            	System.out.println("exiting \n");
            }
            else{
            	System.out.println("nooo");
            }
    	}
    	
    	
    	
    	else{
    		System.out.println("student not found, create new student?(y/n)\n");		//go to create student method if not found   
    		String select = reader.next();
    		if(select.toLowerCase().equals("y") || select.toLowerCase().equals("yes"))
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
		Statement stmt = null;		//INSERT INTO id (id, name, degree, Permission) values ('00624604', 'Clayton', 'cs', 0);
		 String queryid = String.format("INSERT into id VALUES('%s', '%s', '%s', NULL, %d)", id, name, degree, 0);
				 //"INSERT INTO id (id, name, degree, password, Permission) values (" + "'" + id + "', " + "'" + name + "', " + "'" + degree + "', " + "NULL, " + "'0'" + ");";
                 	 		 
		 try{
		    	Connection conn = ConnectDB.getConn();
		    	stmt = conn.createStatement();	
				stmt.executeUpdate(queryid);		
				stmt.close();
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
   
   private void createStudentForm(Student s){
	   Scanner reader = new Scanner(System.in);	   
	   System.out.println("Select Year: ");
	   int year = reader.nextInt();
	   System.out.println("Select Term: ");
	   String term = reader.next();
	   System.out.println("Select prefered days for the term (MWF, TR, MW): ");
	   String days = reader.next();
	   System.out.println("Select prefered times (morning, afternoon, evening): ");
	   String times = reader.next();
	   
	   Statement stmt = null;	
	   
	   String querystud = String.format("INSERT INTO Student_Form (id, year, term, day, time) values %s %d %s %s %s;",
               s.getID(), year, term, days, times);		 
		 try{
		    	Connection conn = ConnectDB.getConn();
		    	stmt = conn.createStatement();					
				stmt.executeQuery(querystud);
				System.out.println("\nStudent form added");
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
	  
	  boolean exit = true;	  
	  while(exit){
	  System.out.println("Student class menu for " + stud.getName() + "\n");
	  System.out.println("1. View enrolled courses\n");
	  System.out.println("2. See available courses\n");
	  System.out.println("3. Enroll in a course\n");
	  System.out.println("4. Create New Form\n");
	  System.out.println("5. Exit");
	  Scanner reader = new Scanner(System.in);
	  int selection = reader.nextInt();
	  
	  switch (selection){
	  case 1:	viewEnrolled(stud); 
		  break;
	  case 2: 	showAvailable(stud);
		  break;
	  case 3: 	showEnroll(stud);
		  break;
	  case 4:   createStudentForm(stud);
	  	  break;
	  case 5:   exit = false;
		  break;
	  }
	  
	  }
	  }
	  
	  private void viewEnrolled(Student s){
		  System.out.println("Select a form");
		  if(s.forms.size()>0){
		  for(int i = 0;i<s.forms.size();i++){
			  System.out.println(s.forms.get(i).getTerm() + " " + s.forms.get(i).getYear());
		  }
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
		  else{
			  System.out.println("Error: No student form has been created");
		  }
	  }
	  
	  private void showAvailable(Student s){
		  if(s.forms.size()>0){
		  System.out.println("Select a form");
		  for(int i = 0;i<s.forms.size();i++){
			  System.out.println(i + ". " + s.forms.get(i).getTerm() + " " + s.forms.get(i).getYear());
		  }
		  
		  Scanner reader = new Scanner(System.in);
		  
		  int selection = reader.nextInt();  
		  
		  
		  Statement stmt = null;
	    	String query = "SELECT * FROM Course WHERE term = " + "'" + s.forms.get(selection).getTerm().toLowerCase() + "'";   	   	
	    	try{    		
	    	Connection conn = ConnectDB.getConn();    	
	    	stmt = conn.createStatement();	
			ResultSet rs = stmt.executeQuery(query); 
			int n = 1;
	    		 while (rs.next()) {    			 
	    			System.out.println(n + ". " + rs.getString("course_number") + " " + rs.getString("name") + "\n");
	    			n++;
	    		 }
	    		 System.out.println("--------------------------");
	    				 		
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
		  else{
			  System.out.println("Error: No student form has been created");
		  }
	  }
	  
	  private void showEnroll(Student s){
		  System.out.println("Select a form");
		  if(s.forms.size()>0){
		  for(int i = 0;i<s.forms.size();i++){
			  System.out.println(i + ". " + s.forms.get(i).getTerm() + " " + s.forms.get(i).getYear());
		  }
		  Scanner reader = new Scanner(System.in);
		  int selection = reader.nextInt();
		  Statement stmt = null;
	    	String query = "SELECT * FROM Course WHERE term = " + "'" + s.forms.get(selection).getTerm().toLowerCase() + "'";   	   	
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
	    	
	    	String input = reader.next();
	    	
	        String enrollquery = String.format("INSERT INTO Form_Course (id, year, term, course_number, ranking) values '%s' %d '%s' '%s' %d;",
	        		s.forms.get(selection).getID(), s.forms.get(selection).getYear(), s.forms.get(selection).getTerm(), input, 0);
	        	try {
	        		stmt.executeQuery(enrollquery);
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
		  else{
			  System.out.println("Error: No student form has been created");
		  }
	  }
	  
 }

