package org.itxtech.synapseapi.multiprotocol.protocol1210.protocol;

import cn.nukkit.network.protocol.AddEntityPacket;
import cn.nukkit.network.protocol.AddItemEntityPacket;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.Binary;
import org.itxtech.synapseapi.multiprotocol.PacketDecoder;
import org.itxtech.synapseapi.multiprotocol.ProtocolGroup;
import org.itxtech.synapseapi.utils.ClassUtils;

/**
 * @author CreeperFace
 */
public class AddItemEntity extends PacketDecoder<AddItemEntityPacket> {

    public static final byte NETWORK_ID = 0x0f;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public byte[] encode(ProtocolGroup group, cn.nukkit.network.protocol.AddItemEntityPacket pk) {
        this.reset();
        this.putEntityUniqueId(pk.entityUniqueId);
        this.putEntityRuntimeId(pk.entityRuntimeId);
        this.putSlot(pk.item);
        this.putVector3f(pk.x, pk.y, pk.z);
        this.putVector3f(pk.speedX, pk.speedY, pk.speedZ);
        this.put(Binary.writeMetadata(pk.metadata));

        if (group.ordinal() >= ProtocolGroup.PROTOCOL_1210.ordinal()) {
            this.putBoolean(false);
        }

        pk.setBuffer(getBuffer());
        return getBuffer();
    }

    @Override
    public void decode(ProtocolGroup group, cn.nukkit.network.protocol.AddItemEntityPacket pk) {
        pk.decode();
    }
}
