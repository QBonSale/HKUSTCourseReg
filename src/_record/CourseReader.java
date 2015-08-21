package _record;

import _processor.CourseReg;

import java.io.*;
import java.net.URL;

@SuppressWarnings("ALL")
public class CourseReader {
    static String buffer = "";
    public static final int TXT = 0;
    public static final int URL = 1;

    public static int mode;
//	public static boolean match = false;

    public static void readFromURL(String site, int schoolIndex, int deptIndex) throws IOException {
        mode = 1;
        BufferedReader in = new BufferedReader(new InputStreamReader((new URL(site)).openStream()));
        do {
            System.out.println(Course.courseCount[schoolIndex][deptIndex]);
            Course.list[schoolIndex][deptIndex][Course.courseCount[schoolIndex][deptIndex]] = getCourse(in);
            //	System.out.println(Course.list[schoolIndex][deptIndex][Course.courseCount[schoolIndex][deptIndex]].code + Course.list[schoolIndex][deptIndex][Course.courseCount[schoolIndex][deptIndex]].match);
            if (Course.list[schoolIndex][deptIndex][Course.courseCount[schoolIndex][deptIndex]] != null) {

                Course.list_code.put(Course.list[schoolIndex][deptIndex][Course.courseCount[schoolIndex][deptIndex]].code, Course.list[schoolIndex][deptIndex][Course.courseCount[schoolIndex][deptIndex]]);

            }
            Course.courseCount[schoolIndex][deptIndex]++;

        } while (Course.list[schoolIndex][deptIndex][Course.courseCount[schoolIndex][deptIndex] - 1] != null);
        Course.courseCount[schoolIndex][deptIndex]--;
    }


