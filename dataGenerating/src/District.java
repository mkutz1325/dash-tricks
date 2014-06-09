


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Class to generate District table id/name/sub_district_id/manager/coor
 * in csv format
 * @author jianzhao
 *
 */
public class District {
	
	public HashMap<Integer, String> district_pair = new HashMap<Integer, String>();
	public HashMap<Integer, String> sub_district_pair = new HashMap<Integer, String>();
	

	
	/**
	 * this method write the data into csv file and update the id and name pair of district
	 */
	public ArrayList<String> districtGenerate(){
		
		ArrayList<String> file = new ArrayList<String>();
		DataRange dr = new DataRange();
		
		int sub_id = 0;
		for(int i = 0; i < dr.DISTRICT; i++){
			int district_id = i + 1;
			String district_name = "District" + district_id;
			//update district_pair 
			district_pair.put(district_id, district_name);

			Random sub_district_gener = new Random();
			int sub_district_range = sub_district_gener.nextInt(dr.SUB_DISTRICT_MAX - dr.SUB_DISTRICT_MIN +1) + dr.SUB_DISTRICT_MIN ;
			
			if(dr.PRINT){
				System.out.println("Number of sub district:   " + sub_district_range);
			}
			
			for(int j = 0; j < sub_district_range; j++){
				sub_id++;
				int sub = 1 + j;
				String sub_name = district_name + "SubDistrict"+sub;
				sub_district_pair.put(sub_id, sub_name);
				String line = district_id + ", '" + district_name + "', " + sub_id + ", '" + "manager" + district_id
						+ "', " + "'coor" + district_id + "'";
				file.add(line);
				
				if(dr.PRINT){
					System.out.println(line);
				}
			}
		}
		return file;
		
	}
}
