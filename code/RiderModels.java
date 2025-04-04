import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.ArrayList;

// used for ridership modeling, March 2025

public class RiderModels
{   
    static String agencyChoice = "no data";
    static String firstChoice = "no data";
    static String stopChoice = "no data";

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
    static String blockGroupCode = "0";

    static int workers = 0;
    static int riders = 0;
    static int workersMoe = 0;
    static int ridersMoe = 0;

    static int totalWorkers = 0;
    static int totalRiders = 0;

    static String lineChoice = "no data";

    static Scanner in = new Scanner(System.in);

    public static void main(String [] args) 
    {
        agencyChoice = "no data";

        System.out.println("Ridership Modeling");
        System.out.println("- Line: Find riders for one line");
        System.out.println("- Bulk: Find riders for all lines");

        System.out.print("Enter choice: ");
        firstChoice = in.nextLine();

        // US agencies only.
        boolean validAgency = false;
        
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
            if (firstChoice.equalsIgnoreCase("line"))
            {
                // 1:02 am ... this is the best i can come up with to fix an issue
                // don't we just love coding? eee
                ArrayList<String> codes2 = new ArrayList<String>();
                ArrayList<Double> lengths2 = new ArrayList<Double>();

                System.out.print("Enter line: ");
                lineChoice = in.nextLine();

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
            }

            // if not a bulk run
            if (!firstChoice.equalsIgnoreCase("bulk"))
            {
                population(false, 0);
                codes.clear();
                lengths.clear();
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
        }
    }

    public static void population(boolean whole, int progress)
    {
        for (int i = 0; i < points.size(); i++)
        {
            double latitude = Double.parseDouble(points.get(i).substring(0, points.get(i).indexOf(",")));
            double longitude = Double.parseDouble(points.get(i).substring(points.get(i).indexOf(",") + 1));

            latitudes.add(latitude);
            longitudes.add(longitude);

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

                    data = data.substring(data.indexOf("\"STATE\":")); // move to state number
                    stateCode = data.substring(9, data.indexOf(",") - 1);

                    data = data.substring(data.indexOf("\"TRACT\":")); // move to tract number
                    tractCode = data.substring(9, data.indexOf(",") - 1);

                    data = data.substring(data.indexOf("\"BLKGRP\":")); // move to block number
                    blockGroupCode = data.substring(10, data.indexOf(",") - 1);

                    data = data.substring(data.indexOf("\"COUNTY\":")); // move to county number
                    countyCode = data.substring(10, data.indexOf("]") - 2);
                }
            }
            catch (Exception e)
            { /* nothing */ }
            
            String geoidGen = stateCode + "-" + countyCode + "-" + tractCode + "-" + blockGroupCode;

            // duplicate block check - does taken list have it
            if (!geoid.contains(geoidGen))
            { 
                // get transit riders and workers
                try
                {
                    Scanner read = new Scanner(new URI("https://api.census.gov/data/2023/acs/acs5?get=B08301_001E,B08301_001M,B08301_010E,B08301_010M&for=block%20group:" + blockGroupCode + "&in=state:" + stateCode + "%20county:" + countyCode + "%20tract:" + tractCode + "&key=4f544f820b55d202c88e452b90045a507c35968f").toURL().openStream());

                    int z = 0;
                    while (read.hasNextLine())
                    {
                      if (z == 0)
                      {
                        read.nextLine();
                        z++;
                      }
                      else
                      {
                        String data = read.nextLine();

                        workers = Integer.parseInt(data.substring(2, data.indexOf(",") - 1));
                        data = data.substring(data.indexOf(",") + 1);
                        workersMoe = Integer.parseInt(data.substring(1, data.indexOf(",") - 1));
                        data = data.substring(data.indexOf(",") + 1);
                        riders = Integer.parseInt(data.substring(1, data.indexOf(",") - 1));
                        data = data.substring(data.indexOf(",") + 1);
                        ridersMoe = Integer.parseInt(data.substring(1, data.indexOf(",") - 1));
                      }
                    }

                    workers = workers + workersMoe;
                    riders = riders + ridersMoe;

                    totalWorkers += workers;
                    totalRiders += riders;
                }
                catch (Exception e)
                { /* nothing */ }

                // print progress
                if (whole) // display route on if bulk
                {
                    System.out.println("Route " + progress + " - Block Groups Processed: " + (i + 1) + " / " + latitudes.size());
                }
                else
                {
                    System.out.println("Block Groups Processed: " + (i + 1) + " / " + latitudes.size());
                }
            }
            geoid.add(geoidGen);
        }
        
        // riders per mile
        double riderPerMi = 0.00;
        for (int i = 0; i < codes.size(); i++)
        {
            if (lineChoice.equals(codes.get(i)))
            {
                riderPerMi = totalRiders / lengths.get(i);
                riderPerMi = Math.round(riderPerMi * 100.0) / 100.0;   
            }
        }
        
        // ending display
        if (whole)
        {
            agencyDisplay.add(lineChoice + ": " + totalRiders + " (" + riderPerMi + " / mi.), " + (totalRiders * 261) + " | " + (totalRiders * 365));
        }
        else
        {
            System.out.println(("Agency: " + agencyChoice));

            System.out.println("Line: " + lineChoice);

            System.out.println("Riders Served: " + totalRiders);
            System.out.println("Riders Per Mile: " + riderPerMi);

            System.out.println("Expected Annual (Mon-Fri): " + (totalRiders * 261));
            System.out.println("Expected Annual (Daily): " + (totalRiders * 365));

            System.out.println("");
            System.out.println("- Line: Find riders for one line");
            System.out.println("- Bulk: Find riders for all lines");

            System.out.print("Enter choice: ");
            firstChoice = in.nextLine();
        }

        points.clear();
        latitudes.clear();
        longitudes.clear();
        geoid.clear();

        totalWorkers = 0;
        totalRiders = 0;
    }
}