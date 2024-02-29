package org.eliotindex;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Generators {
	/**
	 * Creates individual agency maps
	 */
	static void agencyMap() {
		ArrayList<String> agencies = Util.getAgencyIds();

		for (int a = 0; a < agencies.size(); a++) {
			ArrayList<Stop> load = Util.getAgencyRouteStops(agencies.get(a));
			ArrayList<Route> routes = Util.getAgencyEdis(agencies.get(a));
			ArrayList<String> maps = Util.mapInit(agencies.get(a) + " routes");

			for (Route route : routes) {
				if (load.get(0).getLine().equals(route.getLineCode())) {
					maps.add("\t<Placemark> \n\t\t<name>" + route.getLineName() + "</name>");

					// yeah yeah i gotta do this twice
					if (route.getEdi() >= 1.0 && route.getEdi() < 1.5) {
						maps.add("\t\t<styleUrl>#1.0</styleUrl>");
					} else if (route.getEdi() >= 1.5 && route.getEdi() < 2.0) {
						maps.add("\t\t<styleUrl>#1.5</styleUrl>");
					} else if (route.getEdi() >= 2.0 && route.getEdi() < 2.5) {
						maps.add("\t\t<styleUrl>#2.0</styleUrl>");
					} else if (route.getEdi() >= 2.5 && route.getEdi() < 3.0) {
						maps.add("\t\t<styleUrl>#2.5</styleUrl>");
					} else if (route.getEdi() >= 3.0 && route.getEdi() < 3.5) {
						maps.add("\t\t<styleUrl>#3.0</styleUrl>");
					} else if (route.getEdi() >= 3.5 && route.getEdi() < 4.0) {
						maps.add("\t\t<styleUrl>#3.5</styleUrl>");
					} else if (route.getEdi() >= 4.0 && route.getEdi() < 10.0) {
						maps.add("\t\t<styleUrl>#4.0</styleUrl>");
					} else // (routes.get(j).getEdi() >= 10.0)
					{
						maps.add("\t\t<styleUrl>#10.0</styleUrl>");
					}

					maps.add("\t\t<description>Branch: " + route.getBranch() + "<br/>Agency: " + agencies.get(a) + "<br/>EDI: " + route.getEdi() + "</description>");
				}
			}

			maps.add("\t\t<LineString> \n\t\t\t<coordinates>");
			maps.add("\t\t\t" + load.get(0).getLon() + "," + load.get(0).getLat() + ",0");
			for (int i = 1; i < load.size(); i++) {
				if (load.get(i).getLine().equals(load.get(i - 1).getLine())) {
					maps.add("\t\t\t" + load.get(i).getLon() + "," + load.get(i).getLat() + ",0");
				} else {
					maps.add("\t\t\t</coordinates> \n\t\t</LineString> \n\t</Placemark>");

					for (Route route : routes) {
						if (load.get(i).getLine().equals(route.getLineCode())) {
							maps.add("\t<Placemark> \n\t\t<name>" + route.getLineName() + "</name>");

							if (route.getEdi() >= 1.0 && route.getEdi() < 1.5) {
								maps.add("\t\t<styleUrl>#1.0</styleUrl>");
							} else if (route.getEdi() >= 1.5 && route.getEdi() < 2.0) {
								maps.add("\t\t<styleUrl>#1.5</styleUrl>");
							} else if (route.getEdi() >= 2.0 && route.getEdi() < 2.5) {
								maps.add("\t\t<styleUrl>#2.0</styleUrl>");
							} else if (route.getEdi() >= 2.5 && route.getEdi() < 3.0) {
								maps.add("\t\t<styleUrl>#2.5</styleUrl>");
							} else if (route.getEdi() >= 3.0 && route.getEdi() < 3.5) {
								maps.add("\t\t<styleUrl>#3.0</styleUrl>");
							} else if (route.getEdi() >= 3.5 && route.getEdi() < 4.0) {
								maps.add("\t\t<styleUrl>#3.5</styleUrl>");
							} else if (route.getEdi() >= 4.0 && route.getEdi() < 10.0) {
								maps.add("\t\t<styleUrl>#4.0</styleUrl>");
							} else // (routes.get(j).getEdi() >= 10.0)
							{
								maps.add("\t\t<styleUrl>#10.0</styleUrl>");
							}

							maps.add("\t\t<description>Branch: " + route.getBranch() + "<br/>Agency: " + agencies.get(a) + "<br/>EDI: " + route.getEdi() + "</description>");
						}
					}
					maps.add("\t\t<LineString> \n\t\t\t<coordinates>");
					maps.add("\t\t\t" + load.get(i).getLon() + "," + load.get(i).getLat() + ",0");
				}
			}
			maps.add("\t\t\t</coordinates> \n\t\t</LineString> \n\t</Placemark> \n</Document> \n</kml>");

			try {
				File newFile1 = new File("maps/map-" + agencies.get(a) + ".kml");
				FileWriter fileWriter1 = new FileWriter(newFile1);

				fileWriter1.write(maps.get(0) + "\n");

				for (int i = 1; i < maps.size(); i++) {
					fileWriter1.append(maps.get(i)).append("\n");
				}

				fileWriter1.close();
			} catch (Exception e) {
				System.out.println("Error.");
			}

			System.out.println("Agency Maps: " + (a + 1) + " / " + agencies.size());
		}
	}

	/**
	 * Generates maps/lines/map-all.kml
	 */
	static void fullMap() {
		ArrayList<String> agencies = Util.getAgencyIds();
		ArrayList<String> maps = Util.mapInit("Eliot Deviation Index Routes");

		for (int a = 0; a < agencies.size(); a++) {
			ArrayList<Stop> load = Util.getAgencyRouteStops(agencies.get(a));
			ArrayList<Route> routes = Util.getAgencyEdis(agencies.get(a));

			maps.add("\t<Document> \n\t\t<name>" + agencies.get(a) + " routes</name>");
			// colors for groups of EDI values
			for (Route stop : routes) {
				if (load.get(0).getLine().equals(stop.getLineCode())) {
					maps.add("\t\t<Placemark> \n\t\t\t<name>" + stop.getLineName() + "</name>");

					// yeah yeah i gotta do this twice
					if (stop.getEdi() >= 1.0 && stop.getEdi() < 1.5) {
						maps.add("\t\t\t<styleUrl>#1.0</styleUrl>");
					} else if (stop.getEdi() >= 1.5 && stop.getEdi() < 2.0) {
						maps.add("\t\t\t<styleUrl>#1.5</styleUrl>");
					} else if (stop.getEdi() >= 2.0 && stop.getEdi() < 2.5) {
						maps.add("\t\t\t<styleUrl>#2.0</styleUrl>");
					} else if (stop.getEdi() >= 2.5 && stop.getEdi() < 3.0) {
						maps.add("\t\t\t<styleUrl>#2.5</styleUrl>");
					} else if (stop.getEdi() >= 3.0 && stop.getEdi() < 3.5) {
						maps.add("\t\t\t<styleUrl>#3.0</styleUrl>");
					} else if (stop.getEdi() >= 3.5 && stop.getEdi() < 4.0) {
						maps.add("\t\t\t<styleUrl>#3.5</styleUrl>");
					} else if (stop.getEdi() >= 4.0 && stop.getEdi() < 10.0) {
						maps.add("\t\t<styleUrl>#4.0</styleUrl>");
					} else // (routes.get(j).getEdi() >= 10.0)
					{
						maps.add("\t\t<styleUrl>#10.0</styleUrl>");
					}

					maps.add("\t\t\t<description>Branch: " + stop.getBranch() + "<br/>Agency: " + agencies.get(a) + "<br/>EDI: " + stop.getEdi() + "</description>");
				}
			}

			maps.add("\t\t\t<LineString> \n\t\t\t\t<coordinates>");
			maps.add("\t\t\t\t" + load.get(0).getLon() + "," + load.get(0).getLat() + ",0 ");
			for (int i = 1; i < load.size(); i++) {
				if (load.get(i).getLine().equals(load.get(i - 1).getLine())) {
					maps.set(maps.size() - 1, maps.get(maps.size() - 1) + load.get(i).getLon() + "," + load.get(i).getLat() + ",0 ");
				} else {
					maps.add("\t\t\t\t</coordinates> \n\t\t\t</LineString> \n\t\t</Placemark>");

					for (Route route : routes) {
						if (load.get(i).getLine().equals(route.getLineCode())) {
							maps.add("\t\t<Placemark> \n\t\t\t<name>" + route.getLineName() + "</name>");

							if (route.getEdi() >= 1.0 && route.getEdi() < 1.5) {
								maps.add("\t\t\t<styleUrl>#1.0</styleUrl>");
							} else if (route.getEdi() >= 1.5 && route.getEdi() < 2.0) {
								maps.add("\t\t\t<styleUrl>#1.5</styleUrl>");
							} else if (route.getEdi() >= 2.0 && route.getEdi() < 2.5) {
								maps.add("\t\t\t<styleUrl>#2.0</styleUrl>");
							} else if (route.getEdi() >= 2.5 && route.getEdi() < 3.0) {
								maps.add("\t\t\t<styleUrl>#2.5</styleUrl>");
							} else if (route.getEdi() >= 3.0 && route.getEdi() < 3.5) {
								maps.add("\t\t\t<styleUrl>#3.0</styleUrl>");
							} else if (route.getEdi() >= 3.5 && route.getEdi() < 4.0) {
								maps.add("\t\t\t<styleUrl>#3.5</styleUrl>");
							} else if (route.getEdi() >= 4.0 && route.getEdi() < 10.0) {
								maps.add("\t\t<styleUrl>#4.0</styleUrl>");
							} else // (routes.get(j).getEdi() >= 10.0)
							{
								maps.add("\t\t<styleUrl>#10.0</styleUrl>");
							}

							maps.add("\t\t\t<description>Branch: " + route.getBranch() + "<br/>Agency: " + agencies.get(a) + "<br/>EDI: " + route.getEdi() + "</description>");
						}
					}
					maps.add("\t\t\t<LineString> \n\t\t\t\t<coordinates>");
					maps.add("\t\t\t" + load.get(i).getLon() + "," + load.get(i).getLat() + ",0 ");
				}
			}
			maps.add("\t\t\t\t</coordinates> \n\t\t\t</LineString> \n\t\t</Placemark> \n\t</Document>");

			System.out.println("Full Map: " + (a + 1) + " / " + agencies.size());
		}

		maps.add("</Folder></kml>");

		try {
			File newFile1 = new File("maps/map-all.kml");
			FileWriter fileWriter1 = new FileWriter(newFile1);

			fileWriter1.write(maps.get(0) + "\n");

			for (int i = 1; i < maps.size(); i++) {
				fileWriter1.append(maps.get(i)).append("\n");
			}

			fileWriter1.close();
		} catch (Exception e) {
			System.out.println("Error.");
		}
	}

	/**
	 * Generates routes.html
	 */
	static void routeList() {
		ArrayList<String> agencies = Util.getAgencyIds();
		ArrayList<String> fullAgencies = Util.getAgencyNames();

		// creates page
		try {
			File newFile1 = new File("routes.html");
			FileWriter fileWriter1 = new FileWriter(newFile1);

			fileWriter1.write("<title>Route Listing - Eliot Deviation Index</title> \n");
			fileWriter1.append("<link rel=stylesheet href=style.css> \n");
			fileWriter1.append("<ul><li><a href=index.html>Home</a></li>");
			fileWriter1.append("<li><a href=stops.html>Stop Listing</a></li> \n");
			fileWriter1.append("<li><a href=routes.html class=active>Route Listing</a></li> \n");
			fileWriter1.append("<li><a href=detailed.html>Route Detail</a></li> \n");
			fileWriter1.append("<li><a href=stats.html>Statistics</a></li> \n");
			fileWriter1.append("<li><a href=calculator.html>Calculator</a></li></ul> \n");
			fileWriter1.append("<h1>Route Listing</h1> \n");

			// listing with links at top
			for (int i = 0; i < agencies.size(); i++) {
				fileWriter1.append("<ul class=bullet style=background-color:#1f1f1f><a href=#").append(agencies.get(i)).append(">").append(fullAgencies.get(i)).append("</a> (").append(agencies.get(i)).append(")</ul> \n");
			}

			int routeCount = 0;

			// loop to list routes
			for (int i = 0; i < agencies.size(); i++) {
				fileWriter1.append("<h3 id=").append(agencies.get(i)).append(">").append(fullAgencies.get(i)).append(" (").append(agencies.get(i)).append(")</h3> \n");

				ArrayList<String> routeCode = new ArrayList<>();
				ArrayList<String> routeDist = new ArrayList<>();
				ArrayList<String> routeEdi = new ArrayList<>();
				ArrayList<String> routeOfficial = new ArrayList<>();
				int agencyCount = 0;
				// takes routes from /edi folder
				try {
					Scanner s = new Scanner(new File("edis/" + agencies.get(i) + ".txt"));
					while (s.hasNextLine()) {
						String data = s.nextLine();
						if (data.charAt(0) != '*') // * - unverified
						{
							if (data.charAt(0) == '!') {
								data = data.substring(1);
								routeOfficial.add("n");
							} else {
								routeOfficial.add("y");
							}
							String code = data.substring(0, data.indexOf(";"));
							routeCode.add(code);
							data = data.substring(data.indexOf(";") + 1);
							String dist = data.substring(0, data.indexOf(";"));
							routeDist.add(dist);
							data = data.substring(data.indexOf(";") + 1);
							String edi = data.substring(0, data.indexOf(";"));
							routeEdi.add(edi);
							routeCount++;
							agencyCount++;
						}
					}
				} catch (FileNotFoundException e) {
					// System.out.println("Error, no EDI file (" + agencies.get(i) + ")"); // expected error if no routes in database.
				}

				fileWriter1.append("<table><tr><th>Route Code</th><th>Line Length</th><th>Eliot Deviation Index</th></tr> \n");

				// route table created
				for (int j = 0; j < routeCode.size(); j++) {
					if (routeOfficial.get(j).equals("y")) {
						fileWriter1.append("<tr><td style=color:#1aab2d>").append(routeCode.get(j)).append("</td><td>").append(routeDist.get(j)).append(" mi.</td><td>").append(routeEdi.get(j)).append("</td></tr> \n");
					} else {
						fileWriter1.append("<tr><td style=color:#0097a7>").append(routeCode.get(j)).append("</td><td>").append(routeDist.get(j)).append(" mi.</td><td>").append(routeEdi.get(j)).append("</td></tr> \n");
					}
				}

				fileWriter1.append("</table> \n");
				System.out.println("Agency Routes (" + agencies.get(i) + "): " + agencyCount);
				int currentSize = agencyCount;

				// check for sets
				try {
					// load in the sets
					ArrayList<String> agencySets = new ArrayList<>();
					Scanner t = new Scanner(new File("sets/" + agencies.get(i) + ".txt"));
					while (t.hasNextLine()) {
						String data = t.nextLine();
						agencySets.add(data);
					}

					// load in from the set
					// loop through all sets
					for (String agencySet : agencySets) {
						Scanner s = new Scanner(new File("edis/sets/" + agencies.get(i) + "-" + agencySet + ".txt"));
						while (s.hasNextLine()) {
							String data = s.nextLine();
							// i'm gonna list them all blue (unofficial b/c post-rollover)
							String code = data.substring(0, data.indexOf(";"));
							routeCode.add(code);
							data = data.substring(data.indexOf(";") + 1);
							String dist = data.substring(0, data.indexOf(";"));
							routeDist.add(dist);
							data = data.substring(data.indexOf(";") + 1);
							String edi = data;
							routeEdi.add(edi);
							routeCount++;
							agencyCount++;
						}

						fileWriter1.append("<p><b>").append(agencySet).append(" Set</b></p>\n");
						fileWriter1.append("<table><tr><th>Route Code</th><th>Line Length</th><th>Eliot Deviation Index</th></tr> \n");

						for (int k = currentSize; k < routeCode.size(); k++) {
							fileWriter1.append("<tr><td style=color:#0097a7>").append(routeCode.get(k)).append("</td><td>").append(routeDist.get(k)).append(" mi.</td><td>").append(routeEdi.get(k)).append("</td></tr> \n");
						}

						fileWriter1.append("</table> \n");
						currentSize = agencyCount; // set things for the next set if applicable
					}
				} catch (Exception e) {
					// just don't do anything then
				}
			}

			fileWriter1.append("<p><b>Route Count: </b> ").append(String.valueOf(routeCount)).append("</p>");
			fileWriter1.close();
			System.out.println("Route Count: " + routeCount);
		} catch (IOException e) {
			System.out.println("Error, can't save routes.");
		}
	}

	/**
	 * Creates stats page
	 * Stats command line in Stats.java, no page generation there
	 * Basically the same code though
	 * EDI miles will probably be ignored
	 */
	static void statsList() {
		// start with the global
		ArrayList<String> agencies = new ArrayList<>();
		ArrayList<String> fullAgencies = new ArrayList<>();
		try {
			Scanner s = new Scanner(new File("agencies.txt"));
			while (s.hasNextLine()) {
				String data = s.nextLine();
				String code = data.substring(0, data.indexOf(";"));
				agencies.add(code);
				data = data.substring(data.indexOf(";") + 1);
				fullAgencies.add(data);
			}
		} catch (Exception e) {
			System.out.println("Error - no agencies.");
		}

		try {
			File newFile1 = new File("stats.html");
			FileWriter fileWriter1 = new FileWriter(newFile1);

			fileWriter1.write("<title>Statistics - Eliot Deviation Index</title> \n");
			fileWriter1.append("<link rel=stylesheet href=style.css> \n");
			fileWriter1.append("<ul><li><a href=index.html>Home</a></li>");
			fileWriter1.append("<li><a href=stops.html>Stop Listing</a></li> \n");
			fileWriter1.append("<li><a href=routes.html>Route Listing</a></li> \n");
			fileWriter1.append("<li><a href=detailed.html>Route Detail</a></li> \n");
			fileWriter1.append("<li><a href=stats.html class=active>Statistics</a></li> \n");
			fileWriter1.append("<li><a href=calculator.html>Calculator</a></li></ul> \n");
			fileWriter1.append("<h1>Statistics</h1> \n");

			// listing with links at top
			fileWriter1.append("<ul class=bullet style=background-color:#1f1f1f><a href=#global>Entire Database</a></ul> \n");
			for (int i = 0; i < agencies.size(); i++) {
				fileWriter1.append("<ul class=bullet style=background-color:#1f1f1f><a href=#").append(agencies.get(i)).append(">").append(fullAgencies.get(i)).append("</a> (").append(agencies.get(i)).append(")</ul> \n");
			}

			fileWriter1.append("<h3 id=global>Entire Database</h3> \n");

			ArrayList<Double> lengths = new ArrayList<>(); // stores all lengths
			ArrayList<Double> edis = new ArrayList<>();
			double totals = 0.00; // total EDI miles in agency

			for (String agency : agencies) {
				try {
					Scanner s = new Scanner(new File("edis/" + agency + ".txt"));
					while (s.hasNextLine()) {
						String data = s.nextLine();
						data = data.substring(data.indexOf(";") + 1);
						double miles = Double.parseDouble(data.substring(0, data.indexOf(";")));
						data = data.substring(data.indexOf(";") + 1);
						double edi = Double.parseDouble(data.substring(0, data.indexOf(";")));

						lengths.add(miles);
						edis.add(edi);
						totals += miles;
					}
				} catch (Exception e) {
					continue; // skip because agency not in database
				}

				// check for sets - yes, sets are being counted with global stats list
				try {
					// load in the sets
					ArrayList<String> agencySets = new ArrayList<>();
					Scanner t = new Scanner(new File("sets/" + agency + ".txt"));
					while (t.hasNextLine()) {
						String data = t.nextLine();
						agencySets.add(data);
					}

					// load in from the set
					// loop through all sets
					for (String agencySet : agencySets) {
						Scanner s = new Scanner(new File("edis/sets/" + agency + "-" + agencySet + ".txt"));
						while (s.hasNextLine()) {
							String data = s.nextLine();
							data = data.substring(data.indexOf(";") + 1);
							double miles = Double.parseDouble(data.substring(0, data.indexOf(";")));
							data = data.substring(data.indexOf(";") + 1);
							double edi = Double.parseDouble(data);

							lengths.add(miles);
							edis.add(edi);
							totals += miles;
						}
					}
				} catch (Exception e) {
					// skip, agency has no sets
				}
			}

			// does all the math for the global
			Collections.sort(lengths);
			Collections.sort(edis);

			double meanEdi;
			double medianEdi;
			double meanLength;
			double medianLength;

			// mean EDI
			double totalEdi = 0.0;
			for (Double aDouble : edis) {
				totalEdi = totalEdi + aDouble;
			}
			meanEdi = totalEdi / edis.size();
			meanEdi = Math.round(meanEdi * 100.0) / 100.0;

			// mean length
			double totalLength = 0.0;
			for (Double length : lengths) {
				totalLength = totalLength + length;
			}
			meanLength = totalLength / lengths.size();
			meanLength = Math.round(meanLength * 100.0) / 100.0;

			// median EDI
			if (edis.size() % 2 == 1) // odd amount of EDIs
			{
				if (edis.size() == 1) {
					medianEdi = edis.get(0);
				} else {
					medianEdi = edis.get(edis.size() / 2);
				}
			} else // even amount of EDIs
			{
				if (edis.size() == 2) {
					medianEdi = (edis.get(0) + edis.get(1)) / 2.0;
				} else {
					medianEdi = (edis.get((edis.size() / 2) - 1) + edis.get(edis.size() / 2)) / 2.0;
				}
				medianEdi = Math.round(medianEdi * 100.0) / 100.0;
			}

			// median length
			if (lengths.size() % 2 == 1) // odd amount of lengths
			{
				if (lengths.size() == 1) {
					medianLength = lengths.get(0);
				} else {
					medianLength = lengths.get(lengths.size() / 2);
				}
			} else // even amount of lengths
			{
				if (lengths.size() == 2) {
					medianLength = (lengths.get(0) + lengths.get(1)) / 2.0;
				} else {
					medianLength = (lengths.get((lengths.size() / 2) - 1) + lengths.get(lengths.size() / 2)) / 2.0;
				}
				medianLength = Math.round(medianLength * 100.0) / 100.0;
			}

			// round total miles
			totals = Math.round(totals * 100.0) / 100.0;

			fileWriter1.append("<p><b>Total Routes: </b>").append(String.valueOf(lengths.size())).append("<br>\n");
			fileWriter1.append("<b>Mean EDI: </b>").append(String.valueOf(meanEdi)).append("<br>\n");
			fileWriter1.append("<b>Median EDI: </b>").append(String.valueOf(medianEdi)).append("<br>\n");
			fileWriter1.append("<b>Mean Length: </b>").append(String.valueOf(meanLength)).append(" mi.<br>\n");
			fileWriter1.append("<b>Median Length: </b>").append(String.valueOf(medianLength)).append(" mi.</p>\n");

			lengths.clear();
			edis.clear();

			System.out.println("Stats List: Global");

			// loop through all the agencies
			for (int a = 0; a < agencies.size(); a++) {
				fileWriter1.append("<h3 id=").append(agencies.get(a)).append(">").append(fullAgencies.get(a)).append(" (").append(agencies.get(a)).append(")</h3> \n");

				try {
					Scanner s = new Scanner(new File("edis/" + agencies.get(a) + ".txt"));
					while (s.hasNextLine()) {
						String data = s.nextLine();
						data = data.substring(data.indexOf(";") + 1);
						double miles = Double.parseDouble(data.substring(0, data.indexOf(";")));
						data = data.substring(data.indexOf(";") + 1);
						double edi = Double.parseDouble(data.substring(0, data.indexOf(";")));

						lengths.add(miles);
						edis.add(edi);
						totals += miles;
					}
				} catch (Exception e) {
					continue; // skip because agency not in database
				}

				Collections.sort(lengths);
				Collections.sort(edis);

				// mean EDI
				totalEdi = 0.0;
				for (Double aDouble : edis) {
					totalEdi = totalEdi + aDouble;
				}
				meanEdi = totalEdi / edis.size();
				meanEdi = Math.round(meanEdi * 100.0) / 100.0;

				// mean length
				totalLength = 0.0;
				for (Double length : lengths) {
					totalLength = totalLength + length;
				}
				meanLength = totalLength / lengths.size();
				meanLength = Math.round(meanLength * 100.0) / 100.0;

				// median EDI
				if (edis.size() % 2 == 1) // odd amount of EDIs
				{
					if (edis.size() == 1) {
						medianEdi = edis.get(0);
					} else {
						medianEdi = edis.get(edis.size() / 2);
					}
				} else // even amount of EDIs
				{
					if (edis.size() == 2) {
						medianEdi = (edis.get(0) + edis.get(1)) / 2.0;
					} else {
						medianEdi = (edis.get((edis.size() / 2) - 1) + edis.get(edis.size() / 2)) / 2.0;
					}
					medianEdi = Math.round(medianEdi * 100.0) / 100.0;
				}

				// median length
				if (lengths.size() % 2 == 1) // odd amount of lengths
				{
					if (lengths.size() == 1) {
						medianLength = lengths.get(0);
					} else {
						medianLength = lengths.get(lengths.size() / 2);
					}
				} else // even amount of lengths
				{
					if (lengths.size() == 2) {
						medianLength = (lengths.get(0) + lengths.get(1)) / 2.0;
					} else {
						medianLength = (lengths.get((lengths.size() / 2) - 1) + lengths.get(lengths.size() / 2)) / 2.0;
					}
					medianLength = Math.round(medianLength * 100.0) / 100.0;
				}

				// round total miles
				totals = Math.round(totals * 100.0) / 100.0;

				fileWriter1.append("<p><b>Current Routes: </b>").append(String.valueOf(lengths.size())).append("<br>\n");
				fileWriter1.append("<b>Mean EDI: </b>").append(String.valueOf(meanEdi)).append("<br>\n");
				fileWriter1.append("<b>Median EDI: </b>").append(String.valueOf(medianEdi)).append("<br>\n");
				fileWriter1.append("<b>Mean Length: </b>").append(String.valueOf(meanLength)).append(" mi.<br>\n");
				fileWriter1.append("<b>Median Length: </b>").append(String.valueOf(medianLength)).append(" mi.</p>\n");

				lengths.clear();
				edis.clear();

				// check for sets
				try {
					// load in the sets
					ArrayList<String> agencySets = new ArrayList<>();
					Scanner t = new Scanner(new File("sets/" + agencies.get(a) + ".txt"));
					while (t.hasNextLine()) {
						String data = t.nextLine();
						agencySets.add(data);
					}

					// load in from the set
					// loop through all sets
					for (String agencySet : agencySets) {
						Scanner s = new Scanner(new File("edis/sets/" + agencies.get(a) + "-" + agencySet + ".txt"));
						while (s.hasNextLine()) {
							String data = s.nextLine();
							data = data.substring(data.indexOf(";") + 1);
							double miles = Double.parseDouble(data.substring(0, data.indexOf(";")));
							data = data.substring(data.indexOf(";") + 1);
							double edi = Double.parseDouble(data);

							lengths.add(miles);
							edis.add(edi);
							totals += miles;
						}

						Collections.sort(lengths);
						Collections.sort(edis);

						// mean EDI
						totalEdi = 0.0;
						for (Double edi : edis) {
							totalEdi = totalEdi + edi;
						}
						meanEdi = totalEdi / edis.size();
						meanEdi = Math.round(meanEdi * 100.0) / 100.0;

						// mean length
						totalLength = 0.0;
						for (Double length : lengths) {
							totalLength = totalLength + length;
						}
						meanLength = totalLength / lengths.size();
						meanLength = Math.round(meanLength * 100.0) / 100.0;

						// median EDI
						if (edis.size() % 2 == 1) // odd amount of EDIs
						{
							if (edis.size() == 1) {
								medianEdi = edis.get(0);
							} else {
								medianEdi = edis.get(edis.size() / 2);
							}
						} else // even amount of EDIs
						{
							if (edis.size() == 2) {
								medianEdi = (edis.get(0) + edis.get(1)) / 2.0;
							} else {
								medianEdi = (edis.get((edis.size() / 2) - 1) + edis.get(edis.size() / 2)) / 2.0;
							}
							medianEdi = Math.round(medianEdi * 100.0) / 100.0;
						}

						// median length
						if (lengths.size() % 2 == 1) // odd amount of lengths
						{
							if (lengths.size() == 1) {
								medianLength = lengths.get(0);
							} else {
								medianLength = lengths.get(lengths.size() / 2);
							}
						} else // even amount of lengths
						{
							if (lengths.size() == 2) {
								medianLength = (lengths.get(0) + lengths.get(1)) / 2.0;
							} else {
								medianLength = (lengths.get((lengths.size() / 2) - 1) + lengths.get(lengths.size() / 2)) / 2.0;
							}
							medianLength = Math.round(medianLength * 100.0) / 100.0;
						}

						// round total miles
						totals = Math.round(totals * 100.0) / 100.0;

						fileWriter1.append("<p><b>").append(agencySet).append(" Set</b><br>\n");
						fileWriter1.append("<b>Set Routes: </b>").append(String.valueOf(lengths.size())).append("<br>\n");
						fileWriter1.append("<b>Mean EDI: </b>").append(String.valueOf(meanEdi)).append("<br>\n");
						fileWriter1.append("<b>Median EDI: </b>").append(String.valueOf(medianEdi)).append("<br>\n");
						fileWriter1.append("<b>Mean Length: </b>").append(String.valueOf(meanLength)).append(" mi.<br>\n");
						fileWriter1.append("<b>Median Length: </b>").append(String.valueOf(medianLength)).append(" mi.</p>\n");

						lengths.clear();
						edis.clear();
					}
				} catch (Exception e) {
					// don't do a damn thing
				}

				System.out.println("Stats List: " + (a + 1) + " / " + agencies.size());
			}
			fileWriter1.close();
		} catch (Exception e) {
			System.out.println("Error creating stats page.");
		}
	}

	/**
	 * Generates stops.html
	 */
	static void stopList() {
		ArrayList<String> agencies = Util.getAgencyIds();
		ArrayList<String> fullAgencies = Util.getAgencyNames();

		int stopCounter = 0; // total amount of stops

		for (int i = 0; i < agencies.size(); i++) {
			// creates each agency page
			try {
				int agencyStops = 0; // agency stop counter
				File newFile1 = new File("stops/" + agencies.get(i) + ".html");
				FileWriter fileWriter1 = new FileWriter(newFile1);

				fileWriter1.write("<title>" + fullAgencies.get(i) + " Stops - Eliot Deviation Index</title> \n");
				fileWriter1.append("<link rel=stylesheet href=../style.css> \n");
				fileWriter1.append("<ul><li><a href=../index.html>Home</a></li>");
				fileWriter1.append("<li><a href=../stops.html class=active>Stop Listing</a></li> \n");
				fileWriter1.append("<li><a href=../routes.html>Route Listing</a></li> \n");
				fileWriter1.append("<li><a href=../detailed.html>Route Detail</a></li> \n");
				fileWriter1.append("<li><a href=../stats.html>Statistics</a></li> \n");
				fileWriter1.append("<li><a href=../calculator.html>Calculator</a></li></ul> \n");
				fileWriter1.append("<h1>Stop Listing</h1> \n");

				fileWriter1.append("<h3>").append(fullAgencies.get(i)).append(" (").append(agencies.get(i)).append(")</h3> \n");

				ArrayList<String> stopID = new ArrayList<>();
				ArrayList<String> stopName = new ArrayList<>();
				ArrayList<String> stopLat = new ArrayList<>();
				ArrayList<String> stopLon = new ArrayList<>();

				try {
					// port .txt list to webpage
					Scanner s = new Scanner(new File("stops/" + agencies.get(i) + ".txt"));
					while (s.hasNextLine()) {
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
				} catch (FileNotFoundException e) {
					System.out.println("Error, no stop file for " + agencies.get(i) + ".");
				}

				fileWriter1.append("<table><tr><th>Stop ID</th><th>Stop Name</th><th>Stop Latitude</th><th>Stop Longitude</th></tr> \n");

				for (int j = 0; j < stopID.size(); j++) {
					fileWriter1.append("<tr><td style=color:#1aab2d>").append(stopID.get(j)).append("</td><td>").append(stopName.get(j)).append("</td><td>").append(stopLat.get(j)).append("</td><td>").append(stopLon.get(j)).append("</td></tr> \n");
					agencyStops++;
					stopCounter++;
				}

				fileWriter1.append("</table> \n");
				fileWriter1.append("<p><b>Agency Stops:</b> ").append(String.valueOf(agencyStops)).append("</p>");
				fileWriter1.flush();
				fileWriter1.close();
				System.out.println("Agency Stops (" + agencies.get(i) + "): " + agencyStops);
			} catch (IOException e) {
				System.out.println("Error, can't save stops for " + agencies.get(i) + ".");
			}
		}

		// main stops home page
		try {
			File newFile2 = new File("stops.html");
			FileWriter fileWriter2 = new FileWriter(newFile2);
			fileWriter2.write("<title>Stop Listing - Eliot Deviation Index</title> \n");
			fileWriter2.append("<link rel=stylesheet href=style.css> \n");
			fileWriter2.append("<ul><li><a href=index.html>Home</a></li>");
			fileWriter2.append("<li><a href=stops.html class=active>Stop Listing</a></li> \n");
			fileWriter2.append("<li><a href=routes.html>Route Listing</a></li> \n");
			fileWriter2.append("<li><a href=detailed.html>Route Detail</a></li> \n");
			fileWriter2.append("<li><a href=stats.html>Statistics</a></li> \n");
			fileWriter2.append("<li><a href=calculator.html>Calculator</a></li></ul> \n");
			fileWriter2.append("<h1>Stop Listing</h1> \n");
			fileWriter2.append("<p> \n");

			for (int a = 0; a < agencies.size(); a++) {
				fileWriter2.append("<ul class=bullet style=background-color:#1f1f1f><a href=stops/").append(agencies.get(a)).append(".html>").append(fullAgencies.get(a)).append("</a> (").append(agencies.get(a)).append(")</ul> \n");
			}

			fileWriter2.append("<p><b>Total Stops: </b>").append(String.valueOf(stopCounter)).append("</p>");
			fileWriter2.flush();
			fileWriter2.close();
			System.out.println("Stop Count: " + stopCounter);
		} catch (Exception e) {
			System.out.println("Error, can't create stop home page.");
		}
	}

	static void stopMap() {
		ArrayList<String> agencies = Util.getAgencyIds();

		for (String agency : agencies) {
			ArrayList<Stop> load = Util.getAgencyStops(agency);
			ArrayList<String> maps = new ArrayList<>();

			maps.add("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			maps.add("<kml xmlns=\"http://www.opengis.net/kml/2.2\">");
			maps.add("<Document> \n\t<name>" + agency + " stops</name>");
			maps.add("\t<Style id=\"stop\"> \n\t\t<IconStyle> \n\t\t\t<scale>0.5</scale> \n\t\t\t<Icon> \n\t\t\t\t<href>icon.png</href> \n\t\t\t</Icon> \n\t\t</IconStyle> \n\t</Style>");
			maps.add("\t<Placemark> \n\t\t<styleUrl>#stop</styleUrl>");
			maps.add("\t\t<description>Name: " + load.get(0).getName() + "<br/>ID: " + load.get(0).getId() + "</description>");
			maps.add("\t\t<Point> \n\t\t\t<coordinates>");
			maps.add("\t\t\t" + load.get(0).getLon() + "," + load.get(0).getLat() + ",0");

			for (int i = 1; i < load.size(); i++) {
				maps.add("\t\t\t</coordinates> \n\t\t</Point> \n\t</Placemark>");
				maps.add("\t<Placemark> \n\t\t<styleUrl>#stop</styleUrl>");
				maps.add("\t\t<description>Name: " + load.get(i).getName() + "<br/>ID: " + load.get(i).getId() + "</description>");
				maps.add("\t\t<Point> \n\t\t\t<coordinates>");
				maps.add("\t\t\t" + load.get(i).getLon() + "," + load.get(i).getLat() + ",0");
			}

			maps.add("\t\t\t</coordinates> \n\t\t</Point> \n\t</Placemark> \n</Document> \n</kml>");

			try {
				File newFile1 = new File("maps/stops/map-" + agency + ".kml");
				FileWriter fileWriter1 = new FileWriter(newFile1);

				fileWriter1.write(maps.get(0) + "\n");

				for (int i = 1; i < maps.size(); i++) {
					fileWriter1.append(maps.get(i)).append("\n");
				}

				fileWriter1.close();
			} catch (Exception e) {
				System.out.println("Error.");
			}
		}
	}
}
