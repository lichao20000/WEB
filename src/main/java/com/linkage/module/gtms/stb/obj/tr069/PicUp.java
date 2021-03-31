package com.linkage.module.gtms.stb.obj.tr069;


public class PicUp {
	
	
	private Task task = null ;
	
	public PicUp()
	{
	}
	
	public PicUp(Task task){
		this.task = task;
	}

	
	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}
}
