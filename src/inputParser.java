import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class inputParser {


	private String filePath;
	private int totalTestCases = 0;

	public inputParser(String filePath){
		this.filePath = filePath;
	}

	public List parse(String file) {

		BufferedReader br = null;
		List<String> testCaseList = new ArrayList<String>();
		
		//HashMap<String, ArrayList<edge>> adjLists_dict = new HashMap<String, ArrayList<edge>>();
		//*******************************************************
		//need to fix the path of file read here for vocarium
		//*******************************************************
		try {
			String sCurrentLine;
			String fileName = "./src/" + filePath;	//for local eclipse execution
			//String fileName = filePath;		//for vocarium	

			br = new BufferedReader(new FileReader(fileName));

			while ((sCurrentLine = br.readLine()) != null) {
				testCaseList.add(sCurrentLine);
				//System.out.println(sCurrentLine);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		return testCaseList;

	}	


}


