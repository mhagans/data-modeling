
package DM;

import java.util.Scanner;

public class MainMenu {
    public MainMenu(){
        Scanner r=new Scanner(System.in);
        int uin=0;
        while (uin != 4) {
            System.out.println("Main Menu\n"
                    + "1) Student Menu\n"
                    + "2) Faculty Menu\n"
                    + "3) Admin Menu\n"
                    + "4) Exit");
            uin = r.nextInt();
            switch (uin) {
                case 1:
                    break;
                case 2:
                    new Faculty();
                    break;
                case 3:
                    new AdminMenu();
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
