import java.io.*;

import javax.servlet.http.*;
import javax.servlet.*;
import java.net.* ;
import javax.json.* ;


public class ShowStatesServlet extends HttpServlet {
  public void doGet (HttpServletRequest req,
                     HttpServletResponse res)
    throws ServletException, IOException
  {
        PrintWriter out = res.getWriter();
    URL url = new URL("https://www.cs.waikato.ac.nz/~shaoqun/COMP575/Jetty/all.json");
URLConnection connection = url.openConnection();
InputStream in = connection.getInputStream();
BufferedReader reader = new BufferedReader(new InputStreamReader(in));
// read all.json line by line and write to out
String whole="";
while (true) {
    String line = reader.readLine();
    
    if (line == null) {
        break;
    }
    whole=whole.concat(line);
} 
reader.close();

Reader r = new StringReader(whole);
JsonReader jsonReader = Json.createReader(r);
JsonObject obj = jsonReader.readObject();
jsonReader.close();
JsonArray results = obj.getJsonObject("RestResponse").getJsonArray("result");
out.println("<html><body><style>table,td,th{border:2px solid #9932CC;}</style><table><th>Name</th><th>abbr</th><th>area</th><th>largest_city</th><th>capital</th>");
    for (JsonObject result : results.getValuesAs(JsonObject.class)) {
            out.println("<tr><td>" +result.getString("name","Empty") + "</td> " + "<td>" + result.getString("abbr","Empty") + "</td><td>" +result.getString("area","null") +"</td>" + "<td>" + result.getString("largest_city","Empty") + "</td>" + "<td>" + result.getString("capital","Empty") + "</td>" + "</tr>");
    }
    out.println("</table</body></html>");
  }
}