    public static void readFromTXT(String fileName, int schoolIndex, int deptIndex) throws IOException {


        BufferedReader in = new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(fileName))));


        do {
            Course.list[schoolIndex][deptIndex][Course.courseCount[schoolIndex][deptIndex]] = getCourse(in);
            //	System.out.println(Course.list[schoolIndex][deptIndex][Course.courseCount[schoolIndex][deptIndex]].code + Course.list[schoolIndex][deptIndex][Course.courseCount[schoolIndex][deptIndex]].match);
            if (Course.list[schoolIndex][deptIndex][Course.courseCount[schoolIndex][deptIndex]] != null) {

                Course.list_code.put(Course.list[schoolIndex][deptIndex][Course.courseCount[schoolIndex][deptIndex]].code, Course.list[schoolIndex][deptIndex][Course.courseCount[schoolIndex][deptIndex]]);

            }
            Course.courseCount[schoolIndex][deptIndex]++;
        } while (Course.list[schoolIndex][deptIndex][Course.courseCount[schoolIndex][deptIndex] - 1] != null);
        Course.courseCount[schoolIndex][deptIndex]--;

    }


    public static Course getCourse(BufferedReader in) throws IOException {
        if (mode == 0) {

            String strLine = buffer;
            buffer = null;
            if (strLine == null) strLine = in.readLine();
            //while (strLine!=null  && !strLine.matches(".*\\d-\\d-\\d:\\d.*")) strLine = in.readLine();

            while (strLine != null && !isFirstLineOfCourse(strLine)) strLine = in.readLine();

            if (strLine == null) return null;
            if (strLine != null) System.out.println(strLine);
            String[] token = strLine.split(" ");

            //String courseCode = token[0];
            String courseCode = token[0] + token[1];


            String courseName = "";
            int i = 3;

            while (i < token.length) {
                courseName += (token[i] + " ");
                i++;
            }

            //int credit = (token[i].charAt(7))-48;
            int credit = -1;


//		while (i<token.length && !token[i].equals("matching")) i++;
//		boolean match = false;
//		if (i!=token.length) match = true; 

            boolean match = false;

            Course.Session[][] sessions = new Course.Session[3][Course.MAX_SESSION];
            int[] type = new int[3];
            Course.Session s;

            int sessionNumber = 0;
            do {
                s = readFromText_getSession(in);

                if (s != null) {
                    if (s.type.equals("L")) {
                        sessions[Course.L][type[Course.L]] = s;
                        type[Course.L]++;
                    }
                    if (s.type.equals("LA")) {
                        sessions[Course.LA][type[Course.LA]] = s;
                        type[Course.LA]++;
                    }
                    if (s.type.equals("T")) {
                        sessions[Course.T][type[Course.T]] = s;
                        type[Course.T]++;/*System.out.println(s.type+s.name+"   " + type[Course.T]);*/
                    }
                    try {
                        Integer.parseInt(s.name);
                    } catch (Exception e) {
                        match = true;
                    }
                }
                sessionNumber++;
            } while (s != null);
            sessionNumber--;
            return new Course(courseCode, courseName, sessions, sessionNumber, type, credit, match);
        } else if (mode == 1) {
            String strLine = buffer;
            buffer = null;
            boolean match = false;
            if (strLine == null) strLine = in.readLine();
            while (strLine != null && !strLine.matches("<h2>.*</h2>")) {
                if (strLine.matches("<div class=\"matching\">.*")) match = true;
                strLine = in.readLine();
            }
            if (strLine == null) return null;
            //CourseReg.myWindow.updateNote(strLine.substring(4, strLine.length() - 5));
            System.out.println(strLine);

            String courseName = "";
            String courseCode;
            int credit;
            String[] token = strLine.split(" ");
            int i = 0;
            //get course code
            courseCode = token[0].substring(4, 8) + token[1];
            //get course name
            for (i = 3; i < token.length - 2; i++) courseName = courseName + token[i] + " ";

            //get course credit
            i = i + 1;
            credit = token[i].charAt(1) - 48;

            Course.Session[][] sessions = new Course.Session[3][Course.MAX_SESSION];
            int[] type = new int[3];
            Course.Session s;
            int sessionNumber = 0;

//		strLine = in.readLine();
//		strLine = in.readLine();
            //if (strLine.matches(".*[Note : students are required to enrol in matching lecture, tutorial and laboratory sections.].*")) match = true; else match = false;
            do {
                s = readFromURL_getSessions(in);

                if (s != null) {
                    if (s.type.equals("L")) {
                        sessions[Course.L][type[Course.L]] = s;
                        type[Course.L]++;
                    }
                    if (s.type.equals("LA")) {
                        sessions[Course.LA][type[Course.LA]] = s;
                        type[Course.LA]++;
                    }
                    if (s.type.equals("T")) {
                        sessions[Course.T][type[Course.T]] = s;
                        type[Course.T]++;/*System.out.println(s.type+s.name+"   " + type[Course.T]);*/
                    }

                }
                sessionNumber++;
            } while (s != null);
            sessionNumber--;
            return new Course(courseCode, courseName, sessions, sessionNumber, type, credit, match);


        } else
//	if (mode ==2) 
        {
            String strLine = buffer;
            buffer = null;
            if (strLine == null) strLine = in.readLine();

            while (strLine != null && !isFirstLineOfCourse(strLine)) strLine = in.readLine();

            if (strLine == null) return null;
            if (strLine != null) System.out.println(strLine);
            String[] token = strLine.split(" ");

            //String courseCode = token[0];
            String courseCode = token[0] + token[1];


            String courseName = "";
            int i = 3;

            while (i < token.length) {
                courseName += (token[i] + " ");
                i++;
            }

            //int credit = (token[i].charAt(7))-48;
            int credit = token[i - 2].charAt(1) - 48;

            boolean match = false;

            Course.Session[][] sessions = new Course.Session[3][Course.MAX_SESSION];
            int[] type = new int[3];
            Course.Session s;
            int sessionNumber = 0;
            strLine = in.readLine();
//		while (!strLine.matches("COURSE INFO")) {
            do {
                s = readFromText2_getSessions(in);
                if (s != null) {
                    if (s.type.equals("L")) {
                        sessions[Course.L][type[Course.L]] = s;
                        type[Course.L]++;
                    }
                    if (s.type.equals("LA")) {
                        sessions[Course.LA][type[Course.LA]] = s;
                        type[Course.LA]++;
                    }
                    if (s.type.equals("T")) {
                        sessions[Course.T][type[Course.T]] = s;
                        type[Course.T]++;/*System.out.println(s.type+s.name+"   " + type[Course.T]);*/
                    }
                }
                try {
                    Integer.parseInt(s.name);
                } catch (Exception e) {
                    match = true;
                }
                sessionNumber++;
            } while (s != null);
            sessionNumber--;
            return new Course(courseCode, courseName, sessions, sessionNumber, type, credit, match);


        }
    }

    public static Course.Session readFromText2_getSessions(BufferedReader in) throws IOException {
        String strLine;
        if (buffer == null) {
            strLine = in.readLine();
        } else {
            strLine = buffer;
            buffer = null;
        }

        if (strLine == null || strLine.matches("COURSE INFO")) {
            buffer = strLine;
            return null;
        }
        Course.TimeSlot[] slots = new Course.TimeSlot[Course.MAX_SLOT];
        int slotNumber = 0;
        String type = " ";
        String name;
        int i = 1;

        switch (strLine.charAt(0)) {
            case 'L':
                if (strLine.charAt(1) == 'A') {
                    type = "LA";
                    i = 2;
                } else type = "L";
                break;
            case 'R':
                type = "T";
                break;
            case 'T':
                type = "T";
                break;

        }
        ;
        System.out.println(strLine);
        int j = i;
        while (strLine.charAt(j) != ' ') j = j + 1;
        name = strLine.substring(i, j);
        while (strLine.charAt(j) != '(') j = j + 1;
        while (strLine.charAt(j) != ')') j = j + 1;
        j = j + 1;

        while (strLine.charAt(j) == '\t') j = j + 1;
        String strLine0 = strLine.substring(j, strLine.length());
        strLine = strLine0;
        while (!isFirstLineOfSession(strLine) && !strLine.matches("COURSE INFO")) {

            if (isLineOfSlot1(strLine)) {
                i = 0;
                while (strLine.charAt(i) != ' ') i++;
                Course.TimeSlot temp = getSlot(strLine.substring(i + 1, i + 17));
                j = 0;
                while (strLine.charAt(j) != ' ') {
                    slots[slotNumber] = new Course.TimeSlot(getBase(strLine.substring(j, j + 2)) + temp.start, getBase(strLine.substring(j, j + 2)) + temp.end);
                    slotNumber++;
                    j = j + 2;
                }
            }
            strLine = in.readLine();
        }
        buffer = strLine;
        return new Course.Session(slots, slotNumber, type, name);
    }

    public static Course.Session readFromURL_getSessions(BufferedReader in) throws IOException {
        String strLine;
        if (buffer == null) {
            strLine = in.readLine();
        } else {
            strLine = buffer;
            buffer = null;
        }


        while (strLine != null && !strLine.matches("<td align=\"center\".*")) {
            if (strLine.matches("</div>")) {
                buffer = strLine;
                return null;
            }
            strLine = in.readLine();
        }

        Course.TimeSlot[] slots = new Course.TimeSlot[Course.MAX_SLOT];
        int slotNumber = 0;

        String type = "";
        String name = "";
        String[] token = strLine.split(" ");

        String tmp = strLine;
        int j = 0;
        while (tmp.charAt(j) != '>') j++;
        int k = j;
        while (tmp.charAt(k) != ' ') k++;
        tmp = tmp.substring(j + 1, k);
        //	System.out.println(tmp);
        int i = 1;
        switch (tmp.charAt(0)) {
            case 'L':
                if (tmp.charAt(1) == 'A') {
                    type = "LA";
                    i = 2;
                } else type = "L";
                break;
            case 'R':
                type = "T";
                break;
            case 'T':
                type = "T";
                break;
        }
        name = tmp.substring(i);

        strLine = in.readLine();
        while (strLine.matches(".*<td>.* ..:.... - ..:....</td>.*")) {
            token = strLine.split(" ");
            i = 0;
            tmp = "";
            while (!token[i].matches("..:....")) i++;
            tmp = token[i] + token[i + 1] + token[i + 2];
            Course.TimeSlot temp = getSlot(tmp.substring(0, 15));
            j = 0;
            while (token[i - 1].charAt(j) != '<') j++;
            j = j + 4;
            while (j < token[i - 1].length()) {
                slots[slotNumber] = new Course.TimeSlot(getBase(token[i - 1].substring(j, j + 2)) + temp.start, getBase(token[i - 1].substring(j, j + 2)) + temp.end);
                j = j + 2;
                slotNumber++;
            }
            strLine = "";
            for (k = i + 3; k < token.length; k++)
                strLine = strLine + " " + token[k];
            //strLine = in.readLine();
            //System.out.println(strLine);
            if (!strLine.matches(".*<td>.* ..:.... - ..:....</td>.*"))
                strLine = in.readLine();
        }

        buffer = strLine;
        return new Course.Session(slots, slotNumber, type, name);

    }

    public static Course.Session readFromText_getSession(BufferedReader in) throws IOException {
        String strLine;
        if (buffer == null) {
            strLine = in.readLine();
        } else {
            strLine = buffer;
            buffer = null;
        }
        //if (strLine == null || strLine.matches(".*\\d-\\d-\\d:\\d.*")) {buffer = strLine; return null;}

        if (strLine == null || isFirstLineOfCourse(strLine)) {
            buffer = strLine;
            return null;
        }
        ;

        //	String[] token = strLine.split("\\s");

        //while (token.length==0||!token[0].equals("L") && !token[0].equals("LA") && !token[0].equals("T")) {
        while (!isFirstLineOfSession(strLine) && !isFirstLineOfCourse(strLine)) {
            strLine = in.readLine();

            if (strLine != null && !isFirstLineOfCourse(strLine)) {
                //token = strLine.split("[\\s\\t]");
            } else {
                buffer = strLine;
                break;
            }

        }


        if (strLine == null || isFirstLineOfCourse(strLine)) {
            buffer = strLine;
            return null;
        }


        Course.TimeSlot[] slots = new Course.TimeSlot[Course.MAX_SLOT];
        int slotNumber = 0;


        String type = "";
        int i = 1;

        switch (strLine.charAt(0)) {
            case 'L':
                if (strLine.charAt(1) == 'A') {
                    type = "LA";
                    i = 2;
                } else type = "L";
                break;
            case 'R':
                type = "T";
                break;
            case 'T':
                type = "T";
                break;

        }
        ;
        int j = i;
        while (strLine.charAt(j) != '-') j++;

        String name = strLine.substring(i, j);
        strLine = in.readLine();
        while (strLine != null && !isLineOfSlot(strLine) && !isFirstLineOfSession(strLine) && !isFirstLineOfCourse(strLine))
            strLine = in.readLine();
        if (strLine == null) return null;

        if (!isLineOfSlot(strLine)) {
            buffer = strLine;
            return null;
        }

        while (strLine != null && !isFirstLineOfCourse(strLine) && !isFirstLineOfSession(strLine)) {
            if (isLineOfSlot(strLine)) {
                i = 0;
                while (strLine.charAt(i) != ' ') i++;

                Course.TimeSlot temp = getSlot(strLine.substring(i + 1, i + 14));
                j = 0;
                while (strLine.charAt(j) != ' ') {
                    slots[slotNumber] = new Course.TimeSlot(getBase(strLine.substring(j, j + 2)) + temp.start, getBase(strLine.substring(j, j + 2)) + temp.end);
                    slotNumber++;
                    j = j + 2;
                }
                //	System.out.println(strLine);
            }
            strLine = in.readLine();


        }

        //buffer = strLine;

/*
        String type = token[0];
		String name = token[1];

		do{
			int i = 2;
			//System.out.println(strLine);
			while (i<token.length && !token[i].matches(".*\\d\\d:\\d\\d-\\d\\d:\\d\\d.*")) i++;
			//	System.out.println(strLine);
			if (i==token.length) return null;
			Course.TimeSlot temp = getSlot(token[i]);

			i--;

			String[] token1 = token[i].split(",");
		
			try {
				int j = 0;
				while (true) {
					slots[slotNumber] = new Course.TimeSlot(getBase(token1[j])+temp.start, getBase(token1[j])+temp.end);
					slotNumber++;
					j++;
				}
			}
			catch (ArrayIndexOutOfBoundsException e){
			
			}
			strLine = in.readLine();
			if (strLine!=null)
			token = strLine.split("[\\s\\t]");
		}while (strLine!=null && strLine.matches(".*\\d\\d:\\d\\d-\\d\\d:\\d\\d.*") && !token[0].equals("L") && !token[0].equals("LA") && !token[0].equals("T"));
		buffer = strLine;
	*/
        buffer = strLine;
        return new Course.Session(slots, slotNumber, type, name);
    }

    static boolean isFirstLineOfCourse(String s) {

        if (s.matches("[A-Z]{4}+ [0-9]{4}+.*")) return true;
        else return false;
    }

    static boolean isFirstLineOfSession(String s) {
//		System.out.println(s);
        if (s.matches(".*\\(\\d\\d\\d\\d\\).*"))
            return true;
        else return false;
    }

    static boolean isLineOfSlot(String s) {

        if (s.matches(".*\\d\\d:\\d\\d - \\d\\d:\\d\\d.*")) return true;
        else return false;
    }

    static boolean isLineOfSlot1(String s) {

        if (s.matches(".*\\d:\\d\\d.M - .*\\d:\\d\\d.M.*")) return true;
        else return false;
    }

    public static int getBase(String str) {
        if (str.equals("Mo")) return 0;
        if (str.equals("Tu")) return 30;
        if (str.equals("We")) return 60;
        if (str.equals("Th")) return 90;
        if (str.equals("Fr")) return 120;
        if (str.equals("Sa")) return 150;
        return -1;
    }

    public static Course.TimeSlot getSlot(String str) {
        String[] token = str.split("[-\\s\\t]");
        int start = getTime(token[0]);
        int end = getTime(token[token.length - 1]);

        return new Course.TimeSlot(start, end);
    }

    public static int getTime(String str) {
        int time = -1;
        if (mode == 0) {
            String[] token = str.split(":");
            int x = Integer.parseInt(token[0]);
            int y = Integer.parseInt(token[1]);
            switch (y) {
                case 20:
                    y = 1;
                    break;
                case 30:
                    y = 1;
                    break;
                case 50:
                    y = 2;
                    break;
            }
            time = (x - Course.START_H) * 2 + y - Course.START_M;
        } else if (mode == 1) {
//			System.out.println(str);
            String[] token = str.split(":");
            int x = Integer.parseInt(token[0]);
            String tmp = token[1].substring(0, 2);
            int y = Integer.parseInt(tmp);

            switch (y) {
                case 20:
                    y = 1;
                    break;
                case 30:
                    y = 1;
                    break;
                case 50:
                    y = 2;
                    break;
            }
            time = (x - Course.START_H) * 2 + y - Course.START_M;
            if (token[1].charAt(2) == 'P' && x != 12) time = time + 12 * 2;
        }
        return time;
    }
}
