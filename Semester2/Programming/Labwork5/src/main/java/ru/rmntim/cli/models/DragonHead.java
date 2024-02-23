package ru.rmntim.cli.models;

public class DragonHead {
    private Double eyesCount; //Поле не может быть null

    public DragonHead(Double eyesCount) {
        setEyesCount(eyesCount);
    }

    public Double getEyesCount() {
        return eyesCount;
    }

    public void setEyesCount(Double eyesCount) {
        if (eyesCount == null) {
            throw new IllegalArgumentException("Eyes count cannot be null");
        }
        this.eyesCount = eyesCount;
    }
}
