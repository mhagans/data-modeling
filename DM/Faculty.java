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
        Connection conn = ConnectDB.getConn();
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
        int num = 0;
        String query = null;

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
                    if (nextYear % 2 == 0) {
                        isEven = 1;
                    }else {
                        isEven = 0;
                    }
                    year = nextYear;
                    isSummer = true;
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
                    } while (!valid);
                      break;
                case 4:
                  break display;
                default:
                  term = "Invalid Selection";
                  break;
              }
            //endregion
              System.out.println("Select an option \n" +
                                   "1) Create new report\n"+
                                   "2) View previous reports\n"+
                                   "3) Delete current term's form");                  
                    try {
                       br = new BufferedReader(new InputStreamReader(System.in));
                       choice = Integer.parseInt(br.readLine());
                       Statement stmt = conn.createStatement();
                    switch (choice){
                          case 1:
                            break;
                          case 2:
                            ResultSet rs = stmt.executeQuery("SELECT year,term,day_1,day_2,time_1,time_2 from faculty_form WHERE id='"+id+"'");
                            while(rs.next())
                            {
                              System.out.println("Year: "+rs.getInt(1));
                              System.out.println("Term: "+rs.getString(2));
                              System.out.println("Day 1 preference: "+rs.getString(3));
                              System.out.println("Day 2 preference: "+rs.getString(4));
                              System.out.println("Time 1 preference: "+rs.getString(5));
                              System.out.println("Time 2 preference: "+rs.getString(6)+"\n");            
                            }
                            break display;
                          case 3:
                            stmt.executeUpdate("DELETE from faculty_form WHERE id='"+id+"' AND term='"+term+"'");
                            System.out.println("This term's form has been deleted");
                            break display;
                          default:
                            System.out.println("Invalid Selection");
                            break;
                      }
                   } catch (Exception e) {
                       System.out.println("Invalid Selection");
                   }
                //region Fall Course Load
                if(!isSummer) {
                  System.out.println("Enter Yes or No if Course Release is Expected.\n");
                  br = new BufferedReader(new InputStreamReader(System.in));

                  try {
                    String pick = br.readLine();
                    if(pick.equalsIgnoreCase("yes")){
                      courseRelease = 1;
                    } else {
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
                  } else {
                      profDevelopment = 0;
                  }
                  } catch (IOException e) {
                      System.out.println("IO error trying to read selection: " + e);
                  }
               }
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
                    // query to populate Course list
                    //conn = ConnectDB.getConn();
                    query = "SELECT course_number, name from COURSE WHERE term = '" + term + "' AND iseven = " + isEven + " ORDER BY course_number";
                    Statement stmt = null;
                    ResultSet rs = null;
                    try {
                        stmt = conn.createStatement();
                        rs = stmt.executeQuery(query);
                        courseList = new ArrayList<>();

                        while(rs.next()){
                            listing = new Course(rs.getString("course_number"), rs.getString("name"));
                            courseList.add(listing);
                        }
                        rs.close();
                    } catch (SQLException e) {
                        System.out.println(e);
                    }

                    int index = 1;
                    System.out.printf("Select a course from the list.\n");
                    for (int j = 0; j < courseList.size(); j++)
                        System.out.printf("%d) %s\n", j, courseList.get(j).toString());
                    
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
                    
                    String selQuery = "SELECT count(*) FROM form_course WHERE course_number = '"+courseNumber+"' AND id='"+id+"' AND term='"+term+"' AND year="+year;

                    try {
                    stmt = conn.createStatement();
                    rs = stmt.executeQuery(selQuery);
                    while(rs.next())
                    num = rs.getInt(1);
                    }
                    catch (SQLException e) {
                      System.out.println(e);
                    }
                    
                    if(num > 0) {
                      System.out.println("Form already exists for this term");
                      System.exit(1);
                    }
                    
                    //Insert into the course _ form ID, Year, Term, Course_number, ranking
                    query = "INSERT into form_course VALUES ('" + id + "', '" + year + "', '" + term + "', '" + courseNumber + "', " + ranking + ")";
                    try {
                        stmt = conn.createStatement();
                        stmt.executeUpdate(query);
                        stmt.close();
                        //conn.close();
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
                if(isSummer) {
                Boolean valid = false;
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
                    } else if(term.equalsIgnoreCase("summer a") || term.equalsIgnoreCase("summer b")) {
                        System.out.println("1) MTWR\n");
                        br = new BufferedReader(new InputStreamReader(System.in));
                        try {
                          choice = Integer.parseInt(br.readLine());
                          if(choice != 1){
                             System.out.println("Invalid Selection");
                          } else {
                              summer_choice = "MTWR";
                              valid = true;
                          }
                        } catch (IOException e) {
                            System.out.println("Invalid Selection\n");
                        }
                    } else if(term.equalsIgnoreCase("summer d")) {
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
                    } else {
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
                //endregion

                //region Fall/Spring Insert into the faculty_form table
                //conn = ConnectDB.getConn();
                if(!isSummer) {
                // INSERT INTO FACULTY_FORM PARAMETERS (INT, STRING,STRING, INT, INT, INT, INT, S, S, S, S, S, S, I, I, I)
                query = String.format("INSERT into faculty_form VALUES(%d, '%s', '%s', %d, %d, %d, %d, '%s', '%s', " +
                                      "'%s', '%s', '%s', '%s', %d, %d, %d)", year, term, id, numberOfCourses, priority[0], priority[1], priority[2],
                                        days_of_the_week[0], days_of_the_week[1], days_of_the_week[2], time_of_day[0],time_of_day[1],time_of_day[2],
                                        courseRelease, sabbatical, profDevelopment);
                }
                if(isSummer) {
                query = String.format("INSERT into faculty_form VALUES(%d, '%s', '%s', %d, %d, %d, %d, '%s', '%s', " +
                                      "'%s', '%s', '%s', '%s', %d, %d, %d)", year, term, id, numberOfCourses, priority[0], priority[1], priority[2],
                                        summer_choice, null, null, time_of_day[0],time_of_day[1],time_of_day[2],
                                        courseRelease, sabbatical, profDevelopment);
                }
                Statement stmt = null;
                try {
                    stmt = conn.createStatement();
                    stmt.executeUpdate(query);
                    stmt.close();
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                //endregion
            }
        }
    }