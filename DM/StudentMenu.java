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
    private LinkIterator studentlinkedlist;
    
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
    	String query = "SELECT id FROM id WHERE Permission = 0";
    	
    	LinkIterator iterator = new LinkIterator();
    	
    	try{
    		openDBcon();						//Open the DB connection
    		stmt = conn.createStatement();	
    		 ResultSet rs = stmt.executeQuery(query);   		 
    		 while (rs.next()) {
    			 Student student = new Student();
    			 student.setID(rs.getString("id"));
    			 Link link = new Link();
    			 link.setStudent(student);
    			 
    			 if(iterator.noFirstLink()){
    				 iterator.setFirstLink(link);
    			 }
    			 
    			 else{
    				 iterator.getcurrentlink().setnextlink(link);
    				 iterator.nextlink();
    			 }    			 
    		 }
    		 closeDBcon();					//close DB connection
    		 iterator.resetIterator();      //puts iterator back on first link
    		 studentlinkedlist = iterator;  //if completed without issues puts the iterator to the class iterator for wider scope
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
    
    public void selectStudent(){
    	System.out.println("Please select a student ID:\n");
    	Scanner sc = new Scanner(System.in);
    	
    	String lookup = sc.next();
    	
    	//look through students to see if an ID matches
    	while((studentlinkedlist.getcurrentlink().getStudent().getID() != lookup) && studentlinkedlist.getcurrentlink() != null){
    		studentlinkedlist.nextlink();
    	}
    	//if the list is exhausted and there was no match, reset the iterator, ask create new student
    	if(studentlinkedlist.getcurrentlink() == null){
    		studentlinkedlist.resetIterator();
    		system.out.println("A match wasn't found, create new student?(y/n)\n");
    		String selection = sc.next();
    		if(selection.toLowerCase() == "yes" || "y"){
    			//go to create student method
    			
    		}
    		else if(selection.toLowerCase() == "no" || "n"){
    			//break out?
    			break;
    		}
    		else{
    			System.out.println("not a valid input\n");
    		}
    	}
    	//if a match is found, reset iterator, go to students info method
    	else if(student.linkedlist.getcurrentlink().getStudent().getID() == lookup){
    		studentlinkedlist.resetIterator();
    		system.out.println("Match found\n")
    		//go to a method that displays students info
    	}
    	else{
    		System.out.println("A student wasnt found and end of list didnt happen, welp\n");
    	}
    }
    
    //A method that displays student info
    public void displayStudentInfo(String s){
    	//open a db connection
    	openDBcon();
    	Statement stmt = null;
    	String query = "SELECT * FROM Student_Form where id = " + s;
    	//get all of the student info, print to screen
    	stmt = conn.createStatement();	
		 ResultSet rs = stmt.executeQuery(query);   		 
		 while (rs.next()) {		
			 System.out.println("Student ID: " + rs.getString("id") + "\n");	//Find and print the student's ID
		 }
    }
    //A method that creates student
    public void createStudent(){
    	//get all the info to create a student first
    	Scanner sc = new Scanner(system.in);
    	System.out.println("Student name?\n");
    	String sname = sc.next();    	
    	//make sure it fits
    	while(sname.length() > 110){
    		System.out.printlin("Name is too long, please adjust\n");
    		sname = sc.next();
    	}
    	System.out.println("Student degree?\n")
    	String sdegree = sc.next();
    	System.out.println("Student year?\n");
    	String syear = sc.next();
    	
    	//prefered times? need clarification on this
    	//open the db connection
    	openDBcon();
    	Statement stmt = null;
    	String query = "";
    }
    
   /* Disp student menu mothballed, students selected by direct id
    * public void displayStudentMenu(){
    	
    	int n = 1;
    	System.out.println("Hello, please select a student, or create a new one:\n");		//friendly message
    	while(studentlinkedlist.getcurrentlink() != null){				//while there are more links    		
    		System.out.println(n + ". " + studentlinkedlist.getcurrentlink().getStudent().getName() + "\n");	//get the students name
    		studentlinkedlist.nextlink();	//increment the linkedlist
    		n++; //increment the index
    	}
    	System.out.println("Create new student (type 'new')\n");		//create new dialog
    }   
    */ 
}

class Link{
	  
	  private Link nextlink;
	  private Link prevlink;
	  private Student student;
	  
	  

	  public Student getStudent(){
	    return(student);
	  }

	  public void setStudent(Student x){
	    student = x;
	  }

	  public Link getnextlink(){
	    return(nextlink);
	  }

	  public void setnextlink(Link x){
	    nextlink = x;
	  }

	  public Link getprevlink(){
	    return(prevlink);
	  }

	  public void setprevlink(Link x){
	    prevlink = x;
	  }
}

class LinkIterator{
	  private Link currentlink;
	  private Link firstlink;
	  
	  public boolean noFirstLink(){
		  if(firstlink == null){
			  return true;
		  }
		  else{
			  return false;
		  } 		  
	  }
	  
	  public Link getFirstLink(){
		  return firstlink;
	  }
	  
	  public void setFirstLink(Link x){
		  firstlink = x;
	  }
	  
	  public void setcurrentlink(Link x){
	    currentlink = x;
	  }
	  
	  public Link getcurrentlink(){
	    return(currentlink);
	  }

	  public void nextlink(){
	   if(currentlink.getnextlink() != null)
	     currentlink = currentlink.getnextlink();
	   else
	     System.out.print("End of list");   
	}
	  
	  public void resetIterator(){
		  currentlink = firstlink;
	  }
}
