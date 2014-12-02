
package DM;
import DM.LoginServices.AccessLevel;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import java.util.Scanner;

public class MainMenu {
    public MainMenu(){
        Scanner r=new Scanner(System.in);
        int uin=0;
		String user = null;
        while (uin != 4) {
            System.out.println("Main Menu\n"
                    + "1) Student Menu\n"
                    + "2) Faculty Menu\n"
                    + "3) Admin Menu\n"
                    + "4) Exit");
            uin = r.nextInt();
            switch (uin) {
                case 1:
                    /*String P=LoginServices.encryptPassword("teama1admin");
                    String insertAdmin="insert into ID values (?, ?, ?, ?, ?)";
                    Connection conn=ConnectDB.getConn();
                    PreparedStatement ct;
                    try{
                        ct=conn.prepareStatement(insertAdmin);
                        ct.setString(1,"12345678");
                        ct.setString(2,"Admin");
                        ct.setString(3,"CS");
                        ct.setString(4,P);
                        ct.setInt(5,2);
                        ct.executeQuery();
                    }
                    catch(SQLException e){
                        System.exit(1);
                    }*/
                    break;
                case 2:
					if((user = LoginServices.login(LoginServices.AccessLevel.FACULTY)) != null)
                    {
                    //new Faculty(user);
                    }
                    break;
                case 3:
					if((user = LoginServices.login(LoginServices.AccessLevel.ADMIN)) != null)
                {
						new AdminMenu();
                }
                    break;
                case 4:
                    System.exit(1);
                    break;
                default:
                    System.out.println("Bad input: please enter a number between 1-4");
            }
        }
    }
}
