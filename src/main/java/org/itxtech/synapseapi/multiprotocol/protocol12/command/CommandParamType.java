package org.itxtech.synapseapi.multiprotocol.protocol12.command;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
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
    WILDCARD_INT(0x01),
    TARGET(0x04, new MapBuilder()
            .put(ProtocolGroup.PROTOCOL_14, 0x05)
            .put(ProtocolGroup.PROTOCOL_18, 0x06)
            .build()
    ),
    WILDCARD_TARGET(0x04, new MapBuilder()
            .put(ProtocolGroup.PROTOCOL_16, 0x06)
            .put(ProtocolGroup.PROTOCOL_18, 0x07)
            .build()
    ),

    STRING(0x0d, new MapBuilder()
            .put(ProtocolGroup.PROTOCOL_14, 0x0f)
            .put(ProtocolGroup.PROTOCOL_18, 0x1a)
            .put(ProtocolGroup.PROTOCOL_19, 28)
            .put(ProtocolGroup.PROTOCOL_111, 27)
            .build()
    ),

    POSITION(0x0e, new MapBuilder()
            .put(ProtocolGroup.PROTOCOL_14, 0x10)
            .put(ProtocolGroup.PROTOCOL_18, 0x1c)
            .put(ProtocolGroup.PROTOCOL_19, 30)
            .put(ProtocolGroup.PROTOCOL_111, 29)
            .build()
    ),

    RAWTEXT(0x11, new MapBuilder()
            .put(ProtocolGroup.PROTOCOL_14, 0x15)
            .put(ProtocolGroup.PROTOCOL_18, 0x21)
            .put(ProtocolGroup.PROTOCOL_19, 35)
            .put(ProtocolGroup.PROTOCOL_111, 34)
            .build()
    ),

    TEXT(0x13, new MapBuilder() //also message
            .put(ProtocolGroup.PROTOCOL_14, 0x13)
            .put(ProtocolGroup.PROTOCOL_18, 0x1f)
            .put(ProtocolGroup.PROTOCOL_19, 33)
            .put(ProtocolGroup.PROTOCOL_111, 32)
            .build()
    ),

    JSON(0x16, new MapBuilder()
            .put(ProtocolGroup.PROTOCOL_14, 0x18)
            .put(ProtocolGroup.PROTOCOL_18, 0x24)
            .put(ProtocolGroup.PROTOCOL_19, 38)
            .put(ProtocolGroup.PROTOCOL_111, 37)
            .build()
    ),

    COMMAND(0x1d, new MapBuilder()
            .put(ProtocolGroup.PROTOCOL_14, 0x1f)
            .put(ProtocolGroup.PROTOCOL_18, 0x2b)
            .put(ProtocolGroup.PROTOCOL_19, 45)
            .put(ProtocolGroup.PROTOCOL_111, 44)
            .build()
    );

    private static EnumMap<cn.nukkit.command.data.CommandParamType, CommandParamType> LOOKUP = new EnumMap<>(cn.nukkit.command.data.CommandParamType.class);

    private static EnumMap<ProtocolGroup, Int2ObjectMap<CommandParamType>> PROTOCOL_LOOKUP = new EnumMap<>(ProtocolGroup.class);

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

        ProtocolGroup[] protocols = ProtocolGroup.values();
        for (CommandParamType value : values()) {
            int lastId = value.id;

            for (ProtocolGroup protocol : protocols) {
                Integer newId;

                if ((newId = value.ids.get(protocol)) != null) {
                    lastId = newId;
                }

                Int2ObjectMap<CommandParamType> map = PROTOCOL_LOOKUP.get(protocol);

                if(map == null) {
                    map = new Int2ObjectOpenHashMap<>();
                    PROTOCOL_LOOKUP.put(protocol, map);
                }

                map.put(lastId, value);
            }
        }
    }

    private final int id;
    private final EnumMap<ProtocolGroup, Integer> ids;

    CommandParamType(int id) {
        this(id, new EnumMap<>(ProtocolGroup.class));
    }

    CommandParamType(int id, EnumMap<ProtocolGroup, Integer> ids) {
        this.id = id;
        this.ids = ids;
    }

    public int getId(ProtocolGroup protocol) {
        return ids.getOrDefault(protocol, id);
    }

    public static int getId(cn.nukkit.command.data.CommandParamType param, ProtocolGroup group) {
        return LOOKUP.get(param).getId(group);
    }

    public static CommandParamType from(int id, ProtocolGroup protocol) {
        return PROTOCOL_LOOKUP.get(protocol).get(id);
    }

    private static class MapBuilder {

        private EnumMap<ProtocolGroup, Integer> map = new EnumMap<>(ProtocolGroup.class);

        public MapBuilder put(ProtocolGroup group, Integer value) {
            map.put(group, value);
            return this;
        }

        EnumMap<ProtocolGroup, Integer> build() {
            return map;
        }
    }
}
