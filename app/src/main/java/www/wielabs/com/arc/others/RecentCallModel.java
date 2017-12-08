package www.wielabs.com.arc.others;

/**
 * Created by Adil on 06-12-2017.
 */

public class RecentCallModel {
    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getData_and_time() {
        return data_and_time;
    }

    public void setData_and_time(String data_and_time) {
        this.data_and_time = data_and_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }


    public RecentCallModel(String mobile_number, String data_and_time, String duration) {
        this.mobile_number = mobile_number;
        this.data_and_time = data_and_time;
      //  this.status = status;
        this.duration = duration;
    }


    String mobile_number;

    String data_and_time;
    String status;
    String duration;

}
