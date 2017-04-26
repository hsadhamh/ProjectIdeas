package com.parser.json;

import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.parser.json.database.DatabaseConnection;
import com.parser.json.database.DatabaseConnection.DbEngine;
import com.parser.json.events.Event;
import com.parser.json.events.EventCategory;
import com.parser.json.events.EventList;
import com.parser.json.events.EventProperty.Religion;
import com.parser.json.events.Location;
import com.parser.json.events.Recurrence.RecurType;
import com.parser.json.events.WebReference;
import com.parser.json.log.Logger;

public class EventsRetrieval {
	DatabaseConnection mysql = null;
	String sTempQueryRetrieval = "select "
			+ "ev.id, ev.uid, ev.name, ev.description, ev.webref, ev.category, ev.recur_type, ev.recurrence, "
			+ "sum(case when ep.prop_name like 'holiday' then 1 else 0 end) holiday, "
			+ "sum(case when ep.prop_name like 'Religion' then ep.prop_value else 0 end) Religion, "
			+ "sum(case when ep.prop_name like 'All Day' then 1 else 0 end) AllDay "
			+ "from do_events ev join event_prop ep "
			+ "on ev.id = ep.event_id "
			+ "group by ev.id";
	
	String sTempCityQuery = "select city_id, city_name, s.state_id, s.state_name, "
			+ "s.state_code, ct.id, ct.country_name, ct.country_code "
			+ "from loc_city c join loc_state s on c.state_id = s.state_id "
			+ "JOIN loc_country ct ON s.country_id = ct.id";
	
	String sTempStateQuery = "select s.state_id, s.state_name, s.state_code, ct.id, ct.country_name, ct.country_code "
			+ "from loc_state s JOIN loc_country ct "
			+ "ON s.country_id = ct.id";
	
	String sTempLocations = "SELECT locations.* FROM `event_prop` "
			+ "join locations on event_prop.prop_value = locations.location_id "
			+ "where prop_name like 'Location' and event_id = ";
	
