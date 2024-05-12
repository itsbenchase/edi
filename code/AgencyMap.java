import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;

// creates individual agency maps
// rewritten may 2024, agency maps public

public class AgencyMap
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

    for (int a = 0; a < agencies.size(); a++)
    {
      ArrayList<Stop> load = new ArrayList<Stop>();
      ArrayList<Stop> routes = new ArrayList<Stop>();
      ArrayList<String> maps = new ArrayList<String>();
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

          name = name.replace("&", "&amp;");
          branch = branch.replace("&", "&amp;");

          routes.add(new Stop(line2, edi, length, name, branch));
        }
      }
      catch (Exception e)
      {
        System.out.println("Error (agency route list).");
      }

      maps.add("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
      maps.add("<kml xmlns=\"http://www.opengis.net/kml/2.2\">");
      maps.add("<Document> \n\t<name>" + agencies.get(a) + " routes</name>");

      maps.add("<Schema name=\"RouteData\" id=\"RouteData\">");
      maps.add("<SimpleField type=\"string\" name=\"Agency\"><displayName>Agency</displayName></SimpleField>");
      maps.add("<SimpleField type=\"string\" name=\"LineCode\"><displayName>Line Code</displayName></SimpleField>");
      maps.add("<SimpleField type=\"string\" name=\"LineName\"><displayName>Line Name</displayName></SimpleField>");
      maps.add("<SimpleField type=\"string\" name=\"LineBranch\"><displayName>Branch</displayName></SimpleField>");
      maps.add("<SimpleField type=\"double\" name=\"LineLength\"><displayName>Length</displayName></SimpleField>");
      maps.add("<SimpleField type=\"double\" name=\"LineEDI\"><displayName>EDI</displayName></SimpleField>");
      maps.add("</Schema>");

      maps.add("\t<Style id=\"1.0\"> \n\t\t<LineStyle> \n\t\t\t<color>ff00701e</color> \n\t\t\t<width>4.0</width> \n\t\t</LineStyle> \n\t\t</Style>");
      maps.add("\t<Style id=\"1.5\"> \n\t\t<LineStyle> \n\t\t\t<color>ff00a054</color> \n\t\t\t<width>4.0</width> \n\t\t</LineStyle> \n\t\t</Style>");
      maps.add("\t<Style id=\"2.0\"> \n\t\t<LineStyle> \n\t\t\t<color>ff00cfa4</color> \n\t\t\t<width>4.0</width> \n\t\t</LineStyle> \n\t\t</Style>");
      maps.add("\t<Style id=\"2.5\"> \n\t\t<LineStyle> \n\t\t\t<color>ff00f2ff</color> \n\t\t\t<width>4.0</width> \n\t\t</LineStyle> \n\t\t</Style>");
      maps.add("\t<Style id=\"3.0\"> \n\t\t<LineStyle> \n\t\t\t<color>ff00a1ff</color> \n\t\t\t<width>4.0</width> \n\t\t</LineStyle> \n\t\t</Style>");
      maps.add("\t<Style id=\"3.5\"> \n\t\t<LineStyle> \n\t\t\t<color>ff0051ff</color> \n\t\t\t<width>4.0</width> \n\t\t</LineStyle> \n\t\t</Style>");
      maps.add("\t<Style id=\"4.0\"> \n\t\t<LineStyle> \n\t\t\t<color>ff0000ff</color> \n\t\t\t<width>4.0</width> \n\t\t</LineStyle> \n\t\t</Style>");

      for (int j = 0; j < routes.size(); j++)
      {
        if (load.get(0).getLineEDI().equals(routes.get(j).getLineEDI()))
        {
          maps.add("\t<Placemark> \n\t\t<name>" + routes.get(j).getLineEDI() + "</name>");
          
          // yeah yeah i gotta do this twice
          if (routes.get(j).getEdi() >= 1.0 && routes.get(j).getEdi() < 1.5)
          {
            maps.add("\t\t<styleUrl>#1.0</styleUrl>");
          }
          else if (routes.get(j).getEdi() >= 1.5 && routes.get(j).getEdi() < 2.0)
          {
            maps.add("\t\t<styleUrl>#1.5</styleUrl>");
          }
          else if (routes.get(j).getEdi() >= 2.0 && routes.get(j).getEdi() < 2.5)
          {
            maps.add("\t\t<styleUrl>#2.0</styleUrl>");
          }
          else if (routes.get(j).getEdi() >= 2.5 && routes.get(j).getEdi() < 3.0)
          {
            maps.add("\t\t<styleUrl>#2.5</styleUrl>");
          }
          else if (routes.get(j).getEdi() >= 3.0 && routes.get(j).getEdi() < 3.5)
          {
            maps.add("\t\t<styleUrl>#3.0</styleUrl>");
          }
          else if (routes.get(j).getEdi() >= 3.5 && routes.get(j).getEdi() < 4.0)
          {
            maps.add("\t\t<styleUrl>#3.5</styleUrl>");
          }
          else
          {
            maps.add("\t\t<styleUrl>#4.0</styleUrl>");
          }

          maps.add("<ExtendedData><SchemaData schemaUrl=\"#RouteData\">");
          maps.add("<SimpleData name=\"Agency\">" + agencies.get(a) + "</SimpleData>");
          maps.add("<SimpleData name=\"LineCode\">" + routes.get(j).getLineEDI() + "</SimpleData>");
          maps.add("<SimpleData name=\"LineName\">" + routes.get(j).getLineName() + "</SimpleData>");
          maps.add("<SimpleData name=\"LineBranch\">" + routes.get(j).getBranch() + "</SimpleData>");
          maps.add("<SimpleData name=\"LineLength\">" + routes.get(j).getLength() + "</SimpleData>");
          maps.add("<SimpleData name=\"LineEDI\">" + routes.get(j).getEdi() + "</SimpleData>");
          maps.add("</SchemaData></ExtendedData>");
        }
      }

      maps.add("\t\t<LineString> \n\t\t\t<coordinates>");
      maps.add("\t\t\t" + load.get(0).getLon() + "," + load.get(0).getLat() + ",0");
      for (int i = 1; i < load.size(); i++)
      {
        if (load.get(i).getLineEDI().equals(load.get(i - 1).getLineEDI()))
        {
          maps.add("\t\t\t" + load.get(i).getLon() + "," + load.get(i).getLat() + ",0");
        }
        else
        {
          maps.add("\t\t\t</coordinates> \n\t\t</LineString> \n\t</Placemark>");
          
          for (int j = 0; j < routes.size(); j++)
          {
            if (load.get(i).getLineEDI().equals(routes.get(j).getLineEDI()))
            {
              maps.add("\t<Placemark> \n\t\t<name>" + routes.get(j).getLineEDI() + "</name>");
              
              // yeah yeah i gotta do this twice
              if (routes.get(j).getEdi() >= 1.0 && routes.get(j).getEdi() < 1.5)
              {
                maps.add("\t\t<styleUrl>#1.0</styleUrl>");
              }
              else if (routes.get(j).getEdi() >= 1.5 && routes.get(j).getEdi() < 2.0)
              {
                maps.add("\t\t<styleUrl>#1.5</styleUrl>");
              }
              else if (routes.get(j).getEdi() >= 2.0 && routes.get(j).getEdi() < 2.5)
              {
                maps.add("\t\t<styleUrl>#2.0</styleUrl>");
              }
              else if (routes.get(j).getEdi() >= 2.5 && routes.get(j).getEdi() < 3.0)
              {
                maps.add("\t\t<styleUrl>#2.5</styleUrl>");
              }
              else if (routes.get(j).getEdi() >= 3.0 && routes.get(j).getEdi() < 3.5)
              {
                maps.add("\t\t<styleUrl>#3.0</styleUrl>");
              }
              else if (routes.get(j).getEdi() >= 3.5 && routes.get(j).getEdi() < 4.0)
              {
                maps.add("\t\t<styleUrl>#3.5</styleUrl>");
              }
              else
              {
                maps.add("\t\t<styleUrl>#4.0</styleUrl>");
              }

              maps.add("<ExtendedData><SchemaData schemaUrl=\"#RouteData\">");
              maps.add("<SimpleData name=\"Agency\">" + agencies.get(a) + "</SimpleData>");
              maps.add("<SimpleData name=\"LineCode\">" + routes.get(j).getLineEDI() + "</SimpleData>");
              maps.add("<SimpleData name=\"LineName\">" + routes.get(j).getLineName() + "</SimpleData>");
              maps.add("<SimpleData name=\"LineBranch\">" + routes.get(j).getBranch() + "</SimpleData>");
              maps.add("<SimpleData name=\"LineLength\">" + routes.get(j).getLength() + "</SimpleData>");
              maps.add("<SimpleData name=\"LineEDI\">" + routes.get(j).getEdi() + "</SimpleData>");
              maps.add("</SchemaData></ExtendedData>");
            }
          }
          maps.add("\t\t<LineString> \n\t\t\t<coordinates>");
          maps.add("\t\t\t" + load.get(i).getLon() + "," + load.get(i).getLat() + ",0");
        }
      }
      maps.add("\t\t\t</coordinates> \n\t\t</LineString> \n\t</Placemark> \n</Document> \n</kml>");

      try
      {
        File newFile1 = new File("../maps/map-" + agencies.get(a) + ".kml");
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

      System.out.println("Agency Maps: " + (a + 1) + " / " + agencies.size());
    }
  }
}