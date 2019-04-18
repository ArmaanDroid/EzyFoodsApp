package sanguinebits.com.ezyfoods.home.fragments.notifications;

import java.util.List;

import base.BaseView;
import model.Notification;

public interface NotificationView extends BaseView {
    void onNotificationsLoaded(List<Notification> notifications);
}
