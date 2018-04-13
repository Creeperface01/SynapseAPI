package org.itxtech.synapseapi.multiprotocol.protocol11.command;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.data.CommandOverload;

import java.util.HashMap;
import java.util.Map;

public class CommandData11 implements Cloneable {

    public String[] aliases = new String[0];
    public String description = "description";
    public Map<String, CommandOverload> overloads = new HashMap<>();
    public String permission = "any";

    @Override
    public CommandData11 clone() {
        try {
            return (CommandData11) super.clone();
        } catch (Exception e) {
            return new CommandData11();
        }
    }

    public static CommandDataVersions11 generate(Command command, Player p) {
        if (!command.testPermission(p)) {
            return null;
        }

        CommandData11 customData = new CommandData11();
        customData.aliases = command.getAliases();
        customData.description = p.getServer().getLanguage().translateString(command.getDescription());
        customData.permission = command.getPermission() != null && p.hasPermission(command.getPermission()) ? "any" : "false";
        command.getCommandParameters().forEach((key, par) -> {
            CommandOverload overload = new CommandOverload();
            overload.input.parameters = par;
            customData.overloads.put(key, overload);
        });

        if (customData.overloads.size() == 0) customData.overloads.put("default", new CommandOverload());
        CommandDataVersions11 versions = new CommandDataVersions11();
        versions.versions.add(customData);
        return versions;
    }
}
