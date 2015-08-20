package _interface;


import _record.Course;

import javax.swing.*;


@SuppressWarnings("ALL")
public class CourseSpecifyWindow extends JFrame{
	
	JPanel[] p;

	JComboBox[] cb;
	
	JPanel p0;
	
	
	Course c;
	
	

	CourseSpecifyWindow(Course co) {
		
		p = new JPanel[3];
		c = co;
		cb = new JComboBox[3];
		
		for (int i = 0; i<3; i++)	p[i] = getPanel(c.sessions[i],i);
		
		p0 = new JPanel();
		p0.add(p[0]);
		p0.add(p[1]);
		p0.add(p[2]);
		
		
		this.add(p0);
		
		this.setSize(300,120);
		
	}
	
	
	JPanel getPanel(Course.Session[] s, int type) {
		JPanel p = new JPanel();


		String[] names = new String[c.type[type]];
		for (int i = 0; i<c.type[type]; i++) names[i] = s[i].name + "   ";
		
		cb[type] = new JComboBox(names);
		
		
		
		p.add(cb[type]);
		
		
		switch (type) {
		case Course.L: p.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"Lectures"));break;
		case Course.LA: p.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"Labs"));break;
		case Course.T: p.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"Tutorials"));break;
		};
		
		return p;
		
	}

	boolean[] getStates() {
		boolean[]  state = new boolean[c.state];
		
		int[] selected = new int[3];
		
		for (int i = 0; i<3; i++) selected[i] = cb[i].getSelectedIndex();
		
			
		int s;
		
		if (c.type[Course.L] == 0) {
			if (c.type[Course.LA] == 0) {
				s = selected[Course.T];
			}
			else {
				if (c.type[Course.T] == 0) {
					s = selected[Course.LA];
				}
				else {
					s = c.type[Course.T] * selected[Course.LA] + selected[Course.T];
				}
					
			}
		}
		else {
			if (c.type[Course.LA] == 0) {
				if (c.type[Course.T] == 0) {
					s = selected[Course.L];
				}
				else {
					s= c.type[Course.T] * selected[Course.L] + selected[Course.T];
				}
			}
			else {
				if (c.type[Course.T] == 0) {
					s = c.type[Course.LA] * selected[Course.L] + selected[Course.LA];
				}
				else {
					s = c.type[Course.LA]*c.type[Course.T] * selected[Course.L] + c.type[Course.T]*selected[Course.LA] + selected[Course.T];
				}
			}
		}
		

	for (int i = 0; i<c.state; i++) if (i!=s) state[i] = false; else state[i] = true;

		return state;
	}
	
}
