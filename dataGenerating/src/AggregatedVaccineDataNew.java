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
public class AggregatedVaccineDataNew {
	
	public ArrayList<String> aggVaccineDataGenerate(HashMap<Integer, Pair<String, int[]>> vaccInfo, HashMap<Integer, String> facility){
		ArrayList<String> file = new ArrayList<String>();
		Random sLevel_gener = new Random();
		Random sOut_gener = new Random();
		Random cover_gener = new Random();
		Random rece_gener = new Random();
		Random use_gener = new Random();
		DataRange dr = new DataRange();
		
		
		HashMap<String, Integer> rece_use_pair = new HashMap<String,Integer>();
		Iterator<Integer> iterVacc = vaccInfo.keySet().iterator();
		int index = 0;
		while(iterVacc.hasNext()){
			int vaccID = iterVacc.next();
			Iterator<Integer> iterFaci = facility.keySet().iterator();
			int min = dr.VACC_COVERAGE_MIN[index];
			int max = dr.VACC_COVERAGE_MAX[index];
			
			System.out.println("index: " + index + ", 	min: " + min + ",	max: " + max);
			
			while(iterFaci.hasNext()){
				int facilityID = iterFaci.next();
			//	System.out.println(facilityID);
				//int level = sLevel_gener.nextInt(dr.STOCK_LEVEL_MAX - dr.STOCK_LEVEL_MIN + 1) + dr.STOCK_LEVEL_MIN;
				int out = sOut_gener.nextInt(dr.STOCK_OUT_MAX - dr.STOCK_OUT_MIN + 1) + dr.STOCK_OUT_MIN;
				//int coverage = cover_gener.nextInt(dr.VACC_COVERAGE_MAX - dr.VACC_COVERAGE_MIN + 1) + dr.VACC_COVERAGE_MIN;
				int coverage = cover_gener.nextInt(max - min + 1) + min;
				
				String month = dr.YEAR + "-" + 1;
				int rece = rece_gener.nextInt(dr.RECEIVED_MAX + 1);
				int level = sLevel_gener.nextInt(rece + 1);
				//int used = use_gener.nextInt(rece + 1);
				int used = rece - level;
				//int[] data = {rece, used, level};
				rece_use_pair.put(vaccID + ", " + facilityID, level);
			//	System.out.println(vaccID + ", " + facilityID + ", " + rece + ", " + used + ", " + level);
								
				String line = vaccID + ", " + facilityID + ", '" + month + "', " + rece + ", " + used + ", " + level + ", " + out + ", " + coverage;
				file.add(line);
				if(dr.PRINT){
					System.out.println(line);
				}
					
				//break;	
			}
			index++;
			
		}
		/*Iterator<String> temp = rece_use_pair.keySet().iterator();
		while(temp.hasNext()){
			String ke = temp.next();
			int a = rece_use_pair.get(ke);
			System.out.print(ke +  "        value" );
			
			System.out.println();
		}
		
		*/
		
		
		for(int m = 2; m <= dr.MONTH; m++){
			iterVacc = vaccInfo.keySet().iterator();
			index = 0;
			while(iterVacc.hasNext()){
				int vaccID = iterVacc.next();
				Iterator<Integer> iterFaci = facility.keySet().iterator();
				int min = dr.VACC_COVERAGE_MIN[index];
				int max = dr.VACC_COVERAGE_MAX[index];
				
				System.out.println("index: " + index + ", 	min: " + min + ",	max: " + max);
				while(iterFaci.hasNext()){
					int facilityID = iterFaci.next();
					//System.out.println(facilityID);
					String key = vaccID + ", " + facilityID;
					int remain = rece_use_pair.get(key);
					//int level = sLevel_gener.nextInt(dr.STOCK_LEVEL_MAX - dr.STOCK_LEVEL_MIN + 1) + dr.STOCK_LEVEL_MIN;
					int out = sOut_gener.nextInt(dr.STOCK_OUT_MAX - dr.STOCK_OUT_MIN + 1) + dr.STOCK_OUT_MIN;
					//int coverage = cover_gener.nextInt(dr.VACC_COVERAGE_MAX - dr.VACC_COVERAGE_MIN + 1) + dr.VACC_COVERAGE_MIN;
					int coverage = cover_gener.nextInt(max - min + 1) + min;
					int rece = rece_gener.nextInt(dr.RECEIVED_MAX + 1);
					int level = sLevel_gener.nextInt(remain + rece + 1);
				/*	System.out.println("hellllllll         " + vacc_faci);
					int[] test = rece_use_pair.get(vacc_faci); 
					
					for(int i:  test){
						System.out.print(i + "   ");
					}
					
					*/
				
					int used = remain + rece - level;

					
					rece_use_pair.put(key, level);
					String month = dr.YEAR + "-" + m;
					String line = vaccID + ", " + facilityID + ", '" + month + "', "  + rece + ", " + used + ", " + level + ", " + out + ", " + coverage;
					file.add(line);
					if(dr.PRINT){
						System.out.println(line);
					}
				}
				index++;
			}
		}
		
		return file;
		
	}

}
