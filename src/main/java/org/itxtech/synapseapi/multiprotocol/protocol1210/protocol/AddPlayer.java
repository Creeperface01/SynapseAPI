package org.itxtech.synapseapi.multiprotocol.protocol1210.protocol;

import cn.nukkit.network.protocol.AddPlayerPacket;
import org.itxtech.synapseapi.multiprotocol.PacketDecoder;
import org.itxtech.synapseapi.multiprotocol.ProtocolGroup;

/**
 * @author CreeperFace
 */
public class AddPlayer extends PacketDecoder<AddPlayerPacket> {

    public static final byte NETWORK_ID = 0x0c;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public byte[] encode(ProtocolGroup group, AddPlayerPacket pk) {
        this.reset();
        this.putUUID(pk.uuid);
        this.putString(pk.username);

        if (group.ordinal() >= ProtocolGroup.PROTOCOL_1213.ordinal()) {
            this.putString(""); //third party name
            this.putVarInt(0); //platform id
        }

        this.putEntityUniqueId(pk.entityUniqueId);
        this.putEntityRuntimeId(pk.entityRuntimeId);

        if (group.ordinal() >= ProtocolGroup.PROTOCOL_1213.ordinal()) {
            this.putString(""); //platform chat id
        }

        this.putVector3f(pk.x, pk.y, pk.z);
        this.putVector3f(pk.speedX, pk.speedY, pk.speedZ);
        this.putLFloat(pk.pitch);
        this.putLFloat(pk.yaw); //TODO headrot
        this.putLFloat(pk.yaw);
        this.putSlot(pk.item);

        this.putMetadata(pk.metadata, group);

        pk.setBuffer(getBuffer());
        return getBuffer();
    }

    @Override
    public void decode(ProtocolGroup group, AddPlayerPacket pk) {
        pk.decode();
    }
}
