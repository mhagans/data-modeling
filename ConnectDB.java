package DM;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;



public class ConnectDB{
    
    
    public static void main(String[] args)throws SQLException{
        Connection conn=getConn();//used to see if you are on the school network or not
        //if you are not it will throw an error and exit
        conn.close();
        while(1==1){
        new MainMenu();
        }
    }
    
    public static Connection getConn(){
        String userName;
        String password;
        String url;
        userName="teama1dm2f14";//oracle account user name goes here
        password="team1bcchlrt";//password goes here
        url="jdbc:oracle:thin:@olympia.unfcsd.unf.edu:1521:dworcl";
        Connection conn;
        try{
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            conn=DriverManager.getConnection(url, userName, password);
            System.out.println("Connected");
            return conn;
        }
        catch (Exception ie) {
            if (ie.getMessage().equals("IO Error: The Network Adapter could not establish the connection")) {
                System.out.println("Error The network adapter could not establish the connection\n"
                        + "Make sure you are connected to the unf campus internet either locally or through a VPN"
                        + "\nSee the unf support page for info on VPNing into the campus internet");
            } else {
                System.out.println(ie.getMessage());
            }
            System.exit(1);
        }
        return null;
    }
}
