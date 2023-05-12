// runs all the update files in one go
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
  }
}