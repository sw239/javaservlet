# COMPX575- Task Six
Due on **Friday the 9th of April at midday**. 

# Task Goal
* Installing a web server (Jetty)
* Port numbers
* Stopping and restarting web servers
* Web apps
* Servlets
* jar (Java Archive) files
* war (Web application Archive) files
* Compiling Java programs
* Writing web apps

## Preamble
1. Fork this repository using the button at the top of the project page.
2. Make sure that the visibility of your project is private. (Settings > expand Permissions > Project visibility: Private; Save changes).  *Note: Class teachers and tutors will still have read access to your project for marking purposes.*
3. Clone the new repository to your computer using Git.  Store the repository in your COMPX575 directory.
4. Remember to commit and push regularly as you work on the project!  

### Part One: Install the Jetty web server
1. Go to https://www.eclipse.org/jetty/ to learn more about Jetty.
2. Jetty is installed on the lab machines under /home/compx575/jetty/jetty-9.4.9.  You can also download it from: https://www.eclipse.org/jetty/download.html
3. In that directory, read the sections "Running Jetty" and "Jetty Base" in the file *README.TXT*
4. Set the JETTY\_HOME environment variable to this path: *export JETTY\_HOME=/home/compx575/jetty/jetty-9.4.9*

You will need to do this in each terminal window you open.

5. Try running the demo:
```
cd $JETTY_HOME/demo-base
java -jar $JETTY_HOME/start.jar
```

6. Open a web browser and go to http://localhost:8080.  You should see Jetty's weblcome page.
7. The default port number of Jetty is 8080; now we restart it using port 8088 (Google "port number" to learn more about port numbers)
    * Stop Jetty: Ctrl + C
    * Restart Jetty: _java -jar $JETTY\_HOME/start.jar jetty.http.port=8088_
8. Open http://localhost:8080 again.  You should see an error "Unable to connect".  Change 8080 to 8088 in the URL to get the welcome page.
9. Stop the Jetty server using Ctrl + C

### Part Two: Set up a simple web app
1. Create a directory called *mybase* and initialise Jetty:
    * mkdir mybase
    * cd mybase
    * java -jar $JETTY_HOME/start.jar --add-to-start=http,deploy
2. Have a look at what was generated in the *mybase* directory: *ls*
3. Copy *helloworld.war* and *hellworld.xml* into *mybase/webapps*
4. In *mybase* start the Jetty server: *java -jar $JETTY_HOME/start.jar*
5. Open the browser and go to *http://localhost:8080/hw/helloworld*.  You should see the string "Hello, world!".
6. The file *helloworld.xml* is used to tell the web server how to deploy *helloworld.war*.  Open *helloworld.xml* in a text editor.  It contains two XML elements:
    * _\<Set name="contextPath">/hw\</Set>_
    * _\<Set name="war">...\</Set>_

Here *contextPath* is */hw*, and is used to distinguish different web apps.  *war* is used to tell the server where to find the war file.

7. Change the context path by opening *helloworld.xml* in a text editor, replacing *hw* with *123*, and saving.
8. Open *http://localhost:8080/hw/helloworld* again. What error do you get? Change *hw* to *123* in the URL. You should see "Hello, world!" again.
9. Change the context path in *helloworld.xml* back to */hw*

### Part Three: What's under the hood?
Let's look at the structure of a web app and learn how to deploy it.  There are many ways to write Jetty web apps; here we use *war* files.  Google *war file* to learn more.

1. In *mybase*, create a directory called *helloworldwar* and copy *helloworld.war* into it.
2. Run *jar xvf helloworld.war*.  This extracts a directory called *WEB-INF* and other files.
3. Examine the *WEB-INF* directory.  It contains a directory called *classes* and a file called *web.xml*
4. Examine the *classes* directory.  It contains two files: *HelloWorldServlet.java* and *HelloWorldServlet.class*.  The *.class* file is the result of compiling the *.java* file.  This program is the entry point of the *HelloWorld* web app.  It is called a Java "servlet".  Servlets are programs that run on a web server and build web pages; they are Java's answer to web application development.
5. Open *web.xml* in a text editor.  This file is used to tell the server how to map a URL to a Java servlet.
    * _\<servlet\> ... \</servlet\>_ specifies the name of the servlet, its class and whether to load it when the server starts up.
    * _\<servlet-mapping\> ... \</servlet-mapping\>_ specifies the name of the servlet and the URL pattern that will invoke it.

