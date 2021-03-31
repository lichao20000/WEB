package com.linkage.module.gwms.report.dao;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

/**
 * @author songxq
 * @version 1.0
 * @category
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 * @since 2020/8/4.
 */
@SuppressWarnings({"rawtypes","unchecked"})
public class LanGatherInfoDAO extends SuperDAO 
{
    Logger logger = LoggerFactory.getLogger(LanGatherInfoDAO.class);

    private Map<String, String> cityMap = null;
    private Map<String, String> venderMap = null;
    private Map<String, String> deviceModelMap = null;

    
	public Map query(String cityId, String startOpenDate, String endOpenDate) 
	{
        int geNum = 0;
        int useGeNum = 0;
        int noMatchNum = 0;
        int useLan3Num = 0;
        int useLan4Num = 0;

        PrepareSQL psql2 = new PrepareSQL();
        if(DBUtil.GetDB()==Global.DB_MYSQL){
        	//TODO wait
        	psql2.append("select count(*) as num ");
        }else{
        	psql2.append("select count(1) as num ");
        }
        psql2.append("from tab_gw_device a,tab_device_version_attribute b,tab_city c ");
        psql2.append("where a.devicetype_id=b.devicetype_id and a.city_id=c.city_id ");
        psql2.append("and b.gigabit_port=1 and (c.city_id=? or c.parent_id=? ) ");
        psql2.setString(1,cityId);
        psql2.setString(2,cityId);
        Map numMap = jt.queryForMap(psql2.getSQL());
        geNum = StringUtil.getIntValue(numMap,"num",0);
        
        PrepareSQL psql3 = new PrepareSQL();
        psql3.append("select a.lan_num from tab_gather_lan_info a where a.city_id = ? " +
                " and a.gather_time > ? and a.gather_time <= ? and a.lan_status = 'Up' ");
        psql3.setString(1,cityId);
        psql3.setLong(2,StringUtil.getLongValue(startOpenDate));
        psql3.setLong(3,StringUtil.getLongValue(endOpenDate));
        List<HashMap<String, String>> lan3And4List = jt.queryForList(psql3.getSQL());

        for (HashMap lan3And4:lan3And4List) {
            String port = StringUtil.getStringValue(lan3And4.get("lan_num"));
            if("3".equals(port)){
                useLan3Num++;
            }else if("4".equals(port)){
                useLan4Num++;
            }
        }

        PrepareSQL psql1 = new PrepareSQL();
        psql1.append("select max(a.is_use_gbPort) as is_use_gbport ,max(a.is_match) as is_match " +
                " from tab_gather_lan_info a where a.city_id = ? " +
                " and a.gather_time > ? and a.gather_time <= ? and gather_result = 1 group by a.device_id");
        psql1.setString(1,cityId);
        psql1.setLong(2,StringUtil.getLongValue(startOpenDate));
        psql1.setLong(3,StringUtil.getLongValue(endOpenDate));
        List<HashMap<String, String>> list = jt.queryForList(psql1.getSQL());
        for (HashMap map:list)
        {
            int isUseGePort = StringUtil.getIntegerValue(map.get("is_use_gbport"));
            if(1 == isUseGePort){
                useGeNum++;
            }
            String isMatch  = StringUtil.getStringValue(map.get("is_match"));
            if("0".equals(isMatch)){
                noMatchNum++;
            }
        }

        Map<String,String> resultMap = new HashMap<String, String>();
        if(useGeNum == 0)
        {
            resultMap.put("geNoUsePer","0.0%");
        }
        else
        {
            //百分比
            BigDecimal b = new BigDecimal(noMatchNum*100/useGeNum);
            //对平均数后两位小数做四舍五入
            double geNoUsePer = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            resultMap.put("geNoUsePer",StringUtil.getStringValue(geNoUsePer)+"%");
        }

        resultMap.put("city_id", cityId);
        resultMap.put("city_name", CityDAO.getCityName(cityId));
        resultMap.put("geDevNum",StringUtil.getStringValue(geNum));
        resultMap.put("geUseNum",StringUtil.getStringValue(useGeNum));
        resultMap.put("noMatchNum", StringUtil.getStringValue(noMatchNum));
        resultMap.put("lan3Num",StringUtil.getStringValue(useLan3Num));
        resultMap.put("lan4Num",StringUtil.getStringValue(useLan4Num));
        //result.add(resultMap);
        return resultMap;
    }

