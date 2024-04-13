import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

// generates population.html

public class PopulationList
{
  public static void main(String [] args)
  {
    ArrayList<String> agencies = new ArrayList<String>();
    ArrayList<String> fullAgencies = new ArrayList<String>();
    ArrayList<Boolean> densityBoolean = new ArrayList<Boolean>();

    // new lists of eligible agencies
    ArrayList<String> searchAgencies = new ArrayList<String>();
    ArrayList<String> searchFull = new ArrayList<String>();
    
    try
    {
      Scanner s = new Scanner(new File("../agencies.txt"));
      while (s.hasNextLine())
      {
        String data = s.nextLine();
        String code = data.substring(0, data.indexOf(";"));
        agencies.add(code);
        data = data.substring(data.indexOf(";") + 1);
        fullAgencies.add(data.substring(data.indexOf(";") + 1));
        data = data.substring(data.indexOf(";") + 1);
        densityBoolean.add(Boolean.parseBoolean(data));
      }
    }
    catch (FileNotFoundException e)
    {
      System.out.println("Error, no agencies.txt.");
    }

    // find eligible agencies
    for (int i = 0; i < agencies.size(); i++)
    {
      if (densityBoolean.get(i))
      {
        searchAgencies.add(agencies.get(i));
        searchFull.add(fullAgencies.get(i));
      }
    }

    // creates page
    try
    {
      File newFile1 = new File("../population.html");
      FileWriter fileWriter1 = new FileWriter(newFile1);

      fileWriter1.write("<title>Population Data - Eliot Deviation Index</title> \n");
      fileWriter1.append("<link rel=stylesheet href=style.css> \n");
      fileWriter1.append("<ul><li><a href=index.html>Home</a></li>");
      fileWriter1.append("<li><a href=stops.html>Stop Listing</a></li> \n");
      fileWriter1.append("<li><a href=routes.html>Route Listing</a></li> \n");
      fileWriter1.append("<li><a href=detailed.html>Route Detail</a></li> \n");
      fileWriter1.append("<li><a href=stats.html>Statistics</a></li> \n");
      fileWriter1.append("<li><a href=calculator.html>Calculator</a></li> \n");
      fileWriter1.append("<li><a href=resources.html class=active>Resources</a></li></ul> \n");
      fileWriter1.append("<h1>Population Data</h1> \n <p>");

      // listing with links at top
      for (int i = 0; i < searchAgencies.size(); i++)
      {
        fileWriter1.append("<a href=#" + searchAgencies.get(i) + ">" + searchFull.get(i) + "</a> (" + searchAgencies.get(i) + ")<br>\n");
      }
      
      int routeCount = 0;

      // loop to list routes
      for (int i = 0; i < searchAgencies.size(); i++)
      {
        fileWriter1.append("<h3 id=" + searchAgencies.get(i) + ">" + searchFull.get(i) + " (" + searchAgencies.get(i) + ")</h3> \n");

        ArrayList<String> routeCode = new ArrayList<String>();
        ArrayList<String> routeEdi = new ArrayList<String>();
        ArrayList<String> routePop = new ArrayList<String>();
        ArrayList<String> routeDensity = new ArrayList<String>();

        int agencyCount = 0;
        // takes routes from /edi folder
        try
        {
          Scanner s = new Scanner(new File("../edis/" + searchAgencies.get(i) + ".txt"));
          while (s.hasNextLine())
          {
            String data = s.nextLine();
            String code = data.substring(0, data.indexOf(";"));
            routeCode.add(code);
            data = data.substring(data.indexOf(";") + 1); // skip length
            data = data.substring(data.indexOf(";") + 1);
            String edi = data.substring(0, data.indexOf(";"));
            routeEdi.add(edi);
            data = data.substring(data.indexOf(";") + 1);
            String pop = data.substring(0, data.indexOf(";"));
            routePop.add(pop);
            data = data.substring(data.indexOf(";") + 1);
            String dense = data.substring(0, data.indexOf(";"));
            routeDensity.add(dense);
            routeCount++;
            agencyCount++;
          }
        }
        catch (FileNotFoundException e)
        {
          // System.out.println("Error, no EDI file (" + agencies.get(i) + ")"); // expected error if no routes in database.
        }

        fileWriter1.append("<table><tr><th>Route Code</th><th>EDI</th><th>Population Served</th><th>Density</th></tr> \n");

        // route table created
        for (int j = 0; j < routeCode.size(); j++)
        {
          fileWriter1.append("<tr><td style=color:#2ecc71>" + routeCode.get(j) + "</td><td>" + routeEdi.get(j) + "</td><td>" + routePop.get(j) + "</td><td>" + routeDensity.get(j) + " / mi.</td></tr> \n");
        }

        fileWriter1.append("</table> \n");
        System.out.println("Population List (" + searchAgencies.get(i) + "): " + agencyCount);
        int currentSize = agencyCount;
      }

      fileWriter1.append("<p><b>Route Count: </b> " + routeCount + "</p>");   
      fileWriter1.close();
      System.out.println("Route Count (Density): " + routeCount);
    }
    catch (IOException e)
    {
      System.out.println("Error, can't save routes.");
    }
  }
}