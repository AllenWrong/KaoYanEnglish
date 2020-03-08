package readComprehension;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Process the Chinese character, blank line in the input file.
 * The output file is named by the form: inputFileName+clean+".txt"
 * The function cToe can be reused in other class.
 * @author Thingcor
 */
public class GetCleanContent {
	private static final String[] regs = { "£¡", "£¬", "¡£", "£»", "~", "¡¶", "¡·", "£¨", "£©", 
										   "£¿", "¡±", "£û", "£ý", "¡°", "£º", "¡¾", "¡¿", "¡±", "¡®",
										   "¡¯", "¡ª¡ª","¡ª","¨C",
										   "!", ",", ".", ";", "`", "<", ">", "(", ")", "?", 
										   "'", "{", "}", "\"", ":", "{", "}", "\"", "\'", "\'",
										   "--","--","--"};
	private static HashSet<String> regSet;
	private BufferedReader reader;
	private BufferedWriter writer;
	
	public GetCleanContent(File inputFile) {
		String outPutDir = inputFile.getParent()+"\\data\\";
		String outPutFileName = inputFile.getParent()+"\\data\\"+inputFile.getName().split("\\.")[0]+"clean.txt";
		File outputFile = new File(outPutFileName);
		if(outputFile.exists()) {
			outputFile.delete();
		}
		new File(outPutDir).mkdirs();
		try {
			this.reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile)));
			this.writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	static {
		regSet = new HashSet<>();
		for(int i = 0;i<regs.length;i++) {
			regSet.add(regs[i]);
		}
	}
	
	public void setWriter(BufferedWriter writer) {
		this.writer = writer;
	}

	public void clean() {
		try {
			String line = null;
			while((line = reader.readLine())!=null) {
				line = line.trim();
				if(line.equals("")||
					line.contains("¡ú")) {
					continue;
				}
				String regex = "[\u4e00-\u9fa5]+";
				Matcher matcher = Pattern.compile(regex).matcher(line);
				if(matcher.find()) {
					continue;
				}else if(line.matches("^[Q]{2}[0-9]+")) {
					continue;
				}
				char[] lineChar = line.toCharArray();
				StringBuffer finalLine = new StringBuffer();
				for(int i = 0;i < lineChar.length;i++) {
					String str = lineChar[i]+"";
					if(regSet.contains(str)) {
						finalLine.append(cToe(str));
					}else {
						finalLine.append(lineChar[i]);
					}
				}
				writer.write(finalLine.toString()+"\n");
				writer.flush();
			}
			
			this.reader.close();
			this.writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static final String cToe(String str) {
		for (int i = 0; i < regs.length / 2; i++) {
			str = str.replaceAll(regs[i], regs[i + regs.length / 2]);
		}
		return str;
	}
}
