// creates stats page
// stats command line in Stats.java, no page generation there
// basically the same code though
// edi miles will probably be ignored
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;

public class StatsList
{
  public static void main(String [] args)
  {
    // start with the global
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
    catch (Exception e)
    {
      System.out.println("Error - no agencies.");
    }

    try
    {
      File newFile1 = new File("stats.html");
      FileWriter fileWriter1 = new FileWriter(newFile1);

      fileWriter1.write("<title>Statistics - Eliot Deviation Index</title> \n");
      fileWriter1.append("<link rel=stylesheet href=style.css> \n");
      fileWriter1.append("<ul><li><a href=index.html>Home</a></li>");
      fileWriter1.append("<li><a href=stops.html>Stop Listing</a></li> \n");
      fileWriter1.append("<li><a href=routes.html>Route Listing</a></li> \n");
      fileWriter1.append("<li><a href=stats.html class=active>Statistics</a></li> \n");
      fileWriter1.append("<li><a href=calculator.html>Calculator</a></li></ul> \n");
      fileWriter1.append("<h1>Statistics</h1> \n");

      // listing with links at top
      fileWriter1.append("<ul class=bullet style=background-color:#d9ffde><a href=#global>Entire Database</a></ul> \n");
      for (int i = 0; i < agencies.size(); i++)
      {
        fileWriter1.append("<ul class=bullet style=background-color:#d9ffde><a href=#" + agencies.get(i) + ">" + fullAgencies.get(i) + "</a> (" + agencies.get(i) + ")</ul> \n");
      }

      fileWriter1.append("<h3 id=global>Enitre Database</h3> \n");
      
      ArrayList<Double> lengths = new ArrayList<Double>(); // stores all lengths
      ArrayList<Double> edis = new ArrayList<Double>();
      double totals = 0.00; // total EDI miles in agency
      
      for (int a = 0; a < agencies.size(); a++)
      {
        try
        {
          Scanner s = new Scanner(new File("edis/" + agencies.get(a) + ".txt"));
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
        catch (Exception e)
        {
          continue; // skip because agency not in database
        }

        // check for sets - yes, sets are being counted with global stats list
        try
        {
          // load in the sets
          ArrayList<String> agencySets = new ArrayList<String>();
          Scanner t = new Scanner(new File("sets/" + agencies.get(a) + ".txt"));
          while (t.hasNextLine())
          {
            String data = t.nextLine();
            agencySets.add(data);
          }

          // load in from the set
          for (int j = 0; j < agencySets.size(); j++) // loop through all sets
          {
            Scanner s = new Scanner(new File("edis/sets/" + agencies.get(a) + "-" + agencySets.get(j) + ".txt"));
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

      // does all the math for the global
      Collections.sort(lengths);
      Collections.sort(edis);

      double meanEdi = 0.0;
      double medianEdi = 0.0;
      double meanLength = 0.0;
      double medianLength = 0.0;

      // mean EDI
      double totalEdi = 0.0;
      for (int i = 0; i < edis.size(); i++)
      { 
        totalEdi = totalEdi + edis.get(i);
      }
      meanEdi = totalEdi / edis.size();
      meanEdi = Math.round(meanEdi * 100.0) / 100.0;

      // mean length
      double totalLength = 0.0;
      for (int i = 0; i < lengths.size(); i++)
      { 
        totalLength = totalLength + lengths.get(i);
      }
      meanLength = totalLength / lengths.size();
      meanLength = Math.round(meanLength * 100.0) / 100.0;

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
      
      fileWriter1.append("<p><b>Total Routes: </b>" + lengths.size() + "<br>\n");
      fileWriter1.append("<b>Mean EDI: </b>" + meanEdi + "<br>\n");
      fileWriter1.append("<b>Median EDI: </b>" + medianEdi + "<br>\n");
      fileWriter1.append("<b>Mean Length: </b>" + meanLength + " mi.<br>\n");
      fileWriter1.append("<b>Median Length: </b>" + medianLength + " mi.</p>\n");

      lengths.clear();
      edis.clear();

      System.out.println("Stats List: Global");

      // loop through all the agencies
      for (int a = 0; a < agencies.size(); a++)
      {
        fileWriter1.append("<h3 id=" + agencies.get(a) + ">" + fullAgencies.get(a) + " (" + agencies.get(a) + ")</h3> \n");
        
        try
        {
          Scanner s = new Scanner(new File("edis/" + agencies.get(a) + ".txt"));
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
        catch (Exception e)
        {
          continue; // skip because agency not in database
        } 

        Collections.sort(lengths);
        Collections.sort(edis);
  
        meanEdi = 0.0;
        medianEdi = 0.0;
        meanLength = 0.0;
        medianLength = 0.0;
  
        // mean EDI
        totalEdi = 0.0;
        for (int i = 0; i < edis.size(); i++)
        { 
          totalEdi = totalEdi + edis.get(i);
        }
        meanEdi = totalEdi / edis.size();
        meanEdi = Math.round(meanEdi * 100.0) / 100.0;
  
        // mean length
        totalLength = 0.0;
        for (int i = 0; i < lengths.size(); i++)
        { 
          totalLength = totalLength + lengths.get(i);
        }
        meanLength = totalLength / lengths.size();
        meanLength = Math.round(meanLength * 100.0) / 100.0;
  
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

        fileWriter1.append("<p><b>Current Routes: </b>" + lengths.size() + "<br>\n");
        fileWriter1.append("<b>Mean EDI: </b>" + meanEdi + "<br>\n");
        fileWriter1.append("<b>Median EDI: </b>" + medianEdi + "<br>\n");
        fileWriter1.append("<b>Mean Length: </b>" + meanLength + " mi.<br>\n");
        fileWriter1.append("<b>Median Length: </b>" + medianLength + " mi.</p>\n");

        lengths.clear();
        edis.clear();

        // check for sets
        try
        {
          // load in the sets
          ArrayList<String> agencySets = new ArrayList<String>();
          Scanner t = new Scanner(new File("sets/" + agencies.get(a) + ".txt"));
          while (t.hasNextLine())
          {
            String data = t.nextLine();
            agencySets.add(data);
          }

          // load in from the set
          for (int j = 0; j < agencySets.size(); j++) // loop through all sets
          {
            Scanner s = new Scanner(new File("edis/sets/" + agencies.get(a) + "-" + agencySets.get(j) + ".txt"));
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

            Collections.sort(lengths);
            Collections.sort(edis);
      
            meanEdi = 0.0;
            medianEdi = 0.0;
            meanLength = 0.0;
            medianLength = 0.0;
      
            // mean EDI
            totalEdi = 0.0;
            for (int i = 0; i < edis.size(); i++)
            { 
              totalEdi = totalEdi + edis.get(i);
            }
            meanEdi = totalEdi / edis.size();
            meanEdi = Math.round(meanEdi * 100.0) / 100.0;
      
            // mean length
            totalLength = 0.0;
            for (int i = 0; i < lengths.size(); i++)
            { 
              totalLength = totalLength + lengths.get(i);
            }
            meanLength = totalLength / lengths.size();
            meanLength = Math.round(meanLength * 100.0) / 100.0;
      
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

            fileWriter1.append("<p><b>" + agencySets.get(j) + " Set</b><br>\n");
            fileWriter1.append("<b>Set Routes: </b>" + lengths.size() + "<br>\n");
            fileWriter1.append("<b>Mean EDI: </b>" + meanEdi + "<br>\n");
            fileWriter1.append("<b>Median EDI: </b>" + medianEdi + "<br>\n");
            fileWriter1.append("<b>Mean Length: </b>" + meanLength + " mi.<br>\n");
            fileWriter1.append("<b>Median Length: </b>" + medianLength + " mi.</p>\n");
    
            lengths.clear();
            edis.clear();
          }
        }
        catch (Exception e)
        {
          // don't do a damn thing
        }

        System.out.println("Stats List: " + (a + 1) + " / " + agencies.size());
      }
      fileWriter1.close();
    }
    catch (Exception e)
    {
      System.out.println("Error creating stats page.");  
    }
  }
}