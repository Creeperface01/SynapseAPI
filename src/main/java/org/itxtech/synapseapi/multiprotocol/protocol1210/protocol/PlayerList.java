package org.itxtech.synapseapi.multiprotocol.protocol1210.protocol;

import cn.nukkit.entity.data.Skin.Cape;
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

            if (pk.type == cn.nukkit.network.protocol.PlayerListPacket.TYPE_ADD) {
                this.putVarLong(entry.entityId);
                this.putString(entry.name);

                if (group.ordinal() >= ProtocolGroup.PROTOCOL_1213.ordinal()) {
                    this.putString(""); //third party name
                    this.putVarInt(0); //platform id
                }

                this.putSkin(entry.skin, group);
                if (group.ordinal() >= ProtocolGroup.PROTOCOL_1213.ordinal()) {
                    this.putLInt(1); //always 1
                }
                Cape cape = entry.skin.getCape();
                if (group.ordinal() >= ProtocolGroup.PROTOCOL_1213.ordinal()) {
                    if (cape.getData().length > 0) {
                        putLInt(1);
                        this.putByteArray(cape.getData());
                    } else {
                        putLInt(0);
                    }
                } else {
                    this.putByteArray(cape.getData());
                }

                this.putString(entry.geometryModel);
                this.putByteArray(entry.geometryData);
                this.putString(entry.xboxUserId);

                if (group.ordinal() >= ProtocolGroup.PROTOCOL_1210.ordinal()) {
                    this.putString(""); //platform chat id
                }
            } else {
                putString("");
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
