package tawseel.com.tajertawseel.activities;

/**
 * Created by Monis on 3/1/2017.
 */

public class NotificationData {
    String StatusCode,Time,Date,GroupName,DelegateName,Price;

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getStatusCode() {
        return StatusCode;
    }

    public void setStatusCode(String statusCode) {
        StatusCode = statusCode;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

    public String getDelegateName() {
        return DelegateName;
    }

    public void setDelegateName(String delegateName) {
        DelegateName = delegateName;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }
}
