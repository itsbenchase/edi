import java.util.Scanner;
import java.util.ArrayList;
import java.io.*;
import java.net.*;
import java.io.IOException;

// online variant, offered for download
public class ediOnline
{
  static String agencyChoice;
  public static void main(String [] args)
  {
    Scanner a = new Scanner(System.in); // for agency choice
    // intro

    System.out.println("Welcome to the Eliot Deviation Index calculator!");
    System.out.println("");
	 
	  System.out.println("Before starting, please select the agency you would like to access data for. Agency codes are listed online at www.eliotindex.org.");

    System.out.println("");

     
    System.out.println("INSTRUCTIONS - EXISTING LINES");
    System.out.println("Enter line name when prompted.");

    System.out.println("");

    System.out.println("INSTRUCTIONS - CUSTOM LINES");
    System.out.println("- When prompted for a line, enter \"custom\".");
    System.out.println("- Enter the stop IDs in order. A full list can be found at dev.eliotindex.org/stops.html.");
    System.out.println("- Enter \"-1\" to add a custom stop.");
    System.out.println("- Enter \"-2\" to remove the last stop added.");
    System.out.println("- Enter \"-3\" to add a stop from another agency.");
    System.out.println("- Enter \"-0\" after the last stop is added.");
    System.out.println("- Enter \"segment\" to add a segment of a line (see below).");


    System.out.println("");

    System.out.println("BONUS FEATURES");
    System.out.println("- Enter \"segment\" to find the Eliot Deviation Index of a segment of a line. You will need the stop orders from the full line.");
    System.out.println("- Enter \"reverse\" to reverse the order of the line.");
    
    System.out.println("");
    
    System.out.println("Enjoy!");

    System.out.println("");
    agency();
  }

