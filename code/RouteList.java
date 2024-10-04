import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
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
      fileWriter1.append("<script src=routes.js></script> \n");
      fileWriter1.append("<body onload=getAgencies()> \n");
      fileWriter1.append("<ul><li><a href=index.html>Home</a></li> \n");
      fileWriter1.append("<li><a href=stops.html>Stop Listing</a></li> \n");
      fileWriter1.append("<li><a href=routes.html class=active>Route Listing</a></li> \n");
      fileWriter1.append("<li><a href=detailed.html>Route Detail</a></li> \n");
      fileWriter1.append("<li><a href=calculator.html>Calculator</a></li> \n");
      fileWriter1.append("<li><a href=resources.html>Resources</a></li></ul> \n");
      fileWriter1.append("<h1>Route Listing</h1> \n");

      fileWriter1.append("<p>Only want the worst of the bunch? <a href=deviatory.html>Here</a> is a list of all routes with an EDI over 3.0, our threshold for being too deviatory.</p> \n");

      fileWriter1.append("<p>Agency: <select id=\"agencyDrop\"><option value=\"none\">Select an agency</option></select> <button onClick=\"getRoutes()\">Enter</button></p>> \n");
      
      int routeCount = 0;

      // loop to list routes
      for (int i = 0; i < agencies.size(); i++)
      {
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
            currentSize = agencyCount; // set things for the next set if applicable
          }
        }
        catch (Exception e)
        {
          // just don't do anything then  
        }
      }

      // stats side of everything - literally just copied from Stats.java
      ArrayList<Double> lengths = new ArrayList<Double>();
      ArrayList<Double> edis = new ArrayList<Double>();
      double totals = 0.00;
      double medianEdi = 0.0;
      double medianLength = 0.0;
      for (int a = 0; a < agencies.size(); a++)
      {
        try
        {
          Scanner s = new Scanner(new File("../edis/" + agencies.get(a) + ".txt"));
          while (s.hasNextLine())
          {
            String data = s.nextLine();
            data = data.substring(data.indexOf(";") + 1);
            double miles = Double.parseDouble(data.substring(0, data.indexOf(";")));
            data = data.substring(data.indexOf(";") + 1);
            double edi = Double.parseDouble(data.substring(0, data.indexOf(";")));

            lengths.add(miles);
            edis.add(edi);
            totals += miles;
          }
        }
        catch (Exception e)
        {
          continue; // skip because agency not in database
        }

        // check for sets - yes, sets are being counted with global stats list
        try
        {
          // load in the sets
          ArrayList<String> agencySets = new ArrayList<String>();
          Scanner t = new Scanner(new File("../sets/" + agencies.get(a) + ".txt"));
          while (t.hasNextLine())
          {
            String data = t.nextLine();
            agencySets.add(data);
          }

          // load in from the set
          for (int j = 0; j < agencySets.size(); j++) // loop through all sets
          {
            Scanner s = new Scanner(new File("../edis/sets/" + agencies.get(a) + "-" + agencySets.get(j) + ".txt"));
            while (s.hasNextLine())
            {
              String data = s.nextLine();
              data = data.substring(data.indexOf(";") + 1);
              double miles = Double.parseDouble(data.substring(0, data.indexOf(";")));
              data = data.substring(data.indexOf(";") + 1);
              double edi = Double.parseDouble(data);
  
              lengths.add(miles);
              edis.add(edi);
              totals += miles;
            }
          }
        }
        catch (Exception e)
        {
          // skip, agency has no sets
        }
      }

      Collections.sort(lengths);
      Collections.sort(edis);

      // median EDI
      if (edis.size() % 2 == 1) // odd amount of EDIs
      {
        if (edis.size() == 1)
        {
          medianEdi = edis.get(0);
        }
        else
        {
          medianEdi = edis.get(edis.size() / 2);
        }
      }
      else // even amount of EDIs
      {
        if (edis.size() == 2)
        {
          medianEdi = (edis.get(0) + edis.get(1)) / 2.0;
        }
        else
        {
          medianEdi = (edis.get((edis.size() / 2) - 1) + edis.get(edis.size() / 2)) / 2.0;
        }
        medianEdi = Math.round(medianEdi * 100.0) / 100.0;
      }

      // median length
      if (lengths.size() % 2 == 1) // odd amount of lengths
      {
        if (lengths.size() == 1)
        {
          medianLength = lengths.get(0);
        }
        else
        {
          medianLength = lengths.get(lengths.size() / 2);
        }
      }
      else // even amount of lengths
      {
        if (lengths.size() == 2)
        {
          medianLength = (lengths.get(0) + lengths.get(1)) / 2.0;
        }
        else
        {
          medianLength = (lengths.get((lengths.size() / 2) - 1) + lengths.get(lengths.size() / 2)) / 2.0;
        }
        medianLength = Math.round(medianLength * 100.0) / 100.0;
      }

      // round total miles
      totals = Math.round(totals * 100.0) / 100.0;

      fileWriter1.append("<table><tr><th></th><th>Entire Database</th><th>Selected Agency</th></tr> \n");
      fileWriter1.append("<tr><td><b>Route Count</b></td><td>" + routeCount + "</td><td id=\"agencyRoutes\"></td></tr> \n");
      fileWriter1.append("<tr><td><b>Total Miles</b></td><td>" + totals + " mi.</td><td id=\"agencyMiles\"></td></tr> \n");
      fileWriter1.append("<tr><td><b>Median Length</b></td><td>" + medianLength + " mi.</td><td id=\"medianLength\"></td></tr> \n");
      fileWriter1.append("<tr><td><b>Median EDI</b></td><td>" + medianEdi + "</td><td id=\"medianEdi\"></td></tr></table> \n");
      fileWriter1.append("<p><table id=\"listing\"></p>");
      fileWriter1.close();
      System.out.println("Route Count: " + routeCount);
    }
    catch (IOException e)
    {
      System.out.println("Error, can't save routes.");
    }
  }
}