
package DM;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Scanner;


public class Report {
    public Report(){
        
        Scanner r=new Scanner(System.in);
        String uin="";
        display: while (true) {
            System.out.println("Reports\n"
                    + "1) Course Listing\n"
                    + "2) Day Listing\n"
                    + "3) Time Listing\n"
                    + "4) Student Listing\n"
                    + "5) Faculty Listing\n"
                    + "6) Back");
            uin = r.next();
            switch (uin) {
                case "1":
                    CL();
                    break;
                case "2":
                    //DL();
                    break;
                case "3":
                    //TL();
                    break;
                case "4":
                    //SL();
                    break;
                case "5":
                    //FL();
                    break;
                case "6":
                    break display;
                default:
                    System.out.println("Bad input: please enter a number between 1-6");
            }
        }
    }
    public void CL(){
        //Connection conn=ConnectDB.getConn();
        System.out.println("Got here");
    }
}
