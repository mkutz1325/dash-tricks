import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;


/**
 * this class generate subDistrict table id/name/facility_id
 * @author Chelsea
 *
 */
public class SubDistrict {
	
	public static HashMap<Integer, String> facility_pair = new HashMap<Integer, String>();
	
	/**
	 * generate csv format for subDistrict table
	 * @param args
	 */
	public ArrayList<String> subDistrictGenerate(HashMap<Integer, String> subPair){
		ArrayList<String> file = new ArrayList<String>();
		DataRange dr = new DataRange();
		Iterator<Integer> iter = subPair.keySet().iterator();
		int fid = 0;
		Random facility_gener = new Random();
		while(iter.hasNext()){			
			int facility_range = facility_gener.nextInt(dr.FACILITY_MAX - dr.FACILITY_MIN + 1) + dr.FACILITY_MIN;
			int sub_id = iter.next();
			String sub_name = subPair.get(sub_id);
			for(int i = 1; i <= facility_range; i++){
				fid++;
				String facility_name = sub_name + "Facility" + i;
				facility_pair.put(fid, facility_name);
				String line = sub_id + ", '" + sub_name + "', " + fid;
				file.add(line);
				if(dr.PRINT){
					System.out.println(line);
				}
			}
		}
		return file;
	}

}
