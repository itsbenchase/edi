import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;

// not public, too niche for js adaptation

public class Segment
{
  public static void main(String [] args)
  {
    Scanner in = new Scanner(System.in);
    ArrayList<Stop> stops = new ArrayList<Stop>();
    Calculator calc = new Calculator();
    
    // get agency
    System.out.print("Enter agency: ");
    String agencyChoice = in.nextLine();

    // segment code from Calculator.java
    // loads in EDI file with existing routes only
    try
    {
      Scanner s = new Scanner(new File("../files/" + agencyChoice + "-edi.txt"));
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

    int choice = 1;
    while (choice == 1)
    {
      System.out.print("Enter line: ");
      String lineChoice = in.nextLine();
      System.out.print("Starting stop: ");
      int startStop = in.nextInt();
      // int endStop is going to change
  
      // how long is the line
      int totalStops = 0; // total line length
      for (int i = 0; i < stops.size(); i++)
      {
        if (stops.get(i).getLineEDI().equalsIgnoreCase(lineChoice))
        {
          totalStops++;
        }
      }
      
      // this part gets looped
      Stop [] theLine;
      for (int a = 1; a <= totalStops; a++) // a will be endStop equivalent
      {
        int stopCount = 0;
        // check if in part of line desired
        if (a == startStop)
        {
          // bypass.
        }
        else if (a > startStop)
        {
          for (int i = 0; i < stops.size(); i++)
          {
            if (stops.get(i).getLineEDI().equalsIgnoreCase(lineChoice) && stops.get(i).getOrder() <= a && stops.get(i).getOrder() >= startStop)
            {
              stopCount++;
            }
          }
      
          theLine = new Stop[stopCount];
          for (int i = 0; i < stops.size(); i++)
          {
            if (stops.get(i).getLineEDI().equalsIgnoreCase(lineChoice) && stops.get(i).getOrder() <= a && stops.get(i).getOrder() >= startStop)
            {
              theLine[stops.get(i).getOrder() - startStop] = stops.get(i);
            }
          }
          // display
          System.out.println(startStop + " - " + a + ": " + calc.calcIndex(theLine));
        }
        
        else if (a < startStop)
        {
          for (int i = 0; i < stops.size(); i++)
          {
            if (stops.get(i).getLineEDI().equalsIgnoreCase(lineChoice) && stops.get(i).getOrder() <= startStop && stops.get(i).getOrder() >= a)
            {
              stopCount++;
            }
          }
      
          theLine = new Stop[stopCount];
          for (int i = 0; i < stops.size(); i++)
          {
            if (stops.get(i).getLineEDI().equalsIgnoreCase(lineChoice) && stops.get(i).getOrder() <= startStop && stops.get(i).getOrder() >= a)
            {
              theLine[stops.get(i).getOrder() - a] = stops.get(i);
            }
          }
          // display
          System.out.println(a + " - " + startStop + ": " + calc.calcIndex(theLine));
        }
      }

      System.out.print("Enter 1 to search again: ");
      choice = in.nextInt();
      in.nextLine(); // absorb enter
    }
  }
}