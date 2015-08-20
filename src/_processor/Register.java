package _processor;

import _interface.Solution;
import _interface.SolutionWindow;
import _record.Course;

import javax.swing.*;

@SuppressWarnings("ALL")
public class Register {
    Course[] list;
    int courseNumber;
    int[] state;
    int[] starts;
    int[] ends;
    boolean[][] legal;
    boolean[] suc;
    int slotNumber;
    SolutionWindow s;
    int solutionCount = 0;
    int flag = 0;

    int failstate;

    public static int max = 50;

    boolean abort;

    String taskTitle;

    public Register(Course[] selected, boolean[][] proceed, int num, SolutionWindow so, String title, int f) {
        list = selected;
        courseNumber = num;
        legal = proceed;
        s = so;
        abort = false;
        taskTitle = title;
        failstate = -1;
        flag = f;
    }

    public static int start(Course[] selected, boolean[][] legal, int num, SolutionWindow s, String title, int solNumber, int flag) {
        Register r = new Register(selected, legal, num, s, title, flag);
        r.starts = new int[3 * Course.MAX_SLOT * num];
        r.ends = new int[3 * Course.MAX_SLOT * num];

        max = solNumber;

        r.state = new int[num];
        r.suc = new boolean[num];
        int result = r.solve();
        if (flag == 1) {
            return result;
        } else {

            r.end();
            return 0;
        }
        //JOptionPane.showMessageDialog(null, "Computation completed");

    }

    void end() {
        if (solutionCount == 0) {
            String msg = "No solution\r\n";
            for (int i = 0; i < failstate; i++)
                msg = msg + list[i].code + "  ";
            msg = msg + "arranged but fail at course  ";
            msg = msg + list[failstate].code;
            JOptionPane.showMessageDialog(null, msg);
        }

    }

    int solve() {
        slotNumber = 0;
        for (int i = 0; i < courseNumber; i++) state[i] = -1;
        int i = 0;
        while (i >= 0 && !abort) {
            if (i == courseNumber) {
                //record();
                if (flag == 1) return 1;
                else print();
                i--;
            } else {
                if (state[i] != -1 && suc[i]) {
                    remove(i);
                }
                state[i]++;

                if (state[i] >= list[i].state) {
                    state[i] = -1;
                    suc[i] = false;
                    if (i > failstate) failstate = i;
                    i--;
                } else {
                    if (legal[i][state[i]] && put(i)) {
                        suc[i] = true;
                        i++;
                    } else {
                        //	System.out.println("fail at"+ i + "state" + state[i]);
                        suc[i] = false;
                    }
                }
            }
        }
        return -1;
    }

    public static int[] getState(Course c, int stateInd) {
        int[] s = new int[3];
        if (c.type[Course.L] == 0) {
            s[Course.L] = -1;
            if (c.type[Course.LA] == 0) {
                s[Course.LA] = -1;
                s[Course.T] = stateInd;

            } else {
                if (c.type[Course.T] == 0) {
                    s[Course.T] = -1;
                    s[Course.LA] = stateInd;

                } else {
                    s[Course.LA] = stateInd / c.type[Course.T];
                    s[Course.T] = stateInd % c.type[Course.T];

                }
            }
        } else {
            if (c.type[Course.LA] == 0) {
                s[Course.LA] = -1;
                if (c.type[Course.T] == 0) {
                    s[Course.T] = -1;
                    s[Course.L] = stateInd;

                } else {
                    s[Course.L] = stateInd / c.type[Course.T];
                    s[Course.T] = stateInd % c.type[Course.T];
                }
            } else {
                if (c.type[Course.T] == 0) {
                    s[Course.T] = -1;
                    s[Course.L] = stateInd / c.type[Course.LA];
                    s[Course.LA] = stateInd % c.type[Course.LA];
                } else {
                    s[Course.L] = stateInd / c.type[Course.LA] / c.type[Course.T];
                    s[Course.LA] = (stateInd % (c.type[Course.LA] * c.type[Course.T])) / c.type[Course.T];
                    s[Course.T] = (stateInd % (c.type[Course.LA] * c.type[Course.T])) % c.type[Course.T];
                }
            }

        }
        return s;
    }

