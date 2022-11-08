// generates routes.html
import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class routeList
{
  public static void main(String [] args)
  {
    ArrayList<String> agencies = new ArrayList<String>();
    ArrayList<String> fullAgencies = new ArrayList<String>();
    
    try
    {
      Scanner s = new Scanner(new File("agencies.txt"));
      while (s.hasNextLine())
      {
        String data = s.nextLine();
        String code = data.substring(0, data.indexOf(";"));
        agencies.add(code);
        data = data.substring(data.indexOf(";") + 1);
        fullAgencies.add(data);
      }
    }
    catch (FileNotFoundException e)
    {
      System.out.println("Error, no agencies.txt.");
    }

    // creates page
    try
    {
      File newFile1 = new File("routes.html");
      FileWriter fileWriter1 = new FileWriter(newFile1);

      fileWriter1.write("<title>Route Listing - Eliot Deviation Index</title> \n");
      fileWriter1.append("<link rel=stylesheet href=style.css> \n");
      fileWriter1.append("<ul><li><a href=index.html>Home</a></li>");
      fileWriter1.append("<li><a href=stops.html>Stop Listing</a></li> \n");
      fileWriter1.append("<li><a href=routes.html class=active>Route Listing</a></li> \n");
      fileWriter1.append("<li><a href=calculator.html>Calculator</a></li></ul> \n");
      fileWriter1.append("<h1>Route Listing</h1> \n");

      // listing with links at top
      for (int i = 0; i < agencies.size(); i++)
      {
        fileWriter1.append("<ul class=bullet style=background-color:#d9ffde><a href=#" + agencies.get(i) + ">" + fullAgencies.get(i) + "</a></ul> \n");
      }
      
      int routeCount = 0;

      // loop to list routes
      for (int i = 0; i < agencies.size(); i++)
      {
        fileWriter1.append("<h3 id=" + agencies.get(i) + ">" + fullAgencies.get(i) + " (" + agencies.get(i) + ")</h3> \n");

        ArrayList<String> routeCode = new ArrayList<String>();
        ArrayList<String> routeDisp = new ArrayList<String>();
        ArrayList<String> routeEdi = new ArrayList<String>();
        int agencyCount = 0;

        // takes routes from /edi folder
        try
        {
          Scanner s = new Scanner(new File("edis/" + agencies.get(i) + ".txt"));
          while (s.hasNextLine())
          {
            String data = s.nextLine();
            if (!data.substring(0, 1).equals("*")) // * - unverified
            {
              String code = data.substring(0, data.indexOf(";"));
              routeCode.add(code);
              data = data.substring(data.indexOf(";") + 1);
              String disp = data.substring(0, data.indexOf(";"));
              routeDisp.add(disp);
              data = data.substring(data.indexOf(";") + 1);
              String edi = data;
              routeEdi.add(edi);
              routeCount++;
              agencyCount++;
            }
          }
        }
        catch (FileNotFoundException e)
        {
          // System.out.println("Error, no EDI file (" + agencies.get(i) + ")"); // expected error if no routes in database.
        }

        fileWriter1.append("<table><tr><th>Route Code</th><th>Line Length (mi)</th><th>Eliot Deviation Index</th></tr> \n");

        // route table created
        for (int j = 0; j < routeCode.size(); j++)
        {
          fileWriter1.append("<tr><td style=color:red>" + routeCode.get(j) + "</td><td>" + routeDisp.get(j) + "</td><td>" + routeEdi.get(j) + "</td></tr> \n");
        }

        fileWriter1.append("</table> \n");
        System.out.println("Agency Routes (" + agencies.get(i) + "): " + agencyCount);
      }

      fileWriter1.append("<p><b>Route Count: </b> " + routeCount + "</p>");   
      fileWriter1.close();
      System.out.println("Route Count: " + routeCount);
    }
    catch (IOException e)
    {
      System.out.println("Error, can't save routes.");
    }
  }
}