import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;


public class SubDistrictReal {
	public static HashMap<Integer, String> sub_pair = new HashMap<Integer, String>();
	/**
	 * generate csv format for subDistrict table
	 * @param args
	 */
	public ArrayList<String> subDistrictGenerate(){
		
		FileInputStream file = null;
		DataInputStream input = null;
		BufferedReader in = null;
		ArrayList<String> list = new ArrayList<String>();
		try {
			file = new FileInputStream("subdistrictsOriginal.csv");
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
				String temp =  token[0].trim() + ", '" + name + "', " + token[2].trim() + ", " + coor;
				list.add(temp);
				sub_pair.put(Integer.parseInt(token[0]), token[1]);
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
