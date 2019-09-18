package com.nichetech.smartonsite.benchmark.RequestClass;

/**
 * Created by kaushal on 13/12/16.
 */

public class RequestLogin {

    public String Username, Password, device_type, DeviceId,NotificationId;

    public RequestLogin(String user_id, String password, String device_type, String deviceId,String notificationId) {
        this.Username = user_id;
        this.Password = password;
        this.device_type = device_type;
        this.DeviceId = deviceId;
        this.NotificationId = notificationId;
    }
}
