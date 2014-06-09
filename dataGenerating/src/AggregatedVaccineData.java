import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

/**
 * this class define aggregated vaccine stock table
 * vaccine id/ facility id/ month/ stock level/ stock out/coverage rate
 * 
 * @author Chelsea
 *
 */
public class AggregatedVaccineData {
	
	public ArrayList<String> aggVaccineDataGenerate(HashMap<Integer, Pair<String, int[]>> vaccInfo, HashMap<Integer, String> facility){
		ArrayList<String> file = new ArrayList<String>();
		Random sLevel_gener = new Random();
		Random sOut_gener = new Random();
		Random cover_gener = new Random();
		Random rece_gener = new Random();

		DataRange dr = new DataRange();
		for(int m = 1; m <= dr.MONTH; m++){
			Iterator<Integer> iterVacc = vaccInfo.keySet().iterator();
			while(iterVacc.hasNext()){
				int vaccID = iterVacc.next();
				Iterator<Integer> iterFaci = facility.keySet().iterator();
				while(iterFaci.hasNext()){
					int facilityID = iterFaci.next();
					int received = rece_gener.nextInt(dr.RECEIVED_MAX + 1);
					int level = sLevel_gener.nextInt(received + 1);
					//int level = sLevel_gener.nextInt(dr.STOCK_LEVEL_MAX - dr.STOCK_LEVEL_MIN + 1) + dr.STOCK_LEVEL_MIN;
					int out = sOut_gener.nextInt(dr.STOCK_OUT_MAX - dr.STOCK_OUT_MIN + 1) + dr.STOCK_OUT_MIN;
					int coverage = cover_gener.nextInt(dr.VACC_COVERAGE_MAX - dr.VACC_COVERAGE_MIN + 1) + dr.VACC_COVERAGE_MIN;
					int used = received - level;
					String month = dr.YEAR + "-" + m;
					String line = vaccID + ", " + facilityID + ", '" + month + "', " + received + ", " + used + ", " + level + ", " + out + ", " + coverage;
					file.add(line);
					if(dr.PRINT){
						System.out.println(line);
					}
				}
			}
		}
		
		return file;
		
	}

}
