import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class DistrictReal {
	/**
	 * this method write the data into csv file and update the id and name pair of district
	 */
	public ArrayList<String> districtGenerate(){
		FileInputStream file = null;
		DataInputStream input = null;
		BufferedReader in = null;
		ArrayList<String> list = new ArrayList<String>();
		try {
			file = new FileInputStream("districtsOriginal.csv");
			input = new DataInputStream(file);			
			in = new BufferedReader(new InputStreamReader(input));
			String line = "";
			int man = 1;
			
			while((line = in.readLine()) != null){
				String[] token = line.split(",");
				String coor = token[3];
				for(int i = 4; i < token.length; i++){
					coor += ", " + token[i];
				}
				String name = token[1].trim().replace("'", "_");
				String temp =  token[0].trim() + ", '" + name + "', " + token[2].trim() + ", '" + "manager" + man
						+ "', " + coor;
				list.add(temp);
				man++;

			} 
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		return list;
		
	}
}
