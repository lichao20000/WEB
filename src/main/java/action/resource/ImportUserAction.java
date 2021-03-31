package action.resource;

import java.io.File;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import dao.resource.ImportUserDAO;

public class ImportUserAction extends ActionSupport implements ModelDriven {

	/**
	 * 
	 */
	private static final long serialVersionUID = 116006328120703040L;

	//导入文件
	private File file;
	
	private ImportUserDAO importUser;
	
	//导入的资源类型 0：家庭网关 1：企业网关
	private String infoType = "0";
	
	//导入文件的来源 0：BSS  1：IPOSS  导入的BSS帐号需要新增和更新 导入的IPOSS帐号只需要更新已有的
	private String resArea = "0";

	public void setInfoType(String infoType) {
		this.infoType = infoType;
	}

	public String getInfoType() {
		return infoType;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public void setImportUser(ImportUserDAO importUser) {
		this.importUser = importUser;
	}

	public String execute() throws Exception {
		
		//设置参数
		importUser.setResArea(resArea);
		//导出文件
		importUser.importFile(file,infoType);
		
		return SUCCESS;
	}

	public Object getModel() {
		return infoType;
	}

	public void setResArea(String resArea) {
		this.resArea = resArea;
	}
	
	
}
