
package DM;

import java.util.Scanner;

public class AdminMenu {
    
    public AdminMenu(){
        Scanner r=new Scanner(System.in);
        int uin =0;
        display: while (true) {
            System.out.println("Admin Menu\n"
                    + "1) Create Faculty Accoun\n"
                    + "2) Create Admin Account\n"
                    + "3) View Reports\n"
                    + "4) Back");
            uin = r.nextInt();
            switch (uin) {
                case 1:
                    break;
                case 2:
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
}
