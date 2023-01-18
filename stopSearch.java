import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class stopSearch
{
  public static void main(String [] args)
  {
    System.out.println("Eliot Deviation Index");
    System.out.println("Stop Database");
    System.out.println("-----------------------");
    
    System.out.println("");
    
    System.out.println("INSTRUCTIONS");
    System.out.println("- To search one agency, enter the agency code as listed on the stop listing webpage, https://edi.benchase.info/stops.html.");
    System.out.println("- To search the global database, enter \"global\".");
    System.out.println("- Program will list all terms containing the contents entered, and display stop ID and agency (if searching the global database).");
    
    System.out.println("");
    
    Scanner s2 = new Scanner(System.in);
    ArrayList<Stop> stops = new ArrayList<Stop>();
    ArrayList<String> agencies = new ArrayList<String>(); // for global search

    System.out.print("Enter agency: ");
    String agencyChoice = s2.nextLine();

    int cont = 1; 
    
    // loads stop file
    if (agencyChoice.equals("global")) // global search
    {
      // first get agencies
      try
      {
        Scanner a = new Scanner(new File("agencies.txt"));
        while (a.hasNextLine())
        {
          String data = a.nextLine();
          String code = data.substring(0, data.indexOf(";"));
          agencies.add(code);
        }
      }
      catch (FileNotFoundException e)
      {
        System.out.println("Error, can't load agencies.txt.");
      }

      // loop through all agencies
      for (int i = 0; i < agencies.size(); i++)
      {
        try
        {
          Scanner s = new Scanner(new File("stops/" + agencies.get(i) + ".txt"));
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

            stops.add(new Stop(agencies.get(i), id, name, lat, lon));
          }
        }
        catch (FileNotFoundException e)
        {
          System.out.println("Error, can't load stop file " + agencyChoice + ".");
        }
      }
    }

    else
    {
      try
      {
        Scanner s = new Scanner(new File("stops/" + agencyChoice + ".txt"));
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
      catch (FileNotFoundException e)
      {
        System.out.println("Error, can't load stop file " + agencyChoice + ".");
      }
    }

    while (cont == 1) // loop it
    {
      ArrayList<String> results = new ArrayList<String>();
      int resultCount = 0;

      System.out.print("Enter search: ");
      String search = s2.nextLine();

      for (int i = 0; i < stops.size(); i++)
      {
        if (stops.get(i).getName().toLowerCase().contains(search.toLowerCase()))
        {
          if (agencyChoice.equals("global"))
          {
            results.add("- " + stops.get(i).getName() + " (" + stops.get(i).getAgency() + " | " + stops.get(i).getID() + ")");
          }
          else
          {
            results.add("- " + stops.get(i).getName() + " (" + stops.get(i).getID() + ")");
          }
          resultCount++;
        }
      }

      Collections.sort(results);

      for (int i = 0; i < results.size(); i++)
      {
        System.out.println(results.get(i));
      }
      System.out.println("Results: " + resultCount);

      System.out.print("Enter 1 to search again: ");
      cont = s2.nextInt();

      s2.nextLine(); // absorbed enter
    }
  }
}