    boolean put(int k) {
        int[] s = getState(list[k], state[k]);


        if (s[Course.L] == -1 || notOccupiedSession(list[k].sessions[Course.L][s[Course.L]])) {
            if (s[Course.L] != -1) selectSession(list[k].sessions[Course.L][s[Course.L]]);

            if (s[Course.LA] == -1 || notOccupiedSession(list[k].sessions[Course.LA][s[Course.LA]])) {
                if (s[Course.LA] != -1) selectSession(list[k].sessions[Course.LA][s[Course.LA]]);

                if (s[Course.T] == -1 || notOccupiedSession(list[k].sessions[Course.T][s[Course.T]])) {
                    if (s[Course.T] != -1) selectSession(list[k].sessions[Course.T][s[Course.T]]);
                    return true;
                } else {
                    if (s[Course.LA] != -1) cleanSession(list[k].sessions[Course.LA][s[Course.LA]]);
                    if (s[Course.L] != -1) cleanSession(list[k].sessions[Course.L][s[Course.L]]);
                    return false;
                }
            } else {
                if (s[Course.L] != -1) cleanSession(list[k].sessions[Course.L][s[Course.L]]);
                return false;
            }

        } else return false;

		
		
		/*


		if ( (s[Course.L]==-1 || notOccupiedSession(list[k].sessions[Course.L][s[Course.L]])) 
				&& ((s[Course.T]==-1) || notOccupiedSession(list[k].sessions[Course.T][s[Course.T]]))
			&& ((s[Course.LA] == -1) || notOccupiedSession(list[k].sessions[Course.LA][s[Course.LA]]))
			) {
			if (s[Course.L] != -1) selectSession(list[k].sessions[Course.L][s[Course.L]]);
			if (s[Course.LA] != -1) selectSession(list[k].sessions[Course.LA][s[Course.LA]]);
			if (s[Course.T] != -1) selectSession(list[k].sessions[Course.T][s[Course.T]]);
			return true;
			}
		else return false;
		*/
    }

    void selectSession(Course.Session s) {
        for (int i = 0; i < s.num; i++) selectSlot(s.slot[i]);
        qsort(0, slotNumber - 1, starts);
        qsort(0, slotNumber - 1, ends);
    }

    void selectSlot(Course.TimeSlot s) {
        starts[slotNumber] = s.start;
        ends[slotNumber] = s.end;
        slotNumber++;
    }

    boolean notOccupiedSession(Course.Session s) {
        boolean flag = true;
        for (int i = 0; i < s.num; i++) flag = flag && notOccupiedSlot(s.slot[i]);
        return flag;
    }

    boolean notOccupiedSlot(Course.TimeSlot s) {
        int i = 0;

        while (i < slotNumber && s.start > starts[i]) i++;
        if (slotNumber == 0) return true;
        if (i == 0)
            if (s.start == starts[0]) {
                return false;
            } else if (s.end <= starts[0]) return true;
            else return false;
        if (i == slotNumber) {
            if (s.start >= ends[i - 1])
                return true;
            else return false;
        }
        if (s.start == starts[i]) return false;
        else {

            if (s.start >= ends[i - 1] && s.end <= starts[i]) {
                return true;
            } else return false;
        }

    }

    void remove(int k) {

        int[] s = getState(list[k], state[k]);
        if (s[Course.L] != -1) cleanSession(list[k].sessions[Course.L][s[Course.L]]);
        if (s[Course.LA] != -1) cleanSession(list[k].sessions[Course.LA][s[Course.LA]]);
        if (s[Course.T] != -1) cleanSession(list[k].sessions[Course.T][s[Course.T]]);

    }

    boolean cleanSession(Course.Session s) {
        boolean flag = true;
        for (int i = 0; i < s.num; i++) flag = flag && cleanSlot(s.slot[i]);
        return flag;
    }

    boolean cleanSlot(Course.TimeSlot s) {
        int i = 0;
        while (i < slotNumber && starts[i] != s.start) i++;
        if (i == slotNumber || ends[i] != s.end) return false;
        else {
            while (i < slotNumber - 1) {
                starts[i] = starts[i + 1];
                ends[i] = ends[i + 1];
                i++;
            }
            slotNumber--;
            qsort(0, slotNumber - 1, starts);
            qsort(0, slotNumber - 1, ends);
            return true;

        }
    }

    public static void qsort(int s, int t, int[] array) {
        if (s < t) {
            int i = s;
            int j = t;
            int temp;
            while (i < j) {
                while ((i < j) && (array[i] < array[j])) i++;
                if (i < j) {
                    temp = array[i];
                    array[i] = array[j];
                    array[j] = temp;
                    j--;
                }
                while ((i < j) && (array[i] < array[j])) j--;
                if (i < j) {
                    temp = array[i];
                    array[i] = array[j];
                    array[j] = temp;
                    i++;
                }
            }
            qsort(s, i - 1, array);
            qsort(i + 1, t, array);
        }
    }

    void print() {
        solutionCount++;
        s.addSolution(new Solution(list, state, courseNumber, solutionCount, taskTitle));
        s.setVisible(true);
        if (solutionCount == max) {

            JOptionPane.showMessageDialog(null, "The number of solution is exceeding " + String.valueOf(max) + "\n Would you please add more restrictions such as unselecting some sessions?");
            abort = true;
            // JFrame temp = new JFrame();
            // temp.setSize(s.getSize());
            // s.setSize(200,200);
            // s.setSize(temp.getSize());

            // s.repaint();
        /*	 try {
				 wait();
			 }
			 catch(InterruptedException ie) {
				 
			 }*/
        }
    }


}
