package myplugin.generator.options;

public class TypeMapping {
	private String uMLType;
	private String destType;
	private String libraryName;
	
	public TypeMapping(String uMLType, String destType, String libraryName) {
		super();
		this.uMLType = uMLType;
		this.destType = destType;
		this.libraryName = libraryName;
	}

	public String getuMLType() {
		return uMLType;
	}

	public void setuMLType(String uMLType) {
		this.uMLType = uMLType;
	}

	public String getDestType() {
		return destType;
	}

	public void setDestType(String destType) {
		this.destType = destType;
	}

	public String getLibraryName() {
		return libraryName;
	}

	public void setLibraryName(String libraryName) {
		this.libraryName = libraryName;
	}
}
