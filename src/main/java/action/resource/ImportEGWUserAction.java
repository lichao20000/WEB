package action.resource;

import java.io.File;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import dao.resource.ImportEGWUserDAO;

public class ImportEGWUserAction extends ActionSupport implements ModelDriven {

	/**
	 * 
	 */
	private static final long serialVersionUID = 116006328120703040L;

	//导入文件
	private File file;
	
	private ImportEGWUserDAO importEGWUser;
	
	//导入的资源类型 0：家庭网关 1：企业网关
	private String infoType = "0";

	public void setInfoType(String infoType) {
		this.infoType = infoType;
	}

	public String getInfoType() {
		return infoType;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public void setImportUser(ImportEGWUserDAO importUser) {
		this.importEGWUser = importUser;
	}

	public String execute() throws Exception {
		
		//设置参数
//		importEGWUser.setResArea(resArea);
		//导出文件
		importEGWUser.importFile(file,infoType);
		
		return SUCCESS;
	}

	public Object getModel() {
		return infoType;
	}

//	public void setResArea(String resArea) {
//		this.resArea = resArea;
//	}
	
	
}
