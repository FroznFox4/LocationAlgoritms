package KalmanFilter;

public class GPSSingleData {
    private float speed;
    private double lon;
    private double lat;
    private long timestamp;
    private long accuracy;

    public GPSSingleData(float speed, double lon, double lat, long timestamp) {
        this.speed = speed;
        this.lon = lon;
        this.lat = lat;
        this.timestamp = timestamp;
        this.accuracy = 1;
    }

    public GPSSingleData(float speed, double lon, double lat, long timestamp, long accuracy) {
        this.speed = speed;
        this.lon = lon;
        this.lat = lat;
        this.timestamp = timestamp;
        this.accuracy = accuracy;
    }

    public float getSpeed() {
        return speed;
    }

    public double getLongitude() {
        return lon;
    }

    public double getLatitude() {
        return lat;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public long getAccuracy() {
        return accuracy;
    }

//    @Override
//    public String toString() {
//        return speed + " " + lon + " " + lat + " " + timestamp + "" + accuracy;
//    }
    @Override
    public String toString() {
        return lon + "; " + lat;
    }
}
