
package DM;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;


public class Report {
    //gets the courses ordered by term offered
    private String CorQ="select * from Course";
    //gets Student's courses ordered 
    //make sure to add coursenumber and term resrictions
    //private String StudCQ="select * from Form_Course where rank=0 and course_number='";
    private String StudCQ="select * from Form_Course where rank=0 and course_number= ? "
            + "and term= ? order by year";
    //private String FacCQ="select * from Form_Course where rank>0 and course_number='";
    //gets id
    private String FacCQ="select * from Form_Course where rank>0 and course_number= ? "
            + "and term= ? order by year";
    //private String IdQ="select * from Id where id='";//make sure to add id and a "'"
    private String IdQ="select * from Id where id = ?";
    //gets preffered times
    //make sure to ad id and a "'" and year and term restrictions
    //private String STDQ="select day_1, day_2, day_3, time_1,"
                            //+ " time_2, time_3 from Student_Form where id='";
    private String STDQ="select day, time,from Student_Form where id=?"
            + "and year = ? and term = ?";
    //private String FTDQ="select day_1, day_2, day_3, time_1,"
                            //+ " time_2, time_3, rank from Faculty_Form where id='";
    private String FTDQ="select day_1, day_2, day_3, time_1,"
                            + " time_2, time_3 from Faculty_Form where id=?"
            + "and year = ? and term = ?";
    
    
    public Report(){
        
        Scanner r=new Scanner(System.in);
        String uin="";
        display: while (true) {
            System.out.println("Reports\n"
                    + "1) Course Listing\n"
                    + "2) Day Listing\n"
                    + "3) Time Listing\n"
                    + "4) Student Listing\n"
                    + "5) Faculty Listing\n"
                    + "6) Back");
            uin = r.next();
            switch (uin) {
                case "1":
                    CL();
                    break;
                case "2":
                    DL();
                    break;
                case "3":
                    //TL();
                    break;
                case "4":
                    //SL();
                    break;
                case "5":
                    //FL();
                    break;
                case "6":
                    break display;
                default:
                    System.out.println("Bad input: please enter a number between 1-6");
            }
        }
    }
    public void CL(){
        String cn,term,id,degree,name,d1,d2,d3,t1,t2,t3;
        int year,rank;
        Connection conn=ConnectDB.getConn();
        PreparedStatement ct=null,fct=null,it=null,st=null;
        
        try{
            /*ct = conn.createStatement();
            fct = conn.createStatement();
            it = conn.createStatement();
            st = conn.createStatement();*/
            ResultSet cs, fcs, is, ss;
            ct=conn.prepareStatement(CorQ);
            cs = ct.executeQuery();

            while (cs.next()) {
                cn = cs.getString("course_number");
                term = cs.getString("term");
                System.out.println(cn + " " + term + "\nStudents: ");
                //display student info and times
                ct=conn.prepareStatement(StudCQ);
                ct.setString(1, cn);
                ct.setString(2, term);
                fcs = ct.executeQuery();
                while (fcs.next()) {
                    id = fcs.getString("id");
                    year = fcs.getInt("year");
                    //get student info
                    it=conn.prepareStatement(IdQ);
                    it.setString(1, id);
                    is = it.executeQuery();
                    if (is.next()) {
                        name = is.getString("name");
                        degree = is.getString("degree");
                        //get student day and times (year is an int so make sure no '')
                        st=conn.prepareStatement(STDQ);
                        st.setString(1, id);
                        st.setInt(2,year);
                        st.setString(3,term);
                        ss = st.executeQuery();
                        if (ss.next()) {
                            d1 = ss.getString("day");
                            t1 = ss.getString("time");

                            System.out.println(year + " " + id + " " + name + " " + degree + " \nPreffered  ordered by preference "
                                    + "day: " + d1 +", time: " + t1 +"\n");
                        }
                        ss.close();
                        st.close();
                    }
                    is.close();
                    is.close();

                }
                fcs.close();
                fct.close();
                //get faculty info
                System.out.println("Faculty: \n");
                fct=conn.prepareStatement(FacCQ);
                fct.setString(1,cn);
                fct.setString(2, term);
                fcs = ct.executeQuery();
                while (fcs.next()) {
                    id = fcs.getString("id");
                    year = fcs.getInt("year");
                    rank = fcs.getInt("rank");
                    //get faculty info
                    it=conn.prepareStatement(IdQ);
                    it.setString(1, id);
                    is = it.executeQuery();
                    if (is.next()) {
                        name = is.getString("name");
                        degree = is.getString("degree");
                        //get faculty day and times, and rank (year is an int so make sure no '')
                        st=conn.prepareStatement(FTDQ);
                        st.setString(1, id);
                        st.setInt(2,year);
                        st.setString(3,term);
                        ss = st.executeQuery();
                        if (ss.next()) {
                            d1 = ss.getString("day_1");
                            d2 = ss.getString("day_2");
                            d3 = ss.getString("day_3");
                            t1 = ss.getString("time_1");
                            t2 = ss.getString("time_2");
                            t3 = ss.getString("time_3");
                            if (d2.equals("NULL")) {
                                d2 = "";
                            }

                            if (d3.equals("NULL")) {
                                d3 = "";
                            }

                            System.out.println(year + " " + id + " " + name + " " + degree + "\n rank= " + rank + " Preffered  "
                                    + "ordered by preference " + "days: " + d1 + ", " + d2 + ", " + d3 + ", times: "
                                    + t1 + ", " + t2 + ", " + "t3\n");
                        }
                        ss.close();
                        st.close();
                        
                    }
                    is.close();
                    it.close();

                }
                fcs.close();
                fct.close();

            }
            cs.close();
            ct.close();
            conn.close();

        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    /*
    Gets courses as above but only displays the course and student and/or faculty if those faculty and
    * students have the preffered day choosen.
    */
    //might need to figure out days and times problems (faculty adn students for everything but summer
    //have to choose the same days and times possiblilityes ( might have to restrict to only Pref1)
    //might just need to signal more clearly prefernce times for student and faculty days and times
    public void DL() {//going to have a problem if summer terms and D2 and D3 will be null and will always be equal between faculty and student
        Connection conn = ConnectDB.getConn();
        String cn, term, id, degree, name, d1, d2, d3, t1, t2, t3;
        int sig = 0, sig2 = 0, rank, year;

        PreparedStatement dt = null, ct = null, fct = null, it = null, st = null;
        //hard code going through list of days
        String[] days = {"MWF", "MW", "TR", "MTWR", "MW+4", "TR+4"};
        try {
            ResultSet cs, fcs, is, ss;
            System.out.println("Days: \n");
            for (int i = 0; i < 6; i++) {
                System.out.println(days+"\n");
                ct=conn.prepareStatement(CorQ);
                cs = ct.executeQuery();
                while (cs.next()) {
                    cn = cs.getString("course_number");
                    term = cs.getString("term");
                    fct=conn.prepareStatement(StudCQ);
                    fct.setString(1, cn);
                    fct.setString(2, term);
                    fcs = fct.executeQuery();
                    while (fcs.next()) {
                        id = fcs.getString("name");
                        year = fcs.getInt("year");
                        
                        it=conn.prepareStatement(IdQ);
                        it.setString(1, id);
                        is = it.executeQuery();
                        if (is.next()) {//should always occur
                            name = is.getString("name");
                            degree = is.getString("degree");
                            
                            st=conn.prepareStatement(STDQ);
                            st.setString(1, id);
                            st.setInt(2, year);
                            st.setString(3, term);
                            ss = st.executeQuery();
                            if (ss.next()) {
                                d1 = ss.getString(
                                        "day");
                                t1 = ss.getString(
                                        "time");

                                if (d1.equals(days[i])) {
                                    if (sig == 0) {
                                        sig = 1;
                                        System.out.println("Course: " + cn + " term: " + term);
                                    }
                                    if (sig2 == 0) {
                                        sig2 = 1;
                                        System.out.println("Student: \n");
                                    }
                                    System.out.println(year + " " + id + " " + name + " " + degree + " preffered time: " + t1+"\n");
                                }
                                
                            }
                            ss.close();
                            st.close();
                        }
                        is.close();
                        it.close();
                    }
                    fcs.close();
                    fct.close();
                    sig2 = 0;

                    fct=conn.prepareStatement(FacCQ);
                    fct.setString(1, cn);
                    fct.setString(2, term);
                    fcs = fct.executeQuery();
                    while (fcs.next()) {
                        id = fcs.getString("name");
                        year = fcs.getInt("year");
                        rank = fcs.getInt("rank");

                        it=conn.prepareStatement(IdQ);
                        it.setString(1, id);
                        is = it.executeQuery();
                        if (is.next()) {//should always occur
                            name = is.getString("name");
                            degree = is.getString("degree");


                            st=conn.prepareStatement(FTDQ);
                            st.setString(1, id);
                            st.setInt(2, year);
                            st.setString(3, term);
                            ss = st.executeQuery();
                            if (ss.next()) {
                                d1 = ss.getString(
                                        "day_1");
                                d2 = ss.getString(
                                        "day_2");
                                d3 = ss.getString(
                                        "day_3");
                                t1 = ss.getString(
                                        "time_1");
                                t2 = ss.getString(
                                        "time_2");
                                t3 = ss.getString(
                                        "time_3");
                                

                                if (d2.equals("NULL")) {
                                    d2 = "";
                                }

                                if (d3.equals("NULL")) {
                                    d3 = "";
                                }

                                if (d1.equals(days[i]) || d2.equals(days[i]) || d3.equals(days[i])) {
                                    if (sig == 0) {
                                        sig = 1;
                                        System.out.println("Course: " + cn + " term: " + term);
                                    }
                                    if (sig2 == 0) {
                                        sig2 = 1;
                                        System.out.println("Faculty: \n");
                                    }
                                    System.out.println(year + " " + id + " " + name + " " + degree + "rank: " + rank + " preffered times: " + t1 + ", " + t2 + ", " + t3+"\n");
                                }

                            }
                            ss.close();
                            st.close();
                        }
                        is.close();
                        it.close();
                    }
                    fcs.close();
                    fct.close();
                    sig2 = 0;
                    sig = 0;
                }
                cs.close();
                ct.close();
            }
            conn.close();
        } catch (SQLException e) {
            System.out.println(e);
        }

        //select courses and stududent adn faculty as above
        //only display faculty and/or student info if they have the day as one of their pref
    }

    //same as above but 
    public void TL() {
        Connection conn = ConnectDB.getConn();
        String cn, term, id, degree, name, d1, d2, d3, t1, t2, t3;
        int sig = 0, sig2 = 0, rank, year;

        PreparedStatement dt = null, ct = null, fct = null, it = null, st = null;
        //hard code going through list of days
        String[] times = {"Morning", "Afternoon", "Evening"};
        try {
            
            ResultSet cs, fcs, is, ss;
            System.out.println("Times: \n");
            for (int i = 0; i < 3; i++) {
                System.out.println(times + "\n");
                ct=conn.prepareStatement(CorQ);
                cs = ct.executeQuery();
                while (cs.next()) {
                    cn = cs.getString("course_number");
                    term = cs.getString("term");

                    fct=conn.prepareStatement(StudCQ);
                    fct.setString(1, cn);
                    fct.setString(2, term);
                    fcs = fct.executeQuery();
                    while (fcs.next()) {
                        id = fcs.getString("name");
                        year = fcs.getInt("year");

                        it=conn.prepareStatement(IdQ);
                        it.setString(1, id);
                        is = it.executeQuery();
                        if (is.next()) {//should always occur
                            name = is.getString("name");
                            degree = is.getString("degree");

                            st=conn.prepareStatement(STDQ);
                            st.setString(1, id);
                            st.setInt(2, year);
                            st.setString(3, term);
                            ss = st.executeQuery();
                            if (ss.next()) {
                                d1 = ss.getString(
                                        "day");
                                t1 = ss.getString(
                                        "time");

                                if (d1.equals(times[i])) {
                                    if (sig == 0) {
                                        sig = 1;
                                        System.out.println("Course: " + cn + " term: " + term);
                                    }
                                    if (sig2 == 0) {
                                        sig2 = 1;
                                        System.out.println("Student: \n");
                                    }
                                    System.out.println(year + " " + id + " " + name + " " + degree + " preffered day: " + d1+"\n");
                                }

                            }
                            ss.close();
                            st.close();
                        }
                        is.close();
                        it.close();
                    }
                    fcs.close();
                    fct.close();
                    sig2 = 0;

                    fct=conn.prepareStatement(FacCQ);
                    fct.setString(1, cn);
                    fct.setString(2, term);
                    fcs = fct.executeQuery();
                    while (fcs.next()) {
                        id = fcs.getString("name");
                        year = fcs.getInt("year");
                        rank = fcs.getInt("rank");

                        it=conn.prepareStatement(IdQ);
                        it.setString(1, id);
                        is = it.executeQuery();
                        if (is.next()) {//should always occur
                            name = is.getString("name");
                            degree = is.getString("degree");


                            st=conn.prepareStatement(FTDQ);
                            st.setString(1, id);
                            st.setInt(2, year);
                            st.setString(3, term);
                            ss = st.executeQuery();
                            if (ss.next()) {
                                d1 = ss.getString(
                                        "day_1");
                                d2 = ss.getString(
                                        "day_2");
                                d3 = ss.getString(
                                        "day_3");
                                t1 = ss.getString(
                                        "time_1");
                                t2 = ss.getString(
                                        "time_2");
                                t3 = ss.getString(
                                        "time_3");
                                

                                if (d2.equals("NULL")) {
                                    d2 = "";
                                }

                                if (d3.equals("NULL")) {
                                    d3 = "";
                                }

                                if (d1.equals(times[i]) || d2.equals(times[i]) || d3.equals(times[i])) {
                                    if (sig == 0) {
                                        sig = 1;
                                        System.out.println("Course: " + cn + " term: " + term);
                                    }
                                    if (sig2 == 0) {
                                        sig2 = 1;
                                        System.out.println("Faculty: \n");
                                    }
                                    System.out.println(year + " " + id + " " + name + " " + degree + "rank: " + rank + " preffered times: " + t1 + ", " + t2 + ", " + t3);
                                }

                            }
                            ss.close();
                            st.close();
                        }
                        is.close();
                        it.close();
                    }
                    fcs.close();
                    fct.close();
                    sig2 = 0;
                    sig = 0;
                }
                cs.close();
                ct.close();
            }
            conn.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
}
