package madson.ifpe.edu.myapplication;

import java.util.ArrayList;
import java.util.List;

public class NotificationData {
    public static List<NotificationList> notificationsList = new ArrayList<>();

    public static void addNotification(NotificationList notification) {
        notificationsList.add(notification);
    }

    public static List<NotificationList> getNotifications() {
        return notificationsList;
    }
}
