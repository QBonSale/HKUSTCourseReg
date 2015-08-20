package _interface;


import _processor.Register;
import _record.Course;
import _record.FileLocater;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.HashMap;

public class SwapCourseWindow extends JFrame {
    int maxNum = 300;
    int defaultNum = 30;
    JList courseList;
    JList selectedList;
    JScrollPane coursePane;
    JScrollPane selectedPane;
    DefaultListModel selectModel, courseModel;

    JButton moveUp;
    JButton moveDown;

    JButton select;
    JButton remove;
    JButton next;
    JButton cancel;

    JButton load;

    JComboBox solNum;

    String[] names;
    String[] collected;

//	HashMap<String, Object> states;

    JList schoolList;
    JScrollPane schoolPane;
    int chosenSchool;

    JList deptList;
    JScrollPane deptPane;
    DefaultListModel schoolModel, deptModel;

    JButton enterSchool, enterDept;

    JLabel info;
    HashMap<String, JFrame> details;

    JButton detail;

    JButton specify;

    HashMap<String, CourseModifyWindow> modifies;
    HashMap<String, Object> selected;

    JButton save;
    JButton hide;


    public String title;

    JPanel p, p1, p2, p3, p0, p4, p5;

    SwapCourseWindow ref = this;

    Course[] courses = null;
    boolean[][] states0;
    boolean[][] statesToProceed;

    JButton clear;

