package mx.pagos.admc.contracts.structures;

public class HolidayUser {
	private Integer idHolidayUser;
	private Integer idUser;
	private String startDate;
	private String endDate;
	
	public Integer getIdHolidayUser() {
		return idHolidayUser;
	}
	
	public void setIdHolidayUser(Integer idHolidays) {
		this.idHolidayUser = idHolidays;
	}
	
	public Integer getIdUser() {
		return idUser;
	}
	
	public void setIdUser(Integer idUser) {
		this.idUser = idUser;
	}
	
	public String getStartDate() {
		return startDate;
	}
	
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	
	public String getEndDate() {
		return endDate;
	}
	
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	

}
