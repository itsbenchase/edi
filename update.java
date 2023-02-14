// runs all the update files in one go
public class update
{
  public static void main(String [] args)
  {
    StopList a = new StopList();
    RouteList b = new RouteList();
    FullMap c = new FullMap();
    AgencyMap d = new AgencyMap();
    StopMap e = new StopMap();

    a.main(args);
    b.main(args);
    c.main(args);
    d.main(args);
    e.main(args);
  }
}