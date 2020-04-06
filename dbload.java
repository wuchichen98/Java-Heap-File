import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class dbload {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		int pageSize = 4096;
		String readFilePath = "";
		String writeFilePath = "./data";
		boolean argsRight = false;
		
		for(String s :args){
			System.out.println(s+"  "+s.length());
		}
		
		if (args.length == 3) {
			System.out.println("legth true");
			try {
				if(args[0].equals("-p")) {
					System.out.println("args[0] true");
					writeFilePath = args[2];
					pageSize = Integer.parseInt( args[1] );
					System.out.println("filepath:"+writeFilePath);
					System.out.println("pagesize:"+pageSize);
					argsRight = true;
				}
			}catch(Exception e){
				argsRight = false;
			}
		}
		
		argsRight = true;
		pageSize = 4096;
		readFilePath = "D:\\File\\Desktop\\java database\\code\\test.csv";
		
		
		if(argsRight)
		{
			List<String> data = readCsv(new File(readFilePath));
			System.out.println("over");
			
		}else {
			System.out.println("args error");
		}
		
		
		System.out.println("main func over");
		
    }//end of main
	
	
    /**  
     * read data from csv file  
     *  
     * @param file : file path  
     * @return a String list contains data in file
     */   
	public static List<String> readCsv(File file){
		List<String> data=new ArrayList<String>();
        
        BufferedReader br=null;
        try { 
            br = new BufferedReader(new FileReader(file));
            String line = ""; 
            while ((line = br.readLine()) != null) {
            	data.add(line);
            }
        }catch (Exception e) {
        }finally{
            if(br!=null){
                try {
                    br.close();
                    br=null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
 
        return data;
    }//end of readCsv
	
    
}//end of class dbloadgit