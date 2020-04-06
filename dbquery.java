import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
		//pageSize = 4096;
		//text = "PRICE HALL";
		//argsRight = true;
		
		if(argsRight) {
			long  startTime = System.currentTimeMillis();
			searchTextInHF(filepath, pageSize,text);
			long endTime = System.currentTimeMillis();
			System.out.println("\nthe total time taken to do all the search operations in milliseconds:");
			System.out.println((endTime - startTime) + "ms");
			
		}else {
			System.out.println("args error");
		}
		
		//System.out.println("over");
		
	}//end of main
	
	
	/**
     * read the head file one page at once, then search text
     * @param filename: path of head file
     * @param pageSize: page size
     * @param text: the text need search
     * @return
     * @throws IOException
     */
    public static void searchTextInHF(String filename, int pageSize,String text) throws IOException {
        File f = new File(filename);
        if (!f.exists()) {
            throw new FileNotFoundException(filename);
        }
        BufferedInputStream in = null;
        int len = 0,i=0;
        int[] typeList = {1,1,1,1,2,2,2,1,1,1,2,2,2,2,2,2,3,3,4};
        int lineCount =getLineLength(typeList);
        try {
            in = new BufferedInputStream(new FileInputStream(f));
            byte[] pageBuffer = new byte[pageSize];
            while (-1 != (len = in.read(pageBuffer, 0, pageSize))) {
                //bos.write(buffer, 0, len);
            	//System.out.println("page:"+i);
            	analysisPageBuffer(pageBuffer,pageSize,text,lineCount,typeList);
            	i++;
            	
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }//end of searchTextInHF
    
    /**  
     * get length of one line data
     *  
     * @param typeList: array, the type of data  
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
	    //System.out.println("length_byte:"+length_byte);
	    return length_byte;
	}//end of getLineLength
	
    /**  
     * analysis and search one page buffer
     *  
     * @param buffer: one page buffer 
     * @param pageSize:  page size 
     * @param text: the text need search
     * @param lineCount: the byte count of one line data
     * @param typeList: array, the type of data
     * @return void
     */ 
    public static void analysisPageBuffer(byte[] buffer,int pageSize,String text,int lineCount,int[] typeList) {

    	byte[] lineBytes = new byte[lineCount];
    	int i=0,j=0;
    	String buildName;
    	while(i+lineCount < pageSize) {
    		System.arraycopy(buffer, i, lineBytes, 0, lineCount);
    		buildName = getBuildName(lineBytes,4,typeList);//Building name is 4
    		if(buildName.indexOf(text)!=-1) {
        		List<String> strList =  bytesToStringList(lineBytes,typeList);
        		System.out.print(strList.get(0));
        		for(int k=0;k<strList.size();k++) {
        			System.out.print(","+strList.get(k));
        		}
        		System.out.println("");
    		}

    		i += lineCount;
    		j++;
    		
    	}
    	
    }//end of analysisPageBuffer

    /**  
     * get BuildName from byte array of one line
     *  
     * @param buffer: one line buffer 
     * @param index: the index of BuildName in one line
     * @param typeList: array, the type of data
     * @return String buildName
     */ 
	public static String getBuildName(byte[] buffer,int index,int[] typeList) {
		String buildName = "";
		int i,bufferIndex = 0;
		for(i = 0; i<index; i++) {
	    	switch(typeList[i]) {
	    	case 1: // int 4 byte
	    		bufferIndex += 4;
	    		break;
	    	case 2: // 50 char  50 byte
	    		bufferIndex += 50;
	    		break;
	    	case 3: //float  4 byte
	    		bufferIndex += 4;
	    		break;
	    	case 4: //2 float  8 byte
	    		bufferIndex += 8;
	    		break;
	    	}
		}
		int buildCount = 50;
    	switch(typeList[index]) {
    	case 1: // int 4 byte
    		buildCount = 4;
    		break;
    	case 2: // 50 char  50 byte
    		buildCount = 50;
    		break;
    	case 3: //float  4 byte
    		buildCount = 4;
    		break;
    	case 4: //2 float  8 byte
    		buildCount = 8;
    		break;
    	}
		byte[] buildNameBytes = new byte[buildCount];
		System.arraycopy(buffer, bufferIndex, buildNameBytes, 0, buildCount);
		i = buildCount - 1;
		while(i>0 && buildNameBytes[i]==0x00) {
			i--;
		}
		if(i==0) {
			buildName = "";
		}else {
			byte[] tempBuildNameBytes = new byte[i+1];
			System.arraycopy(buildNameBytes, 0, tempBuildNameBytes, 0, i+1);
			buildName = new String(tempBuildNameBytes);
			
		}
		//System.out.println("buildName:"+":["+buildName+"]");
		return buildName;
		
	}// end of getBuildName

    /**  
     * get one line data string from byte array of one line
     *  
     * @param buffer: one line buffer 
     * @param typeList: array, the type of data
     * @return String list
     */ 
	public static List<String> bytesToStringList(byte[] buffer,int[] typeList){
		List<String> strList = new ArrayList<String>();
		int i,j,type,length_byte=0;
		String s="";
		byte[] byte4 = new byte[4];
		byte[] byte50 = new byte[50];
	    for(i=0;i<typeList.length;i++){
	    	type = typeList[i];
	    	switch(type) {
	    	case 1: // int 4 byte
	    		System.arraycopy(buffer, length_byte, byte4, 0, 4);
	    		strList.add(""+byteArrayToInt(byte4));
	    		length_byte += 4;
	    		break;
	    	case 2: // 50 char  50 byte
	    		System.arraycopy(buffer, length_byte, byte50, 0, 50);
	    		j = 49;
	    		while(j>0 && byte50[j]==0x00) {
	    			j--;
	    		}
	    		if(j==0) {
	    			strList.add("");
	    		}else {
	    			byte[] tempBytes = new byte[j+1];
	    			System.arraycopy(byte50, 0, tempBytes, 0, j+1);
	    			strList.add(new String(tempBytes));
	    		}
	    		length_byte += 50;
	    		break;
	    	case 3: //float  4 byte
	    		System.arraycopy(buffer, length_byte, byte4, 0, 4);
	    		strList.add(Float.toString(byte2float(byte4)));
	    		length_byte += 4;
	    		break;
	    	case 4: //2 float  8 byte
	    		System.arraycopy(buffer, length_byte, byte4, 0, 4);
	    		s = Float.toString(byte2float(byte4));
	    		length_byte += 4;
	    		System.arraycopy(buffer, length_byte, byte4, 0, 4);
	    		s = "("+s + "," + Float.toString(byte2float(byte4))+")";
	    		length_byte += 4;
	    		strList.add(s);
	    		
	    		break;
	    	}
	    }
		
		return strList;
	}// end of bytesToStringList
	
	/**
     * byte[] to int
     * @param bytes the array need be changed to int, the length must be 4
     * @return int value
     */
    public static int byteArrayToInt(byte[] bytes) {
        int value=0;
        for(int i = 0; i < 4; i++) {
            int shift= (3-i) * 8;
            value +=(bytes[i] & 0xFF) << shift;
        }
        return value;
    }//end of byte2float
    
	/**
     * byte[] to float
     * @param bytes the array need be changed to float, the length must be 4
     * @return float value
     */
    public static float byte2float(byte[] b) {    
        int l;                                             
        l = b[0];                                  
        l &= 0xff;                                         
        l |= ((long) b[1] << 8);                   
        l &= 0xffff;                                       
        l |= ((long) b[2] << 16);                  
        l &= 0xffffff;                                     
        l |= ((long) b[3] << 24);                  
        return Float.intBitsToFloat(l);                    
    }//end of byte2float
    
    

}