import java.util.InputMismatchException;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.*;
import edu.stanford.nlp.io.IOUtils;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.sequences.DocumentReaderAndWriter;
import edu.stanford.nlp.util.Triple;

import java.util.List;


/*
 * This Class will convert CACM HTML files to CACM txt files and remove html tags.
 */

public class CACMbyNER {

	//This is the directory where CACM files are saved
	static private String filesDir = "";
	//This is the Number of CACM files the user wants to Process.
	static private int numberOfFilesToProcess = 0;
	//This is the Number of Files Which have been process by the software.
	static private int numberOfProcessdFilesCounter = 1;
	// This is the direction to Classifier file
	String serializedClassifier = "classifiers/english.all.3class.distsim.crf.ser.gz";
	
	
	public static void main(String[] args ) throws IOException
	{		
		ProcessOne();	
	}
	
	// Import CACM HTML Files
	public static void ProcessOne() throws IOException{
				
		@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);
		boolean inputIsCorrect = false;
		boolean dirIsCorrect = false;
		String text = "";
		String line = "";
		BufferedReader reader;
		FileReader file;
		BufferedWriter writer = null ;
		
		// Ask user for files directory
		// check if the directory is valid
		// If the directory not valid ask for directory again
		
		while(!dirIsCorrect) {
			try{
				System.out.println("Please Input HTML CACM Directory (ex: C:/Users/Desktop/cacm/) :");
				filesDir = in.nextLine();
				@SuppressWarnings({ "unused", "resource" })
				FileReader checkFile = new FileReader(CACMDirMaker() + ".html");
				dirIsCorrect = true;
				}
			catch(FileNotFoundException ex){
				System.out.println("File Not Found");
				}
		}
		
		// Ask user for number of files to be processed
		// check if the input is a valid number.
		// If input is not valid ask for number again
		while(!inputIsCorrect  && !(numberOfFilesToProcess >= 1 && numberOfFilesToProcess <= 3204)) {
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
		
		/*
		 * While the processed number of files is less the the required files to process and less than 3205
		 * 
		 *  Read CACM file
		 *  
		 *  Remove HTML Tags
		 *  
		 *  Check the line with NER library to get classified words.
		 *  
		 *  Write the result to a text file with the same name.
		 */

		while(numberOfProcessdFilesCounter <= numberOfFilesToProcess &&  numberOfProcessdFilesCounter < 3205)
		{
			file = new FileReader(CACMDirMaker() + ".html");
			reader = new BufferedReader(file);
			writer = new BufferedWriter(new FileWriter(CACMDirMaker() + ".txt"));
			
			line = reader.readLine();
			
			while(line !=null)
			{
				text = line;
				line = reader.readLine();
				
				if(text.length() > 0){
					text = CleanStr(text);
					
					text = CheckLine(text);
					
					if(text.trim().length() > 0)
					{
						writer.write(text);
						writer.newLine();
					}
				}
			}
			writer.close();
			numberOfProcessdFilesCounter++;
		}
		
		System.out.println("File Has Been Transfered to TXT");
	}
	
	/*
	 * This Method will call NER and check if the line has a word classified as word_ANNOTATION
	 *  It will then return the line with classified words
	 */
	static String CheckLine(String line)
	{
		AbstractSequenceClassifier<CoreLabel> classifier = CRFClassifier.getClassifier(serializedClassifier);
		String[] myList = line.split(" ");
		String finalLine;
		
		for (String str : myList) {
			finalList += (classifier.classifyToString(str) + " ");
			}
	      
		return finalLine;
	}
	
	
	// This Method Create CACM File Directory Maker
	static private String CACMDirMaker(){
		String counterStr ="";
		
		if(numberOfProcessdFilesCounter <= 9)
			counterStr = "000" + numberOfProcessdFilesCounter;
		
		else if(numberOfProcessdFilesCounter <= 99 && numberOfProcessdFilesCounter >= 10)
			counterStr = "00" + numberOfProcessdFilesCounter;
		
		else if (numberOfProcessdFilesCounter <= 999 && numberOfProcessdFilesCounter >= 100)
			counterStr = "0" + numberOfProcessdFilesCounter;
		
		else
			counterStr = Integer.toString(numberOfProcessdFilesCounter);	
		
		return filesDir + "CACM-" + counterStr;
	}
	
		
	//This Method Clean unwanted characters from a string
	static String CleanStr(String text){
		String str = text;
		
		str = str.replace("<html>", " ");
		str = str.replace("</html>", " ");
		str = str.replace("<pre>", " ");
		str = str.replace("</pre>", " ");
		str = str.replace("<head>", " ");
		str = str.replace("</head>", " ");
		str = str.replace("<span style=\"background-color: #cc0066\">", " ");
		str = str.replace("<font color=\"white\">", " ");
		str = str.replace("</font>", " ");
		str = str.replace("</span>", " ");
		str = str.replace("<body>", " ");
		str = str.replace("</body>", " ");
		str = str.replace(",", "");

		return str.trim();
	}

}
