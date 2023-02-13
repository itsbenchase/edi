import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;

public class stopMap
{
  public static void main(String [] args)
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
      ArrayList<Stop> load = new ArrayList<Stop>();
      ArrayList<String> maps = new ArrayList<String>();
      String color = "ff0000ff";
      try
      {
        Scanner s = new Scanner(new File("stops/" + agencies.get(a) + ".txt"));
        while (s.hasNextLine())
        {
          String data = s.nextLine();
          String id = data.substring(0, data.indexOf(";"));
          id = id.replace("&", "&amp");
          data = data.substring(data.indexOf(";") + 1);
          String name = data.substring(0, data.indexOf(";"));
          name = name.replace("&", "&amp;");
          data = data.substring(data.indexOf(";") + 1);
          double lat = Double.parseDouble(data.substring(0, data.indexOf(";")));
          data = data.substring(data.indexOf(";") + 1);
          double lon = Double.parseDouble(data);

          load.add(new Stop(id, name, lat, lon));
        }
      }
      catch (Exception e)
      {
        System.out.println("Error, no stop file for " + agencies.get(a) + ".");
      }

      maps.add("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
      maps.add("<kml xmlns=\"http://www.opengis.net/kml/2.2\">");
      maps.add("<Document> \n\t<name>" + agencies.get(a) + " stops</name>");
      maps.add("\t<Style id=\"stop\"> \n\t\t<IconStyle> \n\t\t\t<scale>0.5</scale> \n\t\t\t<Icon> \n\t\t\t\t<href>icon.png</href> \n\t\t\t</Icon> \n\t\t</IconStyle> \n\t</Style>");
      maps.add("\t<Placemark> \n\t\t<styleUrl>#stop</styleUrl>");
      maps.add("\t\t<description>Name: " + load.get(0).getName() + "<br/>ID: " + load.get(0).getID() + "</description>");
      maps.add("\t\t<Point> \n\t\t\t<coordinates>");
      maps.add("\t\t\t" + load.get(0).getLon() + "," + load.get(0).getLat() + ",0");

      for (int i = 1; i < load.size(); i++)
      {
        maps.add("\t\t\t</coordinates> \n\t\t</Point> \n\t</Placemark>");
        maps.add("\t<Placemark> \n\t\t<styleUrl>#stop</styleUrl>");
        maps.add("\t\t<description>Name: " + load.get(i).getName() + "<br/>ID: " + load.get(i).getID() + "</description>");
        maps.add("\t\t<Point> \n\t\t\t<coordinates>");
        maps.add("\t\t\t" + load.get(i).getLon() + "," + load.get(i).getLat() + ",0");
      }

      maps.add("\t\t\t</coordinates> \n\t\t</Point> \n\t</Placemark> \n</Document> \n</kml>");

      try
      {
        File newFile1 = new File("maps/stops/map-" + agencies.get(a) + ".kml");
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
}