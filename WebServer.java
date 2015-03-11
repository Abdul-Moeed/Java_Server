
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServer {

  /**
   * WebServer constructor.
   */
  protected void start() {
    ServerSocket s;

    System.out.println("Server at port 8080");
    System.out.println("(to kill: press ctrl + c)");
    try {
      // create the main server socket
      s = new ServerSocket(8080);
    } catch (Exception e) {
      System.out.println("Error: " + e);
      return;
    }

    System.out.println("Waiting for connection ...");
    for (;;) {
      try {
        // wait for a connection
        Socket remote = s.accept();
        // remote is now the connected socket
        System.out.println("Connection, sending data.");
        BufferedReader in = new BufferedReader(new InputStreamReader(
            remote.getInputStream()));
        PrintWriter out = new PrintWriter(remote.getOutputStream());

         /*read the data sent. We basically ignore it,
         stop reading once a blank line is hit. This
         blank line signals the end of the client HTTP
         headers */
        String str = ".";
        String[] tokens;
        boolean fetch_file = false;
        Reader fr = new Reader();
        
        while (!str.equals("")){
          str = in.readLine();
          //if GET POST OR HEAD method used in request
          if((str.contains("POST") || str.contains("HEAD") || str.contains("GET")) && !str.contains("favicon")){
        	  //tokenize request
        	  tokens = str.split(" ");
        	  //get requested page
        	  String file_name = tokens[1].substring(1, tokens[1].length());
        	  
        	  //HTTP Status Code settings
        	  //if not using HTTP 1.1 - send 505 error code
        	  if(!tokens[2].contains("1.1"))
        		  str = "HTTP/1.1 505 HTTP Version Not Supported";
        	  
        	  //if file not found - send 404 error code
        	  else if(fr.read_file(file_name)<1)
            	  str = "HTTP/1.1 404 File not found";
        	  //if url length greater than 50 characters  
        	  else if(str.length()>50)
        		  str = "HTTP/1.1 414 Request-URI Too Long";
        	  //if file size greater than 500 bytes
        	  else if(fr.size>500)
        		  str = "HTTP/1.1 413 Request Entity Too Large";
        	  //if all above tests pass and request is type HEAD, only send header
        	  else if(tokens[0].equalsIgnoreCase("HEAD"))
        		  str = "HTTP/1.1 200 OK";
        	  //send file to client
        	  else
        		  str = "HTTP/1.1 200 OK";
        		  fetch_file = true;
        	  
        	  // Send the headers
        	  set_headers(out, str);
              
        	  //if file is to be sent to client
        	  if(fetch_file)
        		  print_page(fr, out);
          }
          //if neither GET POST OR HEAD method
          if(!(str.contains("POST") || str.contains("HEAD") || str.contains("GET")) && str.contains("favicon")){
        	  str = "HTTP/1.1 400 BAD REQUEST";
        	  set_headers(out, str);
          }
        }
        remote.close();
      } catch (Exception e) {
        System.out.println("Error: " + e);
      }
    }
  }
  // method to send headers in response
  public void set_headers(PrintWriter out, String http_code){
	  //http status code and statement
      out.println(http_code);
      out.println("Content-Type: text/html");
      out.println("Server: Abdul Moeed's Server");
      // this blank line signals the end of the headers
      out.println("");
  }

  // method for displaying page in HTML
  public void print_page(Reader fr, PrintWriter out){
	  out.println("<style> body{font-family: 'DengXian'; text-align: center;  background-color: #3C5A99; color: white; } </style>"
	  		+ "<body>"
	  		+ "<h1 style='text-decoration: underline;'>Welcome! This is a proprietary server.</h1>"
	  		+ "<h3>Author: Abdul Moeed</h3>");
	  fr.dump_file(out);
	  out.println("</body>");
	  out.flush();
  }

  /**
   * Author: Abdul Moeed
   * 
   * @param args
   *            Command line parameters are not used.
   */
  public static void main(String args[]) {
    WebServer ws = new WebServer();
    ws.start();
  }
}
