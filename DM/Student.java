package DM;

import java.util.ArrayList;

//Students have IDs, names, degrees, semester + year, availible courses, preffered days?
public class Student {

	private int ID;
	private String name;
	private String degree;
	private String year;
	private String times;
	private String days;
	private ArrayList courses;
	
		
	public int getID(){
		return(ID);
	}
	
	public void setID(int x){
		ID = x;
	}
	
	public String getName(){
		return(name);
	}
	
	public void setName(String x){
		name = x;
	}
	
	public String getDegree(){
		return(degree);
	}
	
	public void setDegree(String x){
		degree = x;
	}
	
	public String getYear(){
		return(semesteryear);
	}
	
	public void setYear(String x){
		year = x;
	}
	
	public void setTimes(String x){
		times = x;
	}
	public String getTimes(){
		return(times);
	}
	
	public void setDays(String x){
		days = x;
	}
	
	public String getDays(){
		return(days);
	}
	
}
