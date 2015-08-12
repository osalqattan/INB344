import java.io.*;
import java.util.*;


public class Boolean {

	//Directory Link Where CACM files are saved.
	private static String cacmDir = "";
	//Terms map, every term in documents and the document the term is mentioned in.
	private static Map<String,Vector<String>> wordsMap = new HashMap<String, Vector<String>>();
	//Number of CACM files to process
	private static Integer numOfcacmFileNamesToProcess = 0;
	//Number of CACM files processed 
	private static Integer numOfcacmFileNamesProcessed = 1;
	// CACM File name
	private static String cacmFileName = "";
	// File Words List
	static private List<String> fileWords = new ArrayList<String>();
	// First Word to look for. 
	static final String firstTerm =  "algebraic";
	// Second Word to look for.
	static final String secondTerm = "translators";
	// Third Word to look for.
	static final String thirdTerm = "algorithm";
	// Fourth Word to look for.
	static final String fourthTerm = "rootfinder";
	
	public static void main(String[] args ) throws Exception{
		
		GetNumberAndDir();
		
		for(int a = 0; a < numOfcacmFileNamesToProcess; a++){
		
		fileWords.clear();	
		
		CreateCACMFileName();
		
		ImportcacmFileName();
		
		IndexFile();

		numOfcacmFileNamesProcessed++;
		}
		
		/*for ( String key : wordsMap.keySet() ) {
		    System.out.println( key +" "+ wordsMap.get(key) );
		}
		*/
		SolveQuery();
	}
	
	// This Method Will get Directory and Number CACAM Files to Process
	static void GetNumberAndDir(){
		@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);
		boolean dirIsCorrect = false;
		File f;
		
		while(!dirIsCorrect) {
				System.out.println("Please Input CACM Files Directory :");
				cacmDir = in.nextLine();
				f = new File(cacmDir);
				
				if(f.isDirectory())
				dirIsCorrect = true;
		}
		
