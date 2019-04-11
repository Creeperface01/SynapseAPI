package org.itxtech.synapseapi.multiprotocol.protocol12.protocol;

import cn.nukkit.network.protocol.AddEntityPacket;
import cn.nukkit.network.protocol.types.EntityLink;
import org.itxtech.synapseapi.multiprotocol.PacketDecoder;
import org.itxtech.synapseapi.multiprotocol.ProtocolGroup;
import org.itxtech.synapseapi.multiprotocol.protocol12.entity.EntityUtils;

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
        if (group.ordinal() >= ProtocolGroup.PROTOCOL_18.ordinal()) {
            this.putString(EntityUtils.getEntityLegacyId(pk.type));
        } else {
            this.putUnsignedVarInt(pk.type);
        }
        this.putVector3f(pk.x, pk.y, pk.z);
        this.putVector3f(pk.speedX, pk.speedY, pk.speedZ);
        this.putLFloat(pk.pitch);
        this.putLFloat(pk.yaw);

        if (group.ordinal() >= ProtocolGroup.PROTOCOL_15.ordinal()) {
            this.putLFloat(pk.yaw);
        }

        this.putAttributeList(pk.attributes);
        this.putMetadata(pk.metadata, group);
        this.putUnsignedVarInt(pk.links.length);

        for (EntityLink link : pk.links) {
            putEntityLink(link);
        }

        pk.setBuffer(getBuffer());
        return getBuffer();
    }

    @Override
    public void decode(ProtocolGroup group, AddEntityPacket pk) {

    }
}
