import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
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
			//1:int num,2:50 char,3:float,4:2 float num
			int[] typeList = {1,1,1,1,2,2,2,1,1,1,2,2,2,2,2,2,3,3,4};
			List<String> data = readCsv(new File(readFilePath));
			int length_byte = getLineLength(typeList);
			writeHeadFile(writeFilePath,data,pageSize,typeList,length_byte);
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
	
    
	
    /** write the Head File
     * @param filePath:file path
     * @param data: a String list
     * @param pageSize: page size
     * @param typeList: a int array,the data type and bytes count of line data
     * @param length_byte: the count of bytes
     * @return void
     */
    public static void writeHeadFile(String filePath,List<String>  data,int pageSize,int[] typeList,int length_byte){    
       File file = new File(filePath);
       try {
           if(file.exists()){
               file.createNewFile();
               System.out.println("create New File: "+filePath);
           }
           int pagei = 0,pagecount=0;;
           byte[] pageContent = new byte[pageSize];
           Arrays.fill(pageContent, (byte) 0);
           for(int i =1;i<data.size();i++) {
        	   //System.out.println(i);
        	   byte[] content = lineStrToBytes(lineStrToList(data.get(i)),typeList,length_byte);
        	   
        	   if(length_byte + pagei < pageSize) {
        		   System.arraycopy(content, 0, pageContent, pagei, length_byte);
        		   pagei += length_byte;
        	   }
        	   else {
        		   WriteByteToFile(filePath,pageContent,true);
        		   Arrays.fill(pageContent, (byte) 0);
        		   pagei = 0;
        		   System.arraycopy(content, 0, pageContent, pagei, length_byte);
        		   pagei += length_byte;
        		   pagecount++;
        		   //System.out.println("page count:"+pagecount);
        	   }
        	   
        	   
           }
       } catch (Exception e) {
           e.printStackTrace();
       }
    }
    
    /** write one page bytes array to Head File,
     * @param path: file path
     * @param content: one page bytes array
     * @param Appendable: Appendable
     * @return void
     */
    public static void WriteByteToFile(String path, byte[] content, boolean Appendable) throws IOException {
        FileOutputStream fos = new FileOutputStream(path,Appendable);
        fos.write(content);
        fos.close();
    }//end of WriteByteToFile
    
    /** change one line str to a string list
     * @param lineStr: one line str
     * @return a string list
     */
	public static List<String> lineStrToList(String lineStr){

    	List<String> lineStrList = new ArrayList<String>();

    	return lineStrList;
    
	}//end of lineStrToList
	
    /** change a string list to bytes array
     * @param lineStrList: a string list
     * @param typeList: data type of every data value
     * @param length_byte: length of bytes array
     * @return bytes array
     */	
    public static byte[] lineStrToBytes(List<String> lineStrList,int[] typeList,int length_byte) throws UnsupportedEncodingException {
    	byte[] lineBytes = new byte[length_byte];

        return lineBytes;
    }//end of lineStrToBytes
  
	
	
}//end of class dbloadgit