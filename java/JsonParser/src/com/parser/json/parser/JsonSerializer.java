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
	}	
	
	public String ConvertToString(Object obj) throws JsonProcessingException, UnsupportedEncodingException{
		byte [] sStringVal = mObjMapper.writeValueAsBytes(obj);
		String s = new String(sStringVal);
		return s;
	}
}
