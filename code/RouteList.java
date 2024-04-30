import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

// generates routes.html

public class RouteList
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
      File newFile1 = new File("../routes.html");
      FileWriter fileWriter1 = new FileWriter(newFile1);

      fileWriter1.write("<title>Route Listing - Eliot Deviation Index</title> \n");
      fileWriter1.append("<link rel=stylesheet href=style.css> \n");
      fileWriter1.append("<body onload=getAgencies()> \n");
      fileWriter1.append("<ul><li><a href=index.html>Home</a></li>");
      fileWriter1.append("<li><a href=stops.html>Stop Listing</a></li> \n");
      fileWriter1.append("<li><a href=routes.html class=active>Route Listing</a></li> \n");
      fileWriter1.append("<li><a href=detailed.html>Route Detail</a></li> \n");
      fileWriter1.append("<li><a href=stats.html>Statistics</a></li> \n");
      fileWriter1.append("<li><a href=calculator.html>Calculator</a></li> \n");
      fileWriter1.append("<li><a href=resources.html>Resources</a></li></ul> \n");
      fileWriter1.append("<h1>Route Listing</h1> \n");

      // listing with links at top
      fileWriter1.append("<p id=agencies><a href=#global>Entire Database</a><br></p> \n");
      fileWriter1.append("<script src=list.js></script>");
      
      int routeCount = 0;

      // loop to list routes
      for (int i = 0; i < agencies.size(); i++)
      {
        fileWriter1.append("<h3 id=" + agencies.get(i) + ">" + fullAgencies.get(i) + " (" + agencies.get(i) + ")</h3> \n");

        ArrayList<String> routeCode = new ArrayList<String>();
        ArrayList<String> routeDist = new ArrayList<String>();
        ArrayList<String> routeEdi = new ArrayList<String>();
        ArrayList<String> routeOfficial = new ArrayList<String>();
        int agencyCount = 0;
        // takes routes from /edi folder
        try
        {
          Scanner s = new Scanner(new File("../edis/" + agencies.get(i) + ".txt"));
          while (s.hasNextLine())
          {
            String data = s.nextLine();
            if (!data.substring(0, 1).equals("*")) // * - unverified
            {
              if (data.substring(0, 1).equals("!"))
              {
                data = data.substring(1);
                routeOfficial.add("n");
              }
              else
              {
                routeOfficial.add("y");
              }
              String code = data.substring(0, data.indexOf(";"));
              routeCode.add(code);
              data = data.substring(data.indexOf(";") + 1);
              String dist = data.substring(0, data.indexOf(";"));
              routeDist.add(dist);
              data = data.substring(data.indexOf(";") + 1);
              String edi = data.substring(0, data.indexOf(";"));
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

        fileWriter1.append("<table><tr><th>Route Code</th><th>Line Length</th><th>Eliot Deviation Index</th></tr> \n");

        // route table created
        for (int j = 0; j < routeCode.size(); j++)
        {
          if (routeOfficial.get(j).equals("y"))
          {
            fileWriter1.append("<tr><td style=color:#2ecc71>" + routeCode.get(j) + "</td><td>" + routeDist.get(j) + " mi.</td><td>" + routeEdi.get(j) + "</td></tr> \n");
          }
          else
          {
            fileWriter1.append("<tr><td style=color:#0097a7>" + routeCode.get(j) + "</td><td>" + routeDist.get(j) + " mi.</td><td>" + routeEdi.get(j) + "</td></tr> \n");
          }
        }

        fileWriter1.append("</table> \n");
        System.out.println("Agency Routes (" + agencies.get(i) + "): " + agencyCount);
        int currentSize = agencyCount;
        
        // check for sets
        try
        {
          // load in the sets
          ArrayList<String> agencySets = new ArrayList<String>();
          Scanner t = new Scanner(new File("../sets/" + agencies.get(i) + ".txt"));
          while (t.hasNextLine())
          {
            String data = t.nextLine();
            agencySets.add(data);
          }

          // load in from the set
          for (int j = 0; j < agencySets.size(); j++) // loop through all sets
          {
            Scanner s = new Scanner(new File("../edis/sets/" + agencies.get(i) + "-" + agencySets.get(j) + ".txt"));
            while (s.hasNextLine())
            {
              String data = s.nextLine();
              // i'm gonna list them all blue (unofficial b/c post-rollover)
              String code = data.substring(0, data.indexOf(";"));
              routeCode.add(code);
              data = data.substring(data.indexOf(";") + 1);
              String dist = data.substring(0, data.indexOf(";"));
              routeDist.add(dist);
              data = data.substring(data.indexOf(";") + 1);
              String edi = data;
              routeEdi.add(edi);
              routeCount++;
              agencyCount++;
            }

            fileWriter1.append("<p><b>" + agencySets.get(j) + " Set</b></p>\n");
            fileWriter1.append("<table><tr><th>Route Code</th><th>Line Length</th><th>Eliot Deviation Index</th></tr> \n");

            for (int k = currentSize; k < routeCode.size(); k++)
            {
              fileWriter1.append("<tr><td style=color:#0097a7>" + routeCode.get(k) + "</td><td>" + routeDist.get(k) + " mi.</td><td>" + routeEdi.get(k) + "</td></tr> \n");
            }

            fileWriter1.append("</table> \n");
            currentSize = agencyCount; // set things for the next set if applicable
          }
        }
        catch (Exception e)
        {
          // just don't do anything then  
        }
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