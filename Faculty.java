import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Marcus on 11/17/2014.
 */
public class Faculty {


    //Menu Start
    public static void main(String[] args){
        // Variables
        int selection = 0;
        String term = null;
        BufferedReader br;
        ArrayList<String> courseList;

        while(selection != 4){
            System.out.println("Please select a term then press Enter.\n" +
                                "1. Fall\n" +
                                "2. Spring\n" +
                                "3. Summer\n" +
                                "4. Quit\n");
            br = new BufferedReader(new InputStreamReader(System.in));

            try {
                selection = Integer.parseInt(br.readLine());
            }catch (IOException e){
                System.out.println("IO error trying to read selection!");
                System.exit(1);
            }


            switch (selection) {
                case 1:
                    term = "Fall";
                    break;
                case 2:
                    term = "Spring";
                    break;
                case 3:
                    term = "Summer";
                    break;
                case 4:
                    System.exit(1);
                    break;
                default:
                    term = "Invalid Selection";
                    break;
            }
            System.out.println("You selected " + term + ".\n");
        }

    }



}
