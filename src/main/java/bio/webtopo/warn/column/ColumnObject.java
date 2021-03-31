package bio.webtopo.warn.column;


public abstract class ColumnObject implements ColumnInterface {
	String id    = null;
	boolean visible = true;
	String name = null;
	public ColumnObject(String id,String name,boolean visible){
		 this.id=id;
		 this.name=name;
		 this.visible=visible;
	}
	public String getId() {
		return id;
	}
	public boolean isVisible() {
		return visible;
	}
	public String getName() {
		return name;
	}

}
