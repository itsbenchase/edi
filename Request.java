import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.Scanner;
import java.util.ArrayList;

public class Request
{
  public static void main(String [] args)
  {
    // get request list
    ArrayList<String> requests = new ArrayList<String>();
    try
    {
      Scanner s = new Scanner(new File("requests.txt"));
      while (s.hasNextLine())
      {
        String data = s.nextLine();
        requests.add(data);
      }
    }
    catch (Exception e)
    {
      System.out.println("Error - no requests (odd, you always have some).");
    }

    // get agency list
    ArrayList<String> agencies = new ArrayList<String>();
    try
    {
      Scanner s = new Scanner(new File("agencies.txt"));
      while (s.hasNextLine())
      {
        String data = s.nextLine();
        agencies.add(data.substring(0, data.indexOf(";")));
      }
      agencies.add("zz-none"); // future agency.
    }
    catch (Exception e)
    {
      System.out.println("Error - no agencies.");
    }

    Scanner in = new Scanner(System.in);
    String agencyChoice = "no data";

    boolean actualAgency = false;
    while (!actualAgency)
    {
      System.out.print("Agency: ");
      agencyChoice = in.nextLine();

      for (int i = 0; i < agencies.size(); i++)
      {
        if (agencies.get(i).equalsIgnoreCase(agencyChoice))
        {
          actualAgency = true;
        }
      }
    }

    System.out.print("Request: ");
    String newRequest = in.nextLine();

    requests.add(agencyChoice + " - " + newRequest);

    // adds to request file
    try
    {
      File newFile = new File("requests.txt");
      FileWriter fileWriter = new FileWriter(newFile);

      fileWriter.write(requests.get(0) + "\n");

      for (int i = 1; i < requests.size(); i++)
      {
        fileWriter.append(requests.get(i) + "\n");
      }

      fileWriter.close();

      System.out.println("Request added.");
    }
    catch (Exception e)
    {
      System.out.println("Error.");
    }
  }
}