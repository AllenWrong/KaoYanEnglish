package readComprehension;

import java.util.HashMap;

public class FilePage {
	private HashMap<String, Object[]> map;
	private String[] nameArr = {"2018.pdf","2017.pdf","2016.pdf","2015.pdf","2014.pdf","2013.pdf","2012.pdf","2011.pdf","2010.pdf"};
	private Object[][] startEndPage = {{0,10},{0,11},{0,12},{0,11},{0,9},{0,11},{0,10},{0,10},{0,11}};
	
	public FilePage() {
		if(nameArr.length != startEndPage.length) {
			throw new RuntimeException("Unequals length of nameArr and startEndPage!");
		}
		
		this.map = new HashMap<>();
		for(int i = 0;i<nameArr.length;i++) {
			map.put(nameArr[i], startEndPage[i]);
		}
	}
	
	public int getStartPage(String name) {
		return (int) map.get(name)[0];
	}
	
	public int getEndPage(String name) {
		return (int)map.get(name)[1];
	}
}
