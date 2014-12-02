
package DM;

import java.util.Scanner;
import DM.LoginServices.AccessLevel;

public class AdminMenu
{
    /*
    * Create new AdminMenu, call the run method;
    * AdminMenu adminMenu = new AdminMenu();
    * adminMenu.run();
    */

    private Scanner in;

    private enum Action {
        CREATE_USER, VIEW_REPORTS, EXIT
    }

    public void run()
    {
        in = new Scanner(System.in);

        int input = 0;

        display: while (input != 4)
        {
            System.out.println("Admin Menu\n"
                    + "1) Create Faculty Accoun\n"
                    + "2) Create Admin Account\n"
                    + "3) View Reports\n"
                    + "4) Back");
            input = in.nextInt();
            switch (input)
            {
                case 1:
                  //  createUser(AccessLevel.FACULTY);
                    break;
                case 2:
                    //createUser(AccessLevel.ADMIN);
                    break;
                case 3:
                    new Report();
                    break;
                case 4:
                    break display;
                default:
                    System.out.println("Bad input: please enter a number between 1-4");
            }
        }
    }

    private void createUser(AccessLevel accessLevel)
    {
        System.out.println("Creating new " + accessLevel.name().toUpperCase() + " level user");
        System.out.println("---------------------------------------------");
        
        System.out.print("Enter the users username: ");

        String username = in.next();

        System.out.print("Enter the users password: ");

        String password = in.next();

        System.out.print("Enter the users full name: ");

        String name = in.next();

        System.out.print("Enter the users area of expertise: ");
        
        String areaOfExpertise = in.next();

        //LoginServices.createUser(username, password, name, areaOfExpertise, accessLevel);
    }
}
