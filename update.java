// runs all the update files in one go
import java.time.format.DateTimeFormatter;
import java.time.ZonedDateTime;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
public class update
{
  public static void main(String [] args)
  {
    //StopList a = new StopList();
    RouteList b = new RouteList();
    FullMap c = new FullMap();
    AgencyMap d = new AgencyMap();
    StatsList f = new StatsList();
    Stats g = new Stats("global");

    //a.main(args);
    b.main(args);
    c.main(args);
    d.main(args);
    f.main(args);
    g.main(args);

    // update home page with date
    ArrayList<String> homepage = new ArrayList<String>();
    try
    {
      Scanner s = new Scanner(new File("index.html"));
      while (s.hasNextLine())
      {
        homepage.add(s.nextLine());
      }
    }
    catch (Exception e)
    {
      System.out.println("Error - no index.html. (how in the hell did you get this)");
    }

    DateTimeFormatter currentTimeFormat = DateTimeFormatter.ofPattern("d MMMM yyyy HH:mm z");
    ZonedDateTime now = ZonedDateTime.now();
    String currentTime = currentTimeFormat.format(now);

    homepage.set(homepage.size() - 1, "<p><b>Last Updated: </b> " + currentTime + "</p>");

    try
    {
      File newFile1 = new File("index.html");
      FileWriter fileWriter1 = new FileWriter(newFile1);

      fileWriter1.write(homepage.get(0) + "\n");

      for (int i = 1; i < homepage.size() - 1; i++)
      {
        fileWriter1.append(homepage.get(i) + "\n");
      }

      fileWriter1.append(homepage.get(homepage.size() - 1));
      fileWriter1.close();
    }
    catch (Exception e)
    {
      System.out.println("Error.");
    }

    System.out.println("Update Time: " + currentTime);
  }
}