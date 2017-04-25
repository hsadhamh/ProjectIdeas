package com.parser.json.parser;

import java.io.UnsupportedEncodingException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.parser.json.log.Logger;

public class JsonSerializer {
	
	ObjectMapper mObjMapper = null;
	
	static JsonSerializer mSerializer = null;
	public static JsonSerializer getInstance(){
		if(mSerializer == null)
			mSerializer = new JsonSerializer();
		return mSerializer;
	}
	
	public JsonSerializer(){
		mObjMapper = new ObjectMapper();
		// mObjMapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
	}	
	
	public String ConvertToString(Object obj) throws JsonProcessingException, UnsupportedEncodingException{
		//	return mObjMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
		byte [] sStringVal = mObjMapper.writeValueAsBytes(obj);
		String s = new String(sStringVal);
		Logger.getInstance().Debug("Json formed [%s].", s);
		return s;
		//return mObjMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
	}
}
