package common.domain;

public record DragonHead(Double eyesCount) {
    public boolean validate() {
        return eyesCount != null;
    }
}
