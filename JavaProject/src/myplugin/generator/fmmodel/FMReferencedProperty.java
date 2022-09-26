package myplugin.generator.fmmodel;

public class FMReferencedProperty extends FMProperty {
	private String fetchType;
	private String cascadeType;
	private String columnName;
	private String joinTable;
	private Integer oppositeEnd;
	
	public FMReferencedProperty(FMProperty fmProperty) {
		super(fmProperty.getName(), fmProperty.getType(), fmProperty.getVisibility(),fmProperty.getLower(), fmProperty.getUpper());
	}

	public FMReferencedProperty(String name, String type, String visibility, int lower, int upper, String cascade,
			String fetch, String joinTable, String columnName, Integer oppositeEnd) {
		super(name, type, visibility, lower, upper);
		this.cascadeType = cascade;
		this.fetchType = fetch;
		this.joinTable = joinTable;
		this.columnName = columnName;
		this.oppositeEnd = oppositeEnd;
	}
	
	/*public FMReferencedProperty(FMProperty fmProperty, FetchType fetchType, CascadeType cascadeType, String columnName, 
			String joinTable, FMReferencedProperty oppositeEnd) {
		super(fmProperty.getName(), fmProperty.getType(), fmProperty.getVisibility(),fmProperty.getLower(), fmProperty.getUpper());
		this.fetchType = fetchType;
		this.cascadeType = cascadeType;
		this.columnName = columnName;
		this.joinTable = joinTable;
		this.oppositeEnd = oppositeEnd;
	}*/

	public Integer getOppositeEnd() {
		return oppositeEnd;
	}



	public void setOppositeEnd(Integer oppositeEnd) {
		this.oppositeEnd = oppositeEnd;
	}



	public String getCascade() {
		return cascadeType;
	}


	public void setCascade(String cascade) {
		this.cascadeType = cascade;
	}


	public String getFetch() {
		return fetchType;
	}


	public void setFetch(String fetch) {
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
