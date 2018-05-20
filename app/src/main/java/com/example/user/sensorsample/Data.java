package com.example.user.sensorsample;

public class Data {

    private int status;
    private String time;
    private String activity;


    public Data(int status, String time) {
        this.status = status;
        this.time = time;

    }

    public void setActivity() {
        if (this.status == 0) {
            this.activity = "Stop";
        }

        else if (this.status == 1) {
            this.activity = "Walking";
        }

        else if (this.status == 2) {
            this.activity = "Running";
        }
    }
}
