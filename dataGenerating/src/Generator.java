import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * this class is the executable class to generate all tables
 * @author Chelsea
 *
 */
public class Generator {
	
	public static void main(String[] args){
		//District d = new District();
		DistrictReal d = new DistrictReal();
		ArrayList<String> district = d.districtGenerate();
		writeToFile("district.csv", district);
		
		//SubDistrict s = new SubDistrict();
		SubDistrictReal s = new SubDistrictReal();
		ArrayList<String> subDistrict = s.subDistrictGenerate();
		writeToFile("subDistrict.csv", subDistrict);
		
		FacilityReal f = new FacilityReal();
		ArrayList<String> facility = f.facilityGener(s.sub_pair);
		writeToFile("facility.csv", facility);
		
		//System.out.println(f.facility_pair.keySet().toString());
		
		FridgeModel fmodel = new FridgeModel();
		ArrayList<String> model = fmodel.modelGener();
		writeToFile("fridgeModel.csv", model);
		
		Fridge fridge = new Fridge();
		ArrayList<String> fri = fridge.fridgeGener(f.facility_pair);

		writeToFile("fridge.csv", fri);
		
		Vaccine v = new Vaccine(); 
		ArrayList<String> vaccine = v.vaccineGener();
		writeToFile("vaccine.csv", vaccine);
		
		AggregatedFacilityData afd = new AggregatedFacilityData();
		ArrayList<String> aggFacilityData = afd.aggFacilityDataGener(f.facility_pair);
// 		ArrayList<String> aggFacilityData = afd.aggFacilityDataGener(fridge.fridge_model_capacity_pair, fridge.fridge_facility_pair);
		writeToFile("aggregated_Facility_Capacity_Data.csv", aggFacilityData);
		
		AggregatedVaccineDataNew aggVaccine = new AggregatedVaccineDataNew();
		ArrayList<String> aVc = aggVaccine.aggVaccineDataGenerate(v.vaccine, f.facility_pair);
		writeToFile("aggregated_Facility_Vaccine_Data.csv", aVc);
	}
	
	public static void writeToFile(String fileName, ArrayList<String> file){
		FileWriter fstream;
		try {
			fstream = new FileWriter(fileName);
			BufferedWriter out = new BufferedWriter(fstream);
			for(String s: file){
				out.write(s+"\n");
			}
			out.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
