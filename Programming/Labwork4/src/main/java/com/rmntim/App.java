package com.rmntim;

import com.rmntim.exceptions.NoSuchMemberException;
import com.rmntim.exceptions.NotEnoughSpaceException;
import com.rmntim.models.common.Action;
import com.rmntim.models.common.Communicator;
import com.rmntim.models.common.Item;
import com.rmntim.models.common.Order;
import com.rmntim.models.common.Wearable;
import com.rmntim.models.people.*;
import com.rmntim.models.story.Story;

import java.util.List;

public class App {
    public static void main(String[] args) throws NoSuchMemberException, NotEnoughSpaceException {
        var story = new Story();

        var smarty = new Captain("Знайка", Sex.MALE);

        story.addCharacter(smarty);

        class Shorty extends Person {
            public Shorty(String name, Sex sex) {
                super(name, sex);
            }
        }

        story.addCharacterGroup(new Group<>(
                List.of(new Shorty("Кубик", Sex.MALE),
                        new Shorty("Тюбик", Sex.MALE),
                        new Shorty("Звездочкин", Sex.MALE),
                        new Shorty("Стекляшкин", Sex.MALE),
                        new Shorty("Клепка", Sex.MALE))));

        story.addSentence(story.new Sentence(smarty.think(
                "Если они и отправились куда-нибудь, то, безусловно, пошли по этой дорожке")));

        var group = story.getCharacterGroup(0);

        var orderActions = List.of(
                new Action.Take("приспособления для лазания по горам"),
                new Action.Go("вслед за ним к пирамидальной горе"));

        var order = new Order("тотчас", Communicator.IPHONE_15_PRO, orderActions);

        story.addSentence(story.new Sentence("Придя к такому умозаключению")
                .then(smarty.giveOrder(group, order)));

        var spacesuit = new Wearable("скафандр", 500) {
            @Override
            public String toPlural() {
                return "скафандры";
            }
        };

        story.addSentence(story.new Sentence(group.wear(spacesuit)));

        var actions = List.of(
                new Action.Take(new Item("альпеншток", 5)),
                new Action.Attach(List.of(
                        new Item("ледоруб", 3),
                        new Item("моток прочного капронового шнура", 2)), "пояс"));

        var glassy = group.getMemberByName("Стекляшкин");

        story.addSentence(story.new Sentence(group.performActions(actions))
                .andThen(glassy.performAction(new Action.Hang(new Item("свой телескоп", 10), "спина")))
                .withWhich("обычно не расставался"));

        for (Person member : group.getMembers()) {
            if (member.getCurrentWearable() == null)
                continue;

            System.out.println(member.getName() + " одет в " + member.getCurrentWearable().getName() + " и несет "
                    + member.getCurrentWearable().getItems());
        }

        story.tell();
    }
}
