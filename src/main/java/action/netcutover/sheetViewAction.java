package action.netcutover;

import java.util.List;
import java.util.Map;

import com.opensymphony.xwork2.ActionSupport;

import dao.netcutover.HandSheetDao;

public class sheetViewAction extends ActionSupport {

	//工单ID
	private String sheet_id;

	//工单报告List
	private List<Map> list;

	//DAO
	private HandSheetDao handSheetDao;

	public String execute() throws Exception {

		list = handSheetDao.getSheet(sheet_id);

		return SUCCESS;
	}

	public void setSheet_id(String sheet_id) {
		this.sheet_id = sheet_id;
	}

	public void setHandSheetDao(HandSheetDao handSheetDao) {
		this.handSheetDao = handSheetDao;
	}

	public List<Map> getList() {
		return list;
	}

}
