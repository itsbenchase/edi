import java.util.Scanner;

class Main 
{
  public static void main(String[] args) 
  {
    Scanner a = new Scanner(System.in);
    
    System.out.println("Eliot Deviation Index");
    System.out.println("Online Application");
    System.out.println("-----------------------");
    
    System.out.println("");
    
    System.out.println("Select an option:");
    System.out.println("- calc: The calculator for the Eliot Deviation Index");
    System.out.println("- search: Search the current stop database");
    
    System.out.println("");
    
    System.out.print("Choice: ");
    String choice = a.nextLine();

    if (choice.equalsIgnoreCase("calc")) // typical user experience
    {
      ediOnline edi = new ediOnline();
      edi.main(args);
    }
    else if (choice.equalsIgnoreCase("search"))
    {
      stopSearch search = new stopSearch();
      search.main(args);
    }

    else if (choice.equalsIgnoreCase("ben")) // my copy that'll save to the database
    {
      ediCalc ben = new ediCalc();
      ben.main(args);
    }

    else if (choice.equalsIgnoreCase("update")) // updates database
    {
      update up = new update();
      up.main(args);
    }

    else if (choice.equalsIgnoreCase("add")) // adds stop to database
    {
      addStop add = new addStop();
      add.main(args);
    }
  }
}