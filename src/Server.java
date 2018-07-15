import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
public class Server {
    private static final int PORT = 8080;
    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(PORT);
            System.out.println("MiniServer active " + PORT);
            while (true) {
                new ThreadedSocket(server.accept());
            }
        } catch (Exception e) {
        }
    }
}
class ThreadedSocket extends Thread {
    private Socket insocket;
    ThreadedSocket(Socket insocket) {
        this.insocket = insocket;
        this.start();
    }
    @Override
    public void run() {
        try {
            InputStream is = insocket.getInputStream();
            PrintWriter out = new PrintWriter(insocket.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(is));
            String line;
            line = in.readLine();
            String request_method = line;
            System.out.println(line);
            String contents = parseRequest(line);
            line = "";
            
//            int postDataI = -1;
//            while ((line = in.readLine()) != null && (line.length() != 0)) {
//                System.out.println(line);
//                if (line.indexOf("Content-Length:") > -1) {
//                    postDataI = new Integer(
//                            line.substring(
//                                    line.indexOf("Content-Length:") + 16,
//                                    line.length())).intValue();
//                }
//            }
//            String postData = "";
//  
//            if (postDataI > 0) {
//                char[] charArray = new char[postDataI];
//                in.read(charArray, 0, postDataI);
//                postData = new String(charArray);
//            }
            
            
            out.println("HTTP/1.0 200 OK");
            out.println("Content-Type: text/html; charset=utf-8");
            out.println("Server: MINISERVER");
            out.println("");
            out.println("<script> function load() {document.location = \"https://google.com\"} </script>");
            out.println("<button onclick='load()'>submit</button>");
            contents = "";
            out.close();
            insocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public String parseRequest(String request) {
    		BufferedReader br;
		String contents = "";
    		if (request.equals("GET /file1 HTTP/1.1")) {
			try {
				br = new BufferedReader(new FileReader("file1.html"));
	            String line = br.readLine();
	            while (line != null) {
	    	            	contents += line;
	    	            	line = br.readLine();
	                }
			} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			}
    		}
    		else if (request.equals("GET /file2 HTTP/1.1")) {
    			try {
					br = new BufferedReader(new FileReader("file2.html"));
	                String line = br.readLine();
	                while (line != null) {
	    	            	contents += line;
	    	            	line = br.readLine();
	                }
			} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			}
    		}
    		return contents;
    }
}