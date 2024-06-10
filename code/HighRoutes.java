import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

// generates deviatory.html

public class HighRoutes
{
  public static void main(String [] args)
  {
    ArrayList<String> agencies = new ArrayList<String>();
    ArrayList<String> fullAgencies = new ArrayList<String>();
    
    try
    {
      Scanner s = new Scanner(new File("../agencies.txt"));
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
      File newFile1 = new File("../deviatory.html");
      FileWriter fileWriter1 = new FileWriter(newFile1);

      fileWriter1.write("<title>Extra Deviatory Routes - Eliot Deviation Index</title> \n");
      fileWriter1.append("<link rel=stylesheet href=style.css> \n");
      fileWriter1.append("<body onload=getAgencies()> \n");
      fileWriter1.append("<ul><li><a href=index.html>Home</a></li>");
      fileWriter1.append("<li><a href=stops.html>Stop Listing</a></li> \n");
      fileWriter1.append("<li><a href=routes.html class=active>Route Listing</a></li> \n");
      fileWriter1.append("<li><a href=detailed.html>Route Detail</a></li> \n");
      fileWriter1.append("<li><a href=stats.html>Statistics</a></li> \n");
      fileWriter1.append("<li><a href=calculator.html>Calculator</a></li> \n");
      fileWriter1.append("<li><a href=resources.html>Resources</a></li></ul> \n");
      fileWriter1.append("<h1>Extra Deviatory Routes</h1> \n");

      fileWriter1.append("<p>All routes listed here have an EDI value greater than 3.0, which is the threshold to suggest that an agency should reconsider this route.</p> \n");
      
      int routeCount = 0;

      ArrayList<String> routeAgency = new ArrayList<String>();
      ArrayList<String> routeCode = new ArrayList<String>();
      ArrayList<String> routeDist = new ArrayList<String>();
      ArrayList<String> routeEdi = new ArrayList<String>();

      // loop to list routes
      for (int i = 0; i < agencies.size(); i++)
      {
        // takes routes from /edi folder
        try
        {
          Scanner s = new Scanner(new File("../edis/" + agencies.get(i) + ".txt"));
          while (s.hasNextLine())
          {
            String data = s.nextLine();
            if (!data.substring(0, 1).equals("*")) // * - unverified
            {
              String code = data.substring(0, data.indexOf(";"));
              data = data.substring(data.indexOf(";") + 1);
              String dist = data.substring(0, data.indexOf(";"));
              data = data.substring(data.indexOf(";") + 1);
              String edi = data.substring(0, data.indexOf(";"));

              if (Double.parseDouble(edi) >= 3)
              {
                routeAgency.add(agencies.get(i));
                routeCode.add(code);
                routeDist.add(dist);
                routeEdi.add(edi);
                routeCount++;
              }
            }
          }
        }
        catch (FileNotFoundException e)
        {
          // System.out.println("Error, no EDI file (" + agencies.get(i) + ")"); // expected error if no routes in database.
        }
      }

      fileWriter1.append("<table><tr><th>Agency Code</th><th>Route Code</th><th>Line Length</th><th>Eliot Deviation Index</th></tr> \n");

      // route table created
      for (int j = 0; j < routeCode.size(); j++)
      {
        fileWriter1.append("<tr><td style=color:#30d1c6>" + routeAgency.get(j) + "</td><td style=color:#2ecc71>" + routeCode.get(j) + "</td><td>" + routeDist.get(j) + " mi.</td><td>" + routeEdi.get(j) + "</td></tr> \n");
      }

      fileWriter1.append("</table> \n");

      fileWriter1.append("<p><b>Route Count: </b> " + routeCount + "</p>");   
      fileWriter1.close();

      System.out.println("Extra Deviatory: " + routeCount);
    }
    catch (IOException e)
    {
      System.out.println("Error, can't save routes.");
    }
  }
}