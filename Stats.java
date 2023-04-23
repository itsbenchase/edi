// creates statistics
// median and mean length and EDI
// just for stats display on my end
// stats page generated with StatsList.java
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
import java.io.File;

public class Stats
{
  static boolean bypass = false;
  static String agencyChoice = "no data";

  public Stats(String agency)
  {
    // constructor to only run the program once, used in update
    // bypass will bypass the agency selection
    // will only run program once
    agencyChoice = agency;
    bypass = true;
  }

  public static void main(String [] args)
  {
    Scanner in = new Scanner(System.in);

    int choice = 1; // looping

    while (choice == 1)
    {
      if (!bypass)
      {
        System.out.print("Select agency (or global): ");
        agencyChoice = in.nextLine();
      }

      ArrayList<Double> lengths = new ArrayList<Double>(); // stores all lengths
      ArrayList<Double> edis = new ArrayList<Double>();
      double totals = 0.00; // total EDI miles in agency

      // global database search
      if (agencyChoice.equalsIgnoreCase("global"))
      {
        ArrayList<String> agencies = new ArrayList<String>();
        try
        {
          Scanner s = new Scanner(new File("agencies.txt"));
          while (s.hasNextLine())
          {
            String data = s.nextLine();
            agencies.add(data.substring(0, data.indexOf(";")));
          }
        }
        catch (Exception e)
        {
          System.out.println("Error - no agencies.");
        }

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
        }
      }

      // individual agency
      else
      {
        try
        {
          Scanner s = new Scanner(new File("edis/" + agencyChoice + ".txt"));
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
          System.out.println("Error (agency route list).");
        }
      }

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
          medianEdi = edis.get((edis.size() / 2) + 1);
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
          medianEdi = (edis.get((edis.size() / 2) - 1) + edis.get((edis.size() / 2) + 1)) / 2.0;
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
          medianLength = lengths.get((lengths.size() / 2) + 1);
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
          medianLength = (lengths.get((lengths.size() / 2) - 1) + lengths.get((lengths.size() / 2) + 1)) / 2.0;
        }
        medianLength = Math.round(medianLength * 100.0) / 100.0;
      }

      // round total miles
      totals = Math.round(totals * 100.0) / 100.0;

      System.out.println("Routes Searched: " + lengths.size());
      System.out.println("Total EDI Miles: " + totals + " miles");
      System.out.println("Mean EDI: " + meanEdi);
      System.out.println("Median EDI: " + medianEdi);
      System.out.println("Mean Length: " + meanLength + " miles");
      System.out.println("Median Length: " + medianLength + " miles");
      
      if (!bypass)
      {
        System.out.print("Enter 1 to search again: ");
        choice = in.nextInt();
        in.nextLine(); // absorb enter
      }
      if (bypass)
      {
        choice = 0;
      }
    }
  }
}