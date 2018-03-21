package org.itxtech.synapseapi.multiprotocol.protocol11.protocol;

import cn.nukkit.level.format.FullChunk;
import cn.nukkit.network.protocol.DataPacket;
import org.itxtech.synapseapi.utils.ClassUtils;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class FullChunkDataPacket extends Packet11 {
    public static final byte NETWORK_ID = ProtocolInfo.FULL_CHUNK_DATA_PACKET;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    public int chunkX;
    public int chunkZ;
    public byte[] data;

    @Override
    public DataPacket fromDefault(DataPacket pk) {
        ClassUtils.requireInstance(pk, cn.nukkit.network.protocol.FullChunkDataPacket.class);

        cn.nukkit.network.protocol.FullChunkDataPacket packet = (cn.nukkit.network.protocol.FullChunkDataPacket) pk;
        this.chunkX = packet.chunkX;
        this.chunkZ = packet.chunkZ;
        this.data = packet.data;

        return this;
    }

    @Override
    public DataPacket toDefault() {
        cn.nukkit.network.protocol.FullChunkDataPacket pk = new cn.nukkit.network.protocol.FullChunkDataPacket();
        pk.chunkX = this.chunkX;
        pk.chunkZ = this.chunkZ;
        pk.data = this.data;

        return pk;
    }

    @Override
    public void decode() {

    }

    @Override
    public void encode() {
        this.reset();
        this.putVarInt(this.chunkX);
        this.putVarInt(this.chunkZ);
        this.putByteArray(this.data);
    }
}
