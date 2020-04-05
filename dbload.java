
public class dbload {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		int pagesize = 4096;
		String filepath = "";
		boolean argsRight = false;
		
		for(String s :args){
			System.out.println(s+"  "+s.length());
		}
		
		if (args.length == 3) {
			System.out.println("legth true");
			try {
				if(args[0].equals("-p")) {
					System.out.println("args[0] true");
					filepath = args[2];
					pagesize = Integer.parseInt( args[1] );
					System.out.println("filepath:"+filepath);
					System.out.println("pagesize:"+pagesize);
					argsRight = true;
				}
			}catch(Exception e){
				argsRight = false;
			}
		}
		
		System.out.println("main func over");
		
    }
    
}