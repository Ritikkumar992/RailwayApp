package com.example.trainapi.Model;

public class TrainResponseModel {
    private int train_num;
    private String name;
    private String train_from;
    private String train_to;
    private TrainData data;

    public TrainResponseModel(int train_num, String name, String train_from, String train_to, TrainData data) {
        this.train_num = train_num;
        this.name = name;
        this.train_from = train_from;
        this.train_to = train_to;
        this.data = data;
    }

    public int getTrain_num() {
        return train_num;
    }

    public void setTrain_num(int train_num) {
        this.train_num = train_num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTrain_from() {
        return train_from;
    }

    public void setTrain_from(String train_from) {
        this.train_from = train_from;
    }

    public String getTrain_to() {
        return train_to;
    }

    public void setTrain_to(String train_to) {
        this.train_to = train_to;
    }

    public TrainData getData() {
        return data;
    }

    public void setData(TrainData data) {
        this.data = data;
    }
}
