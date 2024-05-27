import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.ArrayList;

// not public... yet. could possibly be js adapted soon
// only here because of StopInfo

public class PopulationData
{   
    static boolean pushed = false; // is it from stopInfo?
    static String agencyChoice = "no data";
    static String firstChoice = "no data";
    static String stopChoice = "no data";
    static double radius = 0.00;

    static ArrayList<String> points = new ArrayList<String>();
    static ArrayList<Double> latitudes = new ArrayList<Double>();
    static ArrayList<Double> longitudes = new ArrayList<Double>();
    static ArrayList<String> geoid = new ArrayList<String>();
    static ArrayList<Double> lengths = new ArrayList<Double>();
    static ArrayList<String> codes = new ArrayList<String>();
    static ArrayList<String> agencyDisplay = new ArrayList<String>();

    static String stateCode = "0";
    static String countyCode = "0";
    static String tractCode = "0";
    static String blockCode = "0";
    static String population = "0";

    static String stopName = "no data"; // only to be used if stop selected
    static String lineChoice = "no data";
    static boolean stopSelected = false;

    static int totalPop = 0;

    static Scanner in = new Scanner(System.in);


    // override for stopInfo
    public PopulationData(String a, String c)
    {
        pushed = true;
        firstChoice = "stop";
        agencyChoice = a;
        radius = 0.25;
        stopChoice = c;
    }

