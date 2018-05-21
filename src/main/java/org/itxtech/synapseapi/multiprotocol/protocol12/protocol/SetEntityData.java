package org.itxtech.synapseapi.multiprotocol.protocol12.protocol;

import cn.nukkit.network.protocol.SetEntityDataPacket;
import org.itxtech.synapseapi.multiprotocol.PacketDecoder;
import org.itxtech.synapseapi.multiprotocol.ProtocolGroup;

/**
 * @author CreeperFace
 */
public class SetEntityData extends PacketDecoder<SetEntityDataPacket> {

    @Override
    public byte pid() {
        return SetEntityDataPacket.NETWORK_ID;
    }

    @Override
    public byte[] encode(ProtocolGroup group, SetEntityDataPacket pk) {
        reset();
        this.putEntityRuntimeId(pk.eid);
        this.putMetadata(pk.metadata, group);

        pk.setBuffer(getBuffer());
        return getBuffer();
    }

    @Override
    public void decode(ProtocolGroup group, SetEntityDataPacket pk) {

    }
}
