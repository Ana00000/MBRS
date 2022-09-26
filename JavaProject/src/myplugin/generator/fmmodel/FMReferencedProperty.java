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

	
	
	public FMReferencedProperty(FMProperty fmProperty, FetchType fetchType, CascadeType cascadeType, String columnName, 
			String joinTable, FMReferencedProperty oppositeEnd) {
		super(fmProperty.getName(), fmProperty.getType(), fmProperty.getVisibility(),fmProperty.getLower(), fmProperty.getUpper());
		this.fetchType = fetchType;
		this.cascadeType = cascadeType;
		this.columnName = columnName;
		this.joinTable = joinTable;
		this.oppositeEnd = oppositeEnd;
	}

	public FMReferencedProperty getOppositeEnd() {
		return oppositeEnd;
	}



	public void setOppositeEnd(FMReferencedProperty oppositeEnd) {
		this.oppositeEnd = oppositeEnd;
	}



	public CascadeType getCascade() {
		return cascadeType;
	}


	public void setCascade(CascadeType cascade) {
		this.cascadeType = cascade;
	}


	public FetchType getFetch() {
		return fetchType;
	}


	public void setFetch(FetchType fetch) {
		this.fetchType = fetch;
	}


	public String getJoinTable() {
		return joinTable;
	}


	public void setJoinTable(String joinTable) {
		this.joinTable = joinTable;
	}


	public String getColumnName() {
		return columnName;
	}


	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
}
