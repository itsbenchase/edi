import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;

public class StopSpacing
{
  public static void main(String [] args)
  {
    ArrayList<String> routes = new ArrayList<String>();
    ArrayList<Double> lengths = new ArrayList<Double>();
    ArrayList<String> routeCodes = new ArrayList<String>();
    
    Scanner in = new Scanner(System.in);

    System.out.print("Enter agency: ");
    String agency = in.nextLine();

    // get agency route file / /edis
    try
    {
      Scanner s = new Scanner(new File("../edis/" + agency + ".txt"));
      while (s.hasNextLine())
      {
        String data = s.nextLine();
        String route1 = data.substring(0, data.indexOf(";"));
        routes.add(route1);
        data = data.substring(data.indexOf(";") + 1);
        lengths.add(Double.parseDouble(data.substring(0, data.indexOf(";"))));
      }
    }
    catch (Exception e)
    {
      System.out.println("Error, no agency route file.");
    }

    // get agency edi file / /files
    try
    {
      Scanner s = new Scanner(new File("../files/" + agency + "-edi.txt"));
      while (s.hasNextLine())
      {
        String data = s.nextLine();
        data = data.substring(data.indexOf(";") + 1);
        data = data.substring(data.indexOf(";") + 1);
        data = data.substring(data.indexOf(";") + 1);
        data = data.substring(data.indexOf(";") + 1);
        routeCodes.add(data.substring(0, data.indexOf(";")));
      }
    }
    catch (Exception e)
    {
      System.out.println("Error, no agency edi file.");
    }    

    // loop through all routes
    for (int i = 0; i < routes.size(); i++)
    {
      int stopCount = 0;
      for (int j = 0; j < routeCodes.size(); j++)
      {
        if (routes.get(i).equals(routeCodes.get(j)))
        {
          stopCount++;
        }
      }

      double spacing = lengths.get(i) / (stopCount - 1);
      spacing = Math.round(spacing * 100.0) / 100.0;

      System.out.println(spacing);
    }

    // stop spacing is length (miles) / stop count - 1

    // resave to same /edis file?
  }
}