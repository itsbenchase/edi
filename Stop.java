import java.util.ArrayList;

public class Stop
{
  private String id;
  private String name;
  private double lat;
  private double lon;
  private int order;
  private String lineEDI;
  private ArrayList<String> line;

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