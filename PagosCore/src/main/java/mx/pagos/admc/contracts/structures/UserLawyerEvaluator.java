package mx.pagos.admc.contracts.structures;

public class UserLawyerEvaluator {

	private Integer idUser;
	private String name;
	private String firstLastName;
	private String secondLastName;
	private Integer holidaysLeft;
	
	/** Total requisitions assigned by Lawyer **/
	private Integer totalReqByLawyer;
	
	public final Integer getIdUser() {
		return this.idUser;
	}
	
	public final void setIdUser(final Integer idLawyerParameter) {
		this.idUser = idLawyerParameter;
	}
	
	public final String getName() {
		return this.name;
	}
	
	public final void setName(final String nameParameter) {
		this.name = nameParameter;
	}
	
	public final String getFirstLastName() {
		return this.firstLastName;
	}
	
	public final void setFirstLastName(final String firstLastNameParameter) {
		this.firstLastName = firstLastNameParameter;
	}
	
	public final String getSecondLastName() {
		return this.secondLastName;
	}
	
	public final void setSecondLastName(final String secondLastNameParameter) {
		this.secondLastName = secondLastNameParameter;
	}

	public Integer getHolidaysLeft() {
		return holidaysLeft;
	}

	public void setHolidaysLeft(Integer holidaysLeft) {
		this.holidaysLeft = holidaysLeft;
	}

    /**
     * @return the totalReqByLawyer
     */
    public final Integer getTotalReqByLawyer() {
        return totalReqByLawyer;
    }

    /**
     * @param totalReqByLawyer the totalReqByLawyer to set
     */
    public final void setTotalReqByLawyer(Integer totalReqByLawyer) {
        this.totalReqByLawyer = totalReqByLawyer;
    }
}