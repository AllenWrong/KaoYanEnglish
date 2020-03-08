package readComprehension;

import java.io.File;

public class TopDriver {
	private File inputFile;
	private GetOriginText getOriginText;
	private GetCleanContent getCleanContent;
	private boolean isDriectory;
	
	/**
	 * Constructor. You can pass a file or a directory. You can trust that this class can
	 * process it correctly. <b>If you pass a directory, please make sure that directory
	 * only contain the file need to parse.</b>
	 * @param inputFile
	 */
	public TopDriver(File inputFile) {
		this.inputFile = inputFile;
		this.isDriectory = inputFile.isDirectory();
	}

	/**
	 * Use this method to parse you target file.
	 */
	public void parse() {
		if(this.inputFile.isDirectory()) {
			parseDirectory();
		}else {
			parseSingleFile(this.inputFile);
		}
	}
	
	/**
	 * You must configure the FilePage class.
	 */
	private void parseDirectory() {
		FilePage filePage = new FilePage();
		File[] fileList = inputFile.listFiles();
		for(File i:fileList) {
			if(i.isDirectory()) {
				continue;
			}
			int thisFileStartPage = filePage.getStartPage(i.getName());
			int thisFileEndPage = filePage.getEndPage(i.getName());
			this.getOriginText = new GetOriginText(i);
			this.setStartPage(thisFileStartPage);
			this.setEndPage(thisFileEndPage);
			parseSingleFile(i);
		}
	}
	
	public void setStartPage(int startPage) {
		this.getOriginText.setStartPage(startPage);
	}
	public void setEndPage(int endPage) {
		this.getOriginText.setEndPage(endPage);
	}

	/**
	 * @param inputFile Using this parameter to make this method be called conveniently by parseDirectory.
	 */
	private void parseSingleFile(File inputFile) {
		if(!isDriectory) {
			this.getOriginText = new GetOriginText(inputFile);
		}
		
		File middleFile = this.getOriginText.getText();
		this.getCleanContent = new GetCleanContent(middleFile);
		this.getCleanContent.clean();
		middleFile.delete();
	}
	
	
	public static void main(String[] args) {
		File file = new File("D:\\test");
		TopDriver topDriver = new TopDriver(file);
		topDriver.parse();
	}
}
