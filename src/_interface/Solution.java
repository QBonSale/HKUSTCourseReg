package _interface;

import _processor.Register;
import _record.Course;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;


public class Solution {

    public static final String[] WEEKDAY = new String[5];

    public static final int ICON_WIDTH = 100;
    public static final int ICON_HEIGHT = 100;

    JFrame detail;
    JPanel[] courses;


    JFrame graphic;

    String display;
    Course[] list;
    int[] states;
    int courseNum;

    int solNum;

    JLabel[] slots;
    JLabel[] days;

    JButton save;

    public SolutionImagePanel sip;

    HashMap<Course.TimeSlot, JButton> buttons;
    JButton[] buttonsGroup;

    SolutionImage si;
    ImageIcon view;


    JFrame imageFrame;

    String taskTitle;

    static {
        WEEKDAY[0] = "Mon";
        WEEKDAY[1] = "Tue";
        WEEKDAY[2] = "Wed";
        WEEKDAY[3] = "Thu";
        WEEKDAY[4] = "Fri";
    }


    public String toString() {
        return display;
    }

    public Solution(Course[] List, int[] States, int CourseNum, int num, String title) {
        list = List;
        states = States;
        courseNum = CourseNum;
        solNum = num;
        courses = new JPanel[courseNum];

        taskTitle = title;


        si = new SolutionImage();

        view = new ImageIcon(Solution.zoomImage(si.full, ICON_WIDTH, ICON_HEIGHT));

        display = "Solution" + String.valueOf(solNum) + taskTitle;

        prepareDetail();

        graphic = new GraphicFrame();

        save = new JButton("save");

        int[] s = Register.getState(list[0], states[0]);


        save.addActionListener(e -> {
            JFileChooser fc = new JFileChooser(File.separator + "png");

            fc.showSaveDialog(null);
            File f = fc.getSelectedFile();
            try {
                if (f != null) {
                    if (!f.getName().toLowerCase().endsWith("jpg") && !f.getName().toLowerCase().endsWith("png") && !f.getName().toLowerCase().endsWith("bmp"))
                        f = new File(f.getAbsolutePath().concat(".png"));//.setName(f.getName()+ "bmp");
                    ImageIO.write(si.full, "png", f);
                }
            } catch (IOException ie) {

            }
        });

        imageFrame = new JFrame();
        imageFrame.setLayout(new BorderLayout());

        sip = new SolutionImagePanel();
        imageFrame.add(sip);

        imageFrame.add(save, BorderLayout.SOUTH);
        imageFrame.setTitle(display);
        imageFrame.setSize(SolutionImage.FULL_WIDTH + 20, SolutionImage.FULL_HEIGHT + 100);

    }

    public Solution() {
        JFrame f = new JFrame();
        f.setSize(SolutionImage.FULL_WIDTH + 10, SolutionImage.FULL_HEIGHT + 10);
        f.add(new SolutionImagePanel());
        f.setVisible(true);
    }