Change _/helloworld/*_ to _hworld/*_ in *web.xml* and save.

6. Open HelloWorldServlet.java in a text editor. Change the text "Hello, world!" to some other string—it can include HTML tags, e.g. *\<html>\<body>\<h1>Hello, world!\</h1>\</body>\</html>*.
7. Recompile *HelloWorldServlet.java*.  In the *classes* directory, run *javac HelloWorldServlet.java*

You will get an error: package javax.servlet.http does not exist. This is because the compiler cannot find the Java class files required by HelloWorldServlet.java. To fix this, tell the compiler where to find them by specifying the classpath: *javac -classpath $JETTY_HOME/lib/servlet-api-3.1.jar HelloWorldServlet.java*

*servlet-api-3.1.jar* contains the files required by *HelloWorldServlet.java*

8. Re-pack the *war* file to use the newly compile program.  In *helloworldwar* run *jar cvf helloworld.war WEB-INF*
9. Move *helloworld.war* to *mybase/webapps* (overwriting existing if it still exists)
10. Open *http://localhost:8080/hw/helloworld* in a browser. You will see an error page because the URL was changed in step 5. Open *http://localhost:8080/hw/hworld* to see "Hello, world!" in a large font. Check the HTML tags by viewing the page source.

### Part Four: Write your own web app and deploy it
You now know enough to write your own web apps.  Your next job is to write a web app that queries a server to retrieve states and territories of a country and prints the resut in HTML format.

1. Try opening all.json in a browser.  This will return all the states of the USA.  You can also see this file by navigating to https://www.cs.waikato.ac.nz/~shaoqun/COMP575/Jetty/all.json
2. Create a file named *DumpStatesServlet.java* in the *classes* directory and write a servlet that fetches https://www.cs.waikato.ac.nz/~shaoqun/COMP575/Jetty/all.json and dumps the result.  Use *HelloWorldServlet.java* as a template (don't forget to update the name of the class).

Here is some useful code (add _import java.net.*_ at the beginning of the Java file):
```
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
```

3. Compile the Java file.
4. Add the servlet and an appropriate URL mapping to web.xml
5. Repack the WEB-INF directory into a war file and put it in *mybase/webapps*

### Part Five: Parsing the JSON result
The previous task dumps the raw JSON data. Your next job is to use a Java JSON library to parse the JSON results and generate an HTML page containing a table like the following:

| name | abbr | area | largest_city | capital |
| ------ | ------ | ------ | ------ | ------ |
| Alambama | AL | 135767SKM | Birmingham | Montgomery |

The JSON libraries javax.json:javax.json-api and org.glassfish:javax.json are installed in /home/compx575/jetty/json.

Create a file named ShowStatesServlet.java in the classes directory. Start with a copy of DumpStatesServlet.java and modify it to parse the JSON results and generate an HTML table.

This is an example of using the *javax.json* library (add _import javax.json.*_ to imports):
```
String data = "{\"RestResponse\":{\"result\":[{\"name\":\"Alabama\"}]}}";
Reader reader = new StringReader(data);
JsonReader jsonReader = Json.createReader(reader);
JsonObject obj = jsonReader.readObject();
jsonReader.close();
JsonArray results = obj.getJsonObject("RestResponse").getJsonArray("result");
for (JsonObject result : results.getValuesAs(JsonObject.class)) {
    out.println(result.getString("name"));
}
```

You need to make sure the jar files in /home/compx575/jetty/json are in the classpath when compiling the servlet. You can use the -cp (-classpath) option as before: _javac -cp $JETTY\_HOME/lib/servlet-api-3.1.jar:/home/compx575/jetty/json/* ShowStatesServlet.java_

Alternatively you can set the *CLASSPATH* variable before compiling: _export CLASSPATH=.:$JETTY\_HOME/lib/servlet-api-3.1.jar:/home/compx575/jetty/json/*_

You will need to run this command once in each terminal window (after running *export JETTY_HOME=...*), and then you'll be able to compile using *javac ShowStatesServlet.java*.

Add the servlet to *web.xml* and re-pack WEB-INF as before.

When you open the new page you'll get a *NoClassDefFoundError* because Jetty can't find the JSON libraries. On the lab machines they are also installed in *$JETTY_HOME/lib/ext*, which can be enabled by running Jetty as: _java -jar $JETTY\_HOME/start.jar --module=ext_

Note: some of the fields in the JSON data are missing. Consider using *JsonObject.getString(name, defaultValue)* to handle them.

## Grading
This task is worth as much as 5% of your final grade and is marked out of 5.
* Each part is worth 1 mark.

## Submission
On top of the commits to your Git repository, please upload your souce files to moodle.