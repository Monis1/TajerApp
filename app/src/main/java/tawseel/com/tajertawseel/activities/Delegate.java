package tawseel.com.tajertawseel.activities;

/**
 * Created by Monis on 2/27/2017.
 */

public class Delegate {
    String name,last_deliver,mobile,latitude,longitude;

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getLast_deliver() {
        return last_deliver;
    }

    public void setLast_deliver(String last_deliver) {
        this.last_deliver = last_deliver;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    float rating;

}
