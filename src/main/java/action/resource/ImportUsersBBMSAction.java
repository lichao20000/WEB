package action.resource;

import java.io.File;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import dao.resource.ImportUsersBBMSDAO;

public class ImportUsersBBMSAction extends ActionSupport implements ModelDriven {

	/**
	 * 
	 */
	private static final long serialVersionUID = 116006328120703040L;

	//导入文件
	private File file;
	
	private ImportUsersBBMSDAO importUsersBBMS;
	
	//导入的资源类型 0：家庭网关 1：企业网关
	private String infoType = "0";
	
	//导入文件的类型 0：excel文件  1：csv文件
	private String fileType = "0";

	private String isSuccess = "";
	
	public void setInfoType(String infoType) {
		this.infoType = infoType;
	}

	public String getInfoType() {
		return infoType;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public void setImportUsersBBMS(ImportUsersBBMSDAO importUsersBBMS) {
		this.importUsersBBMS = importUsersBBMS;
	}

	public String execute() throws Exception {
		
		//设置参数
		//importUsersBBMS.setResArea(fileType);
		//导出文件
		String resultCode = importUsersBBMS.importFile(file,fileType);
		if ("0".equals(resultCode)) {
			return ERROR;
		} else if ("lackDataErr".equals(resultCode)) {
			return "lackDataErr";
		} else if ("numErr".equals(resultCode)) {
			return "numErr";
		}
		
		return SUCCESS;
	}

	public Object getModel() {
		return infoType;
	}

	public void setResArea(String resArea) {
		this.fileType = resArea;
	}
	
	
}
