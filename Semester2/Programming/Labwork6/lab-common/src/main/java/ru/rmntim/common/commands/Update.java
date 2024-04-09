package ru.rmntim.common.commands;

import ru.rmntim.common.models.Dragon;
import ru.rmntim.common.network.Response;
import ru.rmntim.common.parsers.DragonParser;

import java.util.List;

public class Update extends Command {
    public static final String NAME = "update";
    public static final String DESCRIPTION = "Update element with given id";

    private final int id;
    private final Dragon dragon;

    public Update(int id, Dragon dragon) {
        this.id = id;
        this.dragon = dragon;
    }

    public int getId() {
        return id;
    }

    public Dragon getDragon() {
        return dragon;
    }

    @Override
    public Response accept(Command.Visitor visitor) {
        return visitor.visit(this);
    }

    public static Update create(List<String> args) {
        if (args.size() != 1) {
            throw new IllegalArgumentException(NAME + " needs id as argument");
        }
        try {
            var id = Integer.parseInt(args.get(0));
            var dragon = DragonParser.parse();
            return new Update(id, dragon);
        } catch (NumberFormatException e) {
            System.out.println("Id must be a valid integer");
            return null;
        }
    }
}