	public EventList getEvents(){
		EventList events = new EventList();
		if(OpenConnection())
		{
			Statement ps = null;
			try {
				ps = mysql.getStatementConnection();
				if(ps == null)
				{
					Logger.getInstance().Debug("Get Events Failed Prepare statement.");
					return events;
				}
				
				ResultSet result = ps.executeQuery(sTempQueryRetrieval);
				while(result != null && result.next()){
					Event oEvent = new Event();
					oEvent.setId(result.getLong(1)); // id stored in DB
					oEvent.setEuid(result.getString(2));
					oEvent.setName(result.getString(3));
					
					Logger.getInstance().Debug(" Processing event : " + oEvent.getName());
					
					oEvent.getProperty().setDescription(result.getString(4));
					
					oEvent.getProperty().getWebref().setType(WebReference.WebRefType.WIKI); 
					oEvent.getProperty().getWebref().setValue(result.getString(5));
					
					oEvent.getProperty().getRecur().setType(RecurType.valueOf(result.getInt(7)));
					oEvent.getProperty().getRecur().setValue(result.getInt(8));
					
					oEvent.getProperty().setReligion(getReligion(result.getInt(10)));
					
					oEvent.getProperty().setModified_at(BigInteger.valueOf(System.currentTimeMillis()/1000));
					oEvent.getProperty().setCreated_at(BigInteger.valueOf(System.currentTimeMillis()/1000));
					
					if(result.getInt(9) == 1)
						oEvent.setCategory(oEvent.getCategory() | EventCategory.Category.HOLIDAY.getValue());
					if(result.getInt(10) != 0)
						oEvent.setCategory(oEvent.getCategory() | EventCategory.Category.RELIGIOUS.getValue());
					if(result.getInt(11) == 1)
						oEvent.setCategory(oEvent.getCategory() | EventCategory.Category.ALL_DAY.getValue());	
					
					//	Locations
					oEvent.getLocations().addAll(getEventsLocations(oEvent.getId()));
					
					events.events.add(oEvent);
				}
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally{
				if(ps != null)
					try {
						ps.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						Logger.getInstance().Debug("Get Events close failed.");
						e.printStackTrace();
					};
			}
		}
		else
		{
			Logger.getInstance().Debug("Get Events Failed on Connection.");
		}
			
		return events;
	}
	
	public List<Location> getEventsLocations(long id){
		List<Location> sLocList = new ArrayList();
		if(OpenConnection())
		{
			Statement psLocations = null;
			//	list of locations and its type
			String sMidLocations = sTempLocations + "" + id;
			try {
				
				psLocations = mysql.getStatementConnection();
				if(psLocations == null)
				{
					Logger.getInstance().Debug("Get Events Failed Prepare statement.");
					return sLocList;
				}
				
				ResultSet result = psLocations.executeQuery(sMidLocations);
				
				while(result != null && result.next())
				{
					int nLocType = result.getInt(2);
					long nLocRef = result.getLong(3);
					switch(nLocType)
					{
					case 1:
						String sMidCountry = "select * from loc_country where id = " + nLocRef;
						Statement psCountry = null;
						try
						{
							psCountry = mysql.getStatementConnection();
							if(psCountry == null)
							{
								Logger.getInstance().Debug("Get Events Failed Prepare statement.");
								return sLocList;
							}
							ResultSet countrySet = psCountry.executeQuery(sMidCountry);
							while(countrySet != null && countrySet.next())
							{
								Location loc = new Location();
								loc.setCountry(countrySet.getString(2));
								sLocList.add(loc);
							}
						}
						catch(Exception e){
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						finally{
							if(psCountry != null)
								psCountry.close();
						}
						break;
					case 2:
						String sMidState = sTempStateQuery + " where s.state_id = " + nLocRef;
						Statement psState = null;
						try{
							psState = mysql.getStatementConnection();
							if(psState == null)
							{
								Logger.getInstance().Debug("Get Events Failed Prepare statement.");
								return sLocList;
							}
							ResultSet StateSet = psState.executeQuery(sMidState);
							while(StateSet != null && StateSet.next())
							{
								Location loc = new Location();
								loc.setCountry(StateSet.getString(5));
								loc.setState(StateSet.getString(2));
								sLocList.add(loc);
							}
						}
						catch(Exception e){
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						finally{
							if(psState != null)
								psState.close();
						}
						break;
					case 3: 
						String sCityState = sTempCityQuery + " where s.state_id = " + nLocRef;
						Statement psCity = null;
						try{
							psCity = mysql.getStatementConnection();
							if(psCity == null)
							{
								Logger.getInstance().Debug("Get Events Failed Prepare statement.");
								return sLocList;
							}
							ResultSet citySet = psCity.executeQuery(sCityState);
							while(citySet != null && citySet.next())
							{
								Location loc = new Location();
								loc.setCountry(citySet.getString(7));
								loc.setState(citySet.getString(4));
								loc.setCity(citySet.getString(2));
								sLocList.add(loc);
							}
						}
						catch(Exception ex){
							// TODO Auto-generated catch block
							ex.printStackTrace();
						}
						finally{
							if(psCity != null)
								psCity.close();
						}
						
						break;
					}
				}
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			Logger.getInstance().Debug("Get Events Failed on Connection.");
		}
		return sLocList;
	}
	
	public boolean OpenConnection(){
		// TODO Auto-generated method stub
		if(mysql == null)
			mysql = new DatabaseConnection();
		return mysql.LoadDatabaseDriver(DbEngine.MYSQL);
	}
	
	public Religion getReligion(int n){
		switch(n)
		{
		case 1:
			return Religion.Islam;
		case 2:
			return Religion.Sikh;
		case 3:
			return Religion.Buddish;
		case 4:
			return Religion.Hindu;
		case 5:
			return Religion.Jewish;
		case 6:
			return Religion.Christian;
		case 7:
			return Religion.Orthodox;
		case 8:
			return Religion.Jainism;
		}
		return null;
	}
}
