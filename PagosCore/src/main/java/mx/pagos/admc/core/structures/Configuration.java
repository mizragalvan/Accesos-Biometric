package mx.pagos.admc.core.structures;

import mx.pagos.admc.core.enums.Category;
import mx.pagos.admc.core.enums.ValueTypeEnum;

public class Configuration {
	private String name;
	private String value;
	private String description;
	private Category category;
	private String categorySelectValue;
	private String riskLevel;
	private ValueTypeEnum valueType;
	
	public Configuration() { }
	
	public Configuration(final String nameParameter, final String valueParameter) {
	    this.name = nameParameter;
	    this.value = valueParameter;
	}
	
	public final String getName() {
		return this.name;
	}
	
	public final void setName(final String nameParameter) {
		this.name = nameParameter;
	}
	
	public final String getValue() {
		return this.value;
	}
	
	public final void setValue(final String valueParameter) {
		this.value = valueParameter;
	}
	
	public final String getDescription() {
		return this.description;
	}
	
	public final void setDescription(final String descriptionParameter) {
		this.description = descriptionParameter;
	}
	
	public final Category getCategory() {
		return this.category;
	}
	
	public final void setCategory(final Category categoryParameter) {
		this.category = categoryParameter;
	}

    public final String getCategorySelectValue() {
        return this.categorySelectValue;
    }

    public final void setCategorySelectValue(final String categorySelectValueParameter) {
        this.categorySelectValue = categorySelectValueParameter;
    }

    public final String getRiskLevel() {
        return this.riskLevel;
    }

    public final void setRiskLevel(final String riskLevelParameter) {
        this.riskLevel = riskLevelParameter;
    }

    public final ValueTypeEnum getValueType() {
        return this.valueType;
    }

    public final void setValueType(final ValueTypeEnum valueTypeParameter) {
        this.valueType = valueTypeParameter;
    }
}
