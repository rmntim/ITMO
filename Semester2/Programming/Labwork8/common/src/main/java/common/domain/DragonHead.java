package common.domain;

import java.util.Objects;

public final class DragonHead {
    private int id;
    private Double eyesCount;

    public DragonHead(Double eyesCount) {
        this.eyesCount = eyesCount;
    }

    public boolean validate() {
        return eyesCount != null;
    }

    public void setEyesCount(double eyesCount) {
        this.eyesCount = eyesCount;
    }

    public Double eyesCount() {
        return eyesCount;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (DragonHead) obj;
        return Objects.equals(this.eyesCount, that.eyesCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eyesCount);
    }

    @Override
    public String toString() {
        return "DragonHead[" +
                "eyesCount=" + eyesCount + ']';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
