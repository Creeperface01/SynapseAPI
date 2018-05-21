package org.itxtech.synapseapi.multiprotocol.protocol12.protocol;

import cn.nukkit.math.Vector3f;
import cn.nukkit.network.protocol.LevelEventPacket;
import org.itxtech.synapseapi.multiprotocol.PacketDecoder;
import org.itxtech.synapseapi.multiprotocol.ProtocolGroup;
import org.itxtech.synapseapi.multiprotocol.protocol12.util.GlobalBlockPalette;

/**
 * @author CreeperFace
 */
public class LevelEvent extends PacketDecoder<LevelEventPacket> {

    @Override
    public byte pid() {
        return LevelEventPacket.NETWORK_ID;
    }

    @Override
    public byte[] encode(ProtocolGroup group, LevelEventPacket pk) {
        reset();
        int data = pk.data;

        if (group.ordinal() >= ProtocolGroup.PROTOCOL_1213.ordinal()) {
            if (pk.evid == LevelEventPacket.EVENT_PARTICLE_DESTROY) {
                int id = data & 0xff;
                int damage = (data >> 8) & 0xff;

                data = GlobalBlockPalette.getOrCreateRuntimeId(group, id, damage);
            } else if (pk.evid == LevelEventPacket.EVENT_PARTICLE_PUNCH_BLOCK) {
                int id = data & 0xff;
                int damage = (data >> 8) & 0xff;
                int face = (data >> 16) & 0xff;

                data = GlobalBlockPalette.getOrCreateRuntimeId(group, id, damage | face << 24);
            }
        }

        putVarInt(pk.evid);
        putVector3f(pk.x, pk.y, pk.z);
        putVarInt(data);

        pk.setBuffer(getBuffer());
        return getBuffer();
    }

    @Override
    public void decode(ProtocolGroup group, LevelEventPacket pk) {
        pk.evid = getVarInt();

        Vector3f pos = getVector3f();
        pk.x = pos.x;
        pk.y = pos.y;
        pk.z = pos.z;
        pk.data = getVarInt();
    }
}