    public static void main(String [] args) 
    {
        if (!pushed)
        {
            agencyChoice = "no data";

            System.out.println("Population Data");
            System.out.println("- Stop: Find population near one stop");
            System.out.println("- Line: Find population near all stops on a line");

            System.out.print("Enter choice: ");
            firstChoice = in.nextLine();
        }

        // US agencies only.
        boolean validAgency = false;
        if (pushed)
        {
            validAgency = true;
        }
        String [] validRegions = 
        {"al-", "ak-", "az-", "ar-", "ca-", "co-", "ct-", "de-", "dc-", "fl-", "ga-", "hi-", "id-",
         "il-", "in-", "ia-", "ks-", "ky-", "la-", "me-", "md-", "ma-", "mi-", "mn-", "ms-", "mo-", 
         "mt-", "ne-", "nv-", "nh-", "nj-", "nm-", "ny-", "nc-", "nd-", "oh-", "ok-", "or-", "pa-", 
         "ri-", "sc-", "sd-", "tn-", "tx-", "ut-", "vt-", "va-", "wa-", "wv-", "wi-", "wy-", "zz-"};

        while (!validAgency)
        {
            System.out.print("Enter agency (US only): ");
            agencyChoice = in.nextLine();

            for (int i = 0; i < 52; i++)
            {
                if (agencyChoice.substring(0, 3).equalsIgnoreCase(validRegions[i]))
                {
                    validAgency = true;
                }
            }
        }

        while (!firstChoice.equalsIgnoreCase("-00"))
        {
            if (!pushed)
            {
                radius = 0.00;
            }
            while (radius == 0.00)
            {
                System.out.print("Enter radius: ");
                radius = in.nextDouble();
                if (radius < 0.125 || radius > 1.0)
                {
                    System.out.println("Radius must be between 0.125 and 1.0 miles.");
                    radius = 0.00;
                }
            }
            
            if (!pushed)
            {
                in.nextLine(); // absorb
            }

            if (firstChoice.equalsIgnoreCase("stop"))
            {
                if (!pushed)
                {
                    System.out.print("Enter Stop ID: ");
                    stopChoice = in.nextLine();
                }
                stopSelected = true;

                // load in only existing stops
                try
                {
                    Scanner s = new Scanner(new File("../stops/list/" + agencyChoice + ".txt"));
                    while (s.hasNextLine())
                    {
                        String data = s.nextLine();
                        String id = data.substring(0, data.indexOf(";"));
                        data = data.substring(data.indexOf(";") + 1);
                        String name = data.substring(0, data.indexOf(";"));
                        data = data.substring(data.indexOf(";") + 1);
                        double lat = Double.parseDouble(data.substring(0, data.indexOf(";")));
                        data = data.substring(data.indexOf(";") + 1);
                        double lon = Double.parseDouble(data);

                        if (id.equalsIgnoreCase(stopChoice))
                        {
                            stopName = name;
                            points.add(lat + "," + lon);
                        }
                    }
                }
                catch (Exception e)
                {
                    System.out.println("Error. can't load stops");
                }
            }

            else if (firstChoice.equalsIgnoreCase("line"))
            {
                // 1:02 am ... this is the best i can come up with to fix an issue
                // don't we just love coding? eee
                ArrayList<String> codes2 = new ArrayList<String>();
                ArrayList<Double> lengths2 = new ArrayList<Double>();

                // only to be used with segments
                int start = 0;
                int end = 0;
                boolean segment = false;

                System.out.print("Enter line: ");
                lineChoice = in.nextLine();

                if (lineChoice.equalsIgnoreCase("segment"))
                {
                    segment = true;

                    System.out.print("Enter start: ");
                    start = in.nextInt();
                    System.out.print("Enter end: ");
                    end = in.nextInt();

                    in.nextLine(); // absorb

                    System.out.print("Enter line: ");
                    lineChoice = in.nextLine();
                }

                // loads in file with all routes
                try
                {
                    Scanner s = new Scanner(new File("../edis/" + agencyChoice + ".txt"));
                    while (s.hasNextLine())
                    {
                        String data = s.nextLine();
                        String code = data.substring(0, data.indexOf(";"));
                        data = data.substring(data.indexOf(";") + 1);
                        double length = Double.parseDouble(data.substring(0, data.indexOf(";")));
                        codes2.add(code);
                        lengths2.add(length);
                    }
                }
                catch (Exception e)
                {
                    System.out.println("Error.");
                }

                // loads in EDI file with existing routes only
                try
                {
                    Scanner s = new Scanner(new File("../files/" + agencyChoice + "-edi.txt"));
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
                        data = data.substring(data.indexOf(";") + 1);

                        if (line.equalsIgnoreCase(lineChoice))
                        {
                            lineChoice = line; // correct formatting
                            codes.add(lineChoice);
                            for (int i = 0; i < codes2.size(); i++)
                            {
                                if (codes2.get(i).equals(lineChoice))
                                {
                                    lengths.add(lengths2.get(i));
                                }
                            }
                            points.add(lat + "," + lon);
                        }
                    }
                }
                catch (Exception e)
                {
                    System.out.println("Error.");
                }

                if (segment)
                {
                    for (int i = end; i < points.size(); i++)
                    {
                        points.remove(end);
                    }
                    
                    for (int i = 1; i < start; i++)
                    {
                        points.remove(0);
                    }
                }
            }

            // bulk whole agency input
            else if (firstChoice.equalsIgnoreCase("bulk"))
            {
                // loads in file with all routes
                try
                {
                    Scanner s = new Scanner(new File("../edis/" + agencyChoice + ".txt"));
                    while (s.hasNextLine())
                    {
                        String data = s.nextLine();
                        String code = data.substring(0, data.indexOf(";"));
                        data = data.substring(data.indexOf(";") + 1);
                        double length = Double.parseDouble(data.substring(0, data.indexOf(";")));

                        codes.add(code);
                        lengths.add(length);
                    }
                }
                catch (Exception e)
                {
                    System.out.println("Error.");
                }

                // for loop
                for (int i = 0; i < codes.size(); i++)
                {
                    lineChoice = codes.get(i);
                    // loads in EDI file with existing routes only
                    try
                    {
                        Scanner s = new Scanner(new File("../files/" + agencyChoice + "-edi.txt"));
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
                            data = data.substring(data.indexOf(";") + 1);

                            if (line.equalsIgnoreCase(lineChoice))
                            {
                                lineChoice = line; // correct formatting
                                points.add(lat + "," + lon);
                            }
                        }
                    }
                    catch (Exception e)
                    {
                        System.out.println("Error.");
                    }

                    System.out.println("Line " + (i + 1) + " / " + codes.size());
                    population(true, (i + 1));
                }

                for (int i = 0; i < agencyDisplay.size(); i++)
                {
                    System.out.println(agencyDisplay.get(i));
                }

                firstChoice = "-00";
            }

