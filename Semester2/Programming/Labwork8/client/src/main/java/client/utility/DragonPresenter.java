package client.utility;

import common.domain.Dragon;
import common.domain.DragonHead;

public class DragonPresenter {
    private final Localizator localizator;

    public DragonPresenter(Localizator localizator) {
        this.localizator = localizator;
    }

    public String describe(Dragon dragon) {
        String info = "";
        info += " ID: " + dragon.id();
        info += "\n " + localizator.getKeyString("Name") + ": " + dragon.name();
        info += "\n " + localizator.getKeyString("Owner") + ": " + dragon.creator().toString();
        info += "\n " + localizator.getKeyString("CreationDate") + ": " + localizator.getDate(dragon.creationDate());
        info += "\n X: " + dragon.coordinates().x();
        info += "\n Y: " + dragon.coordinates().y();
        info += "\n " + localizator.getKeyString("Age") + ": " + dragon.age();
        info += "\n " + localizator.getKeyString("Color") + ": " + dragon.color().toString();
        info += "\n " + localizator.getKeyString("DragonType") + ": " + dragon.type().toString();
        info += "\n " + localizator.getKeyString("DragonCharacter") + ": " + dragon.character().toString();
        info += "\n " + localizator.getKeyString("DragonHead") + describeHead(dragon.head());

        return info;
    }

    public String describeHead(DragonHead head) {
        if (head == null) return ": null";
        return "\n\t" + localizator.getKeyString("DragonHeadEyesCount") + ": " + head.eyesCount();
    }
}
