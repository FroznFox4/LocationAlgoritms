package KalmanFilter;

import com.google.common.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class GPSDataFactory {

    private List<String> gpsDataLines;
    private EventBus bus;

    private final long SLEEP_TIME = 1000;

    public GPSDataFactory() {
        Importer importer = new Importer();
        gpsDataLines = importer.readData();
        registerBus();
        startFactory();
    }

    private void registerBus() {
        bus = BusProvider.getInstance();
    }

    private void startFactory() {
        Thread thread = new Thread() {
            public void run() {
                ArrayList<GPSSingleData> gpsSingleData = proccessLine(gpsDataLines);
                for (GPSSingleData el: gpsSingleData) {
                    bus.post(el);
                }
            }
        };
        thread.start();
    }

    private GPSSingleData proccessLine(String gpsLine) {
        String[] gpsParts = gpsLine.split(" ");
        return new GPSSingleData(Float.parseFloat(gpsParts[1]), Double.parseDouble(gpsParts[2]),
                Double.parseDouble(gpsParts[3]), Long.parseLong(gpsParts[4]), Long.parseLong(gpsParts[5]));
    }

    private ArrayList<GPSSingleData> proccessLine(List<String> gpsLine) {
        ArrayList<GPSSingleData> ar = new ArrayList<GPSSingleData>();
        for(String str: gpsLine) {
            String[] gpsParts = str.split(" ");
            ar.add(new GPSSingleData(Float.parseFloat(gpsParts[1]), Double.parseDouble(gpsParts[2]),
                    Double.parseDouble(gpsParts[3]), Long.parseLong(gpsParts[4]), Long.parseLong(gpsParts[5])));
        }
        return ar;
    }

    private void pauseThread(long sleepTime) {
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
