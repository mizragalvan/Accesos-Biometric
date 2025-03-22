package mx.pagos.admc.contracts.interfaces;

import java.util.List;

import mx.pagos.admc.contracts.structures.Notification;
import mx.pagos.general.exceptions.DatabaseException;

public interface Notifiable {
	List<Notification> findNotificationsByIdUser(final Integer idUser) throws DatabaseException;
	Integer saveNotification (Notification notification) throws DatabaseException;
	void updateNotification (Integer idNotification, boolean unread) throws DatabaseException;
}
