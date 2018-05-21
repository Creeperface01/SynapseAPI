package org.itxtech.synapseapi.multiprotocol.protocol12.protocol;

import cn.nukkit.network.protocol.AddEntityPacket;
import org.itxtech.synapseapi.multiprotocol.PacketDecoder;
import org.itxtech.synapseapi.multiprotocol.ProtocolGroup;

/**
 * @author CreeperFace
 */
public class AddEntity extends PacketDecoder<AddEntityPacket> {

    @Override
    public byte pid() {
        return AddEntityPacket.NETWORK_ID;
    }

    @Override
    public byte[] encode(ProtocolGroup group, AddEntityPacket pk) {
        this.reset();
        this.putEntityUniqueId(pk.entityUniqueId);
        this.putEntityRuntimeId(pk.entityRuntimeId);
        this.putUnsignedVarInt(pk.type);
        this.putVector3f(pk.x, pk.y, pk.z);
        this.putVector3f(pk.speedX, pk.speedY, pk.speedZ);
        this.putLFloat(pk.pitch);
        this.putLFloat(pk.yaw);
        this.putAttributeList(pk.attributes);
        this.putMetadata(pk.metadata, group);
        this.putUnsignedVarInt(pk.links.length);
        for (Object[] link : pk.links) {
            this.putVarLong((long) link[0]);
            this.putVarLong((long) link[1]);
            this.putByte((byte) link[2]);
        }

        pk.setBuffer(getBuffer());
        return getBuffer();
    }

    @Override
    public void decode(ProtocolGroup group, AddEntityPacket pk) {

    }
}
