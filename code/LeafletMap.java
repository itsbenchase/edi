import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;

// creates new leaflet map
// january 2025

public class LeafletMap
{
  public static void main(String [] args)
  {
    ArrayList<String> agencies = new ArrayList<String>();
    try
    {
      Scanner s = new Scanner(new File("../agencies.txt"));
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

    ArrayList<String> maps = new ArrayList<String>();

    for (int a = 0; a < agencies.size(); a++)
    {
      ArrayList<Stop> load = new ArrayList<Stop>();
      ArrayList<Stop> routes = new ArrayList<Stop>();
    
      try
      {
        Scanner s = new Scanner(new File("../files/" + agencies.get(a) + "-edi.txt"));
        while (s.hasNextLine())
        {
          String data = s.nextLine();
          data = data.substring(data.indexOf(";") + 1);
          data = data.substring(data.indexOf(";") + 1);
          double lat = Double.parseDouble(data.substring(0, data.indexOf(";")));
          data = data.substring(data.indexOf(";") + 1);
          double lon = Double.parseDouble(data.substring(0, data.indexOf(";")));
          data = data.substring(data.indexOf(";") + 1); // lines
          String line = data.substring(0, data.indexOf(";"));

          load.add(new Stop(lat, lon, line));
        }
      }
      catch (Exception e)
      {
        continue;
        //System.out.println("Error (agency edi file).");
      }

      try
      {
        Scanner s = new Scanner(new File("../edis/" + agencies.get(a) + ".txt"));
        while (s.hasNextLine())
        {
          String data = s.nextLine();
          String line2 = data.substring(0, data.indexOf(";"));
          data = data.substring(data.indexOf(";") + 1);
          double length = Double.parseDouble(data.substring(0, data.indexOf(";")));
          data = data.substring(data.indexOf(";") + 1);
          double edi = Double.parseDouble(data.substring(0, data.indexOf(";")));
          data = data.substring(data.indexOf(";") + 1);
          String name = data.substring(0, data.indexOf(";"));
          data = data.substring(data.indexOf(";") + 1);
          String branch = data;

          routes.add(new Stop(line2, edi, length, name, branch));
        }
      }
      catch (Exception e)
      {
        System.out.println("Error (agency route list).");
      }

      for (int j = 0; j < routes.size(); j++)
      {
        if (load.get(0).getLineEDI().equals(routes.get(j).getLineEDI()))
        {
          maps.add(agencies.get(a) + ";" + routes.get(j).getLineEDI() + ";" + routes.get(j).getLineName() + ";" + routes.get(j).getBranch() + ";" + routes.get(j).getLength() + ";" + routes.get(j).getEdi() + ";" + load.get(0).getLat() + "," + load.get(0).getLon() + "");
        }
      }

      for (int i = 1; i < load.size(); i++)
      {
        if (load.get(i).getLineEDI().equals(load.get(i - 1).getLineEDI()))
        {
          maps.set(maps.size() - 1, maps.get(maps.size() - 1) + "," + load.get(i).getLat() + "," + load.get(i).getLon() + "");
        }
        else
        {
          for (int j = 0; j < routes.size(); j++)
          {
            if (load.get(i).getLineEDI().equals(routes.get(j).getLineEDI()))
            {
              maps.add(agencies.get(a) + ";" + routes.get(j).getLineEDI() + ";" + routes.get(j).getLineName() + ";" + routes.get(j).getBranch() + ";" + routes.get(j).getLength() + ";" + routes.get(j).getEdi() + ";" + load.get(i).getLat() + "," + load.get(i).getLon() + "");
            }
          }
        }
      }

      System.out.println("Leaflet Map: " + (a + 1) + " / " + agencies.size());
    }

    try
    {
      File newFile1 = new File("../maps/leaflet-all.txt");
      FileWriter fileWriter1 = new FileWriter(newFile1);

      fileWriter1.write(maps.get(0) + "\n");

      for (int i = 1; i < maps.size(); i++)
      {
        fileWriter1.append(maps.get(i) + "\n");
      }

      fileWriter1.close();
    }
    catch (Exception e)
    {
      System.out.println("Error.");
    }
  }
}