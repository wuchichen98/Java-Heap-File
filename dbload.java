import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class dbloadgit {
	
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
			//1:int num,2:50 char,3:float,4:2 float num
			int[] typeList = {1,1,1,1,2,2,2,1,1,1,2,2,2,2,2,2,3,3,4};
			List<String> data = readCsv(new File(readFilePath));
			int length_byte = getLineLength(typeList);
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
	
    /**  
     * get length of one line data
     *  
     * @param typeList : array, the type of data  
     * @return int, the count of bytes array
     */ 
	public static int getLineLength(int[] typeList) {
		int i,type,length_byte=0;
	    for(i=0;i<typeList.length;i++){
	    	type = typeList[i];
	    	switch(type) {
	    	case 1: // int 4 byte
	    		length_byte += 4;
	    		break;
	    	case 2: // 50 char  50 byte
	    		length_byte += 50;
	    		break;
	    	case 3: //float  4 byte
	    		length_byte += 4;
	    		break;
	    	case 4: //2 float  8 byte
	    		length_byte += 8;
	    		break;
	    	}
	    }
	    length_byte = length_byte + 2;//"\r\n"
	    System.out.println("length_byte:"+length_byte);
	    return length_byte;
	}//end of getLineLength
	
    
}//end of class dbloadgit