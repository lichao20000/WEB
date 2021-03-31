package action.hgwip;

import static action.cst.AJAX;

import java.util.HashMap;
import java.util.List;

import dao.hgwip.IPManagerDAO;


public class GetChildCityAction
{
	private String ajax;
	private String cityid;
	private IPManagerDAO ipmDao;
	public String getAjax()
	{
		return ajax;
	}
	public void setIpmDao(IPManagerDAO ipmDao)
	{
		this.ipmDao = ipmDao;
	}
	public String execute() throws Exception
	{
		List cityList=ipmDao.getCountryByCity(cityid);
		ajax+="";	
		for(int i=0;i<cityList.size();i++){
			HashMap map=(HashMap)cityList.get(i);
			ajax+="<option value='"+map.get("city_id")+"'>"+map.get("city_name")+"</option>";
		}
		return AJAX;
	}
	public void setCityid(String cityid)
	{
		this.cityid = cityid;
	}
	
}
