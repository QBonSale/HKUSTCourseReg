package _record;

public class test {


    public static void main(String[] args) throws Exception {

//		Downloader.downloadDept("test/COMP.html","https://w5.ab.ust.hk/wcq/cgi-bin/1230/subject/COMP");
        CourseReader.readFromURL("https://w5.ab.ust.hk/wcq/cgi-bin/1310/subject/COMP", 0, 0);
        String s = "<td>Th 11:30AM - 01:20PM</td><td>Rm 4221, Lift 19</td><td><a href=\"/wcq/cgi-bin/1310/instructor/SHEN, Helen Chi Man\">SHEN, Helen Chi Man</a></td><td align=\"center\">45</td><td align=\"center\">45</td><td align=\"center\"><strong>0</strong></td><td align=\"center\"><strong>3</strong></td><td align=\"center\">&nbsp;</td></tr><tr class=\"newsect sectodd\">";

//		System.out.println(s.matches("<td>.. ..:.... - ..:....</td>.*"));

//		System.out.println(s.substring(15));
        String[] token = s.split(" ");
        for (String aToken : token) {
            System.out.println(aToken);
        }
        String tmp = token[1] + token[2] + token[3];
        System.out.println(tmp.substring(0, 15));

        //	System.out.println(token[1].substring(15));
        //System.out.println(document.toString());

    }


    public static void testing(String s) {
        System.out.println(s.matches(".*\\d\\d\\d\\d."));
    }
}
