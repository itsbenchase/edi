import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class addStop
{
  public static void main(String [] args)
  {
    Scanner s2 = new Scanner(System.in);
    ArrayList<Stop> stops = new ArrayList<Stop>();

    System.out.print("Enter agency: ");
    String agencyChoice = s2.nextLine();

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
      System.out.println("Error.");
    }

    System.out.print("Enter Stop ID: ");
    String newId = s2.nextLine();
    System.out.print("Enter Stop Name: ");
    String newName = s2.nextLine();
    System.out.print("Enter Stop Lat: ");
    Double newLat = s2.nextDouble();
    System.out.print("Enter Stop Lon: ");
    Double newLon = s2.nextDouble();

    stops.add(new Stop(newId, newName, newLat, newLon));
    System.out.println("Stop added.");

    try
    {
      File newFile1 = new File("stops/" + agencyChoice + ".txt");
      FileWriter fileWriter1 = new FileWriter(newFile1);

      fileWriter1.write(stops.get(0).getID() + ";" + stops.get(0).getName() + ";" + stops.get(0).getLat() + ";" + stops.get(0).getLon() + "\n");

      for (int i = 1; i < stops.size(); i++)
      {
        fileWriter1.append(stops.get(i).getID() + ";" + stops.get(i).getName() + ";" + stops.get(i).getLat() + ";" + stops.get(i).getLon() + "\n");
      }

      fileWriter1.close();

      System.out.println("Complete.");
    }
    catch (IOException e)
    {
      System.out.println("Error.");
    }
  }
}