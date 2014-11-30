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
import java.util.Scanner;

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
        String term2= null;
        String courseRelease = null;
        String sabbatical = null;
        String profDevelopment;
        String courseNumber = null;
        boolean isSummer = false;
        Course listing;
        int choice = 0;
        int[] priority = new int[3];
        String[] time_of_day = new String[3];
        int ranking = 0;
        BufferedReader br;
        ArrayList<Course> courseList = null;

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
                    term = "fall";
                    term2 = "Fall";
                    if (year % 2 == 0) {
                        isEven = 1;
                    }else {
                        isEven = 0;
                    }
                    break;
                case 2:
                    term = "spring";
                    term2 ="Spring";
                    isEven = 0;
                    year = nextYear;
                    break;
                case 3:
                    term = "summer";
                    term2 = "Summer";
                    if (nextYear % 2 == 0) {
                        isEven = 1;
                    }else {
                        isEven = 0;
                    }
                    year = nextYear;
                    isSummer = true;
                    break;
                case 4:
                    System.out.println("Exiting Preference Form");
                    System.exit(1);
                    break;
                default:
                    term = "Invalid Selection";
                    break;
            }
            //See if Summer was selected to disable certain menus
            if(isSummer){
                //Start of Summer menu selection
                // Scheduling Factors Importance Rank Order(1-3)
                System.out.println("Rank Your Scheduling Factors Importance by rank order 1-3 with 1 being the highest.");
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
                        choice = Integer.parseInt(br.readLine());
                        priority[count] = Integer.parseInt(br.readLine());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    count++;

                }

                // Select number of courses to teach
                System.out.println("Enter the number of courses to teach for " + term2 + " semester.\n");
                br = new BufferedReader(new InputStreamReader(System.in));

                try {
                    numberOfCourses = Integer.parseInt(br.readLine());

                } catch (IOException e) {
                    System.out.println("IO error trying to read selection: " + e);
                    System.exit(1);
                }
                // Loop to iterate through the courses
                for(int i = 0; i < numberOfCourses; i++){

                    // Course Rankings choose top 5

                    // query to populate Course list
                    Connection conn = ConnectDB.getConn();
                    String query = "";
                    if (term.equals("fall") && isEven == 1){
                        query = "SELECT course_number, name from COURSE WHERE term = '" + term + "' ORDER BY course_number";
                    }else{
                        query = "SELECT course_number, name from COURSE WHERE term = '" + term + "' AND iseven = " + isEven + " ORDER BY course_number";
                    }

                    PreparedStatement stmt = null;
                    ResultSet rs = null;
                    try {
                        stmt = conn.prepareStatement(query);
                        rs = stmt.executeQuery();
                        courseList = new ArrayList<Course>();

                        while(rs.next()){
                            listing = new Course(rs.getString("course_number"), rs.getString("name"));

                            courseList.add(listing);
                        }
                        rs.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    int index = 1;
                    System.out.printf("Select a course from the list.\n");
                    for (int j = 0; j < courseList.size(); j++){

                        System.out.printf("%d) %s\n", j, courseList.get(j).toString());
                    }
                    br = new BufferedReader(new InputStreamReader(System.in));

                    try {

                        courseNumber = courseList.get(Integer.parseInt(br.readLine())).course_number;

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.printf("Rank the course from 1-%d\n", numberOfCourses);
                    br = new BufferedReader(new InputStreamReader(System.in));

                    try {

                        ranking = Integer.parseInt(br.readLine());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //Insert into the course _ form ID, Year, Term, Course_number, ranking
                    query = "INSERT into form_course VALUES (" + i + ", '" + year + "', '" + term + "', '" + courseNumber + "', " + ranking + ")";
                    try {
                        stmt = conn.prepareStatement(query);
                        stmt.executeUpdate();
                        stmt.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                }

            }else{
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

                // Select number of courses to teach
                System.out.println("Enter the number of courses to teach for " + term2 + " semester.\n");
                br = new BufferedReader(new InputStreamReader(System.in));

                try {
                    numberOfCourses = Integer.parseInt(br.readLine());

                } catch (IOException e) {
                    System.out.println("IO error trying to read selection: " + e);
                    System.exit(1);
                }
                // Loop to iterate through the courses
                for(int i = 0; i < numberOfCourses; i++){

                    // Course Rankings choose top 5

                    // query to populate Course list
                    Connection conn = ConnectDB.getConn();
                    String query = "";
                    if (term.equals("fall") && isEven == 1){
                        query = "SELECT course_number, name from COURSE WHERE term = '" + term + "' ORDER BY course_number";
                    }else{
                        query = "SELECT course_number, name from COURSE WHERE term = '" + term + "' AND iseven = " + isEven + " ORDER BY course_number";
                    }

                    PreparedStatement stmt = null;
                    ResultSet rs = null;
                    try {
                        stmt = conn.prepareStatement(query);
                        rs = stmt.executeQuery();
                        courseList = new ArrayList<Course>();

                        while(rs.next()){
                            listing = new Course(rs.getString("course_number"), rs.getString("name"));

                            courseList.add(listing);
                        }
                        rs.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    int index = 1;
                    System.out.printf("Select a course from the list.\n");
                    for (int j = 0; j < courseList.size(); j++){

                        System.out.printf("%d) %s\n", j, courseList.get(j).toString());
                    }
                    br = new BufferedReader(new InputStreamReader(System.in));

                    try {

                        courseNumber = courseList.get(Integer.parseInt(br.readLine())).course_number;

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.printf("Rank the course from 1-%d\n", numberOfCourses);
                    br = new BufferedReader(new InputStreamReader(System.in));

                    try {

                        ranking = Integer.parseInt(br.readLine());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //Insert into the course _ form ID, Year, Term, Course_number, ranking
                    query = "INSERT into form_course VALUES (" + i + ", '" + year + "', '" + term + "', '" + courseNumber + "', " + ranking + ")";
                    try {
                        stmt = conn.prepareStatement(query);
                        stmt.executeUpdate();
                        stmt.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                }

                //Time of Day Preference
                System.out.println("Rank Your Time of Day Preference by rank order 1-3 with 1 being the highest.");
                count = 0;
                prefTitle = null;

                while(count != 3){
                    String timeOf = null;
                    switch (count){
                        case 0:
                            prefTitle = "Morning (9 am - Noon)";
                            timeOf = "Morning";
                            break;
                        case 1:
                            prefTitle = "Afternoon (Noon - 4:15 pm";
                            timeOf = "Afternoon";
                            break;
                        case 2:
                            prefTitle = "Evening (4:30 pm - 9:10pm";
                            timeOf = "Evening";
                    }
                    System.out.println(prefTitle);
                    br = new BufferedReader(new InputStreamReader(System.in));
                    choice = 0;

                    try {
                        choice = Integer.parseInt(br.readLine());
                        time_of_day[choice] = timeOf;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    count++;

                }

                //Fall and Spring Days of Week Preference Rank Order
                //Time of Day Preference
                System.out.println("Rank Your Days of the Week Preference by rank order 1-3 with 1 being the highest.");
                count = 0;
                prefTitle = null;

                while(count != 3){
                    String week = null;
                    switch (count){
                        case 0:
                            prefTitle = "MWF(3 credits, 7 am - 3 pm)";
                            week = "MWF";
                            break;
                        case 1:
                            prefTitle = "Monday and Wednesdays";
                            week = "MW";
                            break;
                        case 2:
                            prefTitle = "Tuesdays and Thursday";
                            week = "TR";
                    }
                    System.out.println(prefTitle);
                    br = new BufferedReader(new InputStreamReader(System.in));
                    choice = 0;

                    try {
                        choice = Integer.parseInt(br.readLine());
                        time_of_day[choice] = week;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    count++;

                }

                // Insert into the faculty_form table

            }



            System.out.println("Enter the course number and its");

            //Times of Day preference ranks (1-3)

            //Make if statement to check if summer was selected to show Summer Term

            // Fall and Spring Days of Week preference Rank (1-3)



            //Insert Statement will go here
            System.out.println("You selected " + term + " semester with " + numberOfCourses +" courses.\n");

        }

    }



}
