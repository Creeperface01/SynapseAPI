package org.itxtech.synapseapi.multiprotocol.protocol1210.protocol;

import cn.nukkit.network.protocol.PlayerHotbarPacket;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.Binary;
import org.itxtech.synapseapi.multiprotocol.PacketDecoder;
import org.itxtech.synapseapi.multiprotocol.ProtocolGroup;

/**
 * @author CreeperFace
 */
public class PlayerHotbar extends PacketDecoder<PlayerHotbarPacket> {

    @Override
    public byte pid() {
        return ProtocolInfo.PLAYER_HOTBAR_PACKET;
    }

    @Override
    public byte[] encode(ProtocolGroup group, PlayerHotbarPacket pk) {
        this.reset();
        this.putUnsignedVarInt(pk.selectedHotbarSlot);
        this.putByte((byte) pk.windowId);

        if (group.ordinal() < ProtocolGroup.PROTOCOL_1213.ordinal()) {
            this.putUnsignedVarInt(pk.slots.length);
            for (int i : pk.slots) {
                this.putUnsignedVarInt(i);
            }
        }

        this.putBoolean(pk.selectHotbarSlot);

        pk.setBuffer(getBuffer());
        return getBuffer();
    }

    @Override
    public void decode(ProtocolGroup group, PlayerHotbarPacket pk) {
        pk.selectedHotbarSlot = (int) this.getUnsignedVarInt();
        pk.windowId = this.getByte();

        if (group.ordinal() < ProtocolGroup.PROTOCOL_1213.ordinal()) {
            int count = (int) this.getUnsignedVarInt();
            pk.slots = new int[count];

            for (int i = 0; i < count; ++i) {
                pk.slots[i] = Binary.signInt((int) this.getUnsignedVarInt());
            }
        }

        pk.selectHotbarSlot = this.getBoolean();
    }
}