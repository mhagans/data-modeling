package DM;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

//Student menu; gets list of students, displays students allows a student to be selected, allows a student to be created
//try to only run this once
public class StudentMenu {
	private String userName="teama1dm2f14";//oracle account user name goes here
    private String password="team1bcchlrt";//password goes here
    private String url="jdbc:oracle:thin:@olympia.unfcsd.unf.edu:1521:dworcl";
    private Connection conn;
    private ArrayList Students;
    
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
    	String query = "SELECT name FROM id WHERE Permission = 0";
    	
    	LinkIterator iterator = new LinkIterator();
    	
    	try{
    		openDBcon();						//Open the DB connection
    		stmt = conn.createStatement();	
    		 ResultSet rs = stmt.executeQuery(query);   		 
    		 while (rs.next()) {
    			 Student student = new Student();
    			 student.setName(rs.getString("name"));
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
    		 closeDBcon();
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
    	    	
    }    
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
}
