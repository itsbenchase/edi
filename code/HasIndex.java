import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;

// finds routes with certain index

public class HasIndex
{
    public static void main(String [] args)
    {
        // some basic input
        Scanner in = new Scanner(System.in);
        System.out.print("Enter index value: ");
        String indexNeededStr = in.nextLine();
        double indexNeeded = 0.00;

        double lowEnd = 0.00;
        double highEnd = 0.10;

        // inclusive range
        if (indexNeededStr.equals("range"))
        {
            System.out.print("Enter low end: ");
            lowEnd = in.nextDouble();
            System.out.print("Enter high end: ");
            highEnd = in.nextDouble();
        }
        else // make that a double
        {
            indexNeeded = Double.parseDouble(indexNeededStr);
        }

        // yeah i'm only doing global search
        ArrayList<String> agencies = new ArrayList<String>();
        try
        {
            Scanner s = new Scanner(new File("../agencies.txt"));
            while (s.hasNextLine())
            {
                String data = s.nextLine();
                String code = data.substring(0, data.indexOf(";"));
                agencies.add(code);
            }
        }
        catch (Exception e)
        {
            System.out.println("Error - no agencies.");
        }

        ArrayList<String> routeAgency = new ArrayList<String>();
        ArrayList<String> routeCode = new ArrayList<String>();
        ArrayList<Double> routeDist = new ArrayList<Double>();
        ArrayList<Double> routeEdi = new ArrayList<Double>();
        for (int i = 0; i < agencies.size(); i++)
        {
            // takes routes from /edi folder
            try
            {
                Scanner s = new Scanner(new File("../edis/" + agencies.get(i) + ".txt"));
                while (s.hasNextLine())
                {
                    routeAgency.add(agencies.get(i)); // categorize what agency route is from
                    String data = s.nextLine();
                    String code = data.substring(0, data.indexOf(";"));
                    routeCode.add(code);
                    data = data.substring(data.indexOf(";") + 1);
                    double dist = Double.parseDouble(data.substring(0, data.indexOf(";")));
                    routeDist.add(dist);
                    data = data.substring(data.indexOf(";") + 1);
                    double edi = Double.parseDouble(data.substring(0, data.indexOf(";")));
                    routeEdi.add(edi);
                }
            }
            catch (FileNotFoundException e)
            {
                // System.out.println("Error, no EDI file (" + agencies.get(i) + ")"); // expected error if no routes in database.
            }
        }

        int indexFound = 0; // count for end.
        if (indexNeededStr.equals("range"))
        {
            for (int i = 0; i < routeEdi.size(); i++)
            {
                if (routeEdi.get(i) >= lowEnd && routeEdi.get(i) <= highEnd)
                {
                    System.out.println(routeAgency.get(i) + " | " + routeCode.get(i) + " (" + routeDist.get(i) + " mi., " + routeEdi.get(i) + ")");
                    indexFound++;
                }
            }
        }
        else
        {
            for (int i = 0; i < routeEdi.size(); i++)
            {
                if (routeEdi.get(i) == indexNeeded)
                {
                    System.out.println(routeAgency.get(i) + " | " + routeCode.get(i) + " (" + routeDist.get(i) + " mi., " + routeEdi.get(i) + ")");
                    indexFound++;
                }
            }
        }

        if (indexFound == 0) // in case there are none
        {
            System.out.println("No routes found.");
        }

        System.out.println("Routes Found: " + indexFound);
    }
}