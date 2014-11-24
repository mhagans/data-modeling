package DM;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Marcus on 11/17/2014.
 */
public class Faculty {


    //Menu Start
    public static void main(String[] args){
        // Variables
        int selection = 0;
        int numberOfCourses = 0;
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int nextYear = year+1;
        String term = null;
        String courseRelease = null;
        String sabbatical = null;
        String profDevelopment;
        int[] priority = new int[3];
        BufferedReader br;
        ArrayList<String> courseList;

        while(true){

            // Select Term
            System.out.println("Please select a term then press Enter.\n" +
                                "1. Fall " + year + "\n" +
                                "2. Spring " + nextYear + "\n" +
                                "3. Summer " + nextYear + "\n" +
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
                    System.out.println("Exiting Preference Form");
                    System.exit(1);
                    break;
                default:
                    term = "Invalid Selection";
                    break;
            }

            // Select number of courses to teach
            System.out.println("Enter the number of courses to teach for " + term + "semester.\n");
            br = new BufferedReader(new InputStreamReader(System.in));

            try {
                numberOfCourses = Integer.parseInt(br.readLine());
            } catch (IOException e) {
                System.out.println("IO error trying to read selection: " + e);
                System.exit(1);
            }
            //Course Release
            System.out.println("Enter Yes or No if Course Release is Expected.\n");
            br = new BufferedReader(new InputStreamReader(System.in));

            try {
                courseRelease = br.readLine();
            } catch (IOException e) {
                System.out.println("IO error trying to read selection: " + e);
            }

            // Sabbatical
            System.out.println("Enter Yes or No if Sabbatical is Expected\n");
            br = new BufferedReader(new InputStreamReader(System.in));

            try {
                sabbatical = br.readLine();
            } catch (IOException e) {
                System.out.println("IO error trying to read selection: " + e);
            }

            //Professional Development Leave
            System.out.println("Enter Yes or No if Professional Development is Expected\n");
            br = new BufferedReader(new InputStreamReader(System.in));

            try {
                profDevelopment = br.readLine();
            } catch (IOException e) {
                System.out.println("IO error trying to read selection: " + e);
            }

            // Scheduling Factors Importance Rank Order(1-3)
            System.out.println("Rank Your Scheduling Factors Importance by rank order 1-3 with 1 being the highest");
            int count = 0;
            String prefTitle = null;
            while(count != 3){
                switch (count){
                    case 0:
                        prefTitle = "Course Preference";
                        break;
                    case 1:
                       prefTitle = "Days of the Week";
                        break;
                    case 2:
                       prefTitle = "Times of the Day";
                }
                System.out.println(prefTitle);
                br = new BufferedReader(new InputStreamReader(System.in));

                try {
                    priority[count] = Integer.parseInt(br.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // Course Rankings choose top 5

            //Times of Day preference ranks (1-3)

            //Make if statement to check if summer was selected to show Summer Term

            // Fall and Spring Days of Week preference Rank (1-3)



            //Insert Statement will go here
            System.out.println("You selected " + term + " semester with " + numberOfCourses +" courses.\n");

        }

    }



}
