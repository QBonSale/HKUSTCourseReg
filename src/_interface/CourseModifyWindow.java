package _interface;

import _processor.Register;
import _record.Course;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("ALL")
public class CourseModifyWindow extends JFrame {

    public static final int tokenEachLine = 4;

    Course c;


    JCheckBox[][] boxes;

    JPanel pLecture, pLab, pTutorial;

    JPanel p, p0, p1;

    JScrollPane sp;

    JCheckBox match;

    JButton checkAll, unCheckAll, checkInvert;

    boolean[] s;


    CourseModifyWindow(Course C, boolean[] States) {
        c = C;
        s = States;
        boxes = new JCheckBox[3][];
        boxes[Course.L] = new JCheckBox[c.type[Course.L]];

        boxes[Course.LA] = new JCheckBox[c.type[Course.LA]];

        boxes[Course.T] = new JCheckBox[c.type[Course.T]];


        pLecture = getPanel(c.sessions[Course.L], boxes[Course.L], Course.L);
        pLab = getPanel(c.sessions[Course.LA], boxes[Course.LA], Course.LA);
        pTutorial = getPanel(c.sessions[Course.T], boxes[Course.T], Course.T);

        match = new JCheckBox("match");
        match.setSelected(c.match);


        checkAll = new JButton("Select All");
        checkAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (JCheckBox[] boxe : boxes)
                    for (int j = 0; j < boxe.length; j++)
                        boxe[j].setSelected(true);
            }
        });

        unCheckAll = new JButton("Select None");
        unCheckAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (JCheckBox[] boxe : boxes)
                    for (int j = 0; j < boxe.length; j++)
                        boxe[j].setSelected(false);
            }
        });


        checkInvert = new JButton("Select Invert");
        checkInvert.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (JCheckBox[] boxe : boxes)
                    for (int j = 0; j < boxe.length; j++)
                        if (boxe[j].isSelected()) boxe[j].setSelected(false);
                        else boxe[j].setSelected(true);
            }
        });

        p = new JPanel();
        p.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;

        if (pLecture != null) {
            p.add(pLecture, c);
            c.gridy++;
        }
        if (pLab != null) {
            p.add(pLab, c);
            c.gridy++;
        }
        if (pTutorial != null) {
            p.add(pTutorial, c);
            c.gridy++;
        }

        p0 = new JPanel();
        p0.setLayout(new BorderLayout());
        p0.add(p, BorderLayout.CENTER);


        p1 = new JPanel();

        p1.setLayout(new FlowLayout());
        p1.add(match);
        p1.add(checkAll);
        p1.add(unCheckAll);
        p1.add(checkInvert);

        p0.add(p1, BorderLayout.SOUTH);

        sp = new JScrollPane(p0);
        if (s != null) checkup();
        this.add(sp);
        this.setSize(400, 400);

    }

    void checkup() {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < boxes[i].length; j++) boxes[i][j].setSelected(false);

        System.out.println("The number of states" + c.state);
        for (int i = 0; i < s.length; i++) {
            if (s[i]) {
                int[] num = Register.getState(c, i);
                //System.out.println(num[0] + "    " + num[1] + "   " + num[2]);
                //	System.out.println(c.type[0] + "    " + c.type[1] + "    " + c.type[2]);

                for (int j = 0; j < 3; j++) {
                    if (num[j] != -1)
                        boxes[j][num[j]].setSelected(s[i]);
                }
            }
        }
    }

    JPanel getPanel(Course.Session[] s, JCheckBox[] box, int type) {
        if (box == null || box.length == 0 || s == null || s.length == 0) return null;
        JPanel p = new JPanel();
        p.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;


        for (int i = 0; i < box.length; i++) {
            box[i] = new JCheckBox(s[i].type + s[i].name);
            box[i].setSelected(true);
            p.add(box[i], c);
            c.gridx++;
            if (c.gridx == tokenEachLine) {
                c.gridx = 0;
                c.gridy++;
            }
        }
        switch (type) {
            case Course.L:
                p.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Lectures"));
                break;
            case Course.LA:
                p.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Labs"));
                break;
            case Course.T:
                p.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Tutorials"));
                break;
        }

        return p;
    }

    boolean[] getLegalStates() {
        boolean[] states = new boolean[c.state];

        for (int i = 0; i < c.state; i++) {
            int[] s = Register.getState(c, i);
            //System.out.println("state # " + i + ": L" + s[0] + "LA" + s[1] + "T" + s[2] );
            if (allSelected(s) && ((!match.isSelected()) || allMatch(s))) {
                states[i] = true;
                //System.out.println("state # " + i + "is legal");
            } else states[i] = false;

        }

        return states;
    }

    boolean allSelected(int[] s) {
        boolean flag = true;
        if (s[Course.L] != -1) flag = (flag && boxes[Course.L][s[Course.L]].isSelected());
        if (s[Course.LA] != -1) flag = (flag && boxes[Course.LA][s[Course.LA]].isSelected());
        if (s[Course.T] != -1) flag = (flag && boxes[Course.T][s[Course.T]].isSelected());
        return flag;
    }

    boolean allMatch(int[] s) {
        //	c.print();
        if (c.type[Course.L] == 1) return true;
        if (s[Course.L] == -1) return true;
        boolean flag = true;
        if (s[Course.LA] != -1)
            flag = (flag && Course.match(c.sessions[Course.L][s[Course.L]], c.sessions[Course.LA][s[Course.LA]]));
        if (s[Course.T] != -1)
            flag = (flag && Course.match(c.sessions[Course.L][s[Course.L]], c.sessions[Course.T][s[Course.T]]));
        return flag;
    }

}