    public List<Map> queryAllCity(String cityId, String startOpenDate, String endOpenDate) 
    {
        List<Map> list = new ArrayList<Map>();
        int geNumAll = 0;
        int useGeNumAll = 0;
        int noMatchNumAll = 0;
        int useLan3NumAll = 0;
        int useLan4NumAll = 0;
        Map<String,String> cityIdNumMap = queryPCityNum();
        List<String> cityList = getSencondCityIdList();
        for (String city_id:cityList)
        {
            Map map = queryAll(city_id,startOpenDate,endOpenDate,cityIdNumMap);
            list.add(map);
        }

        for (Map map : list)
        {
            int geDevNum = StringUtil.getIntegerValue(map.get("geDevNum"));
            int geUseNum = StringUtil.getIntegerValue(map.get("geUseNum"));
            int noMatchNum = StringUtil.getIntegerValue(map.get("noMatchNum"));
            int lan3Num = StringUtil.getIntegerValue(map.get("lan3Num"));
            int lan4Num = StringUtil.getIntegerValue(map.get("lan4Num"));
            geNumAll = geNumAll + geDevNum;
            useGeNumAll = useGeNumAll + geUseNum;
            noMatchNumAll = noMatchNumAll + noMatchNum;
            useLan3NumAll = useLan3NumAll + lan3Num;
            useLan4NumAll = useLan4NumAll + lan4Num;
        }

        Map resultMap = new HashMap();

        if(useGeNumAll == 0)
        {
            resultMap.put("geNoUsePer","0.0%");
        }
        else
        {
            //百分比
            BigDecimal b = new BigDecimal(noMatchNumAll*100/useGeNumAll);
            //对平均数后两位小数做四舍五入
            double geNoUsePer = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            resultMap.put("geNoUsePer",StringUtil.getStringValue(geNoUsePer)+"%");
        }

        resultMap.put("city_id", cityId);
        resultMap.put("city_name", "合计");
        resultMap.put("geDevNum",StringUtil.getStringValue(geNumAll));
        resultMap.put("geUseNum",StringUtil.getStringValue(useGeNumAll));
        resultMap.put("noMatchNum", StringUtil.getStringValue(noMatchNumAll));
        resultMap.put("lan3Num",StringUtil.getStringValue(useLan3NumAll));
        resultMap.put("lan4Num",StringUtil.getStringValue(useLan4NumAll));

        list.add(resultMap);

        return list;
    }

    public List<String> getSencondCityIdList()
    {
        List<String> list = new ArrayList<String>();
        PrepareSQL psql = new PrepareSQL();
        String strSQL = "select city_id from tab_city where parent_id = '00' order by city_id";
        psql.append(strSQL);
        ArrayList<HashMap<String, String>> cityList = DBOperation.getRecords(psql.getSQL());
        for (HashMap cityMap:cityList) {
            list.add(StringUtil.getStringValue(cityMap.get("city_id")));
        }

        return list;
    }

    public List<Map> queryDetail(int curPage_splitPage, int num_splitPage,String cityId,
    		String startOpenDate, String endOpenDate) 
    {
        PrepareSQL psql = new PrepareSQL();
        
        psql.append("select a.city_id,a.username as loid,c.username as servaccount,b.devicetype_id,b.vendor_id,f.speed " +
                "  from hgwcust_serv_info c ,tab_hgwcustomer a,tab_gw_device b, ");
        psql.append(" (select distinct  device_id,");
        if(DBUtil.GetDB()==Global.DB_MYSQL){
        	//TODO wait
        	psql.append("group_concat(lan_speed order by device_id separator ',') speed ");
        }else{
        	psql.append("listagg(lan_speed,',') within group (order by device_id) over(partition by device_id ) speed ");
        }
        psql.append(" from tab_gather_lan_info where  " +
                "  gather_time > ? and gather_time <= ? and is_match = '0' ");
        if(!"00".equals(cityId) && !"".equals(cityId)){
        	psql.append("and city_id='"+cityId+"'");
        }
        psql.append(") f where a.user_id = c.user_id and a.device_id = b.device_id " +
        		"and c.serv_type_id = 10 and a.device_id = f.device_id ");

        psql.setLong(1,StringUtil.getLongValue(startOpenDate));
        psql.setLong(2,StringUtil.getLongValue(endOpenDate));

        cityMap = CityDAO.getCityIdCityNameMap();
        venderMap = getVenderMap();
        deviceModelMap = getDeviceModelMap();

       List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage
                + 1, num_splitPage, new RowMapper()
        {
            public Object mapRow(ResultSet rs, int arg1) throws SQLException {

                Map<String, String> map = new HashMap<String, String>();

                // LOID
                map.put("loid", rs.getString("loid"));

                // 设备厂商
                map.put("vendorName", venderMap.get(rs.getString("vendor_id")));

                // 设备型号
                String devicetype_id = rs.getString("devicetype_id");
                if(!StringUtil.IsEmpty(devicetype_id)) {
                    String devicemodel = deviceModelMap.get(devicetype_id);
                    map.put("devicemodel", devicemodel);
                }

                // 设备属地
                String city_id = rs.getString("city_id");
                if(!StringUtil.IsEmpty(city_id)){
                    String city_name = StringUtil.getStringValue(cityMap.get(city_id));
                    if (!StringUtil.IsEmpty(city_name)){
                        map.put("city_name", city_name);
                    }else{
                        map.put("city_name", "");
                    }
                }else {
                    map.put("city_name", "");
                }

                // 宽带账号
                map.put("username", rs.getString("servaccount"));
                // 不匹配协商速率
                map.put("speed", rs.getString("speed"));

                return map;
            }
        });

