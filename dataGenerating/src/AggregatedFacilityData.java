
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

/**
 * class define the aggregated facility data table
 * fridge id/facility id/ month/ current capacity/ required capacity
 * @author Chelsea
 *
 */
public class AggregatedFacilityData {
	
	/**
	 * build csv format of table
	 * @param fridgeCapacity
	 * @param fridgeFacility
	 * @return 
	 */
	public  ArrayList<String> aggFacilityDataGener(HashMap<Integer, String> facility){	
		ArrayList<String> file = new ArrayList<String>();
		DataRange dr = new DataRange();
		Random curr_gener = new Random();
		Random req_gener = new Random();
		for(int m = 1; m <= dr.MONTH; m++){
			Iterator<Integer> iter = facility.keySet().iterator();
			//Iterator<Integer> iter = fridgeCapacity.keySet().iterator();
			while(iter.hasNext()){
				int fid = iter.next();
				int required = req_gener.nextInt(dr.REQ_CAPACITY_MAC - dr.REQ_CAPACITY_MIN) + dr.REQ_CAPACITY_MIN; 
				int current = required;
				//int facilityID = fridgeFacility.get(fid);
				if(current < 50){
					current += dr.CURR_CAPACITY;
				}else {
					int temp = curr_gener.nextInt(2);
					if(temp < 1){
						current -= dr.CURR_CAPACITY;
					}else{
						current += dr.CURR_CAPACITY;
					}
				}
				
				String month = dr.YEAR + "-" + m;
				String line = fid + ", '" + month + "', " + current + ", " + required;
				file.add(line);
				if(dr.PRINT){
					System.out.println(line);
				}
					
			}
		}
		
		
		return file;
	}
  
}
