package myplugin.generator.fmmodel;

public class FMPersistentProperty extends FMProperty {
	private String columnName;
	private Integer length;
	private Integer precision;
	private Strategy strategy;
	private Boolean isKey;
	private Boolean isNullable;
	private Boolean isUnique;
	
	public FMPersistentProperty(FMProperty fmProperty, String columnName, Integer length, Integer precision, Strategy strategy, Boolean isKey, Boolean isNullable, Boolean isUnique) {
		super(fmProperty.getName(), fmProperty.getType(), fmProperty.getVisibility(), fmProperty.getLower(), fmProperty.getUpper());
		this.columnName = columnName;
		this.length = length;
		this.strategy = strategy;
		this.isKey = isKey;
		this.isNullable = isNullable;
		this.isUnique = isUnique;
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
	
	public Boolean getIsNullable() {
		return isNullable;
	}

	public void setIsNullable(Boolean isNullable) {
		this.isNullable = isNullable;
	}

	public Boolean getIsUnique() {
		return isUnique;
	}

	public void setIsUnique(Boolean isUnique) {
		this.isUnique = isUnique;
	}
}
