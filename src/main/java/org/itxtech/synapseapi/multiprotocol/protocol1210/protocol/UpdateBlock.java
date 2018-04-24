package org.itxtech.synapseapi.multiprotocol.protocol1210.protocol;

import cn.nukkit.block.GlobalBlockPalette;
import cn.nukkit.math.BlockVector3;
import cn.nukkit.network.protocol.UpdateBlockPacket;
import org.itxtech.synapseapi.multiprotocol.PacketDecoder;
import org.itxtech.synapseapi.multiprotocol.ProtocolGroup;

/**
 * @author CreeperFace
 */
public class UpdateBlock extends PacketDecoder<UpdateBlockPacket> {

    @Override
    public byte pid() {
        return UpdateBlockPacket.NETWORK_ID;
    }

    @Override
    public byte[] encode(ProtocolGroup group, UpdateBlockPacket pk) {
        reset();
        this.putBlockVector3(pk.x, pk.y, pk.z);
        if (group.ordinal() >= ProtocolGroup.PROTOCOL_1213.ordinal()) {
            putUnsignedVarInt(GlobalBlockPalette.getOrCreateRuntimeId(pk.blockId, pk.blockData));
            putUnsignedVarInt(pk.flags);
        } else {
            putUnsignedVarInt(pk.blockId);
            putUnsignedVarInt(((0xb << 4) | pk.blockData & 0x0f));
        }

        pk.setBuffer(getBuffer());
        return getBuffer();
    }

    @Override
    public void decode(ProtocolGroup group, UpdateBlockPacket pk) {
        BlockVector3 pos = getBlockVector3();
        pk.x = pos.x;
        pk.y = pos.y;
        pk.z = pos.z;

        /*if(group.ordinal() >= ProtocolGroup.PROTOCOL_1213.ordinal()) {
            putUnsignedVarInt(GlobalBlockPalette.getOrCreateRuntimeId(pk.blockId, pk.blockData));
        } else {
            this.putUnsignedVarInt((long) pk.blockId);
            this.putUnsignedVarInt((long) (176 | pk.blockData & 15));
        }*/
    }
}
