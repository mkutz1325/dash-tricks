package com.example.dashtricks.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DistrictList {

	/**
	 * An array of sample (dummy) items.
	 */
	public static List<District> ITEMS = new ArrayList<District>();

	/**
	 * A map of sample (dummy) items, by ID.
	 */
	public static Map<String, District> ITEM_MAP = new HashMap<String, District>();
	public static String jText;

	public DistrictList(String inputText) {
		jText = inputText;
		parse();
	}
	
	public static void parse() {
		ITEMS.clear();
		ITEM_MAP.clear();
		
		JSONParser parser = new JSONParser();
		try {
			JSONObject districtObj = (JSONObject) parser.parse(jText);
			JSONArray districts = (JSONArray) districtObj.get("districts");
			
			for(Object dist : districts) {
				JSONObject d = (JSONObject) dist;
				addItem(new District(d.get("id").toString(), d.get("name").toString()));
				
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void addItem(District d) {
		ITEMS.add(d);
		ITEM_MAP.put(d.id, d);
	}

	/**
	 * A District list item
	 */
	public static class District {
		public String id;
		public String name;

		public District(String id, String name) {
			this.id = id;
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}
	}
}
