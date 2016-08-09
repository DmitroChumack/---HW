import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class JDBCexample {

 static ResultSet rs = null;

	public static void main(String[] argv) throws IOException,NumberFormatException 
        {
		System.out.println("-------- PostgreSQL " + "JDBC Connection to Restaurant ------------");

		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("Where is your PostgreSQL JDBC Driver? " + "Include in your library path!");
			e.printStackTrace();
			return;
		}

		System.out.println("PostgreSQL JDBC Driver Registered!");
		Connection connection = null;

		try {
			connection = DriverManager.getConnection(
					"jdbc:postgresql://127.0.0.1:5432/Restaurant", "postgres","postgres");
		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return;
		}

		if (connection != null) {
			System.out.println("You take control your database now!");
		} else {
			System.out.println("Failed to make connection!"); return;
		}
       
       InputStreamReader inp=new InputStreamReader(System.in);
       BufferedReader in = new BufferedReader(inp);  
       String task="";
       int r;
       while(true)
       {
        while((task.isEmpty())||(task.length()>1)||(task.charAt(0)<'0')||(task.charAt(0)>'7'))
//             ||((Integer.parseInt(task)<0)||(Integer.parseInt(task)>7))
	{
 	 System.out.println("What table do you want to use:");
	 System.out.println("1.Emploee");
	 System.out.println("2.Dish");
	 System.out.println("3.Menu");
	 System.out.println("4.Order");
	 System.out.println("5.Prepared dish");
	 System.out.println("6.Dish ingredients");
	 System.out.println("7.Ingredients consistation");
	 System.out.println("0.Exit");
                 try
                 {
                   task = in.readLine();
                  }  
         	 catch(IOException ioe)
	   { System.err.println("Input error: " + ioe); } 
	 catch(NumberFormatException nfe) 
	   { System.err.println("NumberFormat error: " + nfe); } 
	}
      
        switch(Integer.parseInt(task))
        {
         case 1: Emploee(connection);break;
         case 2: Dish(connection);   break;
         case 3: Menu(connection);   break;
         case 4: Order(connection);  break;
         case 5: PDish(connection);  break;
         case 6: DishIngr(connection); break;
         case 7: DishCons(connection); break;
         case 0: System.out.println("Exit from Application"); return;
        }
        task="";
       }
   }

         public static void Emploee(Connection connection)
         { 
          try {
          Statement stmt = connection.createStatement();
          rs = stmt.executeQuery("SELECT * FROM \"Emploee\"");
	  System.out.println("==============================");
            while (rs.next()) {
               String str = rs.getString("lname")+" "+rs.getString("fname")+" - "+rs.getString("position")+": ";
               str += rs.getDate("bday")+"; tel-"+rs.getString("tel")+"; sal-"+rs.getFloat("salary");
               System.out.println("Emploee:" + str);
            }
          rs.close();
          stmt.close();
          }
	  catch(SQLException e)
	  {
           System.err.println("SQL stmt error");
	  }
	  System.out.println("==============================");
          return;
         } 

         public static void Dish(Connection connection)
         {
          List<Integer> sp = new ArrayList<Integer>();       
          int k=0;
          String s="";
          try {
          Statement stmt = connection.createStatement();
          rs = stmt.executeQuery("SELECT * FROM \"Dish\"");
	  System.out.println("==============================");
            while (rs.next()) {
              Statement stmt2 = connection.createStatement();
              ResultSet r =stmt2.executeQuery("SELECT array_upper(ingr,1) FROM \"Dish\" WHERE id="+rs.getInt("id"));
               while (r.next()) {
//                System.out.println("K=" + r.getInt(1));
                k=r.getInt(1);
              }
              r.close(); 
              sp.clear();
              for (int i=1; i<=k; i++)
                {
                  r =stmt2.executeQuery("SELECT ingr["+i+"] FROM \"Dish\" WHERE id="+rs.getInt("id"));
                     while (r.next()) { 
//                         System.out.println("i-TOE =" + r.getInt(1));
                         sp.add(r.getInt(1));
                     }
                  r.close();
                }
              String str = rs.getString("name")+": Cat-"+rs.getString("cat")+" W-"+rs.getFloat("weight")+" P-"+rs.getFloat("price");
              System.out.println("Dish:" + str);
              s="";
              for (int i=0; i<sp.size(); i++)
                {
                  r=stmt2.executeQuery("SELECT name FROM \"Ingredient\" WHERE \"Iid\"="+sp.get(i));
                     while (r.next()) { 
                         s+=r.getString("name")+"; ";
                     }
                 r.close();
                }
               stmt2.close();
               System.out.println("   Ingredients:" + " <" + s + ">");
            }
          rs.close();
          stmt.close();
          }
	  catch(SQLException e)
	  {
           System.err.println("SQL stmt error");
	  }
	  System.out.println("==============================");
          return;
         } 

         public static void Menu(Connection connection)
         { 
          List<Integer> sp = new ArrayList<Integer>();       
          int k=0;
          String s="";
          try {
          Statement stmt = connection.createStatement();
          rs = stmt.executeQuery("SELECT * FROM \"Menu\"");
	  System.out.println("==============================");
            while (rs.next()) {
              Statement stmt2 = connection.createStatement();
              ResultSet r =stmt2.executeQuery("SELECT array_upper(\"DishList\",1) FROM \"Menu\" WHERE \"Mid\"="+rs.getInt("Mid"));
               while (r.next()) {
//                 System.out.println("K=" + r.getInt(1));
                 k=r.getInt(1);
              }
              r.close(); 
              sp.clear();
              for (int i=1; i<=k; i++)
                {
                  r =stmt2.executeQuery("SELECT \"DishList\"["+i+"] FROM \"Menu\" WHERE \"Mid\"="+rs.getInt("Mid"));
                     while (r.next()) { 
//                         System.out.println("i-TOE =" + r.getInt(1));
                         sp.add(r.getInt(1));
                     }
                  r.close();
                }

               String str = rs.getString("name");
               System.out.println("Menu:" + str);
              s="";
              for (int i=0; i<sp.size(); i++)
                {
                  r=stmt2.executeQuery("SELECT name FROM \"Dish\" WHERE \"id\"="+sp.get(i));
                     while (r.next()) { 
                         s+=r.getString("name")+"; ";
                     }
                 r.close();
                }
               stmt2.close();
               System.out.println("   Dishes:" + " <" + s + ">");
            }
          rs.close();
          stmt.close();
          }
          catch(SQLException e)
	  {
            System.err.println("SQL stmt error");
	  }
	  System.out.println("==============================");
          return;
         } 

         public static void Order(Connection connection)
         { 
          List<Integer> sp = new ArrayList<Integer>();       
          int k=0;
          String s="";
          try {
          Statement stmt = connection.createStatement();
          rs = stmt.executeQuery("SELECT * FROM \"Order\"");
	  System.out.println("==============================");
            while (rs.next()) {
              Statement stmt2 = connection.createStatement();
              ResultSet r =stmt2.executeQuery("SELECT array_upper(\"DishList\",1) FROM \"Order\" WHERE \"Oid\"="+rs.getInt("Oid"));
               while (r.next()) {
                 k=r.getInt(1);
               }
              r.close(); 
              sp.clear();
              for (int i=1; i<=k; i++)
                {
                  r =stmt2.executeQuery("SELECT \"DishList\"["+i+"] FROM \"Order\" WHERE \"Oid\"="+rs.getInt("Oid"));
                     while (r.next()) { 
                         sp.add(r.getInt(1));
                     }
                  r.close();
                }
               String str = "Table= "+rs.getInt("Ntable")+" Data="+rs.getDate("Data")+" Oficiant=";
               k=rs.getInt("emploee");
               Statement stmt3 = connection.createStatement();
               ResultSet q = stmt3.executeQuery("SELECT * FROM \"Emploee\" WHERE id="+k);
               while (q.next()) { 
                   str+=q.getString("lname");
                 }
               System.out.println("Order: " + str);
               q.close();
               stmt3.close();
              s="";
              for (int i=0; i<sp.size(); i++)
                {
                  r=stmt2.executeQuery("SELECT name FROM \"Dish\" WHERE \"id\"="+sp.get(i));
                     while (r.next()) { 
                         s+=r.getString("name")+"; ";
                     }
                  r.close();
                }
               stmt2.close();
               System.out.println("   Dishes:" + " <" + s + ">");
            }
          rs.close();
          stmt.close();
          }
	  catch(SQLException e)
	  {
           System.err.println("SQL stmt error");
	  }
	  System.out.println("==============================");
          return;
         } 

         public static void PDish(Connection connection)
         { 
          int k;
          String s="";
          try {
          Statement stmt = connection.createStatement();
          rs = stmt.executeQuery("SELECT * FROM \"Prepared\"");
	  System.out.println("==============================");
            while (rs.next()) {
               String str = " Data="+rs.getDate("Data");
               k=rs.getInt("emploee");
               Statement stmt2 = connection.createStatement();
               ResultSet r = stmt2.executeQuery("SELECT * FROM \"Emploee\" WHERE id="+k);
                 while (r.next()) { 
                    s="Cook="+r.getString("lname");
                 }
               r.close();
               k=rs.getInt("Nb");
               r = stmt2.executeQuery("SELECT * FROM \"Dish\" WHERE id="+k);
                 while (r.next()) { 
                    s=s+" "+"Dish="+r.getString("name");
                 }
               r.close();
               k=rs.getInt("order");
               r = stmt2.executeQuery("SELECT * FROM \"Order\" WHERE \"Oid\"="+k);
                 while (r.next()) { 
                    s=s+" "+"Norder="+k+" (table: "+r.getInt("Ntable")+")";
                 }
               r.close();
               stmt2.close();
               str = s+str;
               System.out.println("Prepared Dish:" +rs.getInt("Bid"));
               System.out.println(str);
            }
          rs.close();
          stmt.close();
          }
	  catch(SQLException e)
	  {
           System.err.println("SQL stmt error");
	  }
	  System.out.println("==============================");
          return;
         } 

         public static void DishIngr(Connection connection)
         { 
          try {
          Statement stmt = connection.createStatement();
          rs = stmt.executeQuery("SELECT * FROM \"Ingredient\"");
	  System.out.println("==============================");
            while (rs.next()) {
               String str = rs.getString("name");
               System.out.println("Ingredient: " + str);
            }
          rs.close();
          stmt.close();
          }
	  catch(SQLException e)
	  {
           System.err.println("SQL stmt error");
	  }
	  System.out.println("==============================");
          return;
         } 

         public static void DishCons(Connection connection)
         { 
          try {
          Statement stmt = connection.createStatement();
          rs = stmt.executeQuery("SELECT * FROM \"Consist\"");
	  System.out.println("==============================");
            while (rs.next()) {
               String str = " County= "+rs.getFloat("County");
               Statement stmt2 = connection.createStatement();
               ResultSet r = stmt2.executeQuery("SELECT * FROM \"Ingredient\" WHERE \"Iid\"="+rs.getInt("I_id"));
               while (r.next()) {
                 str = "Ingr: "+r.getString("name")+str;
               }
               r.close();
               stmt2.close();
               System.out.println(str);
            }
          rs.close();
          stmt.close();
          }
	  catch(SQLException e)
	  {
           System.err.println("SQL stmt error");
	  }
	  System.out.println("==============================");
          return;
         } 
}