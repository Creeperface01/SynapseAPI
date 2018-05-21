package org.itxtech.synapseapi.multiprotocol.protocol12.protocol;

import cn.nukkit.command.data.*;
import cn.nukkit.network.protocol.AvailableCommandsPacket;
import cn.nukkit.network.protocol.ProtocolInfo;
import org.itxtech.synapseapi.multiprotocol.PacketDecoder;
import org.itxtech.synapseapi.multiprotocol.ProtocolGroup;

import java.util.*;
import java.util.stream.Collectors;

import static cn.nukkit.network.protocol.AvailableCommandsPacket.ARG_FLAG_ENUM;
import static cn.nukkit.network.protocol.AvailableCommandsPacket.ARG_FLAG_VALID;

/**
 * @author CreeperFace
 */
public class AvailableCommands extends PacketDecoder<AvailableCommandsPacket> {

    @Override
    public byte pid() {
        return ProtocolInfo.AVAILABLE_COMMANDS_PACKET;
    }

    @Override
    public void decode(ProtocolGroup group, AvailableCommandsPacket pk) {
        pk.commands = new HashMap<>();

        List<String> enumValues = new ArrayList<>();
        List<String> postFixes = new ArrayList<>();
        List<CommandEnum> enums = new ArrayList<>();

        int len = (int) getUnsignedVarInt();
        while (len-- > 0) {
            enumValues.add(getString());
        }

        len = (int) getUnsignedVarInt();
        while (len-- > 0) {
            postFixes.add(getString());
        }

        len = (int) getUnsignedVarInt();
        while (len-- > 0) {
            String enumName = getString();
            int enumLength = (int) getUnsignedVarInt();

            List<String> values = new ArrayList<>();

            while (enumLength-- > 0) {
                int index;

                if (enums.size() < 256) {
                    index = getByte();
                } else if (enums.size() < 65536) {
                    index = getShort();
                } else {
                    index = getLInt();
                }

                String enumValue;

                if (index < 0 || (enumValue = enumValues.get(index)) == null) {
                    throw new IllegalStateException("Enum value not found for index " + index);
                }

                values.add(enumValue);
            }

            enums.add(new CommandEnum(enumName, values));
        }

        len = (int) getUnsignedVarInt();

        while (len-- > 0) {
            String name = getString();
            String desctiption = getString();
            int flags = getByte();
            int permission = getByte();
            CommandEnum alias = null;

            int aliasIndex = getLInt();
            if (aliasIndex >= 0) {
                alias = enums.get(aliasIndex);
            }

            Map<String, CommandOverload> overloads = new HashMap<>();

            int length = (int) getUnsignedVarInt();
            while (length-- > 0) {
                CommandOverload overload = new CommandOverload();

                int paramLen = (int) getUnsignedVarInt();

                overload.input.parameters = new CommandParameter[paramLen];
                for (int i = 0; i < paramLen; i++) {
                    String paramName = getString();
                    int type = getLInt();
                    boolean optional = getBoolean();

                    CommandParameter parameter = new CommandParameter(paramName, optional);
                    cn.nukkit.command.data.CommandParamType paramType = cn.nukkit.command.data.CommandParamType.RAWTEXT;

                    if ((type & ARG_FLAG_ENUM) != 0) {
                        int index = type & 0xffff;
                        parameter.enumData = enums.get(index);
                    } else if ((type & ARG_FLAG_VALID) == 0) {
                        int paramTypeIndex;

                        if (((type >> 24) & ARG_FLAG_VALID) > 0) {
                            parameter.postFix = postFixes.get(type & 0xffff);

                            paramTypeIndex = (type >> 24) & 0xffff;
                        } else {
                            paramTypeIndex = type & 0xffff;
                        }

                        paramType = cn.nukkit.command.data.CommandParamType.of(paramTypeIndex);
                    }

                    parameter.type = paramType;
                    overload.input.parameters[i] = parameter;
                }

                overloads.put(Integer.toString(length), overload);
            }

            CommandData data = new CommandData();
            data.aliases = alias;
            data.overloads = overloads;
            data.description = desctiption;
            data.flags = flags;
            data.permission = permission;

            CommandDataVersions versions = new CommandDataVersions();
            versions.versions.add(data);

            pk.commands.put(name, versions);
        }
    }