    public static void agency()
  {
    Scanner a = new Scanner(System.in);
    System.out.print("Agency: ");

    // check if valid agency
    ArrayList<String> agencies = new ArrayList<String>();
    try
    {
      Scanner s = new Scanner(new URI("https://dev.eliotindex.org/agencies.txt").toURL().openStream());
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

    agencyChoice = a.nextLine();
    boolean actualAgency = false;
    for (int i = 0; i < agencies.size(); i++)
    {
      if (agencies.get(i).equalsIgnoreCase(agencyChoice))
      {
        actualAgency = true;
        edi();
      }
    }
    if (!actualAgency)
    {
      System.out.print("Invalid agency. ");
      agency();
    }
  }

  public static void edi()
  {
    Scanner in = new Scanner(System.in);
    System.out.print("Enter line: ");
    String lineChoice = in.nextLine();

    while (!lineChoice.equals("-00"))
    {
      ArrayList<Stop> stops = new ArrayList<Stop>();

      // create line
      int stopCount = 0;

      String lineName = "no data, yet";
      boolean export = false;

      Stop [] theLine;

      if (lineChoice.equalsIgnoreCase("custom"))
      {
        // load in only existing stops
        try
        {
          Scanner s = new Scanner(new URI("https://dev.eliotindex.org/stops/" + agencyChoice + ".txt").toURL().openStream());
          while (s.hasNextLine())
          {
            String data = s.nextLine();
            String id = data.substring(0, data.indexOf(";"));
            data = data.substring(data.indexOf(";") + 1);
            String name = data.substring(0, data.indexOf(";"));
            data = data.substring(data.indexOf(";") + 1);
            double lat = Double.parseDouble(data.substring(0, data.indexOf(";")));
            data = data.substring(data.indexOf(";") + 1);
            double lon = Double.parseDouble(data);

            stops.add(new Stop(id, name, lat, lon));
          }
        }
        catch (Exception e)
        {
          System.out.println("Error - can't load stops.");
        }
        ArrayList<Stop> custom = new ArrayList<Stop>();
        String customStop = "";

        System.out.print("Line Name: ");
        lineName = in.nextLine();
        
        while (!customStop.equals("-0"))
        {
          System.out.print("Stop ID: ");
          customStop = in.nextLine();

          if (customStop.equals("-1"))
          {
            stopCount++;
            System.out.print("New Stop Name: ");
            String custName = in.nextLine();
            System.out.print("New Stop ID: ");
            String custID = in.nextLine();
            System.out.print("New Stop Latitude: ");
            double custLat = in.nextDouble();
            System.out.print("New Stop Longitude: ");
            double custLon = in.nextDouble();

            Stop addStop = new Stop(custID, custName, custLat, custLon, lineName, stopCount);

            stops.add(addStop);
            custom.add(addStop);

            in.nextLine(); // absorb enter
          }
          if (customStop.equals("-2"))
          {
            System.out.println(custom.get(custom.size() - 1).getName() + " (" + custom.get(custom.size() - 1).getID() + ") removed.");
            custom.remove(custom.size() - 1);
            stopCount--;
          }

          // -3 - stop from another agency
          if (customStop.equals("-3"))
          {
            System.out.print("Enter agency: ");
            String extraAgency = in.nextLine();

            // load in stops from new agency
            ArrayList<Stop> stopsExtra = new ArrayList<Stop>(); // not to confuse with stops
            try
            {
              Scanner extra = new Scanner(new URI("https://dev.eliotindex.org/stops/" + extraAgency + ".txt").toURL().openStream());
              while (extra.hasNextLine())
              {
                String data = extra.nextLine();
                String id = data.substring(0, data.indexOf(";"));
                data = data.substring(data.indexOf(";") + 1);
                String name = data.substring(0, data.indexOf(";"));
                data = data.substring(data.indexOf(";") + 1);
                double lat = Double.parseDouble(data.substring(0, data.indexOf(";")));
                data = data.substring(data.indexOf(";") + 1);
                double lon = Double.parseDouble(data);
                stopsExtra.add(new Stop(id, name, lat, lon));
              }
            }
            catch (Exception e)
            {
              System.out.println("Error. can't load stops");
            }

            System.out.print("Stop ID: "); // ask for new id
            customStop = in.nextLine();

            for (int i = 0; i < stopsExtra.size(); i++)
            {
              if (stopsExtra.get(i).getID().equalsIgnoreCase(customStop))
              {
                stopCount++;
                stopsExtra.get(i).setID(extraAgency + " | " + stopsExtra.get(i).getID()); // extra due to formatting on display, need to know it's a different agency
                System.out.print("Added: " + stopsExtra.get(i).getName() + " "); // space for the stupidity
                Stop addStop = new Stop(stopsExtra.get(i).getID(), stopsExtra.get(i).getName(), stopsExtra.get(i).getLat(), stopsExtra.get(i).getLon(), lineName, stopCount);
                custom.add(stopsExtra.get(i));
                break;
              }          
            }
          }

          if (customStop.equals("segment"))
          {
            System.out.print("Enter line: ");
            lineChoice = in.nextLine();

            // segment reverse, almost the same damn thing as normal segment
            if (lineChoice.equals("reverse"))
            {
              System.out.print("Enter line: ");
              lineChoice = in.nextLine();
              System.out.print("Starting stop (low): ");
              int startStop = in.nextInt();
              System.out.print("Ending stop (high): ");
              int endStop = in.nextInt();

              // yes yes i know this is later on as well, but needs to be separate because of how i like the ordering on final file
              ArrayList<Stop> stops3 = new ArrayList<Stop>(); // stops3 - pulls from existing EDI file to add to segment
              try
              {
                Scanner s3 = new Scanner(new URI("https://dev.eliotindex.org/files/" + agencyChoice + "-edi.txt").toURL().openStream());
                while (s3.hasNextLine())
                {
                  String data = s3.nextLine();
                  String id = data.substring(0, data.indexOf(";"));
                  data = data.substring(data.indexOf(";") + 1);
                  String name = data.substring(0, data.indexOf(";"));
                  data = data.substring(data.indexOf(";") + 1);
                  double lat = Double.parseDouble(data.substring(0, data.indexOf(";")));
                  data = data.substring(data.indexOf(";") + 1);
                  double lon = Double.parseDouble(data.substring(0, data.indexOf(";")));
                  data = data.substring(data.indexOf(";") + 1); // lines
                  String line = data.substring(0, data.indexOf(";"));
                  data = data.substring(data.indexOf(";") + 1);
                  int order = Integer.parseInt(data);

                  stops3.add(new Stop(id, name, lat, lon, line, order));
                }
              }
              catch (Exception e)
              {
                System.out.println("Error.");
              }

              Stop [] reverseLine = new Stop[(endStop - startStop) + 1];
              int reverseNum = reverseLine.length;
              // check if in part of line desired (change from normal segment)
              for (int i = 0; i < stops3.size(); i++)
              {
                if (stops3.get(i).getLineEDI().equalsIgnoreCase(lineChoice) && stops3.get(i).getOrder() <= endStop && stops3.get(i).getOrder() >= startStop)
                {
                  reverseLine[reverseNum - 1] = new Stop(stops3.get(i).getID(), stops3.get(i).getName(), stops3.get(i).getLat(), stops3.get(i).getLon());
                  reverseNum--;
                }
              }

              // add reverseLine into the main array, with correct stop ordering
              for (int i = 0; i < reverseLine.length; i++)
              {
                stopCount++;
                Stop addStop = reverseLine[i];
                custom.add(addStop);
              }
            }

            // normal segment (low to high)
            else
            {
              System.out.print("Starting stop: ");
              int startStop = in.nextInt();
              System.out.print("Ending stop: ");
              int endStop = in.nextInt();

              ArrayList<Stop> stops3 = new ArrayList<Stop>();
              try
              {
                Scanner s3 = new Scanner(new URI("https://dev.eliotindex.org/files/" + agencyChoice + "-edi.txt").toURL().openStream());
                while (s3.hasNextLine())
                {
                  String data = s3.nextLine();
                  String id = data.substring(0, data.indexOf(";"));
                  data = data.substring(data.indexOf(";") + 1);
                  String name = data.substring(0, data.indexOf(";"));
                  data = data.substring(data.indexOf(";") + 1);
                  double lat = Double.parseDouble(data.substring(0, data.indexOf(";")));
                  data = data.substring(data.indexOf(";") + 1);
                  double lon = Double.parseDouble(data.substring(0, data.indexOf(";")));
                  data = data.substring(data.indexOf(";") + 1); // lines
                  String line = data.substring(0, data.indexOf(";"));
                  data = data.substring(data.indexOf(";") + 1);
                  int order = Integer.parseInt(data);

                  stops3.add(new Stop(id, name, lat, lon, line, order));
                }
              }
              catch (Exception e)
              {
                System.out.println("Error.");
              }

              // check if in part of line desired
              for (int i = 0; i < stops3.size(); i++)
              {
                if (stops3.get(i).getLineEDI().equalsIgnoreCase(lineChoice) && stops3.get(i).getOrder() <= endStop && stops3.get(i).getOrder() >= startStop)
                {
                  stopCount++;
                  Stop addStop = new Stop(stops3.get(i).getID(), stops3.get(i).getName(), stops3.get(i).getLat(), stops3.get(i).getLon(), lineName, stopCount);
                  custom.add(new Stop(stops3.get(i).getID(), stops3.get(i).getName(), stops3.get(i).getLat(), stops3.get(i).getLon()));
                }
              }
            }
            in.nextLine(); // cancel out running rolling twice
          }
            
          else
          {
            for (int i = 0; i < stops.size(); i++)
            {
              if (stops.get(i).getID().equalsIgnoreCase(customStop))
              {
                stopCount++;
                System.out.print("Added: " + stops.get(i).getName() + " ");
                Stop addStop = new Stop(stops.get(i).getID(), stops.get(i).getName(), stops.get(i).getLat(), stops.get(i).getLon(), lineName, stopCount);
                stops.add(addStop);
                custom.add(stops.get(i));
                break;
              }          
            }
          }
          if (!customStop.equals("-0"))
          {
            rollingEDI(custom); // this is where shit gets stupid
          }     
        }

        theLine = new Stop[custom.size()];
        for (int i = 0; i < custom.size(); i++)
        {
          theLine[i] = custom.get(i);
        }

        System.out.print("Export line? ");
        if (in.nextLine().equalsIgnoreCase("yes"))
        {
          export = true;
        }
      }

      // segments of routes
      else if (lineChoice.equalsIgnoreCase("segment"))
      {
        // loads in EDI file with existing routes only
        try
        {
          Scanner s = new Scanner(new URI("https://dev.eliotindex.org/files/" + agencyChoice + "-edi.txt").toURL().openStream());
          while (s.hasNextLine())
          {
            String data = s.nextLine();
            String id = data.substring(0, data.indexOf(";"));
            data = data.substring(data.indexOf(";") + 1);
            String name = data.substring(0, data.indexOf(";"));
            data = data.substring(data.indexOf(";") + 1);
            double lat = Double.parseDouble(data.substring(0, data.indexOf(";")));
            data = data.substring(data.indexOf(";") + 1);
            double lon = Double.parseDouble(data.substring(0, data.indexOf(";")));
            data = data.substring(data.indexOf(";") + 1); // lines
            String line = data.substring(0, data.indexOf(";"));
            data = data.substring(data.indexOf(";") + 1);
            int order = Integer.parseInt(data);

            stops.add(new Stop(id, name, lat, lon, line, order));
          }
        }
        catch (Exception e)
        {
          System.out.println("Error.");
        }
        System.out.print("Enter line: ");
        lineChoice = in.nextLine();
        System.out.print("Starting stop: ");
        int startStop = in.nextInt();
        System.out.print("Ending stop: ");
        int endStop = in.nextInt();
        

        // check if in part of line desired
        for (int i = 0; i < stops.size(); i++)
        {
          if (stops.get(i).getLineEDI().equalsIgnoreCase(lineChoice) && stops.get(i).getOrder() <= endStop && stops.get(i).getOrder() >= startStop)
          {
            stopCount++;
          }
        }

        if (stopCount == 0)
        {
          System.out.print("Invalid segment. ");
          edi();
          break;
        }

        theLine = new Stop[stopCount];
        for (int i = 0; i < stops.size(); i++)
        {
          if (stops.get(i).getLineEDI().equalsIgnoreCase(lineChoice) && stops.get(i).getOrder() <= endStop && stops.get(i).getOrder() >= startStop)
          {
            theLine[stops.get(i).getOrder() - startStop] = stops.get(i);
          }
        }
      }

      else if (lineChoice.equalsIgnoreCase("reverse")) // reverses order
      {
        // loads in EDI file with existing routes only
        try
        {
          Scanner s = new Scanner(new URI("https://dev.eliotindex.org/files/" + agencyChoice + "-edi.txt").toURL().openStream());
          while (s.hasNextLine())
          {
            String data = s.nextLine();
            String id = data.substring(0, data.indexOf(";"));
            data = data.substring(data.indexOf(";") + 1);
            String name = data.substring(0, data.indexOf(";"));
            data = data.substring(data.indexOf(";") + 1);
            double lat = Double.parseDouble(data.substring(0, data.indexOf(";")));
            data = data.substring(data.indexOf(";") + 1);
            double lon = Double.parseDouble(data.substring(0, data.indexOf(";")));
            data = data.substring(data.indexOf(";") + 1); // lines
            String line = data.substring(0, data.indexOf(";"));
            data = data.substring(data.indexOf(";") + 1);
            int order = Integer.parseInt(data);

            stops.add(new Stop(id, name, lat, lon, line, order));
          }
        }
        catch (Exception e)
        {
          System.out.println("Error.");
        }

        System.out.print("Enter line: ");
        lineChoice = in.nextLine();
        

        for (int i = 0; i < stops.size(); i++)
        {
          if (stops.get(i).getLineEDI().equalsIgnoreCase(lineChoice))
          {
            stopCount++;
          }
        }

        if (stopCount == 0)
        {
          System.out.print("Invalid line. ");
          edi();
          break;
        }

        // reversing using temp array
        Stop [] tempLine = new Stop[stopCount];
        theLine = new Stop[stopCount];
        for (int i = 0; i < stops.size(); i++)
        {
          if (stops.get(i).getLineEDI().equalsIgnoreCase(lineChoice))
          {
            tempLine[stops.get(i).getOrder() - 1] = stops.get(i);
          }
        }

        int remain = tempLine.length;
        for (int i = 0; i < tempLine.length; i++)
        {
          theLine[i] = tempLine[remain - 1];
          remain--;
        }
      }

      // existing line
      else
      { 
        // loads in EDI file with existing routes only
        try
        {
          Scanner s = new Scanner(new URI("https://dev.eliotindex.org/files/" + agencyChoice + "-edi.txt").toURL().openStream());
          while (s.hasNextLine())
          {
            String data = s.nextLine();
            String id = data.substring(0, data.indexOf(";"));
            data = data.substring(data.indexOf(";") + 1);
            String name = data.substring(0, data.indexOf(";"));
            data = data.substring(data.indexOf(";") + 1);
            double lat = Double.parseDouble(data.substring(0, data.indexOf(";")));
            data = data.substring(data.indexOf(";") + 1);
            double lon = Double.parseDouble(data.substring(0, data.indexOf(";")));
            data = data.substring(data.indexOf(";") + 1); // lines
            String line = data.substring(0, data.indexOf(";"));
            data = data.substring(data.indexOf(";") + 1);
            int order = Integer.parseInt(data);

            stops.add(new Stop(id, name, lat, lon, line, order));
          }
        }
        catch (Exception e)
        {
          System.out.println("Error.");
        }
        for (int i = 0; i < stops.size(); i++)
        {
          if (stops.get(i).getLineEDI().equalsIgnoreCase(lineChoice))
          {
            stopCount++;
          }
        }

        if (stopCount == 0)
        {
          System.out.print("Invalid line. ");
          edi();
          break;
        }

        theLine = new Stop[stopCount];
        for (int i = 0; i < stops.size(); i++)
        {
          if (stops.get(i).getLineEDI().equalsIgnoreCase(lineChoice))
          {
            theLine[stops.get(i).getOrder() - 1] = stops.get(i);
          }
        }
      }

      // rolling EDI for each stop added
      for (int i = 0; i < theLine.length; i++)
      {
        System.out.print("- " + theLine[i].getName() + " (" + theLine[i].getID() + ") ");
        ArrayList<Stop> custom2 = new ArrayList<Stop>(); // array for rolling EDI
        for (int j = 0; j <= i; j++)
        {
          custom2.add(theLine[j]);
        }
        rollingEDI(custom2);
      }

      // haversine formula loop
      double dist = 0;
      for (int i = 1; i < theLine.length; i++)
      {
        double lon1 = Math.toRadians(Math.abs(theLine[i - 1].getLon()));
        double lon2 = Math.toRadians(Math.abs(theLine[i].getLon()));
        double lat1 = Math.toRadians(Math.abs(theLine[i - 1].getLat()));
        double lat2 = Math.toRadians(Math.abs(theLine[i].getLat()));
        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dlon / 2), 2);      
        double c = 2 * Math.asin(Math.sqrt(a));
        double r = 3963;

        dist += c * r;
      }

      // full line haversine
      double firstLon = Math.toRadians(Math.abs(theLine[0].getLon()));
      double lastLon = Math.toRadians(Math.abs(theLine[theLine.length  - 1].getLon())); // 11 is temp
      double firstLat = Math.toRadians(Math.abs(theLine[0].getLat()));
      double lastLat = Math.toRadians(Math.abs(theLine[theLine.length - 1].getLat())); // 11 is temp
      double difflon = lastLon - firstLon;
      double difflat = lastLat - firstLat;
      double a1 = Math.pow(Math.sin(difflat / 2), 2) + Math.cos(firstLat) * Math.cos(lastLat) * Math.pow(Math.sin(difflon / 2), 2);      
      double c1 = 2 * Math.asin(Math.sqrt(a1));
      double r1 = 3963;
      double lineDist = c1 * r1;

      // calculate the edi
      double edi = dist / lineDist;
      edi = Math.round(edi * 100.0) / 100.0;
      if (edi < 1) // case for the 0.9x
      {
        edi = 1.0;
      }

      // assorted calcs
      dist = Math.round(dist * 100.0) / 100.0;
      double avgStop = dist / (theLine.length - 1);
      avgStop = Math.round(avgStop * 100.0) / 100.0;

      // display rounded
      System.out.println("Distance: " + dist + " miles");
      System.out.println("Eliot Deviation Index: " + edi);
      System.out.println("Average Stop Spacing: " + avgStop + " miles");

      if (export)
      {
        System.out.println("Export File (copy below this line):");
        System.out.println("Agency: " + agencyChoice);
        System.out.println("Line: " + lineName);

        for (int i = 0; i < theLine.length; i++)
        {
          System.out.println(theLine[i].getID());
        }
        
        System.out.println("-0");
      }

      System.out.print("Enter next line (-00 to exit): ");
      lineChoice = in.nextLine();
    }
  }

  // rolling calculator for custom lines, this is a stupid idea but it's 1 am and i feel like adding it
  public static void rollingEDI(ArrayList<Stop> custom)
  {
    Stop [] theLine = new Stop[custom.size()];
    for (int i = 0; i < custom.size(); i++)
    {
      theLine[i] = custom.get(i);
    }

    // haversine formula loop
    double dist = 0;
    for (int i = 1; i < theLine.length; i++)
    {
      double lon1 = Math.toRadians(Math.abs(theLine[i - 1].getLon()));
      double lon2 = Math.toRadians(Math.abs(theLine[i].getLon()));
      double lat1 = Math.toRadians(Math.abs(theLine[i - 1].getLat()));
      double lat2 = Math.toRadians(Math.abs(theLine[i].getLat()));
      double dlon = lon2 - lon1;
      double dlat = lat2 - lat1;
      double a = Math.pow(Math.sin(dlat / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dlon / 2), 2);      
      double c = 2 * Math.asin(Math.sqrt(a));
      double r = 3963;

      dist += c * r;
    }

    // full line haversine
    double firstLon = Math.toRadians(Math.abs(theLine[0].getLon()));
    double lastLon = Math.toRadians(Math.abs(theLine[theLine.length  - 1].getLon())); // 11 is temp
    double firstLat = Math.toRadians(Math.abs(theLine[0].getLat()));
    double lastLat = Math.toRadians(Math.abs(theLine[theLine.length - 1].getLat())); // 11 is temp
    double difflon = lastLon - firstLon;
    double difflat = lastLat - firstLat;
    double a1 = Math.pow(Math.sin(difflat / 2), 2) + Math.cos(firstLat) * Math.cos(lastLat) * Math.pow(Math.sin(difflon / 2), 2);      
    double c1 = 2 * Math.asin(Math.sqrt(a1));
    double r1 = 3963;
    double lineDist = c1 * r1;

    // calculate the edi
    double edi = dist / lineDist;
    edi = Math.round(edi * 100.0) / 100.0;
    if (edi < 1) // case for the 0.9x
    {
      edi = 1.0;
    }

    dist = Math.round(dist * 100.0) / 100.0;
    System.out.print("(" + theLine.length + ", " + dist + ", ");
    System.out.println(edi + ")");
  }
}