        return list;
    }

    public int queryDetailCount(int curPage_splitPage, int num_splitPage, String cityId, 
    		String startOpenDate, String endOpenDate) 
    {
        PrepareSQL psql = new PrepareSQL();
        
        if(DBUtil.GetDB()==Global.DB_MYSQL){
    		//TODO wait
        	psql.append("select count(*) as num ");
    	}else{
    		psql.append("select count(1) as num ");
    	}
        psql.append("from hgwcust_serv_info c ,tab_hgwcustomer a,tab_gw_device b, ");
        psql.append(" (select distinct  device_id,");
        if(DBUtil.GetDB()==Global.DB_MYSQL){
        	psql.append("group_concat(lan_speed order by device_id separator ',') speed ");
        }else{
        	psql.append("listagg(lan_speed,',') within group (order by device_id) over(partition by device_id ) speed ");
        }
        psql.append("from tab_gather_lan_info where gather_time>? and gather_time<=? and is_match='0' ");
        if(!"00".equals(cityId) && !"".equals(cityId)){
        	psql.append("and city_id='"+cityId+"'");
        }
        psql.append(") f where a.user_id = c.user_id and a.device_id = b.device_id " +
        		"and c.serv_type_id = 10 and a.device_id = f.device_id ");

        psql.setLong(1,StringUtil.getLongValue(startOpenDate));
        psql.setLong(2,StringUtil.getLongValue(endOpenDate));
        
        int total = 0;
        Map map = DBOperation.getRecord(psql.getSQL());
        if(null != map && !map.isEmpty())
        {
            total = StringUtil.getIntegerValue(map.get("num"));
        }
        //int total = jt.queryForInt(psql.getSQL());
        int maxPage = 1;
        if (total % num_splitPage == 0){
            maxPage = total / num_splitPage;
        }else{
            maxPage = total / num_splitPage + 1;
        }
        return maxPage;
    }

    /**
     * 取得所有vendorId和厂商名对应的MAP
     *
     * @param request
     * @return ArrayList
     */
    public HashMap<String, String> getVenderMap() {

        logger.debug("getVenderMap()");

        StringBuffer sql = new StringBuffer("select vendor_id,vendor_name,vendor_add from tab_vendor");

        PrepareSQL psql = new PrepareSQL(sql.toString());
        psql.getSQL();

        List<Map> list = jt.queryForList(sql.toString());

        HashMap<String, String> vendorMap = new HashMap<String, String>();

        for(Map<String, String> map : list){

            String vendorAdd = StringUtil.getStringValue(map.get("vendor_add"));

            if(false == StringUtil.IsEmpty(vendorAdd)){
                vendorMap.put(StringUtil.getStringValue(map.get("vendor_id")), vendorAdd);
            }else {
                vendorMap.put(StringUtil.getStringValue(map.get("vendor_id")), 
                				StringUtil.getStringValue(map.get("vendor_name")));
            }
        }
        return vendorMap;
    }

    /**
     * 获取设备型号版本map
     *
     * @param request
     * @return ArrayList
     */
    public HashMap<String, String> getDeviceModelMap() {

        logger.debug("getDeviceModelMap()");

        String devicemodel = "";
        String devicetype_id = "";
        HashMap<String, String> deviceTypeMap = new HashMap<String,String>();

        StringBuffer sql = new StringBuffer();
        sql.append("select b.devicetype_id,a.device_model ");
        sql.append("  from gw_device_model a, tab_devicetype_info b ");
        sql.append(" where a.device_model_id=b.device_model_id ");

        PrepareSQL psql = new PrepareSQL(sql.toString());
        psql.getSQL();

        List<Map> list = jt.queryForList(sql.toString());

        for (Map<String, String> map : list) {

            devicemodel = StringUtil.getStringValue(map.get("device_model"));

            devicetype_id = StringUtil.getStringValue(map.get("devicetype_id"));

            deviceTypeMap.put(devicetype_id, devicemodel);
        }

        return deviceTypeMap;
    }

    public ArrayList<Map> exportDetail(String cityId, String startOpenDate, String endOpenDate) 
    {
        PrepareSQL psql = new PrepareSQL();
        psql.append("select a.city_id,a.username as loid,c.username as servaccount,b.devicetype_id,b.vendor_id,f.speed " +
                "  from hgwcust_serv_info c ,tab_hgwcustomer a,tab_gw_device b, ");
        psql.append("(select distinct  device_id,");
        if(DBUtil.GetDB()==Global.DB_MYSQL){
    		//TODO wait
        	psql.append("group_concat(lan_speed order by device_id separator ',') speed ");
    	}else{
    		psql.append("listagg(lan_speed,',') within group (order by device_id) over(partition by device_id ) speed ");
    	}
        
        psql.append("from tab_gather_lan_info where gather_time>? and gather_time<=? and is_match='0' ");
        if(!"00".equals(cityId) && !"".equals(cityId) && !"-1".equals(cityId))
        {
        	psql.append("and city_id='"+cityId+"'");
        }
        psql.append(") f where  a.user_id = c.user_id and a.device_id = b.device_id and c.serv_type_id = 10 " +
                " and a.device_id = f.device_id ");
        
        psql.setLong(1,StringUtil.getLongValue(startOpenDate));
        psql.setLong(2,StringUtil.getLongValue(endOpenDate));
        
        cityMap = CityDAO.getCityIdCityNameMap();
        venderMap = getVenderMap();
        deviceModelMap = getDeviceModelMap();
        ArrayList<HashMap<String, String>> list = DBOperation.getRecords(psql.getSQL());
        ArrayList<Map> lists = new ArrayList<Map>();
        for (HashMap<String,String> resultMap:list) {
            resultMap.put("vendorName", venderMap.get(StringUtil.getStringValue(resultMap.get("vendor_id"))));
            // 设备型号
            String devicetype_id = StringUtil.getStringValue(resultMap.get("devicetype_id"));
            if(!StringUtil.IsEmpty(devicetype_id)) {
                String devicemodel = deviceModelMap.get(devicetype_id);
                resultMap.put("devicemodel", devicemodel);
            }
            // 设备属地
            String city_id = StringUtil.getStringValue(resultMap.get("city_id"));
            if(!StringUtil.IsEmpty(city_id)){
                String city_name = StringUtil.getStringValue(cityMap.get(city_id));
                if (!StringUtil.IsEmpty(city_name)){
                    resultMap.put("city_name", city_name);
                }else{
                    resultMap.put("city_name", "");
                }
            }else {
                resultMap.put("city_name", "");
            }
            resultMap.put("username", StringUtil.getStringValue(resultMap.get("servaccount")));
            lists.add(resultMap);
        }

        return lists;
    }

    /**
     * 根据area_id查询city_id
     * @param area_id
     * @return
     */
    public Map queryCityIdByAreaId(String area_id){
        PrepareSQL sql = new PrepareSQL();
        sql.setSQL("select a.city_id,b.city_name from tab_city_area a ");
        sql.append("inner join tab_city b on a.city_id=b.city_id ");

        if (!StringUtil.IsEmpty(area_id) && !"1".equals(area_id)) {
            sql.append("where a.area_id="+area_id+" and b.parent_id='00' ");
        }
        else
        {
            sql.append("where a.area_id="+area_id+" ");
        }
        return jt.queryForMap(sql.toString());
    }


    private Map getCityIdAndPcityId() {
        PrepareSQL psql1 = new PrepareSQL();
        psql1.append("select city_id,parent_id from tab_city ");
        ArrayList<HashMap<String, String>> list = DBOperation.getRecords(psql1.getSQL());
        Map resultMap = new HashMap();
        for (HashMap map : list) {
            resultMap.put(StringUtil.getStringValue(map.get("city_id")),
            				StringUtil.getStringValue(map.get("parent_id")));
        }

        return resultMap;
    }

    public Map queryPCityNum()
    {
        Map resultMap = new HashMap();
        PrepareSQL psql2 = new PrepareSQL();
        if(DBUtil.GetDB()==Global.DB_MYSQL){
        	psql2.append("select a.city_id,count(*) as num ");
        }else{
        	psql2.append("select a.city_id,count(1) as num ");
        }
        psql2.append("from tab_gw_device a,tab_device_version_attribute b ");
        psql2.append("where a.devicetype_id=b.devicetype_id ");
        psql2.append("and b.gigabit_port=1 group by a.city_id");
        ArrayList<HashMap<String,String>> list = DBOperation.getRecords(psql2.getSQL());
        Map cityIdAndPcityIdMap = getCityIdAndPcityId();
        List<String> sencondCityList = getSencondCityIdList();
        for (String  pCityId:sencondCityList ) {
            for (HashMap<String,String> cityMap:list)
            {
                String cityId = StringUtil.getStringValue(cityMap.get("city_id"));
                if("00".equals(cityId))
                {
                    continue;
                }
                if(cityId.equals(pCityId))
                {
                    int num = StringUtil.getIntegerValue(resultMap.get(pCityId));
                    num = num + StringUtil.getIntegerValue(cityMap.get("num"));
                    resultMap.put(pCityId,num);
                    continue;
                }
                String pCityIdResult = StringUtil.getStringValue(cityIdAndPcityIdMap.get(cityId));
                if(pCityIdResult.equals(pCityId))
                {
                    int num = StringUtil.getIntegerValue(resultMap.get(pCityId));
                    num = num + StringUtil.getIntegerValue(cityMap.get("num"));
                    resultMap.put(pCityId,num);
                }
            }
        }
        return resultMap;
    }

    public Map queryAll(String cityId, String startOpenDate, String endOpenDate,Map cityIdNumMap) {

        int useGeNum = 0;
        int noMatchNum = 0;
        int useLan3Num = 0;
        int useLan4Num = 0;


        Map<String,String> resultMap = new HashMap<String, String>();

        PrepareSQL psql3 = new PrepareSQL();
        psql3.append("select a.lan_num from tab_gather_lan_info a where a.city_id = ? " +
                " and a.gather_time > ? and a.gather_time <= ? and a.lan_status = 'Up' ");
        psql3.setString(1,cityId);
        psql3.setLong(2,StringUtil.getLongValue(startOpenDate));
        psql3.setLong(3,StringUtil.getLongValue(endOpenDate));
        List<HashMap<String, String>> lan3And4List = jt.queryForList(psql3.getSQL());

        for (HashMap lan3And4:lan3And4List) {
            String port = StringUtil.getStringValue(lan3And4.get("lan_num"));
            if("3".equals(port))
            {
                useLan3Num++;
            }
            else if("4".equals(port))
            {
                useLan4Num++;
            }
        }

        PrepareSQL psql1 = new PrepareSQL();
        psql1.append("select max(a.is_use_gbPort) as is_use_gbport ,max(a.is_match) as is_match " +
                " from tab_gather_lan_info a where a.city_id = ? " +
                " and a.gather_time > ? and a.gather_time <= ? and gather_result = 1 group by a.device_id");
        psql1.setString(1,cityId);
        psql1.setLong(2,StringUtil.getLongValue(startOpenDate));
        psql1.setLong(3,StringUtil.getLongValue(endOpenDate));
        List<HashMap<String, String>> list = jt.queryForList(psql1.getSQL());
        for (HashMap map:list)
        {
            int isUseGePort = StringUtil.getIntegerValue(map.get("is_use_gbport"));
            if(1 == isUseGePort)
            {
                useGeNum++;
            }
            String isMatch  = StringUtil.getStringValue(map.get("is_match"));
            if("0".equals(isMatch))
            {
                noMatchNum++;
            }
        }

        if(useGeNum == 0)
        {
            resultMap.put("geNoUsePer","0.0%");
        }
        else
        {
            //百分比
            BigDecimal b = new BigDecimal(noMatchNum*100/useGeNum);
            //对平均数后两位小数做四舍五入
            double geNoUsePer = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            resultMap.put("geNoUsePer",StringUtil.getStringValue(geNoUsePer)+"%");
        }


        resultMap.put("city_id", cityId);
        resultMap.put("city_name", CityDAO.getCityName(cityId));
        resultMap.put("geDevNum",StringUtil.getStringValue(cityIdNumMap.get(cityId)));
        resultMap.put("geUseNum",StringUtil.getStringValue(useGeNum));
        resultMap.put("noMatchNum", StringUtil.getStringValue(noMatchNum));
        resultMap.put("lan3Num",StringUtil.getStringValue(useLan3Num));
        resultMap.put("lan4Num",StringUtil.getStringValue(useLan4Num));
        return resultMap;
    }

}
