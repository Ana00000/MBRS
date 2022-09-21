package myplugin.generator.fmmodel;

public class FMReferencedProperty extends FMProperty {
	
	private FetchType fetchType;
	private CascadeType cascadeType;
	private String columnName;
	private String joinTable;
	private FMReferencedProperty oppositeEnd;
	
	public FMReferencedProperty(FMProperty fmProperty) {
		super(fmProperty.getName(), fmProperty.getType(), fmProperty.getVisibility(),fmProperty.getLower(), fmProperty.getUpper());
	}

	public FetchType getFetchType() {
		return fetchType;
	}

	public void setFetchType(FetchType fetchType) {
		this.fetchType = fetchType;
	}

	public CascadeType getCascadeType() {
		return cascadeType;
	}

	public void setCascadeType(CascadeType cascadeType) {
		this.cascadeType = cascadeType;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getJoinTable() {
		return joinTable;
	}

	public void setJoinTable(String joinTable) {
		this.joinTable = joinTable;
	}
	
	public FMReferencedProperty getOppositeEnd() {
		return this.oppositeEnd;
	}
	
	public void setOppositeEnd(FMReferencedProperty oppositeEnd) {
		this.oppositeEnd = oppositeEnd;
	}

}
