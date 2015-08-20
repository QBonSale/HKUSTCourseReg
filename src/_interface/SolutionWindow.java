package _interface;


import _processor.Register;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class SolutionWindow extends JFrame {

    public static final int COLUMN = 4;


    Solution nextSo;

    JList solutionList;
    DefaultListModel solutionModel;
    JScrollPane solutionPane;

    JButton detail;
    JButton graphical;
    JButton image;
    JButton prev;
    JButton next;

    JButton toNew;
    JButton toOld;

    JPanel p0;
    JPanel p;

    JPanel completeP, completeP0;

    JScrollPane sp;

    GridBagConstraints c0;
    SolutionWindow ref = this;

    Solution[] sols;
    int solCount;

    HashMap<JButton, Solution> solutions;

    SolutionWindow() {
        solutions = new HashMap<>();

        sols = new Solution[Register.max];
        solCount = 0;

        solutionModel = new DefaultListModel();
        solutionList = new JList(solutionModel);
        solutionList.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Solution List"));

        solutionPane = new JScrollPane(solutionList);
        solutionPane.setSize(100, 300);

        detail = new JButton("detail");
        detail.addActionListener(e -> {
            if (solutionList.getSelectedValue() != null) {
                Thread t = new Thread(new Runnable() {
                    public void run() {
                        ((Solution) solutionList.getSelectedValue()).detail();
                    }
                });
                t.start();
            }
        });
        /*
        graphical = new JButton("timetable");
		graphical.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (solutionList.getSelectedValue() != null) {
					Thread t = new Thread(new Runnable() {
						public void run() {
							((Solution) solutionList.getSelectedValue()).graphic();
						}
					});
					t.start();
				}
			}
		});
		*/
        image = new JButton("timetable");
        image.addActionListener(e -> {
            if (solutionList.getSelectedValue() != null) {
                Thread t = new Thread(new Runnable() {
                    public void run() {
                        ((Solution) solutionList.getSelectedValue()).image();
                    }
                });
                t.start();
            }
        });

        prev = new JButton("prev");
        prev.addActionListener(e -> {
            if (solutionList.getSelectedValue() != null && solutionList.getSelectedIndex() != 0) {
                Thread t = new Thread(() -> {
                    int selected = solutionList.getSelectedIndex();
                    if (((Solution) solutionList.getSelectedValue()).detailEnabled()) {
                        ((Solution) solutionList.getSelectedValue()).detail_close();
                        ((Solution) solutionModel.getElementAt(selected - 1)).detail();
                    }

                    if (((Solution) solutionList.getSelectedValue()).graphicEnabled()) {
                        ((Solution) solutionList.getSelectedValue()).graphic_close();
                        ((Solution) solutionModel.getElementAt(selected - 1)).graphic();
                    }


                    solutionList.clearSelection();
                    solutionList.setSelectedIndex(selected - 1);


                });
                t.start();
            }
        });

        next = new JButton("next");
        next.addActionListener(e -> {
            if (solutionList.getSelectedValue() != null && solutionList.getSelectedIndex() != solutionModel.getSize() - 1) {
                Thread t = new Thread(new Runnable() {
                    public void run() {
                        int selected = solutionList.getSelectedIndex();
                        if (((Solution) solutionList.getSelectedValue()).detailEnabled()) {
                            ((Solution) solutionList.getSelectedValue()).detail_close();
                            ((Solution) solutionModel.getElementAt(selected + 1)).detail();
                        }

                        if (((Solution) solutionList.getSelectedValue()).graphicEnabled()) {
                            ((Solution) solutionList.getSelectedValue()).graphic_close();
                            ((Solution) solutionModel.getElementAt(selected + 1)).graphic();
                        }


                        solutionList.clearSelection();
                        solutionList.setSelectedIndex(selected + 1);


                    }
                });
                t.start();
            }
        });
        p = new JPanel();
        p.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.gridheight = 5;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        p.add(solutionPane, c);
        c.gridx = 2;
        c.gridheight = 1;
        c.gridwidth = 1;
        p.add(detail, c);
    /*	c.gridy++;
		p.add(graphical, c);
	*/
        c.gridy++;
        p.add(image, c);
        c.gridy++;
        p.add(prev, c);
        c.gridy++;
        p.add(next, c);


        toNew = new JButton("thumbnail view");

        //p.add(solutionPane);
        //	p.add(box, BorderLayout.EAST);

        //	this.add(p);

        completeP = new JPanel();
        completeP.setLayout(new BorderLayout());
        completeP.add(p);
        completeP.add(toNew, BorderLayout.SOUTH);


        toNew.addActionListener(e -> {
            ref.remove(completeP);
            ref.add(completeP0);
            //ref.setSize(ref.getSize());
            ref.repaint();
            ref.setSize(800, 400);
            ref.setVisible(true);

        });
        toOld = new JButton("traditional view");


        p0 = new JPanel();
        p0.setLayout(new GridBagLayout());
        c0 = new GridBagConstraints();

        c0.gridx = 0;
        c0.gridy = 0;
        c0.fill = GridBagConstraints.NONE;

        c0.insets = new Insets(2, 5, 2, 5);

        sp = new JScrollPane(p0);

        completeP0 = new JPanel();
        completeP0.setLayout(new BorderLayout());
        completeP0.add(toOld, BorderLayout.NORTH);
        completeP0.add(sp);

        toOld.addActionListener(e -> {
            ref.remove(completeP0);
            ref.add(completeP);
            //ref.setSize(ref.getSize());
            //ref.repaint();
            ref.setSize(400, 400);
            ref.setVisible(true);
        });

        //	this.add(sp);

        this.add(completeP0);

    }


    public void addSolution(Solution s) {


        JButton button = new JButton(s.view);
        if (String.valueOf(s.solNum).length() == 1) button.setText("0" + String.valueOf(s.solNum));
        else button.setText(String.valueOf(s.solNum));

        solutionModel.addElement(s);

        button.addActionListener(new ButtonListener(s));


        p0.add(button, c0);

        c0.gridx++;
        if (c0.gridx == COLUMN) {
            c0.gridx = 0;
            c0.gridy++;
        }
        ref.setVisible(true);
        //ref.setSize(ref.getSize());
        //ref.repaint();

    }

    class ButtonListener implements ActionListener {
        Solution s;

        ButtonListener(Solution sol) {
            s = sol;
        }

        public void actionPerformed(ActionEvent e) {
            s.image();
        }
    }


}
