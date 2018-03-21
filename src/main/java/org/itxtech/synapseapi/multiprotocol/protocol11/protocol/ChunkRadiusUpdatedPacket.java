package org.itxtech.synapseapi.multiprotocol.protocol11.protocol;

import cn.nukkit.network.protocol.DataPacket;
import org.itxtech.synapseapi.utils.ClassUtils;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class ChunkRadiusUpdatedPacket extends Packet11 {

    public static final byte NETWORK_ID = ProtocolInfo.CHUNK_RADIUS_UPDATED_PACKET;

    public int radius;

    public ChunkRadiusUpdatedPacket fromDefault(DataPacket pkk) {
        ClassUtils.requireInstance(pkk, cn.nukkit.network.protocol.ChunkRadiusUpdatedPacket.class);

        cn.nukkit.network.protocol.ChunkRadiusUpdatedPacket pk = (cn.nukkit.network.protocol.ChunkRadiusUpdatedPacket) pkk;
        this.radius = pk.radius;
        return this;
    }

    public cn.nukkit.network.protocol.ChunkRadiusUpdatedPacket toDefault() {
        cn.nukkit.network.protocol.ChunkRadiusUpdatedPacket pk = new cn.nukkit.network.protocol.ChunkRadiusUpdatedPacket();
        pk.radius = radius;
        return pk;
    }

    @Override
    public void decode() {
        this.radius = this.getVarInt();
    }

    @Override
    public void encode() {
        super.reset();
        this.putVarInt(this.radius);
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    public static Class<? extends DataPacket> getDefaultPacket() {
        return cn.nukkit.network.protocol.ChunkRadiusUpdatedPacket.class;
    }
}
