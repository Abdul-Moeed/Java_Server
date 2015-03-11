import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Reader {
    
    //class attributes
    int total_lines; //lines in file
    int size;		 //file size in bytes
    ArrayList<String> instructions; //operations derived from expression(if any)
    
    /*file reader for automated unit tests.
    Does not get any expression just data*/
    public int read_file(String path) throws FileNotFoundException, IOException {
        try{
        	FileReader fr = new FileReader(path+".txt");
        	File f = new File(path+".txt");
        	size = (int)f.length();
        	String line;
        	//get each line
        	BufferedReader buffer = new BufferedReader(fr); 
    		total_lines = 0;
    		instructions = new ArrayList<String>();
        
    		while( (line = buffer.readLine()) != null){
    			instructions.add(line);
    			total_lines++;
    		}
        	buffer.close();	
        }catch(FileNotFoundException e){
        	return -1;
        }
        return 1;
    }
    
    public void dump_file(PrintWriter out) {
    	out.println("<h4>Your requested page is: </h4>");
        for(int i=0;i<total_lines;i++) {
            out.println(instructions.get(i));
        }
    }
}
