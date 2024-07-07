import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

// generates stop listing on website

public class StopList
{
  public static void main(String [] args)
  {
    ArrayList<String> agencies = new ArrayList<String>();
    ArrayList<String> fullAgencies = new ArrayList<String>();
    
    try
    {
      Scanner s = new Scanner(new File("../agencies.txt"));
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
      System.out.println("Error, no agencies.txt.");
    }

    int stopCounter = 0; // total amount of stops

    for (int i = 0; i < agencies.size(); i++)
    {
      // creates each agency page
      try
      {
        int agencyStops = 0; // agency stop counter
        File newFile1 = new File("../stops/" + agencies.get(i) + ".html");
        FileWriter fileWriter1 = new FileWriter(newFile1);

        fileWriter1.write("<title>" + fullAgencies.get(i) + " Stops - Eliot Deviation Index</title> \n");
        fileWriter1.append("<link rel=stylesheet href=../style.css> \n");
        fileWriter1.append("<ul><li><a href=../index.html>Home</a></li>");
        fileWriter1.append("<li><a href=../stops.html class=active>Stop Listing</a></li> \n");
        fileWriter1.append("<li><a href=../routes.html>Route Listing</a></li> \n");
        fileWriter1.append("<li><a href=../detailed.html>Route Detail</a></li> \n");
        fileWriter1.append("<li><a href=../stats.html>Statistics</a></li> \n");
        fileWriter1.append("<li><a href=../calculator.html>Calculator</a></li> \n");
        fileWriter1.append("<li><a href=../resources.html>Resources</a></li></ul> \n");
        fileWriter1.append("<h1>Stop Listing</h1> \n");

        fileWriter1.append("<h3>" + fullAgencies.get(i) + " (" + agencies.get(i) + ")</h3> \n");

        ArrayList<String> stopID = new ArrayList<String>();
        ArrayList<String> stopName = new ArrayList<String>();
        ArrayList<String> stopLat = new ArrayList<String>();
        ArrayList<String> stopLon = new ArrayList<String>();

        try
        {
          // port .txt list to webpage
          Scanner s = new Scanner(new File("../stops/list/" + agencies.get(i) + ".txt"));
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
          System.out.println("Error, no stop file for " + agencies.get(i) + ".");
        }

        fileWriter1.append("<table><tr><th>Stop ID</th><th>Stop Name</th><th>Stop Latitude</th><th>Stop Longitude</th></tr> \n");

        for (int j = 0; j < stopID.size(); j++)
        {
          fileWriter1.append("<tr><td style=color:#ff0000>" + stopID.get(j) + "</td><td>" + stopName.get(j) + "</td><td>" + stopLat.get(j) + "</td><td>" + stopLon.get(j) + "</td></tr> \n");
          agencyStops++;
          stopCounter++;
        }

        fileWriter1.append("</table> \n");
        fileWriter1.append("<p><b>Agency Stops:</b> " + agencyStops + "</p>");
        fileWriter1.flush();
        fileWriter1.close();
        System.out.println("Agency Stops (" + agencies.get(i) + "): " + agencyStops);
      }
      catch (IOException e)
      {
        System.out.println("Error, can't save stops for " + agencies.get(i) + ".");
      }
    }

    // main stops home page
    try
    {
      File newFile2 = new File("../stops.html");
      FileWriter fileWriter2 = new FileWriter(newFile2);
      fileWriter2.write("<title>Stop Listing - Eliot Deviation Index</title> \n");
      fileWriter2.append("<link rel=stylesheet href=style.css> \n");
      fileWriter2.append("<body onload=getAgencies2()> \n");
      fileWriter2.append("<ul><li><a href=index.html>Home</a></li>");
      fileWriter2.append("<li><a href=stops.html class=active>Stop Listing</a></li> \n");
      fileWriter2.append("<li><a href=routes.html>Route Listing</a></li> \n");
      fileWriter2.append("<li><a href=detailed.html>Route Detail</a></li> \n");
      fileWriter2.append("<li><a href=stats.html>Statistics</a></li> \n");
      fileWriter2.append("<li><a href=calculator.html>Calculator</a></li> \n");
      fileWriter2.append("<li><a href=resources.html>Resources</a></li></ul> \n");
      fileWriter2.append("<h1>Stop Listing</h1> \n");

      fileWriter2.append("<p id=agencies></p> \n");
      fileWriter2.append("<script src=list.js></script>");

      fileWriter2.append("<p><b>Total Stops: </b>" + stopCounter + "</p>");
      fileWriter2.flush();
      fileWriter2.close();
      System.out.println("Stop Count: " + stopCounter);
    }
    catch (Exception e)
    {
      System.out.println("Error, can't create stop home page.");
    }
  }
}