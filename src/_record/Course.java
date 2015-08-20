package _record;

import java.io.Serializable;
import java.util.HashMap;

@SuppressWarnings("ALL")
public class Course implements Serializable{
	public static Course[][][] list;
	public static int[][] courseCount;

	public static HashMap<String, Course> list_code;
	
	
	public static int SCHOOL_OF_BUSI = 0;
	public static int SCHOOL_OF_ENG = 1;
	public static int SCHOOL_OF_SCIE= 2;
	public static int SCHOOL_OF_HUMA = 3;
	public static int SCHOOL_OF_LANG = 4;
	public static int SCHOOL_OF_OTHE = 5;
	
	
	
	//public static int courseCount;
	public static int START_H = 8;
	public static int START_M = 0;
	public static final int MAX_SLOT = 15;
    public static final int MAX_SESSION = 150;

	public static final int L = 0;
	public static final int LA = 1;
	public static final int T = 2;
	
	public static final int SCHOOL_NUM = 5;
	public static final int DEPT_NUM = 16;
    public static final int MAX_COURSE = 150;


    public final Session[][] sessions;
	public final int num;
	public final String name;
	public final String code;
	public final int[] type;
	public final int state;
	public final int credit;
	public final boolean match;
	
	static {
		init();
	}


    public String getName() {
        return name;
	}
	
	public String getCode() {
		return code;
	}
	public static void  init() {
		list = new Course[SCHOOL_NUM][DEPT_NUM][Course.MAX_COURSE];
		courseCount = new int[SCHOOL_NUM][DEPT_NUM];
		list_code = new HashMap<String, Course>();
	}
	
	public Course(String c, String n,Session[][] s, int number, int[] t, int cre, boolean m) {
		name = n;
		code = c;
		sessions = s;
		num = number;
		type = t;
		credit = cre;
		match = m;
		int st = 1;
		if (type[L] == 0 && type[LA] == 0 && type[T] == 0) st = 0;
		if (type[L] != 0) st *=type[L];
		if (type[LA]!= 0 ) st *= type[LA];
		if (type[T] != 0) st *= type[T];
		state = st;
	}
	
	
	public void print() {
		System.out.println("Course Code: " + code + "\n" +
							"Course Name: " + name + "\n" + 
							"Course session number: " + num);
		for (int j = 0; j<=2; j++)
		for (int i = 0; i<type[j]; i++) sessions[j][i].print();
	}
	
	public String[][] present() {
		String[][] info;
		info = new String[num+1][];
		info[0] = new String[1];
		info[0][0] = code + " " + name;
		for (int i = 0; i<type[L]; i++) {info[i+1] = new String[sessions[L][i].getDayPresent().length];info[i+1] =sessions[L][i].getDayPresent();}
		for (int i = 0; i<type[LA]; i++){info[i+1+type[L]] = new String[sessions[LA][i].getDayPresent().length]; info[i+1+type[L]] = sessions[LA][i].getDayPresent();}
		for (int i = 0; i<type[T]; i++) {info[i+1+type[L]+type[LA]] = new String[sessions[T][i].getDayPresent().length];info[i+1+type[L]+type[LA]]=sessions[T][i].getDayPresent();}
		
		
		return info;
	}
	public static boolean match(Session s1, Session s2) {
		//System.out.println("matching  " + s1.name + "  with  " + s2.name);
		if (s1.name.equals(s2.name.substring(0,s1.name.length()))) {
				return true;
		}
		else return false;
	}
	
	public static class Session implements Serializable{
		public final TimeSlot[]  slot;
		public final int num;
		public final String type;
		public final String name;
		 
		
		public void print() {
			System.out.println(type+name);
			System.out.println("Slot number: "+ num);
			for (int i = 0; i<num; i++) slot[i].print();
		}
		
		public String[] getDayPresent() {
			String[] info;
			info = new String[num+1];
			info[0] = type+name + "\r\n";
			for (int i = 0; i<num; i++) info[i+1] = TimeSlot.getDayPresent(slot[i]);
			return info;
		}
		
		
		public Session(TimeSlot[] s, int n, String t, String Name) { 
			slot = s;
			num = n;
			type = t;
			name = Name;
		}
		public final TimeSlot getSlot(int i) {
			return slot[i];
		}
		
		public int getNum() {
			return num;
		}
		
	}

	public static class TimeSlot implements Serializable{
		public final int start;
		public final int end;
		
		public void print(){
			System.out.println(start+ "--" + end);
		}

		
		
		public TimeSlot(int s, int e){
			start = s;
			end = e;
		}
		
		static String getDayPresent(TimeSlot t) {
			int day = t.start / 30;
			String info = "";
			switch (day) {
			case 0: info = "Mon  "; break;
			case 1: info = "Tue  "; break;
			case 2: info = "Wed  "; break;
			case 3: info = "Thu  "; break;
			case 4: info = "Fri  "; break;
			
			}
			
			int startH = (t.start % 30)/2 + START_H;
			int startM = (t.start % 30)%2;
			if (startM == 1) startM = 30;
			
			int endH = (t.end % 30)/2 + START_H;
			int endM = (t.end %30) %2;
			if (endM == 0) {
				endH --;
				endM = 50;
			}
			else endM = 20;
			
			String startTime = String.valueOf(startH);
			if (startM == 0) startTime +=":00";
			else startTime +=":30";
			
			String endTime = String.valueOf(endH) + ":" + String.valueOf(endM);
			
			info += startTime + "-" + endTime + "   ";
			return info;
		}

	}
	
	
	
	
	
}

