package _processor;

import _interface.MyWindow;
import _record.Course;
import _record.CourseReader;
import _record.FileLocater;

import javax.swing.*;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

public class CourseReg {
    static String version = "4.3";
    static String information = "";
    static final String baseURL = "https://w5.ab.ust.hk/wcq/cgi-bin/";
    static String URL;
    public static MyWindow myWindow;

    public static void updateViaURL() throws IOException {
        Course.init();

        CourseReader.mode = 1;

        try {
            URLConnection con = new URL(baseURL).openConnection();
            con.connect();
            con.getInputStream();
            URL = con.getURL().toString() + "subject/";
            System.out.println(URL);

            for (int i = 0; i < FileLocater.SCHOOL_NUM; i++) {
                for (int j = 0; j < FileLocater.list[i].deptNum; j++) {
                    String url = URL + FileLocater.list[i].dept[j];
                    System.out.println("Reading from: " + url);
                    CourseReader.readFromURL(url, i, j);
                }
            }
        } catch (Exception e) {
            System.err.print("Update Failed.");
            e.printStackTrace();
        }

        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("courseInfo.ser"));
        oos.writeObject(Course.courseCount);
        oos.writeObject(Course.list_code);
        oos.writeObject(Course.list);
        oos.close();

    }

    public static void main(String[] args) throws IOException {

        myWindow = new MyWindow(version, information);
        myWindow.setVisible(true);
        myWindow.setSize(400, 150);
        myWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myWindow.setTitle("CourseReg" + version);

        FileInputStream courses;
        try {
            courses = new FileInputStream("courseInfo.ser");
            myWindow.updateNote("Loading course data...");
        } catch(FileNotFoundException e) {
            myWindow.updateNote("Course Data Not Found. Downloading now...");
            updateViaURL();
            courses = new FileInputStream("courseInfo.ser");
        }

        try {
            ObjectInputStream ios = new ObjectInputStream(courses);
            Course.courseCount = (int[][]) ios.readObject();
            Course.list_code = (HashMap<String, Course>) ios.readObject();
            Course.list = (Course[][][]) ios.readObject();
            myWindow.updateNote("Done");
        } catch (ClassNotFoundException enfe) {
            System.err.println("class not found exception");

        }
        //Course[] selectedList = Course.list;
        //Register.start(selectedList, Course.courseCount);
    }


    // Legacy snippets

     /*
        CourseReader.mode=0;
		for (int i = 0; i<FileLocater.SCHOOL_NUM; i++)
		for (int j = 0; j<FileLocater.list[i].deptNum; j++)
			CourseReader.readFromTXT(path+"/" + FileLocater.list[i].name + "/" + FileLocater.list[i].dept[j] + ".txt",i,j);
    */

	/*
	BufferedReader in = new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream("LANG.html"))));
		CourseReader.mode = 2;

		Course c = CourseReader.getCourse(in);
		while (c!=null) {
			c.print();
			c = CourseReader.getCourse(in);

		}
*/

    //	Course.list[0][0][0].print();
    //Course.list_code.get("MATH217").print();

    //CourseReader.readFromTXT("courseInfo/LANG/LANG100-199.txt", 4, 3);

	/*	for (int i = 0; i<Course.list[4][3].length; i++)
			Course.list[4][3][i].print();
		*/	//System.out.prinltn(Course.list[4][3][i].print())

    //		for (int i = 0; i<Course.courseCount; i++) Course.list[i].print();
}

