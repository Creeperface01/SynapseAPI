package org.itxtech.synapseapi.multiprotocol;

import cn.nukkit.Player;
import lombok.Getter;
import org.itxtech.synapseapi.SynapsePlayer;

/**
 * @author CreeperFace
 */
public enum ProtocolGroup {
    PROTOCOL_11(0, 113, 1),
    PROTOCOL_12(134, 160, 3),
    PROTOCOL_1210(200, 222, 3),
    PROTOCOL_1213(223, 260, 3),
    PROTOCOL_14(261, 269, 3),
    PROTOCOL_15(270, 279, 3),
    PROTOCOL_16(280, 289, 1), //TODO: not entirely right
    PROTOCOL_17(290, 291, 1),
    PROTOCOL_18(310, 313, 1),
    PROTOCOL_19(330, 331, 1),
    PROTOCOL_191(332, 339, 1),
    PROTOCOL_110(340, 350, 1);

    private final int minProtocol;
    private final int maxProtocol;

    @Getter
    private final int bufferOffset;

    ProtocolGroup(int minProtocol, int maxProtocol, int bufferOffset) {
        this.minProtocol = minProtocol;
        this.maxProtocol = maxProtocol;
        this.bufferOffset = bufferOffset;
    }

    public static ProtocolGroup from(int protocol) {
        for (ProtocolGroup group : values()) {
            if (protocol >= group.minProtocol && protocol <= group.maxProtocol) {
                return group;
            }
        }

        return protocol < values()[0].minProtocol ? values()[0] : values()[values().length - 1];
    }

    public static ProtocolGroup getOrDefault(Player p) {
        return p instanceof SynapsePlayer ? ((SynapsePlayer) p).getProtocolGroup() : from(p.getProtocol());
    }
}