    @Override
    public byte[] encode(ProtocolGroup group, AvailableCommandsPacket pk) {
        this.reset();

        LinkedHashSet<String> enumValues = new LinkedHashSet<>();
        LinkedHashSet<String> postFixes = new LinkedHashSet<>();
        LinkedHashSet<CommandEnum> enums = new LinkedHashSet<>();

        pk.commands.forEach((name, data) -> {
            CommandData cmdData = data.versions.get(0);

            if (cmdData.aliases != null) {
                enums.add(new CommandEnum(cmdData.aliases.getName(), cmdData.aliases.getValues()));

                enumValues.addAll(cmdData.aliases.getValues());
            }

            for (CommandOverload overload : cmdData.overloads.values()) {
                for (CommandParameter parameter : overload.input.parameters) {
                    if (parameter.enumData != null) {
                        enums.add(new CommandEnum(parameter.enumData.getName(), parameter.enumData.getValues()));

                        enumValues.addAll(parameter.enumData.getValues());
                    }

                    if (parameter.postFix != null) {
                        postFixes.add(parameter.postFix);
                    }
                }
            }
        });

        List<String> enumIndexes = new ArrayList<>(enumValues);
        List<String> enumDataIndexes = enums.stream().map(CommandEnum::getName).collect(Collectors.toList());
        List<String> fixesIndexes = new ArrayList<>(postFixes);

        this.putUnsignedVarInt(enumValues.size());
        for (String enumValue : enumValues) {
            putString(enumValue);
        }

        this.putUnsignedVarInt(postFixes.size());
        for (String postFix : postFixes) {
            putString(postFix);
        }

        this.putUnsignedVarInt(enums.size());
        enums.forEach((cmdEnum) -> {
            putString(cmdEnum.getName());

            List<String> values = cmdEnum.getValues();
            putUnsignedVarInt(values.size());

            for (String val : values) {
                int i = enumIndexes.indexOf(val);

                if (i < 0) {
                    throw new IllegalStateException("Enum value '" + val + "' not found");
                }

                if (enums.size() < 256) {
                    putByte((byte) i);
                } else if (enums.size() < 65536) {
                    putShort(i);
                } else {
                    putLInt(i);
                }
            }
        });

        putUnsignedVarInt(pk.commands.size());

        pk.commands.forEach((name, cmdData) -> {
            CommandData data = cmdData.versions.get(0);

            putString(name);
            putString(data.description);
            putByte((byte) data.flags);
            putByte((byte) data.permission);

            putLInt(data.aliases == null ? -1 : enumDataIndexes.indexOf(data.aliases.getName()));

            putUnsignedVarInt(data.overloads.size());
            for (CommandOverload overload : data.overloads.values()) {
                putUnsignedVarInt(overload.input.parameters.length);

                for (CommandParameter parameter : overload.input.parameters) {
                    putString(parameter.name);

                    int type;
                    if (parameter.enumData != null) {
                        type = ARG_FLAG_ENUM | ARG_FLAG_VALID | enumDataIndexes.indexOf(parameter.enumData.getName());
                    } else if (parameter.postFix != null) {
                        int i = fixesIndexes.indexOf(parameter.postFix);
                        if (i < 0) {
                            throw new IllegalStateException("Postfix '" + parameter.postFix + "' isn't in postfix array");
                        }


                        type = (ARG_FLAG_VALID | parameter.type.getId()) << 24 | i;
                    } else {
                        type = parameter.type.getId() | ARG_FLAG_VALID;
                    }

                    putLInt(type);
                    putBoolean(parameter.optional);
                }
            }
        });

        pk.setBuffer(getBuffer());
        return getBuffer();
    }
}