    void prepareDetail() {
        detail = new JFrame();
        detail.setTitle(display);
        Box box = Box.createVerticalBox();

        for (int i = 0; i < courseNum; i++) {
            courses[i] = getPanel(i);

            courses[i].setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), list[i].code + "--" + list[i].name));
            box.add(courses[i]);
            box.add(Box.createVerticalStrut(10));
        }

        detail.add(box);
    }

    JPanel getPanel(int index) {
        JPanel p = new JPanel();
        int[] s = Register.getState(list[index], states[index]);
        p.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        if (s[Course.L] != -1) {
            p.add(new JLabel(list[index].sessions[Course.L][s[Course.L]].type + list[index].sessions[Course.L][s[Course.L]].name), c);
            c.gridx++;
        }

        if (s[Course.LA] != -1) {
            p.add(new JLabel(list[index].sessions[Course.LA][s[Course.LA]].type + list[index].sessions[Course.LA][s[Course.LA]].name), c);
            c.gridx++;
        }

        if (s[Course.T] != -1) {
            p.add(new JLabel(list[index].sessions[Course.T][s[Course.T]].type + list[index].sessions[Course.T][s[Course.T]].name), c);
            c.gridx++;
        }

        return p;
    }

    void detail() {
        detail.setVisible(true);
        detail.setSize(400, 400);
        detail.setLocation(800, 200);
    }

    void detail_close() {
        detail.setVisible(false);
    }


    boolean detailEnabled() {
        return detail.isVisible();
    }


    void graphic() {
        graphic.setVisible(true);
        graphic.setSize(800, 600);
        //graphic.setResizable(false);
        graphic.setLocation(0, 200);
    }


    void graphic_close() {
        graphic.setVisible(false);
    }

    boolean graphicEnabled() {
        return graphic.isVisible();
    }

    void image() {

        imageFrame.setVisible(true);
        imageFrame.setLocation(0, 200);
        imageFrame.setTitle("Solution " + String.valueOf(solNum) + " -" + taskTitle);

        imageFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                image_close();
            }
        });

        imageFrame.setVisible(true);

    }

    void image_close() {
        imageFrame.setVisible(false);
        //	imageFrame = null;
        //	si.full = null;
        //	si = null;
    }

    boolean imageEnabled() {
        return imageFrame.isVisible();
    }

    class GraphicFrame extends JFrame {
        JLabel[] slots;
        JLabel[] days;
        Color color;


        GraphicFrame() {
            generateLabels();

            buttons = new HashMap<>();

            GridBagLayout gbl = new GridBagLayout();

            setLayout(gbl);

            GridBagConstraints c = new GridBagConstraints();

            c.gridy = 1;
            c.gridx = 0;
            c.fill = GridBagConstraints.BOTH;
            c.weightx = 1;
            c.weighty = 1;
            for (int i = 0; i < 28; i++) {
                add(slots[i], c);
                c.gridy++;

            }

            c.gridy = 0;
            c.gridx = 1;

            for (int i = 0; i < 5; i++) {
                add(days[i], c);
                c.gridx++;
            }

            color = new Color(255, 255, 255);


            for (int i = 0; i < courseNum; i++)


            {
                int[] s = Register.getState(list[i], states[i]);
                String display;
                if (s[Course.L] != -1) {
                    display = list[i].code + "\r\n" + list[i].sessions[Course.L][s[Course.L]].type + list[i].sessions[Course.L][s[Course.L]].name;
                    for (int j = 0; j < list[i].sessions[Course.L][s[Course.L]].num; j++) {
                        putSlot(list[i].sessions[Course.L][s[Course.L]].slot[j], display);
                    }
                }


                if (s[Course.LA] != -1) {
                    display = list[i].code + "\r\n" + list[i].sessions[Course.LA][s[Course.LA]].type + list[i].sessions[Course.LA][s[Course.LA]].name;
                    for (int j = 0; j < list[i].sessions[Course.LA][s[Course.LA]].num; j++) {
                        putSlot(list[i].sessions[Course.LA][s[Course.LA]].slot[j], display);
                    }
                }

                if (s[Course.T] != -1) {
                    display = list[i].code + "\r\n" + list[i].sessions[Course.T][s[Course.T]].type + list[i].sessions[Course.T][s[Course.T]].name;
                    for (int j = 0; j < list[i].sessions[Course.T][s[Course.T]].num; j++) {
                        putSlot(list[i].sessions[Course.T][s[Course.T]].slot[j], display);
                    }
                }

                //color = new Color(color.getRGB() - 100);
                int r = (color.getRed() - 10 * (i + 1) % 3);
                if (r < 0) r += 256;
                int g = (color.getGreen() - 10 * (i % 3));
                if (g < 0) g += 256;
                int b = (color.getBlue() - 10 * (3 - (i % 3)));
                if (b < 0) b += 256;

                color = new Color(r, g, b);
            }
            buttonsGroup = buttons.values().toArray(new JButton[1]);

        }

        void generateLabels() {
            slots = new JLabel[30];

            for (int i = 0; i < 30; i++) {
                slots[i] = new JLabel(getTimeSlot(i), JLabel.CENTER);
            }
            days = new JLabel[5];
            days[0] = new JLabel("Mon", JLabel.CENTER);
            days[1] = new JLabel("Tue", JLabel.CENTER);
            days[2] = new JLabel("Wed", JLabel.CENTER);
            days[3] = new JLabel("Thu", JLabel.CENTER);
            days[4] = new JLabel("Fri", JLabel.CENTER);

        }

        void putSlot(Course.TimeSlot s, String display) {
            GridBagConstraints c = new GridBagConstraints();

            c.gridx = s.start / 30 + 1;
            c.gridy = s.start % 30 + 1;
            c.weightx = 1;
            c.weighty = 1;
            c.fill = GridBagConstraints.BOTH;
            c.gridheight = (s.end - s.start);

            JButton b = new JButton(display);

            b.setBackground(color);

            buttons.put(s, b);
            add(b, c);

        }


        String getTimeSlot(int i) {
            String s;
            String s0 = String.valueOf(i / 2 + Course.START_H);
            if (s0.length() == 1) s0 = "0" + s0;
            s = s0;
            if (i % 2 == 0) {
                s += ":00 - " + s0 + ":20";
            } else {
                s += ":30 - " + s0 + ":50";
            }
            return s;
        }

        public void paint(Graphics g) {
            super.paint(g);

            g.setColor(Color.BLUE);


            for (JButton aButtonsGroup1 : buttonsGroup) {
                g.drawLine(0, aButtonsGroup1.getBounds().y + 25, this.getWidth(), aButtonsGroup1.getBounds().y + 25);
                g.drawLine(0, aButtonsGroup1.getBounds().y + aButtonsGroup1.getBounds().height + 25, this.getWidth(), aButtonsGroup1.getBounds().y + aButtonsGroup1.getBounds().height + 25);

            }
            for (JButton aButtonsGroup : buttonsGroup) {
                aButtonsGroup.repaint();


            }

        }
    }


    class SolutionImagePanel extends JPanel {
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(si.full, 10, 10, si.full.getWidth(), si.full.getHeight(), this);
        }
    }

    class SolutionImage {
        BufferedImage full;
        //	BufferedImage zoomed;


        static final int BLOCK_WIDTH = 80;
        static final int BLOCK_HEIGHT = 20;

        static final int FULL_WIDTH = 6 * BLOCK_WIDTH + 1;
        static final int FULL_HEIGHT = 30 * BLOCK_HEIGHT;


        SolutionImage() {
            full = new BufferedImage(FULL_WIDTH, FULL_HEIGHT, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = full.createGraphics();

            full = g2d.getDeviceConfiguration().createCompatibleImage(FULL_WIDTH, FULL_HEIGHT, Transparency.TRANSLUCENT);
            g2d.dispose();
            g2d = full.createGraphics();


            g2d.setColor(Color.BLACK);

            for (int i = 0; i < 5; i++) {
                drawStringOnBlock(g2d, WEEKDAY[i], 0, i + 1, 0, i + 1);
            }
            String s_1 = "00";
            String s_2 = "30";
            String e_1 = "20";
            String e_2 = "50";

            for (int i = 0; i < 30; i++) {
                String s = String.valueOf(i / 2 + 8);
                if (s.length() == 1) s = "0" + s;

                if (i % 2 == 0) s = s + ":" + s_1 + " - " + s + ":" + e_1;
                else s = s + ":" + s_2 + " - " + s + ":" + e_2;

                drawStringOnBlock(g2d, s, i + 1, 0, i + 1, 0);
            }

            Stroke temp = g2d.getStroke();
            float[] dashPattern = {5, 3};
            g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10, dashPattern, 0));
            for (int i = 0; i < 30; i++) {
                g2d.drawLine(0, (i + 1) * BLOCK_HEIGHT, FULL_WIDTH, (i + 1) * BLOCK_HEIGHT);
            }
            g2d.setStroke(temp);


            for (int i = 0; i < courseNum; i++) {
                g2d.setColor(getColor(i));
                //		System.out.println(g2d.getColor());
                int[] s = Register.getState(list[i], states[i]);
                String display;
                if (s[Course.L] != -1) {
                    display = list[i].code + "|" + list[i].sessions[Course.L][s[Course.L]].type + list[i].sessions[Course.L][s[Course.L]].name;
                    for (int j = 0; j < list[i].sessions[Course.L][s[Course.L]].num; j++) {
                        putSlot(list[i].sessions[Course.L][s[Course.L]].slot[j], display, g2d);
                    }
                }


                if (s[Course.LA] != -1) {
                    display = list[i].code + "|" + list[i].sessions[Course.LA][s[Course.LA]].type + list[i].sessions[Course.LA][s[Course.LA]].name;
                    for (int j = 0; j < list[i].sessions[Course.LA][s[Course.LA]].num; j++) {
                        putSlot(list[i].sessions[Course.LA][s[Course.LA]].slot[j], display, g2d);
                    }
                }

                if (s[Course.T] != -1) {
                    display = list[i].code + "|" + list[i].sessions[Course.T][s[Course.T]].type + list[i].sessions[Course.T][s[Course.T]].name;
                    for (int j = 0; j < list[i].sessions[Course.T][s[Course.T]].num; j++) {
                        putSlot(list[i].sessions[Course.T][s[Course.T]].slot[j], display, g2d);
                    }
                }
            }
        }

        Color getColor(int k) {
            int r, g, b;
            r = 0;
            g = 0;
            b = 0;
            if (k % 3 == 0) {
                r = 192;
                g = 255 - k / 3 * 20;
                b = 255 - k / 3 * 20;
            }

            if (k % 3 == 1) {
                g = 192;
                r = 255 - k / 3 * 30;
                b = 255 - k / 3 * 30;
            }

            if (k % 3 == 2) {
                b = 192;
                r = 255 - k / 3 * 30;
                g = 255 - k / 3 * 30;
            }

            return new Color(r, g, b);
        }

        void putSlot(Course.TimeSlot s, String mes, Graphics2D g) {
            int startY = s.start / 30 + 1;
            int endY = startY;

            int startX = s.start % 30 + 1;
            int endX = s.end % 30;

            fillBlock(g, startX, startY, endX, endY);
            Color temp = g.getColor();
            g.setColor(Color.black);
            drawStringOnBlock(g, mes, startX, startY, endX, endY);
            g.setColor(temp);
        }


        void fillBlock(Graphics2D g, int startX, int startY, int endX, int endY) {
            Color temp = g.getColor();
            g.setColor(Color.BLACK);

            //Graphics2D g1 = view.createGraphics();


            g.drawRect(startY * BLOCK_WIDTH, startX * BLOCK_HEIGHT, (endY + 1 - startY) * BLOCK_WIDTH, (endX - startX + 1) * BLOCK_HEIGHT);

            g.setColor(temp);
            g.fillRect(startY * BLOCK_WIDTH + 1, startX * BLOCK_HEIGHT + 1, (endY + 1 - startY) * BLOCK_WIDTH - 1, (endX - startX + 1) * BLOCK_HEIGHT - 1);
        }


        void drawStringOnBlock(Graphics2D g, String mes, int startX, int startY, int endX, int endY) {

            FontMetrics fm = g.getFontMetrics();
            int i = 0;

            while (i < mes.length() && mes.charAt(i) != '|') i++;

            if (i < mes.length()) {


                String temp = mes;
                mes = temp.substring(0, i);

                int txt_width = fm.stringWidth(mes);
                int ascent = fm.getMaxAscent();
                int descent = fm.getMaxDescent();

                int txt_x = startY * BLOCK_WIDTH + ((endY + 1 - startY) * BLOCK_WIDTH - txt_width) / 2;
                int txt_y = startX * BLOCK_HEIGHT + (endX + 1 - startX) * BLOCK_HEIGHT / 2 - fm.getDescent();

                g.drawString(mes, txt_x, txt_y);

                mes = temp.substring(i + 1, temp.length());

                txt_width = fm.stringWidth(mes);
                txt_x = startY * BLOCK_WIDTH + ((endY + 1 - startY) * BLOCK_WIDTH - txt_width) / 2;
                txt_y = txt_y + fm.getDescent() + fm.getAscent();
                g.drawString(mes, txt_x, txt_y);
            } else {
                int txt_width = fm.stringWidth(mes);
                int ascent = fm.getMaxAscent();
                int descent = fm.getMaxDescent();

                int txt_x = startY * BLOCK_WIDTH + ((endY + 1 - startY) * BLOCK_WIDTH - txt_width) / 2;
                int txt_y = startX * BLOCK_HEIGHT + (endX + 1 - startX) * BLOCK_HEIGHT / 2 - descent / 2 + ascent / 2;

                g.drawString(mes, txt_x, txt_y);

            }

        }


    }

    public static Image zoomImage(BufferedImage image, int width, int height) {
        //int tX = SolutionImage.FULL_WIDTH;
        //int tY = SolutionImage.FULL_HEIGHT;

        //	int[] rgb = new int [tX*tY];
        //	image.getRGB(rgb, 0, tX, 0, 0, tX, tY);

        return image.getScaledInstance(width, height, Image.SCALE_DEFAULT);
    }


}
