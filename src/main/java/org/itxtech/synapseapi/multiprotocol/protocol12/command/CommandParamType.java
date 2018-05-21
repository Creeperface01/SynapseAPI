package org.itxtech.synapseapi.multiprotocol.protocol12.command;

import org.itxtech.synapseapi.multiprotocol.ProtocolGroup;

import java.util.EnumMap;
import java.util.Map;

/**
 * @author CreeperFace
 */
public enum CommandParamType {
    INT(0x01),
    FLOAT(0x02),
    VALUE(0x03),
    WILDCARD_INT(0x01, new MapBuilder().put(ProtocolGroup.PROTOCOL_14, 0x04).build()),
    TARGET(0x04, new MapBuilder().put(ProtocolGroup.PROTOCOL_14, 0x05).build()),
    WILDCARD_TARGET(0x04, new MapBuilder().put(ProtocolGroup.PROTOCOL_14, 0x06).build()),

    STRING(0x0d, new MapBuilder().put(ProtocolGroup.PROTOCOL_14, 0x0f).build()),
    POSITION(0x0e, new MapBuilder().put(ProtocolGroup.PROTOCOL_14, 0x10).build()),

    RAWTEXT(0x11, new MapBuilder().put(ProtocolGroup.PROTOCOL_14, 0x15).build()),

    TEXT(0x13, new MapBuilder().put(ProtocolGroup.PROTOCOL_14, 0x13).build()),

    JSON(0x16, new MapBuilder().put(ProtocolGroup.PROTOCOL_14, 0x18).build()),

    COMMAND(0x1d, new MapBuilder().put(ProtocolGroup.PROTOCOL_14, 0x1f).build());

    private static EnumMap<cn.nukkit.command.data.CommandParamType, CommandParamType> LOOKUP = new EnumMap<>(cn.nukkit.command.data.CommandParamType.class);

    static {
        LOOKUP.put(cn.nukkit.command.data.CommandParamType.INT, INT);
        LOOKUP.put(cn.nukkit.command.data.CommandParamType.FLOAT, FLOAT);
        LOOKUP.put(cn.nukkit.command.data.CommandParamType.VALUE, VALUE);
        LOOKUP.put(cn.nukkit.command.data.CommandParamType.TARGET, TARGET);
        LOOKUP.put(cn.nukkit.command.data.CommandParamType.STRING, STRING);
        LOOKUP.put(cn.nukkit.command.data.CommandParamType.POSITION, POSITION);
        LOOKUP.put(cn.nukkit.command.data.CommandParamType.RAWTEXT, RAWTEXT);
        LOOKUP.put(cn.nukkit.command.data.CommandParamType.TEXT, TEXT);
        LOOKUP.put(cn.nukkit.command.data.CommandParamType.JSON, JSON);
        LOOKUP.put(cn.nukkit.command.data.CommandParamType.COMMAND, COMMAND);
    }

    private final int id;
    private final Map<ProtocolGroup, Integer> ids;

    CommandParamType(int id) {
        this(id, new EnumMap<>(ProtocolGroup.class));
    }

    CommandParamType(int id, Map<ProtocolGroup, Integer> ids) {
        this.id = id;
        this.ids = ids;
    }

    public int getId(ProtocolGroup protocol) {
        return ids.getOrDefault(protocol, id);
    }

    public static int getId(cn.nukkit.command.data.CommandParamType param, ProtocolGroup group) {
        return LOOKUP.get(param).getId(group);
    }

    private static class MapBuilder {

        private EnumMap<ProtocolGroup, Integer> map = new EnumMap<>(ProtocolGroup.class);

        public MapBuilder put(ProtocolGroup group, Integer value) {
            map.put(group, value);
            return this;
        }

        Map<ProtocolGroup, Integer> build() {
            return map;
        }
    }
}
