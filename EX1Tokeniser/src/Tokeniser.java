import java.io.*;
import java.util.*;

public class Tokeniser {

	//This is the list where all words in the CACM files will be saved
	static private List<String> mainList = new ArrayList<String>();
	//This is a list of the words in the files. 
	static private List<String> wordsList = new ArrayList<String>();
	//This is a list of how many times has a word been mentioned in the files.
	static private List<Integer> numOfTimesWordsRepatedInMainList = new ArrayList<Integer>();
	//This is a list of calculated Probability of Occurrence for every word. 
	static private List<Double> probOfEveryWordInWordsList = new ArrayList<Double>();
	//This is the directory where CACM files are saved
	static private String filesDir = "";
	//This is the Number of CACM files the user wants to Process.
	static private int numberOfFilesToProcess = 0;
	//This is the Number of Files Which have been process by the software.
	static private int numberOfProcessdFilesCounter = 1;
	
	
	public static void main(String[] args ) throws Exception
	{
		//Import Files, Read and Out Put Them.
		
		ImportFiles();
		
		//Find words Occurrence Probability 
		findRepeatedWords();
		
		// Calculate Probability of Occurrence
		CalWordProbOfOccurrence();
		
		//Print out Results
		printStatistics();
	}
	
	// This Method Create CACM File Directory Maker
	static private String CACMFileDirectoryMaker()
	{
		String counterStr ="";
		
		if(numberOfProcessdFilesCounter <= 9)
			counterStr = "000" + numberOfProcessdFilesCounter;
		
		else if(numberOfProcessdFilesCounter <= 99 && numberOfProcessdFilesCounter >= 10)
			counterStr = "00" + numberOfProcessdFilesCounter;
		
		else if (numberOfProcessdFilesCounter <= 999 && numberOfProcessdFilesCounter >= 100)
			counterStr = "0" + numberOfProcessdFilesCounter;
		
		else
			counterStr = Integer.toString(numberOfProcessdFilesCounter);	
		
		return filesDir + "CACM-" + counterStr + ".html";
	}
	
	// This method will import the files and then call a read file method.
	static void ImportFiles() throws IOException
	{
		mainList.clear();
		wordsList.clear();
		numOfTimesWordsRepatedInMainList.clear();
		probOfEveryWordInWordsList.clear();
		
		Scanner in = new Scanner(System.in);
		boolean inputIsCorrect = false;
		boolean dirIsCorrect = false;
		
		while(!dirIsCorrect) {
			try{
				System.out.println("Please Input the Directory which the CACM files are located in (ex: C:/Users/Desktop/cacm/) :");
				filesDir = in.nextLine();
				FileReader checkFile = new FileReader(CACMFileDirectoryMaker());
				dirIsCorrect = true;
				}
			catch(FileNotFoundException ex){
				System.out.println("File Not Found");
				}
		}
		
		while(!inputIsCorrect) {
			try{
				System.out.println("Please Input the Number Of CACM files You Want to Process: ");
				numberOfFilesToProcess = in.nextInt();
				inputIsCorrect = true;
				}
			catch(InputMismatchException ex){
			System.out.println("Wrong Input");
			}
		}
		
		System.out.println("Importing and Reading CACM Files");

		while(numberOfProcessdFilesCounter <= numberOfFilesToProcess )
		{
			FileReader file = new FileReader(CACMFileDirectoryMaker());
			BufferedReader reader = new BufferedReader(file);
			
			// Read The File.
			ReadFile(reader);
			
			numberOfProcessdFilesCounter++;
		}

	}
	
	//This Method Print Out Passed Files, make sure they are lower cased and cleaned of unwanted chars.
	static void ReadFile(BufferedReader reader) throws IOException{
		String text = "";
		String line = reader.readLine();
		
		while(line !=null)
		{
			text =line;
			line = reader.readLine();
			//Print File Out.
			System.out.println(text);
			
			//Lower Case, Remove unwanted Chars and Add to main list.
			if(text.length() > 0)
			{
				text = text.toLowerCase();
				text = CleanStr(text);
				AddToMainList(text);
			}
		}
		
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
	
	//This method takes every line, Breaks it into words and store every word in a list
	static void AddToMainList(String line)
	{	
		if(!line.isEmpty()) 
		{
			line = line.trim();
			mainList.addAll(Arrays.asList(line.trim().split(" ")));
			mainList.remove("");
		}
		
	}
	
	// This Method Count How many Has every Words been repeated in the list
	static void findRepeatedWords(){		
		
		System.out.println(" Finding Repeted Word");
		
		for(int a = 0; a < mainList.size() ; a++)
		{
			int wordNomOfOccurrence = 1;
			boolean inWordsList = false;
			
			for(int b = 0; b < wordsList.size(); b++)
			{
				if(mainList.get(a).equals(wordsList.get(b)))
				{
					inWordsList = true;
				}
			}
			
			if(inWordsList == false)
			{
				for(int c = a+1; c < mainList.size(); c++)
				{
					if(mainList.get(a).equals(mainList.get(c)))
					{
						wordNomOfOccurrence++;
					}
				}
				
				wordsList.add(mainList.get(a));
				numOfTimesWordsRepatedInMainList.add(wordNomOfOccurrence);
			}
		}
	

	}
	
	// This method will calculate the Probability of Occurrence for every word in the list.
	static void CalWordProbOfOccurrence()
	{
		for(int a = 0; a < numOfTimesWordsRepatedInMainList.size(); a++ )
		{
			probOfEveryWordInWordsList.add(((double)numOfTimesWordsRepatedInMainList.get(a)/(double) mainList.size()));
			
		}
		
	}
	
	//This method will print out Statistics about processed files. 
	static void printStatistics() throws IOException
	{
		System.out.println("There are:  "+ mainList.size() + " Words in the Processed Files.\n");
		System.out.println("Creating Statistics TXT file");
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(filesDir + "Statistics.txt"));	 
			
		for(int g = 0; g < wordsList.size(); g++)
		{
			writer.write(probOfEveryWordInWordsList.get(g)+ "\t"+ numOfTimesWordsRepatedInMainList.get(g) + " \t" +  wordsList.get(g));
			writer.newLine();
		}
		writer.close();
	}
	
	
	
	
	
	
	
}