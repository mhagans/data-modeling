package DM;

import java.beans.Statement;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Marcus on 11/17/2014.
 */
public class Faculty {


    //Menu Start
    public Faculty(){
        // Variables
        int selection = 0;
        int numberOfCourses = 0;
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int nextYear = year+1;
        int isEven = 0;
        String term = null;
        String courseRelease = null;
        String sabbatical = null;
        String profDevelopment;
        String listing = "";
        int[] priority = new int[3];
        BufferedReader br;
        ArrayList<String> courseList = null;

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
                    if (year % 2 == 0) {
                        isEven = 0;
                    }else {
                        isEven = 1;
                    }
                    break;
                case 2:
                    term = "Spring";
                    if (nextYear % 2 == 0) {
                        isEven = 0;
                    }else {
                        isEven = 1;
                    }
                    break;
                case 3:
                    term = "Summer";
                    if (nextYear % 2 == 0) {
                        isEven = 0;
                    }else {
                        isEven = 1;
                    }
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
            // Loop to iterate through the courses
            for(int i = 0; i < numberOfCourses; i++){
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
                    count++;

                }

                // Course Rankings choose top 5

                // query to populate Course list
                Connection conn = ConnectDB.getConn();


                String query = "SELECT course_number, name from COURSE WHERE term = ? AND iseven = ? ORDER BY course_number";
                PreparedStatement stmt = null;
                ResultSet rs = null;
                try {
                    stmt = conn.prepareStatement(query);
                    stmt.setString(1, term); // set the term
                    stmt.setInt(2, isEven); // set if year is even

                    rs = stmt.executeQuery();
                    courseList = new ArrayList<String>();

                    while(rs.next()){
                        listing = rs.getString("course_number") + " " + rs.getString("name");
                        courseList.add(listing);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                int index = 1;
                System.out.printf("Select a course from the list and rank it from 1-%d.\n", numberOfCourses);
                for (int j = 0; j < courseList.size(); j++){

                    System.out.printf("%d) %s", j + 1, courseList.get(j));
                }

                System.out.println("Enter the course number and its");

                //Times of Day preference ranks (1-3)

                //Make if statement to check if summer was selected to show Summer Term

                // Fall and Spring Days of Week preference Rank (1-3)
            }



            //Insert Statement will go here
            System.out.println("You selected " + term + " semester with " + numberOfCourses +" courses.\n");

        }

    }



}
