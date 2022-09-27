// Stop class, anything and everything a stop has.
import java.util.ArrayList;

public class Stop
{
  private String id; // stop id
  private String name; // stop name
  private double lat; // stop latitude
  private double lon; // stop longitude
  private int order; // order in line
  private String lineEDI; // line stop is on
  private ArrayList<String> line; // not used in EDI (Fixing Fare Zones only)

  // this is a generic class used in both EDI and Fixing Fare Zones.

  public Stop(String n, double la, double lo)
  {
    name = n;
    lat = la;
    lon = lo;
    line = new ArrayList<String>();
  }
  public Stop(String i, String n, double la, double lo)
  {
    id = i;
    name = n;
    lat = la;
    lon = lo;
  }
  public Stop(String i, String n, double la, double lo, String l, int o)
  {
    id = i;
    name = n;
    lat = la;
    lon = lo;
    order = o;
    lineEDI = l;
  }

  public void setName(String n)
  {
    name = n;
  }
  
  public void addLine(String l)
  {
    line.add(l);
  }

  public void setLat(double l)
  {
    lat = l;
  }

  public void setLon(double l)
  {
    lon = l;
  }

  public String getName()
  {
    return name;
  }

  public double getLat()
  {
    return lat;
  }

  public double getLon()
  {
    return lon;
  }

  public int getOrder()
  {
    return order;
  }

  public String getID()
  {
    return id;
  }

  public String getLineEDI()
  {
    return lineEDI;
  }

  public ArrayList<String> getLine()
  {
    return line;
  }
}