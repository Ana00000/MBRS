package myplugin.generator.fmmodel;


public class FMPersistentProperty extends FMProperty {
	
	private String columnName;
	private Integer length;
	private Integer precision;
	private Strategy strategy;
	private Boolean isKey;
	private Boolean isUnique;
	
	public FMPersistentProperty(FMProperty fmProperty) {
		super(fmProperty.getName(), fmProperty.getType(), fmProperty.getVisibility(), fmProperty.getLower(), fmProperty.getUpper());
		
		strategy = Strategy.NONE;
		isKey = false;
		isUnique = false;
	}
	
	public String getColumnName() {
		return columnName;
	}
	
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	
	public Integer getLength() {
		return length;
	}
	
	public void setLength(Integer length) {
		this.length = length;
	}
	
	public Integer getPrecision() {
		return precision;
	}

	public void setPrecision(Integer precision) {
		this.precision = precision;
	}

	public Strategy getStrategy() {
		return strategy;
	}

	public void setStrategy(Strategy strategy) {
		this.strategy = strategy;
	}

	public Boolean getIsKey() {
		return isKey;
	}

	public void setIsKey(Boolean isKey) {
		this.isKey = isKey;
	}

	public Boolean getIsUnique() {
		return isUnique;
	}

	public void setIsUnique(Boolean isUnique) {
		this.isUnique = isUnique;
	}
	
	
	
	
	
	
	
	
	
	

}
