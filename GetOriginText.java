package readComprehension;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.text.PDFTextStripper;

/**
 * This class is used to get the original text from the PDF file.
 * And the output file of this class is a "text" file which contains the original text.
 * If the original text is not clean enough to use. You need to call "GetCleanContent"
 * class to clean it.
 * @author Thingcor
 */
public class GetOriginText {
	private File inputFile;
	/** The start page number. Default as 0*/
	private int startPage;
	/** The end page number. Default as the end of the input file.*/
	private int endPage;
	private PDDocument document;
	
	/**
	 * Constructor. In this constructor, the start page of the parsed target is default 
	 * as 0 and the end page default as the end file of the input file. You can set the
	 * page by the setter method.
	 * @param inputFile
	 */
	public GetOriginText(File inputFile) {
		this.inputFile = inputFile;
		try {
			this.document = PDDocument.load(this.inputFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.startPage = 0;
		this.endPage = this.document.getNumberOfPages();
	}
	
	/**
	 * Get the origin text.
	 * @return the middle file.
	 */
	public File getText() {
		File middleFile = null;
		try {
			AccessPermission ap = document.getCurrentAccessPermission();
			if (!ap.canExtractContent()) {
				throw new IOException("You are not allowed to access this file!");
			}
			
			PDFTextStripper stripper = new PDFTextStripper();
			// Set the order of the output.
			stripper.setSortByPosition(true);
			// Set the start page.
			stripper.setStartPage(this.startPage);
			// Set the end page.
			stripper.setEndPage(this.endPage);
			
			// Get the content.
			String content =stripper.getText(document);
			String fileName = this.inputFile.getName().split("\\.")[0]+".txt";
			middleFile = new File(this.inputFile.getParent() + "\\" + fileName);
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(middleFile)));
			writer.write(content);
			writer.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		return middleFile;
	}
	
	public int getStartPage() {
		return startPage;
	}
	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}
	public int getEndPage() {
		return endPage;
	}
	public void setEndPage(int endPage) {
		this.endPage = endPage;
	}
}
