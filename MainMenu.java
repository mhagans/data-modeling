
package DM;

import java.util.Scanner;

public class MainMenu {
    public MainMenu(){
        Scanner r=new Scanner(System.in);
        String uin="";
        while (!uin.equals("4")) {
            System.out.println("Main Menu\n"
                    + "1) Student Menu\n"
                    + "2) Faculty Menu\n"
                    + "3) Admin Menu\n"
                    + "4) Exit");
            uin = r.next();
            switch (uin) {
                case "1":
                    break;
                case "2":
                    break;
                case "3":
                    new AdminMenu();
                    break;
                case "4":
                    System.exit(1);
                    break;
                default:
                    System.out.println("Bad input: please enter a number between 1-4");
            }
        }
    }
}
