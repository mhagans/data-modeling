
package DM;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.Scanner;


public class Report {
    //gets the courses ordered by term offered
    private String CorQ="select * from Course order by term";
    //gets Student's courses ordered 
    //make sure to add coursenumber and term resrictions
    private String StudCQ="select * from Form_Course where rank=0 and course_number='";
    private String FacCQ="select * from Form_Course where rank>0 and course_number='";
    //gets id
    private String IdQ="select * from Id where id='";//make sure to add id and a "'"
    //gets preffered times
    //make sure to ad id and a "'" and year and term restrictions
    private String STDQ="select day_1, day_2, day_3, time_1,"
                            + " time_2, time_3 from Student_Form where id='";
    private String FTDQ="select day_1, day_2, day_3, time_1,"
                            + " time_2, time_3 from Faculty_Form where id='";
    
    
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
                    //DL();
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
        Statement ct=null,fct=null,it=null,st=null;
        
        try{
            ct=conn.createStatement();
            fct=conn.createStatement();
            it=conn.createStatement();
            st=conn.createStatement();
            ResultSet cs, fcs, is, ss;
            cs=ct.executeQuery(CorQ);
            
            while(cs.next()){
                cn = cs.getString("course_number");
                term = cs.getString("term");
                System.out.println(cn + " " + term + "\nStudents: ");
                //display student info and times
                fcs = ct.executeQuery(StudCQ+cn+"' and term='"+term+"' order by year");
                while (fcs.next()) {
                    id = fcs.getString("id");
                    year = fcs.getInt("year");
                    //get student info
                    is = it.executeQuery(IdQ + id + "'");
                    name = is.getString("name");
                    degree = is.getString("degree");
                    //get student day and times (year is an int so make sure no '')
                    ss = st.executeQuery(STDQ+ id + "'"
                            + " and year=" + year + " and term='" + term + "'");
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

                    if (t2.equals("NULL")) {
                        t2 = "";
                    }

                    if (t3.equals("NULL")) {
                        t3 = "";
                    }
                    System.out.println(year + " " + id + " " + name + " " + degree + " \nPreffered  ordered by preference "
                            + "days: " + d1 + ", " + d2 + ", " + d3 + ", times: " + t1 + ", " + t2 + ", "
                            + "t3\n");
                    is.close();
                    ss.close();
                }
                fcs.close();
                //get faculty info
                System.out.println("Faculty: \n");
                fcs = ct.executeQuery(FacCQ+cn+"' and term='"+term+"' order by year");
                while (fcs.next()) {
                    id = fcs.getString("id");
                    year = fcs.getInt("year");
                    //get student info
                    is = it.executeQuery(IdQ+ id + "'");
                    name = is.getString("name");
                    degree = is.getString("degree");
                    //get student day and times (year is an int so make sure no '')
                    ss = st.executeQuery(FTDQ+ id + "'"
                            + " and year=" + year + " and term='" + term + "'");
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

                    if (t2.equals("NULL")) {
                        t2 = "";
                    }

                    if (t3.equals("NULL")) {
                        t3 = "";
                    }
                    System.out.println(year + " " + id + " " + name + " " + degree + "\n Preffered  "
                            + "ordered by preference " + "days: " + d1 + ", " + d2 + ", " + d3 + ", times: " 
                            + t1 + ", " + t2 + ", " + "t3\n");
                    is.close();
                    ss.close();
                }
                fcs.close();

            }
            cs.close();
            
            ct.close();
            fct.close();
            it.close();
            st.close();
            conn.close();
            
        }
        catch(SQLException e){
            System.out.println(e);
        }
    }
    //gets the courses where the students and faculty both prefered it on the same day, ordered by that day
    public void DL(){
        Connection conn=ConnectDB.getConn();
        Statement dt=null,ct=null,fct=null,it=null, st=null;
        
    }
}
