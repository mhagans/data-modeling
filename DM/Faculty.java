package DM;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class Faculty {
    //Menu Start
    public Faculty(String id) {
        int selection = 0;
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int nextYear = year+1;
        String term = null;
        int isEven = 0;
        String courseNumber = null;
        String[] time_of_day = new String[3];
        BufferedReader br;

        while(true){
            // Select Term
            System.out.println("Please select a term then press Enter.\n" +
                                "1. Fall " + year + "\n" +
                                "2. Spring " + nextYear + "\n" +
                                "3. Summer " + nextYear + "\n" +
                                "4. Quit\n");
            br = new BufferedReader(new InputStreamReader(System.in));
            
              try {
                  String str = br.readLine();
                  if(str.matches("[0-9]")) {                  // Checks if input is number, if so preceed
                    selection = Integer.parseInt(str);
                    if(selection > 4) {                       // Makes sure input value corresponds to a menu option
                      System.out.println("Not valid selection. Expected a number (1-4).");
                      System.out.println("System now exiting");
                      System.exit(1);
                    }
                  }
                  else
                    throw new IOException("\n"+str+" is not a number");
                } catch (IOException e) {
                    System.out.println(e);
                    System.exit(1);
                }

            switch (selection) {
                case 1:
                    term = "fall";
                    if (year % 2 == 0) {
                        isEven = 1;
                    }else {
                        isEven = 0;
                    }
                    break;
                case 2:
                    term = "spring";
                    isEven = 0;
                    year = nextYear;
                    break;
                case 3:
                    term = "summer";
                    if (nextYear % 2 == 0) {
                        isEven = 1;
                    }else {
                        isEven = 0;
                    }
                    year = nextYear;
                    break;
                case 4:
                    System.out.println("Exiting Preference Form");
                    System.exit(1);
                    break;
                default:
                    System.out.println("Invalid Selection");
                    break;
            }
            facPref(term,id);
        }
    }
    
    public void facPref(String term, String id) {
      BufferedReader br;
      int courseRelease = 0;
      int sabbatical = 0;
      int profDevelopment = 0;
      int[] priority = new int[3];
      int numberOfCourses = 0;
      List<Course> courseList = new ArrayList<>();
      String courseNumber = null;
      Course listing;
      int ranking = 0;
      int year = Calendar.getInstance().get(Calendar.YEAR);
      int isEven = 0;
      String[] time_of_day = new String[3];
      String[] days_of_the_week = new String[3];
      int choice = 0;
      
      if(term.equals("fall") || term.equals("spring")) {
       System.out.println("Enter Yes or No if Course Release is Expected.\n");
       br = new BufferedReader(new InputStreamReader(System.in));

      try {
        if(br.readLine().equals("yes"))
          courseRelease = 1;
        else
          courseRelease = 0;
      } catch (IOException e) {
        System.out.println("IO error trying to read course release: " + e);
      }
      // Sabbatical
      System.out.println("Enter Yes or No if Sabbatical is Expected\n");
      br = new BufferedReader(new InputStreamReader(System.in));

      try {
      if(br.readLine().equals("yes"))
        courseRelease = 1;
      else
        courseRelease = 0;
      } catch (IOException e) {
        System.out.println("IO error trying to read sebbatical: " + e);
      }

      //Professional Development Leave
      System.out.println("Enter Yes or No if Professional Development is Expected\n");
        br = new BufferedReader(new InputStreamReader(System.in));

       try {
       if(br.readLine().equals("yes"))
         courseRelease = 1;
       else
         courseRelease = 0;
       } catch (IOException e) {
         System.out.println("IO error trying to read profDevelopment " + e);
       }
      }

      // Scheduling Factors Importance Rank Order(1-3)
      System.out.println("Rank Your Scheduling Factors");
      int count = 0;
      String prefTitle = null;
      while(count != 3){
      switch (count){
        case 0:
          prefTitle = "Course Preference (1-3 with 1 being the highest)";
          break;
        case 1:
          prefTitle = "Days of the Week (1-5)";
          break;
        case 2:
          prefTitle = "Times of the Day (1-3 morning,afternoon,evening)";
      }
      System.out.println(prefTitle);
      br = new BufferedReader(new InputStreamReader(System.in));

      try {
        priority[count] = Integer.parseInt(br.readLine());
      } catch (IOException e) {
        System.out.println(e);
        System.exit(1);
      }
      count++;
      }
       // Select number of courses to teach
      System.out.println("Enter the number of courses to teach for " + term + " semester.\n");
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
         String query = "SELECT course_number, name from COURSE WHERE term = '" + term + "' AND iseven = " + isEven + " ORDER BY course_number";

         Statement stmt = null;
         try {
           stmt = conn.createStatement();
           ResultSet rs = stmt.executeQuery(query);
           courseList = new ArrayList<>();
           while(rs.next()){
             listing = new Course(rs.getString("course_number"), rs.getString("name"));
             courseList.add(listing);
           }
           rs.close();
           } catch (SQLException e) {
             System.out.println(e);
             System.exit(1);
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
             System.out.println(e);
             System.exit(1);
           }
           System.out.printf("Rank the courses from 1-%d\n", numberOfCourses);
           br = new BufferedReader(new InputStreamReader(System.in));

           try {
             ranking = Integer.parseInt(br.readLine());
           } catch (IOException e) {
             System.out.println(e);
           }
          //Insert into the course_form ID, Year, Term, Course_number, ranking
          query = "INSERT into form_course VALUES (" + id + ", '" + year + "', '" + term + "', '" + courseNumber + "', " + ranking + ")";
            try {
              stmt = conn.createStatement();
              stmt.executeUpdate(query);
              stmt.close();
              conn.close();
            } catch (SQLException e) {
              e.printStackTrace();
             }
     }
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
           System.out.println(e);
         }
         count++;
         }
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
               System.out.println(e);
           }
           count++;
           }
         
           Connection conn = ConnectDB.getConn();
           String query = "";
           // INSERT INTO FACULTY_FORM PARAMETERS (INT, STRING,STRING, INT, INT, INT, INT, S, S, S, S, S, S, INT, INT, INT)
           query = String.format("INSERT into faculty_form VALUES(%d, '%s', '%s', %d, %d, %d, %d, '%s', '%s', '%s', '%s', '%s', '%s', %d, %d, %d)",
                   year, term, id, numberOfCourses, priority[0], priority[1], priority[2],days_of_the_week[0], days_of_the_week[1], days_of_the_week[2],
                   time_of_day[0],time_of_day[1],time_of_day[2],courseRelease, sabbatical, profDevelopment);
           Statement stmt = null;
           try {
             stmt = conn.createStatement();
             stmt.executeQuery(query);
             stmt.close();
             conn.close();
            } catch (SQLException e) {
              System.out.println(e);
            }
       }
}