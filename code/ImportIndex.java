import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

// internal use only, takes from GTFS feed on ben's computer

public class ImportIndex
{
  public static void main(String [] args)
  {
    Scanner in = new Scanner(System.in);
    System.out.print("Enter agency code: ");
    String agency = in.nextLine();
    System.out.print("Enter import code: ");
    String importCode = in.nextLine();

    ArrayList<String> importFile = new ArrayList<String>();

    try
    {
      Scanner s = new Scanner(new File("../../gtfs/" + agency + "-export-" + importCode + ".txt"));
      int z = 0; // skip first
      while (s.hasNextLine())
      {
        if (z == 0)
        {
          s.nextLine();
          z++;
        }
        else
        {
          String data = s.nextLine();
          importFile.add(data); 
        }
      }
    }
    catch (Exception e)
    {
      System.out.println("Error - Import file not found.");
    }

    // stop import
    // load in only existing stops
    ArrayList<Stop> stops = new ArrayList<Stop>();
    try
    {
      Scanner s = new Scanner(new File("../stops/list/" + agency + ".txt"));
      int z = 0; // debugging in case of issue
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
        z++;
        System.out.println(z);
        stops.add(new Stop(id, name, lat, lon));
      }
    }
    catch (Exception e)
    {
      System.out.println("Error. can't load stops");
    }

    while (importFile.size() > 0)
    {
      int stopCount = 0;
      String code = importFile.get(0).substring(2);
      String name = importFile.get(1).substring(2);
      String branch = importFile.get(2).substring(2);

      ArrayList<Stop> custom = new ArrayList<Stop>();

      int stopPos = 3;
      for (int i = 3; !importFile.get(i).equals("-0"); i++)
      {
        for (int j = 0; j < stops.size(); j++)
        {
          if (stops.get(j).getID().equalsIgnoreCase(importFile.get(i)))
          {
            stopCount++;
            Stop addStop = new Stop(stops.get(j).getID(), stops.get(j).getName(), stops.get(j).getLat(), stops.get(j).getLon(), name, stopCount);
            custom.add(stops.get(j));
          }          
        }

        stopPos = i;
      }

      Stop [] theLine = new Stop[custom.size()];
      for (int i = 0; i < custom.size(); i++)
      {
        theLine[i] = custom.get(i);
      }

      Calculator index = new Calculator();
      index.saveIndex(theLine, code, name, branch, agency);

      for (int i = 0; i <= stopPos + 1; i++)
      {
        importFile.remove(0);
      }
    }
  }
}
