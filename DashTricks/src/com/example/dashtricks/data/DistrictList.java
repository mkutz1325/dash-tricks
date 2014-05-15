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
	public static String jText = "";

	static {
		// Add 3 sample items.
		JSONParser parser=new JSONParser();
		try {
			//Query q = new Query(null);
			jText = "[{\"name\":\"District1\", \"id\":\"District1\"}, "
					+ "{\"name\":\"District2\", \"id\":\"District2\"}";	
			//JSONObject json = new JSONObject(jText);
			Object obj=parser.parse(jText);
			JSONArray array=(JSONArray)obj;
			JSONObject d1 = (JSONObject) array.get(0);
			JSONObject d2 = (JSONObject) array.get(1);
			
			
			//addItem(new District((String) d1.get("id"), (String) d1.get("name")));
			//addItem(new District((String) d2.get("id"), (String) d2.get("name")));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		addItem(new District("District2", "District2"));
		addItem(new District("District3", "District3"));
	}
	
	public static void setJText(String input) {
		
	}

	private static void addItem(District d) {
		ITEMS.add(d);
		ITEM_MAP.put(d.id, d);
	}

	/**
	 * A dummy item representing a piece of content.
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
