package org.itxtech.synapseapi.multiprotocol.protocol12.protocol;

import cn.nukkit.network.protocol.AnimatePacket;
import cn.nukkit.network.protocol.ProtocolInfo;
import org.itxtech.synapseapi.multiprotocol.PacketDecoder;
import org.itxtech.synapseapi.multiprotocol.ProtocolGroup;

/**
 * @author CreeperFace
 */
public class Animate extends PacketDecoder<AnimatePacket> {

    public static final int NETOWORK_ID = ProtocolInfo.ANIMATE_PACKET;

    @Override
    public byte pid() {
        return 0;
    }

    @Override
    public byte[] encode(ProtocolGroup group, AnimatePacket pk) {
        this.reset();
        this.putVarInt(pk.action.getId());
        this.putEntityRuntimeId(pk.eid);

        if (pk.action == AnimatePacket.Action.ROW_LEFT || pk.action == AnimatePacket.Action.ROW_RIGHT) {
            this.putLFloat(pk.rowingTime);
        }

        pk.setBuffer(getBuffer());
        return getBuffer();
    }

    @Override
    public void decode(ProtocolGroup group, AnimatePacket pk) {
        pk.action = AnimatePacket.Action.fromId(this.getVarInt());
        pk.eid = this.getEntityRuntimeId();

        if (pk.action == AnimatePacket.Action.ROW_LEFT || pk.action == AnimatePacket.Action.ROW_RIGHT) {
            pk.rowingTime = getLFloat();
        }
    }
}
