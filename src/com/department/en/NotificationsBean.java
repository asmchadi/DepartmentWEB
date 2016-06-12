package com.department.en;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;

import com.department.ejb.NotificationService;
import com.department.ejb.PvService;
import com.department.entities.Notification;
import com.department.utils.StatusNotification;


@ManagedBean(name = "e_notifications")
public class NotificationsBean {
	private Notification notification;
	private List<Notification> notifications;
	@EJB
	private NotificationService notificationService;
	@EJB
	private PvService pvService;

	@PostConstruct
	public void init() {
	}
	
	public NotificationsBean() {
	}

	public List<Notification> getNotifications() {
		notifications = notificationService.findAll("unreadDate desc");
		return notifications;
	}

	public Notification getNotification() {
		return notification;
	}

	public List<Notification> unreadNotification() {
		return notificationService.findAll("unreadDate DESC");
	}

	public List<Notification> readNotification() {
		return notificationService.findAll("readDate DESC");
	}

	public int countUnreadNotification() {
		return notificationService.findByField("state", StatusNotification.UNREAD.name(), "").size();
	}
}