    public SwapCourseWindow() {

        selected = new HashMap<>();
        title = this.getTitle();
        this.setTitle(title + "-- Select course");

        schoolModel = new DefaultListModel();
        for (int i = 0; i < FileLocater.SCHOOL_NUM; i++) schoolModel.addElement(FileLocater.list[i]);

        schoolList = new JList(schoolModel);
        schoolList.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "First Character"));

        schoolPane = new JScrollPane(schoolList);
        schoolPane.setSize(100, 80);
        schoolPane.setBorder(null);

        enterSchool = new JButton("Enter");
        enterSchool.addActionListener(e -> {
            Thread t = new Thread(new Runnable() {
                public void run() {
                    if (schoolList.getSelectedValue() != null) {
                        deptModel.clear();
                        for (int i = 0; i < ((FileLocater) schoolList.getSelectedValue()).deptNum; i++) {
                            deptModel.addElement(((FileLocater) schoolList.getSelectedValue()).dept[i]);
                        }
                        chosenSchool = schoolList.getSelectedIndex();
                    }
                }
            });

            t.start();
        });


        deptModel = new DefaultListModel();

        deptList = new JList(deptModel);
        deptList.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Department List"));

        deptPane = new JScrollPane(deptList);
        deptPane.setSize(100, 80);
        deptPane.setBorder(null);

        enterDept = new JButton("Enter");
        enterDept.addActionListener(e -> {
            Thread t = new Thread(() -> {
                if (deptList.getSelectedValue() != null) {
                    courseModel.clear();

                    //System.out.println(Course.list[chosenSchool][deptList.getSelectedIndex()].length);
                    for (int i = 0; i < Course.courseCount[chosenSchool][deptList.getSelectedIndex()]; i++) {

                        courseModel.addElement(Course.list[chosenSchool][deptList.getSelectedIndex()][i].code);
                        //System.out.println(Course.list[chosenSchool][deptList.getSelectedIndex()][i].code);

                    }

                    coursePane.repaint();

                }
            });

            t.start();

        });


        courseModel = new DefaultListModel();

        courseList = new JList(courseModel);
        courseList.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Course List"));

        coursePane = new JScrollPane(courseList);
        coursePane.setSize(600, 600);
        coursePane.setBorder(null);

        selectModel = new DefaultListModel();
        selectedList = new JList(selectModel);
        selectedList.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Selected List"));

        selectedPane = new JScrollPane(selectedList);
        selectedPane.setSize(300, 200);
        selectedPane.setBorder(null);

        //states = new HashMap<String, Object>();

        select = new JButton("select -->");
        select.addActionListener(e -> {
            if (selected.get(courseList.getSelectedValue()) == null) {
                selected.put((String) courseList.getSelectedValue(), new Object());
                selectModel.addElement(courseList.getSelectedValue());
            }
            if (courseList.getSelectedValue() != null) {
                if (modifies.get(courseList.getSelectedValue()) == null) {
                    boolean[] state0 = new boolean[Course.list_code.get(courseList.getSelectedValue()).state];
                    for (int i = 0; i < state0.length; i++) state0[i] = true;
                    modifies.put((String) courseList.getSelectedValue(), new CourseModifyWindow(Course.list_code.get(courseList.getSelectedValue()), state0));

                    //		states.put((String)courseList.getSelectedValue(), state0);

                }

            }
        });

        hide = new JButton("Hide conflict courses");
        hide.addActionListener(e -> {
            DefaultListModel newCourseModel = new DefaultListModel();
            newCourseModel.clear();

            collected = new String[selectModel.getSize()];
            int courseNum = selectModel.getSize() + 1;

            Course[] listToProceed = new Course[courseNum];
            boolean[][] statesToProceed1 = new boolean[courseNum][];

            for (int i = 0; i < courseNum - 1; i++) {
                listToProceed[i] = Course.list_code.get((String) selectModel.elementAt(i));
                statesToProceed1[i] = modifies.get((String) selectModel.elementAt(i)).getLegalStates();
            }

            for (int i = 0; i < courseModel.getSize(); i++) {

                if (selected.get(courseModel.elementAt(i)) == null) {
                    System.out.println(i);
                    listToProceed[courseNum - 1] = Course.list_code.get(courseModel.elementAt(i));
                    statesToProceed1[courseNum - 1] = new boolean[Course.list_code.get(courseModel.elementAt(i)).state];
                    for (int j = 0; j < statesToProceed1[courseNum - 1].length; j++)
                        statesToProceed1[courseNum - 1][j] = true;
                    statesToProceed1[courseNum - 1] = new CourseModifyWindow(Course.list_code.get(courseModel.elementAt(i)), statesToProceed1[courseNum - 1]).getLegalStates();

                    if (Register.start(listToProceed, statesToProceed1, courseNum, null, null, 50, 1) == 1) {

                    } else {
                        courseModel.remove(i);
                        i--;
                    }

                } else {
                    courseModel.remove(i);
                    i--;
                }

            }

            coursePane.repaint();
        });

        //moveUp = new JButton("<-- move up");

        remove = new JButton("<-- remove");
        remove.addActionListener(e -> {
            if (selectedList.getSelectedValue() != null) {

                selected.remove(selectedList.getSelectedValue());
                selectModel.remove(selectedList.getSelectedIndex());

            }
        });

        clear = new JButton("clear");
        clear.addActionListener(e -> {
            for (int i = 0; i < selectModel.size(); i++) {
                selected.remove(selectModel.get(i));

            }
            selectModel.clear();
            selectedPane.repaint();
        });

        String[] solCount = new String[maxNum];
        for (int i = 0; i < maxNum; i++) solCount[i] = "" + (i + 1);
        solNum = new JComboBox(solCount);
        solNum.setSelectedIndex(defaultNum - 1);
        solNum.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Number of solutions displayed"));


        JButton next = new JButton("Next");
        next.addActionListener(e -> {

            Thread t = new Thread(new Runnable() {
                public void run() {
                    SolutionWindow s = new SolutionWindow();
                    s.setVisible(true);
                    s.setSize(800, 400);
                    s.setTitle(title + "- Solution");
                    collected = new String[selectModel.getSize()];
                    int solNumber = solNum.getSelectedIndex() + 1;
                    int courseNum = selectModel.getSize();
                    System.out.println(courseNum);
                    //	System.out.println("I'm here");
                    //System.out.println(ref.bc.length);
                    Course[] listToProceed = new Course[courseNum];
                    boolean[][] statesToProceed1 = new boolean[courseNum][];

                    for (int i = 0; i < courseNum; i++) {
                        listToProceed[i] = Course.list_code.get((String) selectModel.elementAt(i));
                        statesToProceed1[i] = modifies.get((String) selectModel.elementAt(i)).getLegalStates();
                    }


                    Register.start(listToProceed, statesToProceed1, courseNum, s, title, solNumber, 0);


                }
            });

            t.start();
        });


        //dummy
        cancel = new JButton("Cancel");
        cancel.addActionListener(e -> ref.setVisible(false));

        load = new JButton("Load profile");
        load.addActionListener(e -> {
            JFileChooser fc = new JFileChooser(File.separator + "prf");


            fc.setCurrentDirectory(new File("courseInfo.ser"));
            fc.showOpenDialog(null);
            File f = fc.getSelectedFile();

            try {
                ObjectInputStream ios = new ObjectInputStream(new FileInputStream(f));
                courses = (Course[]) ios.readObject();
                states0 = (boolean[][]) ios.readObject();
            /*	for (int j = 0; j<states0.length; j++)
                for (int i = 0; i<states0[j].length; i++)
                    if (states0[j][i] == false) System.out.println("course" + j + "  state# "+ i);
                */
            } catch (IOException ioe) {
                System.err.println("Error while trying to read the profile: IOException");
            } catch (ClassNotFoundException cnfe) {
                System.err.println("Error while trying to read the profile: ClassNotFound");
            }

            //ref.setVisible(false);
            //new TrialWindow(courses,states0,ref,ref.title).setVisible(true);
            for (int i = 0; i < courses.length; i++) {
                if (selected.get(courses[i].code) == null) {
                    selected.put(courses[i].code, new Object());
                    selectModel.addElement(courses[i].code);

                }

                modifies.put(courses[i].code, new CourseModifyWindow(Course.list_code.get(courses[i].code), states0[i]));
            }

        });

        details = new HashMap<>();
        //	specifies = new HashMap<String, CourseSpecifyWindow>();
        modifies = new HashMap<>();

        //dummy, not shown+
        detail = new JButton("Course detail");
        detail.addActionListener(e -> {
            Thread t = new Thread(new Runnable() {
                public void run() {
                    if (details.get((String) selectedList.getSelectedValue()) == null) {
                        details.put((String) selectedList.getSelectedValue(), new CourseDetailWindow(Course.list_code.get((String) selectedList.getSelectedValue())));
                    }
                    //details.get((String) list.getSelectedItem()).setSize(200,300);
                    details.get((String) selectedList.getSelectedValue()).setVisible(true);
                    details.get(selectedList.getSelectedValue()).setTitle((String) selectedList.getSelectedValue() + "-Detail");
                }
            });
            t.start();

        });


        specify = new JButton("Specify session");


        specify.addActionListener(e -> {
            Thread t = new Thread(() -> {
                modifies.get(selectedList.getSelectedValue()).setVisible(true);
                modifies.get(selectedList.getSelectedValue()).setTitle(selectedList.getSelectedValue() + "-Modify");

            });
            t.start();
        });

        JButton save = new JButton("Save profile");
        save.addActionListener(e -> {

            JFileChooser fc = new JFileChooser(File.separator + "prf");


            fc.setCurrentDirectory(new File("courseInfo.ser"));
            fc.showSaveDialog(null);
            File f = fc.getSelectedFile();

            try {
            /*
                boolean[][] states = new boolean[selectModel.getSize()][];

                for (int i = 0; i<courses.length; i++) {

                    states[i] = modifies.get(courses[i].code).getLegalStates();
                }

                */
                collected = new String[selectModel.getSize()];
                int courseNum = selectModel.getSize();
                System.out.println(courseNum);
                //	System.out.println("I'm here");
                //System.out.println(ref.bc.length);
                Course[] listToProceed = new Course[courseNum];
                boolean[][] statesToProceed1 = new boolean[courseNum][];

                for (int i = 0; i < courseNum; i++) {
                    listToProceed[i] = Course.list_code.get((String) selectModel.elementAt(i));
                    statesToProceed1[i] = modifies.get((String) selectModel.elementAt(i)).getLegalStates();
                }

                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
                oos.writeObject(listToProceed);
                oos.writeObject(statesToProceed1);
                oos.close();
            } catch (IOException ioe) {
                System.err.println("IOException while trying to save the profile!");
            }

        });


        info = new JLabel("Please select courses to create a profile\n or Load a profile");
        info.setFont(new Font("SansSerif", 1, 15));

        JPanel p6 = new JPanel();
        p6.setLayout(new BorderLayout());
        p6.add(next);
        p6.add(solNum, BorderLayout.SOUTH);

        p1 = new JPanel();
        p1.setLayout(new BorderLayout());
        p1.add(schoolPane);
        p1.add(enterSchool, BorderLayout.SOUTH);

        p2 = new JPanel();
        p2.setLayout(new BorderLayout());
        p2.add(deptPane);
        p2.add(enterDept, BorderLayout.SOUTH);

        p3 = new JPanel();
        p3.setLayout(new GridLayout(2, 1));
        p3.add(p1);
        p3.add(p2);


        p4 = new JPanel();
        p4.setLayout(new BorderLayout());
        p4.add(coursePane);
        p4.add(select, BorderLayout.SOUTH);
        p4.add(hide, BorderLayout.NORTH);


        p5 = new JPanel();
        p5.setLayout(new BorderLayout());
        p5.add(selectedPane);
        p5.add(remove, BorderLayout.SOUTH);
        p5.add(clear, BorderLayout.NORTH);

        p = new JPanel();
        p.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.gridheight = 4;
        p.add(p4, c);

        c.gridx = 2;
        c.gridy = 0;
        c.gridwidth = 2;
        c.gridheight = 4;
        p.add(p5, c);

        c.gridwidth = 1;
        c.gridheight = 1;
        c.gridx = 4;
        c.gridy = 0;

        p.add(detail, c);

        c.gridy = 1;
        p.add(specify, c);
        c.gridy = 2;
        p.add(p6, c);
        c.gridy = 3;

        p.add(save, c);

        p0 = new JPanel();
        p0.setLayout(new BorderLayout());

        p0.add(p3, BorderLayout.WEST);
        p0.add(load, BorderLayout.SOUTH);
        //	p0.add(info,BorderLayout.NORTH);
        p0.add(p);


        this.add(p0);

    }


}
