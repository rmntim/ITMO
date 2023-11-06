package com.rmntim;

import com.rmntim.models.actions.GoAction;
import com.rmntim.models.actions.TakeAction;
import com.rmntim.models.common.Communicator;
import com.rmntim.models.common.Order;
import com.rmntim.models.people.*;
import com.rmntim.models.story.Sentence;
import com.rmntim.models.story.Story;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class AppTest {
    @Test
    void mainTest() {
        var story = new Story();

        var smarty = new Captain("Знайка", Sex.MALE);

        story.addCharacter(smarty);

        story.addCharacterGroup(new Group<>(
                List.of(new Shorty("Кубик", Sex.MALE),
                        new Shorty("Тюбик", Sex.MALE),
                        new Shorty("Звездочкин", Sex.MALE),
                        new Shorty("Стекляшкин", Sex.MALE),
                        new Engineer("Клепка", Sex.MALE)
                )
        ));

        story.addSentence(new Sentence(smarty.think(
                "Если они и отправились куда-нибудь, то, безусловно, пошли по этой дорожке"
        )));

        var group = story.getCharacterGroup(0);

        var takeAction = new TakeAction("приспособления для лазания по горам");
        var goAction = new GoAction("вслед за ним к пирамидальной горе");

        var actions = List.of(takeAction, goAction);

        var order = new Order("тотчас", new Communicator("радиотелефон"), actions);

        story.addSentence(new Sentence("Придя к такому умозаключению")
                .then(smarty.giveOrder(group, order)));

        Assertions.assertEquals(story.toString(), """
                "Если они и отправились куда-нибудь, то, безусловно, пошли по этой дорожке", -- подумал Знайка.
                Придя к такому умозаключению, он тотчас отдал по радиотелефону приказ Кубику, Тюбику, Звездочкину, Стекляшкину и инженеру Клепке взять с собой приспособления для лазания по горам и отправляться вслед за ним к пирамидальной горе.""");
    }
}
