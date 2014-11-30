
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
    private String StudCQ="select * from Form_Course where ranking=0 and course_number= ? "
            + "and term= ? order by year";
    //gets id
    private String FacCQ="select * from Form_Course where ranking>0 and course_number= ? "
            + "and term= ? order by year";
    
    private String IdQ="select * from id where id = ?";//might have a problem with case
    //gets preffered times
    private String STDQ="select day, time from Student_Form where id=?"
            + "and year = ? and term = ?";
    
    private String FTDQ="select day_1, day_2, day_3, time_1,"
                            + " time_2, time_3 from Faculty_Form where id=?"
            + "and year = ? and term = ?";
    
    private String StudQ="select * from Student_Form where id=? order by year";
    private String FacQ="select * from Faculty_Form where id=? order by year";
    
    private String CourQ="select * from Form_Course where id= ? and year =? and "+
      "term = ?";
    private String CourFQ="select id, ranking from Form_Course where course_number= ? and year = ? and "+
      "term = ? and ranking>0";
    private String CourSQ="select id from Form_Course where course_number = ? and year = ? and term = ?"+
      " and ranking=0";
    
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
    private void CL(){//split and show the faculty and students both on the same year.
        String cn,term,id,degree,name,d1,d2,d3,t1,t2,t3, out;
        int year,rank;
        Connection conn=ConnectDB.getConn();
        PreparedStatement ct,fct,it,st, sct;
        
        try{
            ResultSet cs, fcs, is, ss, scs;
            ct=conn.prepareStatement(CorQ);
            cs = ct.executeQuery();

            while (cs.next()) {
                cn = cs.getString("course_number");
                term = cs.getString("term");
                System.out.println(cn + " " + term + "\nStudents: ");
                //display student info and times
                sct=conn.prepareStatement(StudCQ);//should work
                sct.setString(1, cn);
                sct.setString(2, term);
                scs = sct.executeQuery();
                while (scs.next()) {
                    id = scs.getString("id");
                    year = scs.getInt("year");
                    //get student info
                    it=conn.prepareStatement(IdQ);//should work
                    it.setString(1, id);
                    is = it.executeQuery();
                    if (is.next()) {
                        System.out.println("BLAH");
                        name = is.getString("name");
                        out=name;
                        out=out.replaceFirst("\\s+$", "");
                        degree = is.getString("degree");
                        //get student day and times (year is an int so make sure no '')
                        st=conn.prepareStatement(STDQ);
                        st.setString(1, id);
                        st.setInt(2,year);
                        st.setString(3,term);
                        ss = st.executeQuery();
                        if (ss.next()) {//should work
                            d1 = ss.getString("day");
                            t1 = ss.getString("time");

                            System.out.println(year + " " + id + " " + out + " " + degree + " \nPreffered "
                                    + "Day: " + d1 +", Time: " + t1);
                        }
                        ss.close();
                        st.close();
                    }
                    is.close();
                    is.close();

                }
                scs.close();
                sct.close();
                //get faculty info
                System.out.println("Faculty: \n");
                fct=conn.prepareStatement(FacCQ);
                fct.setString(1,cn);
                fct.setString(2, term);
                fcs = fct.executeQuery();
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
                        out=name;
                        out=out.replaceFirst("\\s+$", "");
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

                            System.out.println(year + " " + id + " " + out + " " + degree + "\n rank= " + rank + " Preffered  "
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
    private void DL() {//need to dispaly year better : display year -> courses in year -> students and fac in course
        Connection conn = ConnectDB.getConn();
        String cn, term, id, degree, name, d1, d2, d3, t1, t2, t3, out;
        int sig = 0, sig2 = 0, rank, year;

        PreparedStatement ct, fct, it, st, sct;
        //hard code going through list of days
        String[] days = {"MWF ", "MW  ", "TR  ", "MTWR", "MW+4", "TR+4"};
        try {
            ResultSet cs, fcs, is, ss, scs;
            System.out.println("Days: \n");
            for (int i = 0; i < 6; i++) {
                System.out.println(days[i]+"\n");
                ct=conn.prepareStatement(CorQ);
                cs = ct.executeQuery();
                while (cs.next()) {
                    cn = cs.getString("course_number");
                    term = cs.getString("term");
                    sct=conn.prepareStatement(StudCQ);
                    sct.setString(1, cn);
                    sct.setString(2, term);
                    scs = sct.executeQuery();
                    while (scs.next()) {
                        id = scs.getString("id");
                        year = scs.getInt("year");
                        
                        it=conn.prepareStatement(IdQ);
                        it.setString(1, id);
                        is = it.executeQuery();
                        if (is.next()) {//should always occur
                            name = is.getString("name");
                            out=name;
                            out=out.replaceFirst("\\s+$", "");
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
                                    System.out.println(year + " " + id + " " + out + " " + degree + " preffered time: " + t1);
                                }
                                
                            }
                            ss.close();
                            st.close();
                        }
                        is.close();
                        it.close();
                    }
                    scs.close();
                    sct.close();
                    sig2 = 0;

                    fct=conn.prepareStatement(FacCQ);
                    fct.setString(1, cn);
                    fct.setString(2, term);
                    fcs = fct.executeQuery();
                    while (fcs.next()) {
                        id = fcs.getString("id");
                        year = fcs.getInt("year");
                        rank = fcs.getInt("rank");

                        it=conn.prepareStatement(IdQ);
                        it.setString(1, id);
                        is = it.executeQuery();
                        if (is.next()) {//should always occur
                            name = is.getString("name");
                            out=name;
                            out=out.replaceFirst("\\s+$", "");
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
                                    System.out.println(year + " " + id + " " + out + " " + degree + "rank: " + rank + " preffered times: " + t1 + ", " + t2 + ", " + t3);
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
    private void TL() {//need to dispaly year better : display year -> courses in year -> students and fac in course
        Connection conn = ConnectDB.getConn();
        String cn, term, id, degree, name, d1, d2, d3, t1, t2, t3, out;
        int sig = 0, sig2 = 0, rank, year;

        PreparedStatement ct, fct, it, st, sct;
        //hard code going through list of days
        String[] times = {"Morning", "Afternoon", "Evening"};
        try {
            
            ResultSet cs, fcs, is, ss, scs;
            System.out.println("Times: \n");
            for (int i = 0; i < 3; i++) {
                System.out.println(times[i] + "\n");
                ct=conn.prepareStatement(CorQ);
                cs = ct.executeQuery();
                while (cs.next()) {
                    cn = cs.getString("course_number");
                    term = cs.getString("term");

                    sct=conn.prepareStatement(StudCQ);
                    sct.setString(1, cn);
                    sct.setString(2, term);
                    scs = sct.executeQuery();
                    while (scs.next()) {
                        id = scs.getString("id");
                        year = scs.getInt("year");

                        it=conn.prepareStatement(IdQ);
                        it.setString(1, id);
                        is = it.executeQuery();
                        if (is.next()) {//should always occur
                            name = is.getString("name");
                            out=name;
                            out=out.replaceFirst("\\s+$", "");
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
                                    System.out.println(year + " " + id + " " + out + " " + degree + " preffered day: " + d1);
                                }

                            }
                            ss.close();
                            st.close();
                        }
                        is.close();
                        it.close();
                    }
                    scs.close();
                    sct.close();
                    sig2 = 0;

                    fct=conn.prepareStatement(FacCQ);
                    fct.setString(1, cn);
                    fct.setString(2, term);
                    fcs = fct.executeQuery();
                    while (fcs.next()) {
                        id = fcs.getString("id");
                        year = fcs.getInt("year");
                        rank = fcs.getInt("rank");

                        it=conn.prepareStatement(IdQ);
                        it.setString(1, id);
                        is = it.executeQuery();
                        if (is.next()) {//should always occur
                            name = is.getString("name");
                            out=name;
                            out=out.replaceFirst("\\s+$", "");
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
                                    System.out.println(year + " " + id + " " + out + " " + degree + "rank: " + rank + " preffered days: " + d1 + ", " + d2 + ", " + d3);
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
        String cn, term, id, degree, name, d1, t1, sig3="", out;
        String ed1, ed2, ed3, et1, et2, et3;
        int sig = 0, sig2 = 0, year;

        PreparedStatement dt, ct, fct, it, st, fit;
    
        //get student id and subsequent info
        try {
            ResultSet ds, cs, fcs, is, ss, fis;
            it = conn.prepareStatement(IDSQ);
            is = it.executeQuery();
            System.out.println("Students:");
            while(is.next()){
                id=is.getString("id");
                name=is.getString("name");
                out=name;
                out=out.replaceFirst("\\s+$", "");
                degree=is.getString("degree");
                System.out.println(out+" "+id+" "+degree);
                
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
                            System.out.println(year);
                        }
                        
                        if(!sig3.equals(term)){
                            sig3=term;
                            System.out.println(term+" day: "+d1+" time: "+t1);
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
                                        out=name;
                                        out=out.replaceFirst("\\s+$", "");
                                        System.out.println(out+" "+id);
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
        String cn, term, id, degree, name, d1, d2, d3, t1, t2, t3, sig="", out;
        String ed, et;
        int  sig2 = 0, rank, year, load, corP, dayP, timP,rel, 
                sab, lea, sig3=0;

        PreparedStatement dt, ct, sct, it, ft, sit;
        
        try{
            ResultSet ds, cs, scs, is, fs, sis;
            it = conn.prepareStatement(IDFQ);
            is = it.executeQuery();
            System.out.println("Faculty: ");
            while(is.next()){
                id=is.getString("id");
                name=is.getString("name");
                out=name;
                out=out.replaceFirst("\\s+$", "");
                degree=is.getString("degree");
                
                System.out.println(out+" "+id+" "+degree);
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
                        d2 = "";
                    }

                    if (d3.equals("NULL")) {
                        d3 = "";
                    }

                    if (sig2 != year) {
                        sig2 = year;
                        System.out.println(year);
                    }
                    //then display term , day and time for that term, 
                    if (!sig.equals(term)) {
                        sig = term;
                        System.out.println(term + " day: " + d1+", "+d2+", "+d3 
                                + " time: " + t1+", "+t2+", "+t3+"\n"
                                + "Load: "+load+" Preferences 1-3:\nCourses: "
                                +corP+" Days: "+dayP+ " Times: "+timP);
                    }
                    //load, course, day, time pref ranking
                    //release, sabbatical, leave options if summer term
                    if(!d2.equals("")){
                        System.out.print("Term Expectations: \nCourse Release: ");
                        if(rel==0)
                            System.out.print("No ");
                        else System.out.print("Yes ");
                        System.out.print("Sabbatical: ");
                        if(sab==0)
                            System.out.print("No ");
                        else System.out.print("Yes ");
                        System.out.print("Leave: ");
                        if(lea==0)
                            System.out.print("No ");
                        else System.out.print("Yes ");
                        System.out.println("");
                    }
                    
                    //then display courses follwed by students that have a common
                    //day and time as the teacher
                    
                    
                    ct=conn.prepareStatement(CourFQ);
                    ct.setString(1, id);
                    ct.setInt(2, year);
                    ct.setString(3, term);
                    cs=ct.executeQuery();
                    System.out.println("Courses: ");
                    while(cs.next()){
                        cn=cs.getString("course_number");
                        rank=cs.getInt("rank");
                        System.out.println(cn+" ranking: (1-5) "+rank);
                        sct=conn.prepareStatement(CourSQ);
                        sct.setString(1, cn);
                        sct.setInt(2, year);
                        sct.setString(3, term);
                        scs=sct.executeQuery();
                        while(scs.next()){
                            id=scs.getString("id");
                            dt=conn.prepareStatement(STDQ);
                            dt.setString(1, id);
                            dt.setInt(2, year);
                            dt.setString(3, term);
                            ds=dt.executeQuery();
                            if(ds.next()){
                                ed=ds.getString("day");
                                et=ds.getString("time");
                                
                                if(ed.equals(d1)||ed.equals(d2)||ed.equals(d3)){
                                    if(et.equals(t1)||et.equals(t3)||et.equals(t3)){
                                        if(sig3==0){
                                            System.out.println("Students: ");
                                            sig3=1;
                                        }
                                        sit=conn.prepareStatement(IdQ);
                                        sit.setString(1,id);
                                        sis=sit.executeQuery();
                                        sis.next();
                                        name=sis.getString("name");
                                        out=name;
                                        out=out.replaceFirst("\\s+$", "");
                                        System.out.println(out +" "+id);
                                        sis.close();
                                        sit.close();
                                    }
                                }
                            }
                            ds.close();
                            dt.close();
                        }
                        sig3=0;
                        scs.close();
                        sct.close();
                    }
                    cs.close();
                    ct.close();
                }
                fs.close();
                ft.close();
            }
            is.close();
            it.close();
            conn.close();
        }catch (SQLException e){
            System.out.println(e);
            System.exit(0);
        }
    }
}
