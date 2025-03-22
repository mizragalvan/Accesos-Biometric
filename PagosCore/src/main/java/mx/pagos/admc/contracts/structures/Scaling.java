package mx.pagos.admc.contracts.structures;

import mx.pagos.admc.enums.ScalingTypeEnum;

public class Scaling {
    private Integer idScaling;
    private String name;
    private String position;
    private String area;
    private String phone;
    private String mail;
    private Integer scalingLevel;
    private Integer idRequisition;
    private ScalingTypeEnum scalingType;
    
    public final Integer getIdScaling() {
        return this.idScaling;
    }
    
    public final void setIdScaling(final Integer idScalingParameter) {
        this.idScaling = idScalingParameter;
    }
    
    public final String getName() {
        return this.name;
    }
    
    public final void setName(final String nameParameter) {
        this.name = nameParameter;
    }
    
    public final String getPosition() {
        return this.position;
    }
    
    public final void setPosition(final String positionParameter) {
        this.position = positionParameter;
    }
    
    public final String getArea() {
        return this.area;
    }
    
    public final void setArea(final String areaParameter) {
        this.area = areaParameter;
    }
    
    public final String getPhone() {
        return this.phone;
    }
    
    public final void setPhone(final String phoneParameter) {
        this.phone = phoneParameter;
    }
    
    public final String getMail() {
        return this.mail;
    }
    
    public final void setMail(final String mailParameter) {
        this.mail = mailParameter;
    }
    
    public final Integer getScalingLevel() {
        return this.scalingLevel;
    }
    
    public final void setScalingLevel(final Integer scalingLevelParameter) {
        this.scalingLevel = scalingLevelParameter;
    }
    
    public final Integer getIdRequisition() {
        return this.idRequisition;
    }
    
    public final void setIdRequisition(final Integer idRequisitionParameter) {
        this.idRequisition = idRequisitionParameter;
    }

	public final ScalingTypeEnum getScalingType() {
		return this.scalingType;
	}

	public final void setScalingType(final ScalingTypeEnum scalingTypeParameter) {
		this.scalingType = scalingTypeParameter;
	}
}
