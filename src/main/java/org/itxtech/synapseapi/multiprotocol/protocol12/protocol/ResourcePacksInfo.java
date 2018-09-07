package org.itxtech.synapseapi.multiprotocol.protocol12.protocol;

import cn.nukkit.network.protocol.ResourcePacksInfoPacket;
import cn.nukkit.resourcepacks.ResourcePack;
import org.itxtech.synapseapi.multiprotocol.PacketDecoder;
import org.itxtech.synapseapi.multiprotocol.ProtocolGroup;

/**
 * @author CreeperFace
 */
public class ResourcePacksInfo extends PacketDecoder<ResourcePacksInfoPacket> {

    @Override
    public byte pid() {
        return ResourcePacksInfoPacket.NETWORK_ID;
    }

    @Override
    public byte[] encode(ProtocolGroup group, ResourcePacksInfoPacket pk) {
        this.reset();
        this.putBoolean(pk.mustAccept);

        encodePacks(pk.resourcePackEntries, group);
        encodePacks(pk.behaviourPackEntries, group);

        pk.setBuffer(getBuffer());
        return this.getBuffer();
    }

    private void encodePacks(ResourcePack[] packs, ProtocolGroup group) {
        this.putLShort(packs.length);
        for (ResourcePack entry : packs) {
            this.putString(entry.getPackId());
            this.putString(entry.getPackVersion());
            this.putLLong(entry.getPackSize());
            this.putString(""); // encryption key
            this.putString(""); // sub-pack name

            if (group.ordinal() >= ProtocolGroup.PROTOCOL_16.ordinal()) {
                this.putString(""); // content identity
            }
        }
    }

    @Override
    public void decode(ProtocolGroup group, ResourcePacksInfoPacket pk) {
        pk.decode();
    }
}
