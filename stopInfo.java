import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class stopInfo
{
  public static void main(String [] args)
  {
    Scanner s2 = new Scanner(System.in);
    ArrayList<Stop> stops = new ArrayList<Stop>();

    System.out.print("Enter agency: ");
    String agencyChoice = s2.nextLine();

    int cont = 1; 
    
    // loads stop file
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

    /*while (cont == 1) // loop it
    {*/
      System.out.print("Stop ID: ");
      String stopChoice = s2.nextLine();

      for (int i = 0; i < stops.size(); i++)
      {
        if (stops.get(i).getID().equalsIgnoreCase(stopChoice))
        {
          System.out.println("Stop Name: " + stops.get(i).getName());
          System.out.println("Stop Lat: " + stops.get(i).getLat());
          System.out.println("Stop Lon: " + stops.get(i).getLon());
        }
      }

      PopulationData pop = new PopulationData(agencyChoice, stopChoice);
      pop.main(args);

      /*System.out.print("Enter 1 to search again: ");
      cont = s2.nextInt();

      s2.nextLine(); // absorbed enter
    }*/
  }
}