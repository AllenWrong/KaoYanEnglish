package readComprehension;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;

import java.util.Scanner;
import java.util.Set;


/**
 * In the current material, the reading and comprehension part start with the line
 * "Section II Reading Comprehension" and end with the line "Section III Writing".
 * And of course, we don't need above two line. <br/>
 * This part is to statistic the word of the reading and comprehension part.
 * @author Thingcor
 *
 */
public class StatisticReadCompre {
	private HashMap<String, Integer> map;
	private String currentLine;
	private String[] curLineTokens;
	private Scanner scanner;
	private File directory;
	private File outputFile;
	private HashSet<String> punctuation;
	private char[] curCharLine;
	private String[] puncArr = { "!", ",", ".", ";", "`", "<", ">", "(", ")", "?", 
			   					 "'", "{", "}", "\"", ":", "{", "}", "\"", "\'", "\'",
			   					 "--"};
	
	public StatisticReadCompre(File directory) {
		this.directory = directory;
		this.map = new HashMap<>();
		this.punctuation = new HashSet<>();
		for(int i = 0;i<puncArr.length;i++) {
			punctuation.add(puncArr[i]);
		}
	}
	
	/**
	 * """ Main Work """
	 * Note: the module orient directory. Because the material is often more than 
	 * one file. 
	 * 1. nextLine()
	 * 2. tokenIt()
	 * 3. cleanToken()
	 *   o throw directions. [¡Ì]
	 *   o token only contain underline throw it. [¡Ì]
	 *   o token end with underline. throw underline. [¡Ì]
	 *   o token contains "[-]{2, }". Split it. If only contains one, no matter. [¡Ì]
	 *   o token is blank.
	 *   o token is space. [¡Ì]
	 * 	 o token contains number, throw it. [¡Ì]
	 *   o token start with "[", get substring. [¡Ì]
	 *   o token end with punctuation. Throw the end punctuation(may be more than one). [¡Ì]
	 *   o token is a punctuation throw it. Construct the common punctuation set. [¡Ì]
	 * 4. statistic()
	 * 5. end()
	 */
	private void tokenLine() {
		this.curLineTokens = this.currentLine.split(" ");
	}
	
	private void characterLine() {
		this.curCharLine = this.currentLine.toCharArray();
	}
	
	/**
	 * 
	 */
	private void checkChar() {
		String token = "";
		for(int i=0;i<this.curCharLine.length;i++) {
			String tmp = this.curCharLine[i]+"";
			if(tmp.matches("[A-Za-z]")) {
				token += tmp;
			}else if(tmp.equals(" ")) {
				if(!token.equals("")) {
					this.addToMap(token);
					token = "";
				}
			}else if(tmp.equals("_")) {
				continue;
			}else if(tmp.equals("-")) {
				String nextChar = this.curCharLine[i+1]+"";
				if(nextChar.equals("-")) {
					if(!token.equals("")) {
						this.addToMap(token);
						token = "";
					}

					i += 1;
					if((i+1)<this.curCharLine.length) {
						while((this.curCharLine[i+1]+"").equals("-")) {
							i++;
						}
					}
				}else {
					token += tmp;
				}
			}else if (tmp.matches("[0-9]")) {
				continue;
			}else if(this.punctuation.contains(tmp)) {
				if(tmp.equals("\'")) {
					if((i+1) < this.curCharLine.length) {
						if((this.curCharLine[i+1]+"").matches("[A-Za-z]")) {
							token += tmp;
						}else {
							continue;
						}
					}
				}
			}else {
				
			}
		}
		// The end of the line?
		if(!token.equals("")) {
			this.addToMap(token);
			token = "";
		}
	}
	
	private void addToMap(String token) {
		if(null == this.map.get(token)) {
			map.put(token, 1);
		}else {
			int pre = map.get(token)+1;
			map.remove(token);
			map.put(token, pre);
		}
	}
	
	private void processDirection() {
		while(true) {
			// Next line.
			this.currentLine = this.scanner.nextLine();
			this.tokenLine();
			if(this.curLineTokens[this.curLineTokens.length-1].equals("points)")) {
				break;
			}
		}
	}
	
	public void statistic() {
		File[] fileList = this.directory.listFiles();
		for(int i = 0;i<fileList.length;i++) {
			// Build the input stream.
			try {
				this.scanner = new Scanner(new FileInputStream(fileList[i]));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			// Statistic this file.
			this.statisticOneFile(fileList[i]);
		}
		
		this.outputFile = new File(this.directory.getPath()+"\\word.txt");
		if(outputFile.exists()) {
			outputFile.delete();
		}
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.outputFile)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Set<Entry<String, Integer>> entrySet = map.entrySet();
		Iterator<Entry<String, Integer>> iterator = entrySet.iterator();
		
		while(iterator.hasNext()) {
			Entry<String, Integer> entry = iterator.next();
			String line = entry.getKey()+","+entry.getValue();
			try {
				writer.write(line+"\n");
				writer.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void statisticOneFile(File file) {
		boolean isStart = false;
		
		while(scanner.hasNext()) {
			this.currentLine = scanner.nextLine();
			// Start condition. But this line we don't need.
			if(this.currentLine.equals("Section II Reading Comprehension")) {
				isStart = true;
				continue;
			}
			// End condition.
			if (this.currentLine.equals("Section III Writing")) {
				isStart = false;
			}
			
			// Begin...
			if(isStart) {
				// Token current line.
				this.tokenLine();
				
				// Special line process.
				switch (this.curLineTokens[0]) {
				case "Directions:":
					this.processDirection();
					continue;
				case "Part":
					continue;
				case "Text":
					continue;
				default:
				}
			}
			
			if(isStart) {
				this.characterLine();
				this.checkChar();
			}
		}
	}
}
