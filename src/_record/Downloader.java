package _record;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

public class Downloader {
    static String baseURL = "https://w5.ab.ust.hk/wcq/cgi-bin/1510/subject/";
    static String baseSave = "Fall2015";

    public static void download() {
        String savePlace;
        String urlAddress;
		for (int i = 0; i<FileLocater.SCHOOL_NUM; i++)
			for (int j = 0; j<FileLocater.list[i].deptNum; j++){
				savePlace = baseSave + "/" + FileLocater.list[i].name + "/" + FileLocater.list[i].dept[j] + ".html"; 
				urlAddress = baseURL + "/" + FileLocater.list[i].dept[j];
				downloadDept(savePlace, urlAddress);
			}
			
	}
	
	public static void downloadDept(String savePlace, String urlAddress){
		
		 try 
         {

             URL u = new URL(urlAddress);
             InputStreamReader in =  new InputStreamReader(u.openStream());

             //in = new BufferedInputStream(in);

             BufferedReader r = new BufferedReader(in);
             File f = new File(savePlace);
             if (!f.exists())
             f.createNewFile();
             BufferedWriter output = new BufferedWriter(new FileWriter(f));
             String c;
             
             while ((c = r.readLine( )) != null) 
             {
              //   System.out.print((char) c);
            	 output.write(c);
            	 
             }
  //           Object o = u.getContent( );
  //           System.out.println("I got a " + o.getClass().getName( ));
         }
         catch (MalformedURLException e) 
         {
             System.err.println(urlAddress + " is not a parseable URL");
         } catch (IOException e)
         {
             System.err.println(e);
         }
         catch (Exception e) {
        	 e.printStackTrace();
         }

		
	}
}
