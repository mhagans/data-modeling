
package DM;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;


public class Report {
    //gets the courses ordered by term offered
    private String CorQ="select * from Course";
    //gets Student's courses ordered 
    private String StudCQ="select * from Form_Course where rank=0 and course_number= ? "
            + "and term= ? order by year";
    //gets id
    private String FacCQ="select * from Form_Course where rank>0 and course_number= ? "
            + "and term= ? order by year";
    
    private String IdQ="select * from id where id = ?";//might have a problem with case
    //gets preffered times
    private String STDQ="select day, time,from Student_Form where id=?"
            + "and year = ? and term = ?";
    
    private String FTDQ="select day_1, day_2, day_3, time_1,"
                            + " time_2, time_3 from Faculty_Form where id=?"
            + "and year = ? and term = ?";
    
    private String StudQ="select * from Student_Form where id=? order by year";
    private String FacQ="select * from Faculty_Form where id=? order by year";
    
    private String CourQ="select course_number from Form_Course where id= ? and year =? and "+
      "term = ?";
    private String CourFQ="select id, rank from Form_Course where course_number= ? and year = ? and "+
      "term = ? and rank>0";
    
    private String IDSQ="select * from id where Permission=0";
    private String IDFQ="select * from id where Permission=1";
    
    
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
                    TL();
                    break;
                case "4":
                    SL();
                    break;
                case "5":
                    FL();
                    break;
                case "6":
                    break display;
                default:
                    System.out.println("Bad input: please enter a number between 1-6");
            }
        }
    }
    private void CL(){
        String cn,term,id,degree,name,d1,d2,d3,t1,t2,t3;
        int year,rank;
        Connection conn=ConnectDB.getConn();
        PreparedStatement ct=null,fct=null,it=null,st=null;
        
        try{
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
                                    + "day: " + d1 +", time: " + t1);
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
                                    + t1 + ", " + t2 + ", " + t3);
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
    private void DL() {//going to have a problem if summer terms and D2 and D3 will be null and will always be equal between faculty and student
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
                                    System.out.println(year + " " + id + " " + name + " " + degree + " preffered time: " + t1);
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

        //select courses and stududent adn faculty as above
        //only display faculty and/or student info if they have the day as one of their pref
    }

    //same as above but by time rather then day
    private void TL() {
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

                                if (t1.equals(times[i])) {
                                    if (sig == 0) {
                                        sig = 1;
                                        System.out.println("Course: " + cn + " term: " + term);
                                    }
                                    if (sig2 == 0) {
                                        sig2 = 1;
                                        System.out.println("Student: \n");
                                    }
                                    System.out.println(year + " " + id + " " + name + " " + degree + " preffered day: " + d1);
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

                                if (t1.equals(times[i]) || t2.equals(times[i]) || t3.equals(times[i])) {
                                    if (sig == 0) {
                                        sig = 1;
                                        System.out.println("Course: " + cn + " term: " + term);
                                    }
                                    if (sig2 == 0) {
                                        sig2 = 1;
                                        System.out.println("Faculty: \n");
                                    }
                                    System.out.println(year + " " + id + " " + name + " " + degree + "rank: " + rank + " preffered days: " + d1 + ", " + d2 + ", " + d3);
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
            System.exit(0);
        }
    
    }
    
    private void SL(){
        //displats student info then a list of courses ordered by year and days anf times and 
        //any faculty that share those
        Connection conn = ConnectDB.getConn();
        String cn, term, id, degree, name, d1, d2, d3, t1, t2, t3, sig3="";
        String ed1, ed2, ed3, et1, et2, et3;
        int sig = 0, sig2 = 0, rank, year;

        PreparedStatement dt = null, ct = null, fct = null, it = null, st = null;
        PreparedStatement fit=null;
    
        //get student id and subsequent info
        try {
            it = conn.prepareStatement(IDSQ);
            ResultSet ds, cs, fcs, is, ss, fis;
            is = it.executeQuery();
            System.out.println("Students:");
            while(is.next()){
                id=is.getString("id");
                name=is.getString("name");
                degree=is.getString("degree");
                
                System.out.println(name+" "+id+" "+degree);
                
                //get student form for year and term and day and time
                st=conn.prepareStatement(StudQ);
                st.setString(1, id);
                ss=st.executeQuery();
                while(ss.next()){
                    year=ss.getInt("year");
                    term=ss.getString("term");
                    d1=ss.getString("day");
                    t1=ss.getString("time");
                    
                    //get courses taken by that student for that year and term
                    ct=conn.prepareStatement(CourQ);
                    ct.setString(1, id);
                    ct.setInt(2, year);
                    ct.setString(3, term);
                    cs = ct.executeQuery();
                    while (cs.next()) {
                        cn = cs.getString("course_number");

                        if (sig2 !=year) {
                            sig2 = year;
                            System.out.print(year+" ");
                        }
                        
                        if(!sig3.equals(term)){
                            sig3=term;
                            System.out.print(term+" day: "+d1+" time: "+t1);
                        }

                        System.out.println("Course: " + cn);
                        //get all faculty that are teaching those courses
                        fct=conn.prepareStatement(CourFQ);
                        fct.setString(1, cn);
                        fct.setInt(2, year);
                        fct.setString(3, term);
                        fcs=fct.executeQuery();
                        while(fcs.next()){
                            id=fcs.getString("id");
                            rank=fcs.getInt("rank");
                            //get and dispaly any faculty that have same 
                            //yetm, year, course, day and time
                            dt=conn.prepareStatement(FTDQ);
                            dt.setString(1, id);
                            dt.setInt(2, year);
                            dt.setString(3, term);
                            ds=dt.executeQuery();
                            if(ds.next()){//should always happen
                                ed1 = ds.getString(
                                        "day_1");
                                ed2 = ds.getString(
                                        "day_2");
                                ed3 = ds.getString(
                                        "day_3");
                                et1 = ds.getString(
                                        "time_1");
                                et2 = ds.getString(
                                        "time_2");
                                et3 = ds.getString(
                                        "time_3");
                                

                                if (ed2.equals("NULL")) {
                                    ed2 = "";
                                }

                                if (ed3.equals("NULL")) {
                                    ed3 = "";
                                }
                                
                                if(ed1.equals(d1)||ed2.equals(d1)||ed3.equals(d1)){
                                    if(et1.equals(t1)||et2.equals(t1)||et3.equals(t1)){
                                        if(sig==0){
                                            System.out.println("Faculty:");
                                            sig=1;
                                        }
                                        
                                        fit=conn.prepareStatement(IdQ);
                                        fit.setString(1,id);
                                        fis=fit.executeQuery();
                                        fis.next();
                                        name=fis.getString("name");
                                        System.out.println(name+" "+id);
                                        fis.close();
                                        fit.close();
                                    }
                                }
                                
                            }
                            ds.close();
                            dt.close();
                        }
                        sig=0;
                        fcs.close();
                        fct.close();
                    }
                    cs.close();
                    ct.close();
                }
                ss.close();
                st.close();
                
                
            }
        is.close();
        it.close();
        conn.close();
        } catch (SQLException e) {
            System.out.println(e);
            System.exit(0);
        }
    }
    
    private void FL(){
        Connection conn = ConnectDB.getConn();
        String cn, term, id, degree, name, d1, d2, d3, t1, t2, t3;
        String ed1, ed2, ed3, et1, et2, et3;
        int sig = 0, sig2 = 0, rank, year, load, corP, dayP, timP,rel, 
                sab, lea, sig3=0;

        PreparedStatement dt = null, ct = null, fct = null, it = null, ft = null;
        PreparedStatement sit=null;
        
        try{
            ResultSet ds, cs, fcs, is, fs, sis;
            it = conn.prepareStatement(IDSQ);
            is = it.executeQuery();
            System.out.println("Faculty: ");
            while(is.next()){
                id=is.getString("id");
                name=is.getString("name");
                degree=is.getString("degree");
                
                System.out.println(name+" "+id+" "+degree);
                //get fac form
                ft=conn.prepareStatement(FacQ);
                ft.setString(1, id);
                fs=ft.executeQuery();
                while(fs.next()){
                    year=fs.getInt("year");
                    term=fs.getString("term");
                    d1=fs.getString("day_1");
                    d2=fs.getString("day_2");
                    d3=fs.getString("day_3");
                    t1=fs.getString("time_1");
                    t2=fs.getString("time_2");
                    t3=fs.getString("time_3");
                    load=fs.getInt("load");
                    corP=fs.getInt("course_pref");
                    dayP=fs.getInt("days_pref");
                    timP=fs.getInt("time_pref");
                    rel=fs.getInt("release");
                    sab=fs.getInt("sabbatical");
                    lea=fs.getInt("leave");
                    
                    if (d2.equals("NULL")) {
                        ed2 = "";
                        sig3=1;
                    }

                    if (d3.equals("NULL")) {
                        ed3 = "";
                    }
                    //might need to change how summer day is handeled in pev options.
                    String out;//might want to build the out put string 
                    //based on the options that need to be shown this time
                    //aka if year changed (prob not needed)
                    
                    System.out.print(year+" "+term);
                    //need to display year once
                    //then display term , day and time for that term, 
                    //load, course, day, time pref ranking
                    //release, sabbatical, leave options if summer term
                    
                    //then display courses follwed by students that have a common
                    //day and time as the teacher
                    
                }
                
            }
            
            
            
        }catch (SQLException e){
            System.out.println(e);
            System.exit(0);
        }
    }
}
