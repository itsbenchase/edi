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

    for (int i = 0; i < agencies.size(); i++)
    {
      try
      {
        int agencyStops = 0; // agency stop counter
        File newFile1 = new File("stops/" + agencies.get(i) + ".html");
        FileWriter fileWriter1 = new FileWriter(newFile1);

        fileWriter1.write("<title>" + fullAgencies.get(i) + " Stops - Eliot Deviation Index</title> \n");
        fileWriter1.append("<link rel=stylesheet href=../style.css> \n");
        fileWriter1.append("<ul><li><a href=../index.html>Home</a></li>");
        fileWriter1.append("<li><a href=../stops.html class=active>Stop Listing</a></li> \n");
        fileWriter1.append("<li><a href=../routes.html>Route Listing</a></li> \n");
        fileWriter1.append("<li><a href=../calculator.html>Calculator</a></li></ul> \n");
        fileWriter1.append("<h1>Stop Listing</h1> \n");

        fileWriter1.append("<h3>" + fullAgencies.get(i) + " (" + agencies.get(i) + ")</h3> \n");

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
          System.out.println("Error 2 - No stop file.");
        }

        fileWriter1.append("<table><tr><th>Stop ID</th><th>Stop Name</th><th>Stop Latitude</th><th>Stop Longitude</th></tr> \n");

        for (int j = 0; j < stopID.size(); j++)
        {
          fileWriter1.append("<tr><td style=color:red>" + stopID.get(j) + "</td><td>" + stopName.get(j) + "</td><td>" + stopLat.get(j) + "</td><td>" + stopLon.get(j) + "</td></tr> \n");
          agencyStops++;
          stopCounter++;
        }

        fileWriter1.append("</table> \n");
        fileWriter1.append("<p><b>Agency Stops:</b> " + agencyStops + "</p>");
        fileWriter1.flush();
        fileWriter1.close();
      }
      catch (IOException e)
      {
        System.out.println("Error 3 - Can't save stops.");
      }
    }

    try
    {
      File newFile2 = new File("stops.html");
      FileWriter fileWriter2 = new FileWriter(newFile2);
      fileWriter2.write("<title>Stop Listing - Eliot Deviation Index</title> \n");
      fileWriter2.append("<link rel=stylesheet href=style.css> \n");
      fileWriter2.append("<ul><li><a href=index.html>Home</a></li>");
      fileWriter2.append("<li><a href=stops.html class=active>Stop Listing</a></li> \n");
      fileWriter2.append("<li><a href=routes.html>Route Listing</a></li> \n");
      fileWriter2.append("<li><a href=calculator.html>Calculator</a></li></ul> \n");
      fileWriter2.append("<h1>Stop Listing</h1> \n");
      fileWriter2.append("<p> \n");

      for (int a = 0; a < agencies.size(); a++)
      {
        fileWriter2.append("<ul class=bullet style=background-color:#d9ffde><a href=stops/" + agencies.get(a) + ".html>" + fullAgencies.get(a) + "</a></ul>");
      }

      fileWriter2.append("<p><b>Total Stops: </b>" + stopCounter + "</p>");
      fileWriter2.flush();
      fileWriter2.close();
    }
    catch (Exception e)
    {
      System.out.println("Error 4 - Can't create stop home page.");
    }
  }
}