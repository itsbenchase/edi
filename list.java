import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class list
{
  public static void main(String [] args)
  {
    ArrayList<String> agencies = new ArrayList<String>();
    ArrayList<String> fullAgencies = new ArrayList<String>();
    
    try
    {
      Scanner s = new Scanner(new File("agencies.txt"));
      while (s.hasNextLine())
      {
        String data = s.nextLine();
        String code = data.substring(0, data.indexOf(";"));
        agencies.add(code);
        data = data.substring(data.indexOf(";") + 1);
        fullAgencies.add(data);
      }
    }
    catch (FileNotFoundException e)
    {
      System.out.println("Error 1 - No agencies.txt.");
    }

    int stopCounter = 0; // total amount of stops
    try
    {
      File newFile1 = new File("stops.html");
      FileWriter fileWriter1 = new FileWriter(newFile1);

      fileWriter1.write("<title>Stop Listing - Eliot Deviation Index</title> \n");
      fileWriter1.append("<link rel=stylesheet href=style.css> \n");
      fileWriter1.append("<ul><li><a href=index.html>Home</a></li>");
      fileWriter1.append("<li><a href=stops.html class=active>Stop Listing</a></li> \n");
      fileWriter1.append("<li><a href=routes.html>Route Listing</a></li> \n");
      fileWriter1.append("<li><a href=calculator.html>Calculator</a></li></ul> \n");
      fileWriter1.append("<h1>Stop Listing</h1> \n");

      for (int i = 0; i < agencies.size(); i++)
      {
        fileWriter1.append("<ul class=bullet style=background-color:#d9ffde><a href=#" + agencies.get(i) + ">" + fullAgencies.get(i) + "</a></ul>");
      }

      for (int i = 0; i < agencies.size(); i++)
      {
        fileWriter1.append("<h3 id=" + agencies.get(i) + ">" + fullAgencies.get(i) + " (" + agencies.get(i) + ")</h3> \n");

        ArrayList<String> stopID = new ArrayList<String>();
        ArrayList<String> stopName = new ArrayList<String>();
        ArrayList<String> stopLat = new ArrayList<String>();
        ArrayList<String> stopLon = new ArrayList<String>();

        try
        {
          Scanner s = new Scanner(new File("stops/" + agencies.get(i) + ".txt"));
          while (s.hasNextLine())
          {
            String data = s.nextLine();
            String id = data.substring(0, data.indexOf(";"));
            stopID.add(id);
            data = data.substring(data.indexOf(";") + 1);
            String name = data.substring(0, data.indexOf(";"));
            stopName.add(name);
            data = data.substring(data.indexOf(";") + 1);
            String lat = data.substring(0, data.indexOf(";"));
            stopLat.add(lat);
            data = data.substring(data.indexOf(";") + 1);
            String lon = data;
            stopLon.add(lon);
          }
        }
        catch (FileNotFoundException e)
        {
          System.out.println("Error 3 - No EDI file.");
        }

        fileWriter1.append("<table><tr><th>Stop ID</th><th>Stop Name</th><th>Stop Latitude</th><th>Stop Longitude</th></tr> \n");

        for (int j = 0; j < stopID.size(); j++)
        {
          fileWriter1.append("<tr><td style=color:red>" + stopID.get(j) + "</td><td>" + stopName.get(j) + "</td><td>" + stopLat.get(j) + "</td><td>" + stopLon.get(j) + "</td></tr> \n");
          stopCounter++;
        }

        fileWriter1.append("</table> \n");
      }

      fileWriter1.append("<p><b>Total Stops: </b>" + stopCounter + "</p>");
      fileWriter1.close();
    }
    catch (IOException e)
    {
      System.out.println("Error 2 - Can't save stops.");
    }
  }
}