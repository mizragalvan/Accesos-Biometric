package mx.pagos.admc.contracts.structures;

import java.io.Serializable;
import java.util.Date;

import mx.pagos.admc.enums.NotificacionTypeEnum;

public class Notification implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer idNotification;
    private Integer idUser;
    private String message;
    private boolean unread;
    private Date createDate;
    private Date expirationDate;
    private NotificacionTypeEnum notificacionType;
    private String idUserNotificaction;
    
    public Notification () {}
    
	public Integer getIdNotification() {
		return idNotification;
	}
	public void setIdNotification(Integer idNotification) {
		this.idNotification = idNotification;
	}
	public Integer getIdUser() {
		return idUser;
	}
	public void setIdUser(Integer idUser) {
		this.idUser = idUser;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public boolean isUnread() {
		return unread;
	}
	public void setUnread(boolean unread) {
		this.unread = unread;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}
	public NotificacionTypeEnum getNotificacionType() {
		return notificacionType;
	}
	public void setNotificacionType(NotificacionTypeEnum notificacionType) {
		this.notificacionType = notificacionType;
	}
	public String getIdUserNotificaction() {
		return idUserNotificaction;
	}
	public void setIdUserNotificaction(String idUserNotificaction) {
		this.idUserNotificaction = idUserNotificaction;
	}
	
}
