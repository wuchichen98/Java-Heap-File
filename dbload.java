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
					System.out.println("writeFilePath:"+writeFilePath);
					System.out.println("pageSize:"+pageSize);
					argsRight = true;
				}
			}catch(Exception e){
				argsRight = false;
			}
		}
		
		//argsRight = true;
		//pageSize = 4096;
		//readFilePath = "D:\\File\\Desktop\\java database\\code\\test.csv";
		
		
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
               if (!file.delete()) {
                   throw new IOException("Delete file failure,path:" + file.getAbsolutePath());
                 }
           }
           file.createNewFile();
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
    	int j,nextComma = 0;
    	List<Integer> indexList = new ArrayList<Integer>();
    	indexList.add(0);
    	while(nextComma < lineStr.length()) {
    		nextComma = nextComma(lineStr, nextComma);
    		indexList.add(nextComma);
    		nextComma++;
    	}
    	for(j=1;j<indexList.size();j++) {
    		String s =lineStr.substring(indexList.get(j-1),indexList.get(j));
    		s = s.replace(",", "");
    		s = s.replace("\"", "");
    		//System.out.println(" "+j+":"+s);
    		lineStrList.add(s);
    	}
    	return lineStrList;
    
	}//end of lineStrToList
	
    /**  
     * get the index of next Comma
     * @param source: the string
     * @param st:the index start
     * @return the index of next Comma
     */    
    private static int nextComma(String source, int st) {    
        int maxPosition = source.length();    
        boolean inquote = false;    
        while (st < maxPosition) {    
            char ch = source.charAt(st);    
            if (!inquote && ch == ',') {    
                break;    
            } else if ('"' == ch) {    
                inquote = !inquote;    
            }    
            st++;
            
        }    
        return st;    
    }//end of nextComma
	

	
    /** change a string list to bytes array
     * @param lineStrList: a string list
     * @param typeList: data type of every data value
     * @param length_byte: length of bytes array
     * @return bytes array
     */	
    public static byte[] lineStrToBytes(List<String> lineStrList,int[] typeList,int length_byte) throws UnsupportedEncodingException {
    	int i,type,j;
    	byte[] lineBytes = new byte[length_byte];
    	Arrays.fill(lineBytes, (byte) 0);
    	lineBytes[length_byte - 2] = (byte)'\r';
    	lineBytes[length_byte - 1] = (byte)'\n';
    	length_byte = 0;
    	byte[] tempBytes;
    	length_byte = 0;
    	for(i=0; i<lineStrList.size(); i++) {
    		String str = lineStrList.get(i);
    		type = typeList[i];
    		str.getBytes("utf-8");
        	switch(type) {
        	case 1: // int 4 byte
        		if(!str.equals("")) {
        			tempBytes = intToByteArray(Integer.parseInt(str));
            		System.arraycopy(tempBytes, 0, lineBytes, length_byte, 4);
             		
        		}
        		length_byte += 4;
        		break;
        	case 2: // 50 char  50 byte
        		tempBytes = str.getBytes("utf-8");
        		System.arraycopy(tempBytes, 0, lineBytes, length_byte, tempBytes.length);
        		length_byte += 50;
        		break;
        	case 3: //float  4 byte
        		if(!str.equals("")) {
	        		tempBytes = float2byte(Float.parseFloat(str));
	        		System.arraycopy(tempBytes, 0, lineBytes, length_byte, tempBytes.length);
	        		length_byte += 4;
        		}
        		break;
        	case 4: //2 float  8 byte
        		str = str.replace("(", "");
        		str = str.replace(")", "");
        		String [] sArray = str.split(" ");
        		tempBytes = float2byte(Float.parseFloat(sArray[0]));
        		System.arraycopy(tempBytes, 0, lineBytes, length_byte, tempBytes.length);
        		length_byte += 4;
        		tempBytes = float2byte(Float.parseFloat(sArray[1]));
        		System.arraycopy(tempBytes, 0, lineBytes, length_byte, tempBytes.length);
        		length_byte += 4;
        		break;
        	}
    		
    		
    	}
        return lineBytes;
    }//end of lineStrToBytes
    
    /**
     * int to byte[] ,high -> low
     * @param i: int value
     * @return byte[]
     */
    public static byte[] intToByteArray(int i) {
        byte[] result = new byte[4];
        result[0] = (byte)((i >> 24) & 0xFF);
        result[1] = (byte)((i >> 16) & 0xFF);
        result[2] = (byte)((i >> 8) & 0xFF);
        result[3] = (byte)(i & 0xFF);
        return result;
    }
 
  
    /** 
     * float to byte  
     * @param f : float num
     * @return  byte[]
     */  
    public static byte[] float2byte(float f) {  
          
        int fbit = Float.floatToIntBits(f);  
          
        byte[] b = new byte[4];    
        for (int i = 0; i < 4; i++) {    
            b[i] = (byte) (fbit >> (24 - i * 8));    
        }   
          
        int len = b.length;  
        byte[] dest = new byte[len];  
        System.arraycopy(b, 0, dest, 0, len);  
        byte temp;  
        for (int i = 0; i < len / 2; ++i) {  
            temp = dest[i];  
            dest[i] = dest[len - i - 1];  
            dest[len - i - 1] = temp;  
        }  
          
        return dest;  
          
    }//end of float2byte
	
}//end of class dbloadgit