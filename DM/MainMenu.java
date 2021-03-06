
package DM;
import DM.LoginServices.AccessLevel;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import DM.LoginServices.AccessLevel;

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
                    new StudentMenu();
                    break;
                case 2:
                    if ((user = LoginServices.login(AccessLevel.FACULTY)) != null) {
                        new Faculty(user);
                    }
                    break;
                case 3:
                    if ((user = LoginServices.login(AccessLevel.ADMIN)) != null) {
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
