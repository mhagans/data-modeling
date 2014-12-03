package DM;

public class StudentForm {
	
	private int year;
	private String term;
	private String days;
	private String times;
	private String id;
	
	public int getYear(){
		return(year);
	}
	
	public void setYear(int x){
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
	
	public String getID(){
		return(id);
	}
	
	public void setID(String studentid){
		id = studentid;
	}
	
	public void setTerm(String x){
		term = x;
	}
	
	public String getTerm(){
		return(term);
	}
}
