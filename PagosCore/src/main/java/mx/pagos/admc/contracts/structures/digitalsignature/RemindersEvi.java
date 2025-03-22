package mx.pagos.admc.contracts.structures.digitalsignature;
import java.util.Arrays;

public class RemindersEvi {
    private String initial;
    private String repeat;
    private String[] days;
    private String[] timeRange;
    private int max;
    private String stop;
    private String timeZone;
    
    public RemindersEvi() {
	}

	public RemindersEvi(String initial, String repeat, String[] days, String[] timeRange, int max, String stop, String timeZone) {
        this.initial = initial;
        this.repeat = repeat;
        this.days = days;
        this.timeRange = timeRange;
        this.max = max;
        this.stop = stop;
        this.timeZone = timeZone;
    }

    public String getInitial() {
        return initial;
    }

    public void setInitial(String initial) {
        this.initial = initial;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    public String[] getDays() {
        return days;
    }

    public void setDays(String[] days) {
        this.days = days;
    }

    public String[] getTimeRange() {
        return timeRange;
    }

    public void setTimeRange(String[] timeRange) {
        this.timeRange = timeRange;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public String getStop() {
        return stop;
    }

    public void setStop(String stop) {
        this.stop = stop;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    @Override
    public String toString() {
        return "SignatureRemindersConfig{" +
                "initial='" + initial + '\'' +
                ", repeat='" + repeat + '\'' +
                ", days=" + Arrays.toString(days) +
                ", timeRange=" + Arrays.toString(timeRange) +
                ", max=" + max +
                ", stop='" + stop + '\'' +
                ", timeZone='" + timeZone + '\'' +
                '}';
    }
}
