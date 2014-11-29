package DM;

/**
 * Created by Marcus on 11/28/2014.
 */
public class Course {

    public String course_number;
    public String name;

    public Course(String course_number, String name){
        this.course_number = course_number;
        this.name = name;
    }

    public String toString(){
        String list = course_number + " " + name;
        return list;
    }
}
