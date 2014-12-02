package DM;

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
    public Faculty(String user){
        // Variables
        int selection = 0;
        int numberOfCourses = 0;
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int nextYear = year+1;
        int isEven = 0;
        String term = null;
        String term2= null;
        int courseRelease = 0;
        int sabbatical = 0;
        int profDevelopment = 0;
        String courseNumber = null;
        boolean isSummer = false;
        Connection conn;
        Course listing;
        int choice = 0;
        int[] priority = new int[3];
        String[] time_of_day = new String[3];
        String[] days_of_the_week = new String[3];
        int ranking = 0;
        String summer_choice = null;
        String id = user;
        BufferedReader br;
        ArrayList<Course> courseList = null;

        display: while (true){

            //region Select Term
            System.out.println("Please select a term then press Enter.\n" +
                                "1. Fall " + year + "\n" +
                                "2. Spring " + nextYear + "\n" +
                                "3. Summer " + nextYear + "\n" +
                                "4. Back\n");
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
                    break display;
                default:
                    term = "Invalid Selection";
                    break;
            }
            //endregion
            //Summer was selected to disable certain menus
            if(isSummer){
                //Start of Summer menu selection
                //Summer Term Preference
                boolean valid = false;
                do {
                    System.out.println("Select Your Summer Term Preference\n" +
                                        "1) Summer C (12 weeks): Wed. May 6 - Fri. Jul. 31\n" +
                                        "2) Summer A (6 weeks): Wed. May 6 - Tue. Jun. 16\n" +
                                        "3) Summer B (6 weeks): Mon. Jun. 22 - Fri Jul. 31\n" +
                                        "4) Summer D (8 weeks): Wed. May 6 - Tue. Jun. 30\n" +
                                        "5) Summer E (10 weeks): Wed. May 6 - Tue. Jul. 14\n");
                   try {
                       br = new BufferedReader(new InputStreamReader(System.in));
                       choice = Integer.parseInt(br.readLine());
                      switch (choice){
                          case 1:
                              term = "summer C";
                              valid = true;
                              break;
                          case 2:
                              term = "summer A";
                              valid = true;
                              break;
                          case 3:
                              term = "summer B";
                              valid = true;
                              break;
                          case 4:
                              term = "summer D";
                              valid = true;
                              break;
                          case 5:
                              term = "summer E";
                              valid = true;
                              break;
                          default:
                              System.out.println("Invalid Selection");
                              break;
                      }
                   } catch (IOException e) {
                       System.out.println("Invalid Selection");

                   }

                }while (!valid);

                //region Scheduling Factors Importance Rank Order(1-3)
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
                            break;
                    }
                    System.out.println(prefTitle);
                    br = new BufferedReader(new InputStreamReader(System.in));

                    try {
                        choice = Integer.parseInt(br.readLine());
                        priority[count] = choice;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    count++;

                }
                //endregion

                //region Summer number of Courses
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
                    conn = ConnectDB.getConn();
                    String query = "";
                    query = "SELECT course_number, name from COURSE WHERE term = '" + term + "' ORDER BY course_number";

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
                    query = "INSERT into form_course VALUES (" + id + ", '" + year + "', '" + term + "', '" + courseNumber + "', " + ranking + ")";
                    try {
                        stmt = conn.prepareStatement(query);
                        stmt.executeUpdate();
                        stmt.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                }
                //endregion

                // region Time Preference

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


                    try {
                        choice = Integer.parseInt(br.readLine());
                        time_of_day[choice-1] = timeOf;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    count++;

                }
                //endregion

                //region Summer Days of Week
                valid = false;
                choice = 0;
                do{
                    System.out.println("Select Summer Days of Week Preference\n");

                    if(term.equalsIgnoreCase("summer c")){
                        System.out.println("1) MW\n" +
                                            "2) TR\n");

                        br = new BufferedReader(new InputStreamReader(System.in));
                        try {
                            choice = Integer.parseInt(br.readLine());
                            switch (choice){
                                case 1:
                                    summer_choice = "MW";
                                    valid = true;
                                    break;
                                case 2:
                                    summer_choice = "TR";
                                    valid = true;
                                    break;
                                default:
                                    System.out.println("Invalid Selection");
                                    break;
                            }

                        } catch (IOException e) {
                            System.out.println("Invalid Selection\n");
                        }
                    }else if(term.equalsIgnoreCase("summer a") || term.equalsIgnoreCase("summer b")){
                        System.out.println("1) MTWR\n");
                        br = new BufferedReader(new InputStreamReader(System.in));
                        try {
                            choice = Integer.parseInt(br.readLine());
                            if(choice != 1){
                                System.out.println("Invalid Selection");
                            }else{
                                summer_choice = "MTWR";
                                valid = true;
                            }

                        } catch (IOException e) {
                            System.out.println("Invalid Selection\n");
                        }
                    }else if(term.equalsIgnoreCase("summer d")){
                        System.out.println("1) MWF\n" +
                                            "2) TR\n");
                        br = new BufferedReader(new InputStreamReader(System.in));
                        try {
                            choice = Integer.parseInt(br.readLine());
                            switch (choice){
                                case 1:
                                    summer_choice = "MWF";
                                    valid = true;
                                    break;
                                case 2:
                                    summer_choice = "TR";
                                    valid = true;
                                    break;
                                default:
                                    System.out.println("Invalid Selection");
                                    break;
                            }

                        } catch (IOException e) {
                            System.out.println("Invalid Selection\n");
                        }
                    }else{
                        System.out.println("1) MW+4 Fridays\n" +
                                            "2) TR+4 Fridays\n");
                        br = new BufferedReader(new InputStreamReader(System.in));
                        try {
                            choice = Integer.parseInt(br.readLine());
                            switch (choice){
                                case 1:
                                    summer_choice = "MW+4";
                                    valid = true;
                                    break;
                                case 2:
                                    summer_choice = "TR+4";
                                    valid = true;
                                    break;
                                default:
                                    System.out.println("Invalid Selection");
                                    break;
                            }

                        } catch (IOException e) {
                            System.out.println("Invalid Selection\n");
                        }
                    }


                }while(!valid);
                //endregion

                //region Insert Summer Form
                conn = ConnectDB.getConn();
                String query = "";
                // INSERT INTO FACULTY_FORM PARAMETERS (INT, STRING,STRING, INT, INT, INT, INT, S, S, S, S, S, S, I, I, I)
                query = String.format("INSERT into faculty_form VALUES(%d, '%s', '%s', %d, %d, %d, %d, '%s', '%s', " +
                                "'%s', '%s', '%s', '%s', %d, %d, %d)", year, term, id, numberOfCourses, priority[0], priority[1], priority[2],
                        summer_choice, "N/A", "N/A", time_of_day[0],time_of_day[1],time_of_day[2],
                        0, 0, 0);
                PreparedStatement stmt = null;
                try {
                    stmt = conn.prepareStatement(query);
                    stmt.executeUpdate();
                    stmt.close();
                    conn.close();

                } catch (SQLException e) {
                    e.printStackTrace();
                }

                Scanner r = new Scanner(System.in);
                System.out.println("Debug to check insert");
                String input = r.nextLine();

                //endregion


            }else{
                //region Fall Course Load
                //Course Release
                System.out.println("Enter Yes or No if Course Release is Expected.\n");
                br = new BufferedReader(new InputStreamReader(System.in));

                try {
                    String pick = br.readLine();
                    if(pick.equalsIgnoreCase("yes")){
                        courseRelease = 1;
                    }else {
                        courseRelease = 0;
                    }

                } catch (IOException e) {
                    System.out.println("IO error trying to read selection: " + e);
                }

                // Sabbatical
                System.out.println("Enter Yes or No if Sabbatical is Expected\n");
                br = new BufferedReader(new InputStreamReader(System.in));

                try {
                    String pick = br.readLine();
                    if(pick.equalsIgnoreCase("yes")){
                        sabbatical = 1;
                    }else {
                        sabbatical = 0;
                    }
                } catch (IOException e) {
                    System.out.println("IO error trying to read selection: " + e);
                }

                //Professional Development Leave
                System.out.println("Enter Yes or No if Professional Development is Expected\n");
                br = new BufferedReader(new InputStreamReader(System.in));

                try {
                    String pick = br.readLine();
                    if(pick.equalsIgnoreCase("yes")){
                        profDevelopment = 1;
                    }else {
                        profDevelopment = 0;
                    }
                } catch (IOException e) {
                    System.out.println("IO error trying to read selection: " + e);
                }
                //endregion

                //region Scheduling Factors Importance Rank Order(1-3)
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
                //endregion

                //region Fall/Spring Number of Course's Code
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



                    // query to populate Course list
                    conn = ConnectDB.getConn();
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
                    query = "INSERT into form_course VALUES ('" + id + "', '" + year + "', '" + term + "', '" + courseNumber + "', " + ranking + ")";
                    try {
                        stmt = conn.prepareStatement(query);
                        stmt.executeUpdate();
                        stmt.close();
                        conn.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                }
                //endregion

                // region Time Preference

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


                    try {
                        choice = Integer.parseInt(br.readLine());
                        time_of_day[choice-1] = timeOf;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    count++;

                }
                //endregion

                //region Fall and Spring Days of Week Preference Rank Order
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
                        days_of_the_week[choice-1] = week;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    count++;

                }
                //endregion

                //region Fall/Spring Insert into the faculty_form table
                conn = ConnectDB.getConn();
                String query = "";
                // INSERT INTO FACULTY_FORM PARAMETERS (INT, STRING,STRING, INT, INT, INT, INT, S, S, S, S, S, S, I, I, I)
                query = String.format("INSERT into faculty_form VALUES(%d, '%s', '%s', %d, %d, %d, %d, '%s', '%s', " +
                                      "'%s', '%s', '%s', '%s', %d, %d, %d)", year, term, id, numberOfCourses, priority[0], priority[1], priority[2],
                                        days_of_the_week[0], days_of_the_week[1], days_of_the_week[2], time_of_day[0],time_of_day[1],time_of_day[2],
                                        courseRelease, sabbatical, profDevelopment);
                PreparedStatement stmt = null;
                try {
                    stmt = conn.prepareStatement(query);
                    stmt.executeUpdate();
                    stmt.close();
                    conn.close();

                } catch (SQLException e) {
                    e.printStackTrace();
                }

                Scanner r = new Scanner(System.in);
                System.out.println("Debug to check insert");
                String input = r.nextLine();

                //endregion

            }

        }

    }

}
