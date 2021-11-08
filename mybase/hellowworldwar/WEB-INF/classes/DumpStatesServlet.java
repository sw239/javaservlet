import java.io.*;

import javax.servlet.http.*;
import javax.servlet.*;
import java.net.* ;
public class DumpStatesServlet extends HttpServlet {
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
while (true) {
    String line = reader.readLine();
    if (line == null) {
        break;
    }
    out.println(line);
} 
reader.close();
  }
}