		while(!(numOfcacmFileNamesToProcess <= 3204 && numOfcacmFileNamesToProcess > 0)) {
			System.out.println("Please Input Number of CACM Files to Process :");
			numOfcacmFileNamesToProcess = Integer.parseInt(in.nextLine());
			}
	
	}
	
	//This Method Will Create A file name for a CACM File
	static void CreateCACMFileName() throws IOException{
		String cacmFileNameName = "CACM-";
		String cacmFileNameNumber = "";
		String cacmFileNameType = ".html";
		
		if(numOfcacmFileNamesProcessed <= 9)
			cacmFileNameNumber = "000" + Integer.toString(numOfcacmFileNamesProcessed);
		
		else if(numOfcacmFileNamesProcessed <= 99 && numOfcacmFileNamesProcessed >= 10)
			cacmFileNameNumber = "00" + Integer.toString(numOfcacmFileNamesProcessed);
		
		else if (numOfcacmFileNamesProcessed <= 999 && numOfcacmFileNamesProcessed >= 100)
			cacmFileNameNumber = "0" + Integer.toString(numOfcacmFileNamesProcessed);
		else
			cacmFileNameNumber = Integer.toString(numOfcacmFileNamesProcessed);	
		
		
		cacmFileName = cacmFileNameName + cacmFileNameNumber + cacmFileNameType;
	}
	
	@SuppressWarnings({ "resource" })
	private static void ImportcacmFileName() throws IOException {
		FileReader cacmFile = new FileReader(cacmDir + cacmFileName);
		BufferedReader cacmReader = new BufferedReader(cacmFile);
		String line = cacmReader.readLine();
		fileWords.clear();
		
		while(line !=null){
			
			if(!line.isEmpty()) 
			{
				line = CleanStr(line);
				line = line.toLowerCase().trim();
				fileWords.addAll(Arrays.asList(line.split(" ")));
				fileWords.remove("");
			}
			
			line = cacmReader.readLine();
		}
	}
	
	private static void IndexFile() throws IOException {
		
	
		for(int a = 0; a < fileWords.size(); a++){
			Vector<String> tempVec = new Vector<String>();
			if(wordsMap.containsKey(fileWords.get(a))){
				if(!wordsMap.get(fileWords.get(a)).contains(cacmFileName))
				{
					tempVec = wordsMap.get(fileWords.get(a));
					tempVec.add(cacmFileName);
					wordsMap.put(fileWords.get(a),tempVec);
				}
			}
			else{
				tempVec.add(cacmFileName);
				wordsMap.put(fileWords.get(a),tempVec);
				}
			}
	}
	
	// This method will solve the queries asked by the user
	private static void SolveQuery() {

		System.out.println("Term " + firstTerm + " results : " + wordsMap.get(firstTerm));
		System.out.println("Term " + secondTerm + " results : " + wordsMap.get(secondTerm) );
		System.out.println("Term " + thirdTerm +" results : " + wordsMap.get(thirdTerm) );
		System.out.println("Term " + fourthTerm +" results : " + wordsMap.get(fourthTerm) );
		System.out.println("Term algorithm AND rootfinder results : " + UseAnd(thirdTerm, fourthTerm));
		System.out.println("Term algorithm NOT rootfinder results : " + UseAndNot(thirdTerm, fourthTerm) );
		System.out.println("Term algorithm AND rootfinder Occured In: " + UseAnd(thirdTerm, fourthTerm).size() + " Files");
		System.out.println("Term algorithm NOT rootfinder Occured In: " + UseAndNot(thirdTerm, fourthTerm).size() + " Files"); 
	}
	
	// This method will return a vector of the files which have word1 and word2 both excite.
	static Vector<String> UseAnd(String word1, String word2){
		Vector<String> tempVec = new Vector<String>();
	 
		for(int a = 0;a < wordsMap.get(word1).size(); a++){
			
			for(int b = 0; b < wordsMap.get(word2).size() ;b++){
				
				if(wordsMap.get(word1).elementAt(a) == wordsMap.get(word2).elementAt(b))
				{
					tempVec.addElement(wordsMap.get(word1).elementAt(a));
					}
			}
		}
		
		return tempVec;
	}
	
	// This method return a vector of the files which have word1 but not word2
	static Vector<String> UseAndNot(String word1, String word2){
		Vector<String> tempVec = new Vector<String>();
		
		for(int a = 0;a < wordsMap.get(word1).size(); a++){
			
			tempVec.add(wordsMap.get(word1).elementAt(a));
			
			for(int b = 0; b < wordsMap.get(word2).size() ;b++){
				
				if(wordsMap.get(word1).elementAt(a) == wordsMap.get(word2).elementAt(b))
				{
					tempVec.remove(wordsMap.get(word1).elementAt(a));
					}
			}
		}
		
		return tempVec;
	}
	
	//This Method Clean unwanted characters from a string
		static String CleanStr(String text){
			String str = text;
		
			str = str.replace("`", " ");
			str = str.replace("~", " ");
			str = str.replace("!", " ");
			str = str.replace("@", " ");
			str = str.replace("#", " ");
			str = str.replace("$", " ");
			str = str.replace("%", " ");
			str = str.replace("^", " ");
			str = str.replace("<html>", " ");
			str = str.replace("</html>", " ");
			str = str.replace("<pre>", " ");
			str = str.replace("</pre>", " ");
			str = str.replace("<head>", " ");
			str = str.replace("</head>", " ");
			str = str.replace("<body>", " ");
			str = str.replace("</body>", " ");
			str = str.replace("<", " ");
			str = str.replace(">", " ");
			str = str.replace(",", " ");
			str = str.replace(".", " ");
			str = str.replace("/", " ");
			str = str.replace(")", " ");
			str = str.replace("("," ");
			str = str.replace("-", "");
			str = str.replace("_", "");
			str = str.replace("=", "");
			str = str.replace("&", "");
			str = str.replace(";", " ");
			str = str.replace(":", " ");
			str = str.replace("\"", " ");
			str = str.replace("\\", " ");
			str = str.replace("+", "");
			str = str.replace("=", "");
			str = str.replace("\t"," ");
			str = str.replace("{"," ");
			str = str.replace("}"," ");
			str = str.replace("["," ");
			str = str.replace("]"," ");
			return str;
		}





	
	
}
