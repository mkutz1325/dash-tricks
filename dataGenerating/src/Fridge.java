import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

/**
 * this class define talbe fridge
 * fidgeID/ facility ID/model ID/ required level (liter)
 * @author Chelsea
 *
 */
public class Fridge {
	
	public HashMap<Integer, Integer> fridge_facility_pair = new HashMap<Integer, Integer>();
	public HashMap<Integer, Pair<Integer, Integer>> fridge_model_capacity_pair = new HashMap<Integer, Pair<Integer,Integer>>();
	
	public ArrayList<String> fridgeGener(HashMap<Integer, String> facility){
		ArrayList<String> file = new ArrayList<String>();
		DataRange dr = new DataRange();
		Iterator<Integer> iter = facility.keySet().iterator();
		Random fridge_gener = new Random();
		Random mid_gener = new Random();
		Random capa_gener = new Random();
		Random month_gener = new Random();
		Random year_gener = new Random();
		int fid = 0;
		while(iter.hasNext()){
			int fridge_range = fridge_gener.nextInt(dr.FRIDGE_MAX - dr.FRIDGE_MIN + 1) + dr.FRIDGE_MIN;
			int facilityID = iter.next();
			for(int i = 1; i <= fridge_range; i++){
				fid++;
				int mid = mid_gener.nextInt(dr.MODEL) + 1;
				fridge_facility_pair.put(fid, facilityID);
				int capacity = capa_gener.nextInt(dr.REQ_CAPACITY_MAC - dr.REQ_CAPACITY_MIN + 1) + dr.REQ_CAPACITY_MIN;
				fridge_model_capacity_pair.put(fid, new Pair (mid, capacity));
				int month = month_gener.nextInt(dr.MONTH)+1;
				int year = year_gener.nextInt(dr.YEAR_MAX - dr.YEAR_MIN + 1) + dr.YEAR_MIN;
				String date = year + "-" + month;
				String line = fid + ", " + facilityID + ", " + mid + ", " + capacity + ", '" + date + "'";
				file.add(line);
				if(dr.PRINT){
					System.out.println(line);
				}
			}
			
		}
		return file;
	}
	

}