            // if not a bulk run
            if (!firstChoice.equalsIgnoreCase("bulk"))
            {
                population(false, 0);
                codes.clear();
                lengths.clear();
            }
        }
    }

    public static void population(boolean whole, int progress)
    {
        // 16 nearby points
        // 1 deg lat = 364080 feet = ~68.95454 miles
        // 1 deg lon = cos(lat in rad) * 69.172 miles
        // 1/8 mile = 660 feet, lat: +/-.001813 (root 2/2 = +/-.001282)
        
        double dist = 0.125; // current distance being searched
        for (int i = 0; i < points.size(); i++)
        {
            dist = 0.125;
            int distMultiplier = 1;
            while (dist <= radius)
            {
                // 1/8, 1/4, 3/8, 1/2, etc... for each stop
                double latitude = Double.parseDouble(points.get(i).substring(0, points.get(i).indexOf(",")));
                double longitude = Double.parseDouble(points.get(i).substring(points.get(i).indexOf(",") + 1));

                // 0 deg
                latitudes.add(latitude + 0);
                longitudes.add(longitude + (dist / (Math.cos(Math.toRadians(latitude)) * 69.172)));

                // 45 deg
                latitudes.add(latitude + (.001282 * distMultiplier));
                longitudes.add(longitude + ((dist / (Math.cos(Math.toRadians(latitude)) * 69.172)) * (Math.sqrt(2) / 2)));

                // 90 deg
                latitudes.add(latitude + (.001813 * distMultiplier));
                longitudes.add(longitude + 0);

                // 135 deg
                latitudes.add(latitude + (.001282 * distMultiplier));
                longitudes.add(longitude - ((dist / (Math.cos(Math.toRadians(latitude)) * 69.172)) * (Math.sqrt(2) / 2)));

                // 180 deg
                latitudes.add(latitude + 0);
                longitudes.add(longitude - (dist / (Math.cos(Math.toRadians(latitude)) * 69.172)));

                // 225 deg
                latitudes.add(latitude - (.001282 * distMultiplier));
                longitudes.add(longitude - ((dist / (Math.cos(Math.toRadians(latitude)) * 69.172)) * (Math.sqrt(2) / 2)));

                // 270 deg
                latitudes.add(latitude - (.001813 * distMultiplier));
                longitudes.add(longitude + 0);

                // 315 deg
                latitudes.add(latitude - (.001282 * distMultiplier));
                longitudes.add(longitude + ((dist / (Math.cos(Math.toRadians(latitude)) * 69.172)) * (Math.sqrt(2) / 2)));

                dist = dist + 0.125;
                distMultiplier++;
            }

            System.out.println("Stops Processed: " + (i + 1) + " / " + points.size());
        }

        for (int i = 0; i < latitudes.size(); i++)
        {
            try
            {
                Scanner read = new Scanner(new URI("https://geocoding.geo.census.gov/geocoder/geographies/coordinates?x=" + longitudes.get(i) + "&y=" + latitudes.get(i) + "&benchmark=Public_AR_Census2020&vintage=Census2020_Census2020&format=json").toURL().openStream());

                while (read.hasNextLine())
                {
                    String data = read.nextLine();
                    data = data.substring(data.indexOf("\"Census Blocks\"")); // truncate to block data

                    data = data.substring(data.indexOf("\"POP100\":")); // move to population
                    population = data.substring(9, data.indexOf(","));

                    data = data.substring(data.indexOf("\"BLOCK\":")); // move to block number
                    blockCode = data.substring(9, data.indexOf(",") - 1);

                    data = data.substring(data.indexOf("\"STATE\":")); // move to state number
                    stateCode = data.substring(9, data.indexOf(",") - 1);

                    data = data.substring(data.indexOf("\"TRACT\":")); // move to tract number
                    tractCode = data.substring(9, data.indexOf(",") - 1);

                    data = data.substring(data.indexOf("\"COUNTY\":")); // move to county number
                    countyCode = data.substring(10, data.indexOf("]") - 2);
                }
            }
            catch (Exception e)
            { /* nothing */ }
            
            String geoidGen = stateCode + "-" + countyCode + "-" + tractCode + "-" + blockCode;

            // duplicate block check - does taken list have it
            if (!geoid.contains(geoidGen))
            {
                totalPop += Integer.parseInt(population);
                if (whole) // display route on if bulk
                {
                    System.out.println("Route " + progress + " - Blocks Processed: " + (i + 1) + " / " + latitudes.size());
                }
                else
                {
                    System.out.println("Blocks Processed: " + (i + 1) + " / " + latitudes.size());
                }
            }
            geoid.add(geoidGen);
        }

        // pop per mile
        double popPerMi = 0.00;
        for (int i = 0; i < codes.size(); i++)
        {
            if (lineChoice.equals(codes.get(i)))
            {
                popPerMi = totalPop / lengths.get(i);
                popPerMi = Math.round(popPerMi * 100.0) / 100.0;   
            }
        }
        
        // ending display
        if (pushed)
        {
            System.out.println("Population: " + totalPop);
            firstChoice = "-00";
        }
        if (whole)
        {
            agencyDisplay.add(lineChoice + ": " + totalPop + " (" + radius + " mi.), " + popPerMi + " / mi");
        }
        else
        {
            System.out.println(("Agency: " + agencyChoice));

            if (firstChoice.equalsIgnoreCase("stop"))
            {
                System.out.println("Stop: " + stopName + " (" + stopChoice + ")");
            }
            else
            {
                System.out.println("Line: " + lineChoice);
            }

            System.out.println("Population Served: " + totalPop + " (" + radius + " mi.)");
            System.out.println("Population Per Mile: " + popPerMi);

            System.out.println("");
            System.out.println("- Stop: Find population near one stop");
            System.out.println("- Line: Find population near all stops on a line");

            System.out.print("Enter choice: ");
            firstChoice = in.nextLine();
        }

        points.clear();
        latitudes.clear();
        longitudes.clear();
        geoid.clear();

        totalPop = 0;
    }
}