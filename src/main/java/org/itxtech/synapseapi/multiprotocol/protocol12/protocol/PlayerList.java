package org.itxtech.synapseapi.multiprotocol.protocol12.protocol;

import cn.nukkit.network.protocol.PlayerListPacket;
import cn.nukkit.network.protocol.PlayerListPacket.Entry;
import org.itxtech.synapseapi.multiprotocol.PacketDecoder;
import org.itxtech.synapseapi.multiprotocol.ProtocolGroup;

/**
 * @author CreeperFace
 */
public class PlayerList extends PacketDecoder<PlayerListPacket> {

    public static final byte NETWORK_ID = 0x3f;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public byte[] encode(ProtocolGroup group, PlayerListPacket pk) {
        this.reset();
        this.putByte(pk.type);
        this.putUnsignedVarInt(pk.entries.length);
        for (Entry entry : pk.entries) {
            this.putUUID(entry.uuid);

            if (pk.type == PlayerListPacket.TYPE_ADD) {
                this.putVarLong(entry.entityId);
                this.putString(entry.name);

                if (group.ordinal() >= ProtocolGroup.PROTOCOL_1213.ordinal()) {
                    this.putString(""); //third party name
                    this.putVarInt(0); //platform id
                }

                this.putSkin(entry.skin, group);

                this.putString(entry.geometryModel);
                this.putByteArray(entry.geometryData);
                this.putString(entry.xboxUserId);

                if (group.ordinal() >= ProtocolGroup.PROTOCOL_1213.ordinal()) {
                    this.putString(""); //platform chat id
                }
            }
        }

        pk.setBuffer(getBuffer());
        return getBuffer();
    }

    @Override
    public void decode(ProtocolGroup group, PlayerListPacket pk) {
        pk.decode();
    }
}
