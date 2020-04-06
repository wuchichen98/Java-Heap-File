import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class dbquery {
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		for(String s:args) {
			//System.out.println(s);
		}
		
		String text = "PRICE HALL";
		int pageSize = 4096;
		String filepath = "./data";
		boolean argsRight = false;
		
		if (args.length == 2) {
			//System.out.println("legth true");
			try {
				text = args[0];
				pageSize = Integer.parseInt( args[1] );
				argsRight = true;
				
			}catch(Exception e){
				argsRight = false;
			}
		}
		pageSize = 4096;
		argsRight = true;
		
		if(argsRight) {
			//searchTextInHF(filepath, pageSize,text);
		}else {
			System.out.println("args error");
		}
		
		//System.out.println("over");
		
	}//end of main

}