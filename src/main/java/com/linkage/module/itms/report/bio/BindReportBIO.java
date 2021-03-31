package com.linkage.module.itms.report.bio;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.itms.report.dao.BindReportDAO;

public class BindReportBIO {

	/** log */
	private static Logger logger = LoggerFactory
			.getLogger(BindReportBIO.class);

	private BindReportDAO dao;

	@SuppressWarnings("unchecked")
	/**
	 * 绑定率统计   页面列表显示
	 * 
	 * 
	 */
	public List<Map> getBindRateByCityid(String starttime1, String endtime1,
			String cityId, String usertype, String gw_type) {

		logger.debug("getBindRateByCityid({},{},{},{})", new Object[] {
				starttime1, endtime1, cityId, usertype });

		List<Map> list = new ArrayList<Map>();

		// 按本地网统计
		Map cityMap = CityDAO.getCityIdCityNameMap();

		// 根据属地cityId获取下一层属地ID(不包含自己)
		ArrayList<String> cityList = CityDAO
				.getNextCityIdsByCityPidCore(cityId);
		Collections.sort(cityList);
		Map demap = dao.getOperOkNum(starttime1, endtime1, cityId, usertype,
				gw_type); // 统计已绑定数
		Map nomap = dao.getOperUnOkNum(starttime1, endtime1, cityId, usertype,
				gw_type); // 统计未绑定数

		// 已绑定数
		long detotal = 0;
		// 未绑定数
		long nototal = 0;

		ArrayList<String> tlist = null;

		// 选择的属地
		Map<String, Object> amap = new HashMap();
		amap.put("city_id", cityId); // 属地ID
		amap.put("city_name", cityMap.get(cityId)); // 属地名称
		detotal = 0;
		nototal = 0;
		tlist = CityDAO.getAllNextCityIdsByCityPid(cityId);
		for (int j = 0; j < tlist.size(); j++) {
			detotal = detotal
					+ StringUtil.getLongValue(demap.get(tlist.get(j)));
			nototal = nototal
					+ StringUtil.getLongValue(nomap.get(tlist.get(j)));
		}
		amap.put("detotal", detotal); // 已绑定
		amap.put("nototal", nototal); // 未绑定
		amap.put("percent", percent(detotal, nototal)); // 绑定率
		amap.put("usertype", usertype); // 用户终端类型
		list.add(amap);
		for (int i = 0; i < cityList.size(); i++) {
			Map<String, Object> tmap = new HashMap<String, Object>();
			// 已部署数
			detotal = 0;
			// 未部署数
			nototal = 0;
			String city_id = cityList.get(i);
			tlist = CityDAO.getAllNextCityIdsByCityPid(city_id);
			tmap.put("city_id", city_id);
			tmap.put("city_name", cityMap.get(city_id));
			for (int j = 0; j < tlist.size(); j++) {
				detotal = detotal
						+ StringUtil.getLongValue(demap.get(tlist.get(j)));
				nototal = nototal
						+ StringUtil.getLongValue(nomap.get(tlist.get(j)));
			}
			String percent = percent(detotal, nototal);
			tmap.put("detotal", detotal); // 已绑定
			tmap.put("nototal", nototal); // 未绑定
			tmap.put("percent", percent); // 绑定率
			tmap.put("usertype", usertype); // 用户终端类型
			list.add(tmap);
			tlist = null;
		}
		cityMap = null;
		cityList = null;
		tlist = null;
		return list;
	}

	/**
	 * 用户列表查询
	 * 
	 * @param starttime1
	 * @param endtime1
	 * @param cityId
	 * @param reform_flag
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @param isAll
	 * @param usertype
	 * @return
	 */
	public List<Map> getUsersList(String starttime1, String endtime1,
			String cityId, String bind_flag, int curPage_splitPage,
			int num_splitPage, String usertype, String gw_type) {
		return dao.getUserList(starttime1, endtime1, cityId, bind_flag,
				curPage_splitPage, num_splitPage, usertype, gw_type);
	}

	public int getUsersCount(String starttime1, String endtime1, String cityId,
			String bind_flag, int curPage_splitPage, int num_splitPage,
			String usertype,String gw_type) {
		return dao.getUsersCount(starttime1, endtime1, cityId, bind_flag,
				curPage_splitPage, num_splitPage, usertype,gw_type);
	}

	public String percent(long p1, long p2) {
		double p3;
		if (p1 == 0 && p2 == 0) {
			return "N/A";
		} else {
			p3 = (double) p1 / (p1 + p2);
		}
		NumberFormat nf = NumberFormat.getPercentInstance();
		nf.setMinimumFractionDigits(2);
		String str = nf.format(p3);
		return str;
	}

	public void setDao(BindReportDAO dao) {
		this.dao = dao;
	}

}
