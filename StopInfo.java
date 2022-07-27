import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;

public class StopInfo
{
  public static void main(String [] args)
  {
    ArrayList<Stop> stops = new ArrayList<Stop>();

    try
    {
      Scanner s = new Scanner(new File("rail-stops.txt"));
      
      int count = 0; // amount of stops
      while (s.hasNextLine())
      {
        String data = s.nextLine();
        String name = data.substring(0, data.indexOf(";"));
        data = data.substring(data.indexOf(";") + 1);
        double lat = Double.parseDouble(data.substring(0, data.indexOf(";")));
        data = data.substring(data.indexOf(";") + 1);
        double lon = Double.parseDouble(data.substring(0, data.indexOf(";")));
        data = data.substring(data.indexOf(";") + 1); // remaining - list of lines

        stops.add(new Stop(name, lat, lon));

        if (data.indexOf(",") < 0)
        {
          stops.get(count).addLine(data);
        }
        else
        {
          while (data.indexOf(",") > 0)
          {
            stops.get(count).addLine(data.substring(0, data.indexOf(",")));
            data = data.substring(data.indexOf(",") + 1);
          }
          stops.get(count).addLine(data);
        }

        count++;
      }
    }
    catch (FileNotFoundException e)
    {
      System.out.println("Error.");
    }

    Scanner in = new Scanner(System.in);
    System.out.print("Stop or line: ");
    String choice1 = in.nextLine();

    if (choice1.equalsIgnoreCase("stop"))
    {
      System.out.print("Enter stop: ");
      String choice2 = in.nextLine();

      int stopNum = -1;

      for (int i = 0; i < stops.size(); i++)
      {
        if (choice2.equalsIgnoreCase(stops.get(i).getName()))
        {
          stopNum = i;
          break;
        }
      }

      System.out.println("Location: " + stops.get(stopNum).getLat() + ", " + stops.get(stopNum).getLon());
      System.out.println("Lines: " + stops.get(stopNum).getLine());
    }
    else if (choice1.equalsIgnoreCase("line"))
    {
      ArrayList<String> lineStops = new ArrayList<String>();

      System.out.print("Enter line: ");
      String choice3 = in.nextLine();

      for (int i = 0; i < stops.size(); i++)
      {
        for (int j = 0; j < stops.get(i).getLine().size(); j++)
        {
          if (choice3.equalsIgnoreCase(stops.get(i).getLine().get(j)))
          {
            lineStops.add(stops.get(i).getName());
          }
        }
      }

      int a = 0;
      while (a < lineStops.size())
      {
        System.out.println("- " + lineStops.get(a));
        a++;
      }
    }
  }
}