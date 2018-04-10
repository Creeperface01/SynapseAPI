package org.itxtech.synapseapi.multiprotocol.protocol1210.protocol;

import cn.nukkit.network.protocol.AddItemEntityPacket;
import org.itxtech.synapseapi.multiprotocol.PacketDecoder;
import org.itxtech.synapseapi.multiprotocol.ProtocolGroup;

/**
 * @author CreeperFace
 */
public class AddItemEntity extends PacketDecoder<AddItemEntityPacket> {

    public static final byte NETWORK_ID = AddItemEntityPacket.NETWORK_ID;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public byte[] encode(ProtocolGroup group, AddItemEntityPacket pk) {
        this.reset();
        this.putEntityUniqueId(pk.entityUniqueId);
        this.putEntityRuntimeId(pk.entityRuntimeId);
        this.putSlot(pk.item);
        this.putVector3f(pk.x, pk.y, pk.z);
        this.putVector3f(pk.speedX, pk.speedY, pk.speedZ);
        this.putMetadata(pk.metadata, group);

        if (group.ordinal() >= ProtocolGroup.PROTOCOL_1213.ordinal()) {
            this.putBoolean(false); //from fishing
        }

        pk.setBuffer(getBuffer());
        return getBuffer();
    }

    @Override
    public void decode(ProtocolGroup group, AddItemEntityPacket pk) {
        pk.decode();
    }
}
