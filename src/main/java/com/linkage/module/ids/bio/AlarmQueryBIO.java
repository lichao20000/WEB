package com.linkage.module.ids.bio;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.ids.dao.AlarmQueryDAO;

public class AlarmQueryBIO {
	private static Logger logger = LoggerFactory
			.getLogger(AlarmQueryBIO.class);
	private AlarmQueryDAO dao;

	@SuppressWarnings("rawtypes")
	public List<Map> getIdsarmInfoList(String alarmname, String alarmcode,
			String alarmlevel, String alarmobject, String hour, String count,
			String temperature, String timedelay, String lightpower,
			String packetloss, int curPage_splitPage, int num_splitPage) {
		return dao.getIdsarmInfoList(alarmname, alarmcode, alarmlevel,
				alarmobject, hour, count, temperature, timedelay, lightpower,
				packetloss, curPage_splitPage, num_splitPage);
	}

	public int countIdsarmInfoList(String alarmname, String alarmcode,
			String alarmlevel, String alarmobject, String hour, String count,
			String temperature, String timedelay, String lightpower,
			String packetloss, int curPage_splitPage, int num_splitPage) {
		return dao.countIdsarmInfoList(alarmname, alarmcode, alarmlevel,
				alarmobject, hour, count, temperature, timedelay, lightpower,
				packetloss, curPage_splitPage, num_splitPage);
	}

	@SuppressWarnings("rawtypes")
	public List<Map> getIdsarmInfoListExcel(String alarmname, String alarmcode,
			String alarmlevel, String alarmobject, String hour, String count,
			String temperature, String timedelay, String lightpower,
			String packetloss) {
		return dao.getIdsarmInfoListExcel(alarmname, alarmcode, alarmlevel,
				alarmobject, hour, count, temperature, timedelay, lightpower,
				packetloss);
	}

	public void insert(String alarmname, String alarmcode, String alarmlevel,
			String alarmobject, String hour, String count, String temperature,
			String timedelay, String lightpower, String packetloss) {
		dao.insert(alarmname, alarmcode, alarmlevel, alarmobject, hour, count,
				temperature, timedelay, lightpower, packetloss);
	}
	
	public void delete(String id) {
		dao.delete(id);
	}
	
	public void update(String id, String alarmname, String alarmcode,
			String alarmlevel, String alarmobject, String hour, String count,
			String temperature, String timedelay, String lightpower,
			String packetloss) {
		dao.update(id,alarmname, alarmcode, alarmlevel, alarmobject, hour, count,
				temperature, timedelay, lightpower, packetloss);
	}

	public AlarmQueryDAO getDao() {
		return dao;
	}

	public void setDao(AlarmQueryDAO dao) {
		this.dao = dao;
	}

}
