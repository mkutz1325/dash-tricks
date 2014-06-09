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
public class Facility {
	
	/**
	 * build csv fromat strings
	 * @param subPair
	 */
	public ArrayList<String> facilityGener(HashMap<Integer, String> facilityPair){
		ArrayList<String> file = new ArrayList<String>();
		Iterator<Integer> iter = facilityPair.keySet().iterator();
		DataRange dr = new DataRange();
		Random population_gener = new Random();
		int count = 0;
		while(iter.hasNext()){
			count++;
			int fid = iter.next();
			String f_name = facilityPair.get(fid);
			int population = population_gener.nextInt(dr.FACILITY_POPULATION_MAX - dr.FACILITY_POPULATION_MIN + 1) + dr.FACILITY_POPULATION_MIN;
			
			String line = fid + ", '" + f_name + "', " + population + ", " + "'coor'";
			file.add(line);
			
			if(dr.PRINT){
				System.out.println(line);
			}
			
		}
		return file;
	}

}
