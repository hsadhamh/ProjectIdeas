package com.parser.json;

import com.parser.json.events.EventList;
import com.parser.json.log.Logger;
import com.parser.json.parser.JsonSerializer;

public class JsonGenerateMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EventsRetrieval oEvents = new EventsRetrieval();
		EventList objList = oEvents.getEvents();
		
		Logger.getInstance().Debug("Found Objects [%d].", objList.events.size());		
		try
		{
			String sJson = JsonSerializer.getInstance().SerializeToString(objList);
			Logger.getInstance().Debug("Json formed [%s].", sJson);
			
			EventList converted = (EventList) JsonSerializer.getInstance().UnserializeToObject(sJson, EventList.class);
			
			Logger.getInstance().Debug("Json formed [%s].", converted.events.get(0).getName());
		}
		catch(Exception ex){
			Logger.getInstance().Debug("Exception on convert.");
			ex.printStackTrace();
		}
		
	}
}
