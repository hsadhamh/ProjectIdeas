package com.parser.json;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;

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
			String sJson = JsonSerializer.getInstance().SerializeToString(objList, true);
			Logger.getInstance().Debug("Json formed [%s].", sJson);
			
			//	Writing to FILE (no encoding)
			try(PrintWriter out = new PrintWriter("JsonCreated.txt")){
				out.println(sJson);
			}
			
			try(Writer out1 = new BufferedWriter(
		            new OutputStreamWriter(new FileOutputStream(
		                    "JsonCreatedUTF.txt"), "UTF-8"))){
				out1.write(sJson);
			}
			
			/*
			
			EventList converted = (EventList) JsonSerializer.getInstance().UnserializeToObject(sJson, EventList.class);
			
			Logger.getInstance().Debug("Json formed [%s].", converted.events.get(0).getName());
			
			*/
		}
		catch(Exception ex){
			Logger.getInstance().Debug("Exception on convert.");
			ex.printStackTrace();
		}
		
	}
}
