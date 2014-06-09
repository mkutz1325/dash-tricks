import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

/**
 * this class build csv format for Facility table
 * id/name/popuplation/corr
 * @author Chelsea
 *
 */
public class FacilityReal {
	public static HashMap<Integer, String> facility_pair = new HashMap<Integer, String >();
	
	/**
	 * build csv fromat strings
	 * @param subPair
	 */
	public ArrayList<String> facilityGener(HashMap<Integer, String> subPair){
		ArrayList<String> file = new ArrayList<String>();
		Iterator<Integer> iter = subPair.keySet().iterator();
		DataRange dr = new DataRange();
		Random population_gener = new Random();
		Random facility_gener = new Random();
		int fid = 0;
		while(iter.hasNext()){
			
			int subID = iter.next();

			
			int facility = facility_gener.nextInt(dr.FACILITY_MAX - dr.FACILITY_MIN + 1) + dr.FACILITY_MIN;
			for(int i = 0; i < facility; i++){
				fid++;
				String f_name = "Facility" + fid;
				facility_pair.put(fid, f_name);
				int population = population_gener.nextInt(dr.FACILITY_POPULATION_MAX - dr.FACILITY_POPULATION_MIN + 1) + dr.FACILITY_POPULATION_MIN;
				String line = fid + ", '" + f_name + "', " + population + ", "  + subID + ", 'coor'";
				file.add(line);
			}
			
		}
		return file;
	}

}
