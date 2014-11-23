import java.sql.DriverManager;
import java.sql.Connection;


public class ConnectDB{
    
    public static void main(String[] args){
        String userName="teama1dm2f14";//oracle account user name goes here
        String password="team1bcchlrt";//password goes here
        String url="jdbc:oracle:thin:@olympia.unfcsd.unf.edu:1521:dworcl";
        Connection conn;
        try{
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            //Can use either one need to test which will work. The one below was the one we used in SE
            //Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
            conn=DriverManager.getConnection(url, userName, password);
            System.out.println("Connected to DB");
            conn.close();
        }
        catch (Exception ie) {
            if (ie.getMessage().equals("IO Error: The Network Adapter could not establish the connection")) {
                System.out.println("Error The network adapter could not establish the connection\n"
                        + "Make sure you are connected to the unf campus internet either locally or through a VPN"
                        + "\nSee the unf support page for info on VPNing into the campus internet");
            } else {
                System.out.println(ie.getMessage());
            }
        }
    }
}


