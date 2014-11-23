import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//Student menu; gets list of students, displays students allows a student to be selected, allows a student to be created
public class StudentMenu {
	private String userName="teama1dm2f14";//oracle account user name goes here
    private String password="team1bcchlrt";//password goes here
    private String url="jdbc:oracle:thin:@olympia.unfcsd.unf.edu:1521:dworcl";
    private Connection conn;
    
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
    
    StudentMenu(){
    	
    }
    
    public void displayStudentMenu(){
    	
    }
    
    
}
