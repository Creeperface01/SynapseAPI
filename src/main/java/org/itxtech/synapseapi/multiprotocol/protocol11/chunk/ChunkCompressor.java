package org.itxtech.synapseapi.multiprotocol.protocol11.chunk;

import cn.nukkit.Server;
import cn.nukkit.level.Level;
import cn.nukkit.level.Level.ChunkCacher;
import cn.nukkit.network.protocol.BatchPacket;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.utils.Binary;
import cn.nukkit.utils.Zlib;
import org.itxtech.synapseapi.multiprotocol.protocol11.protocol.FullChunkDataPacket;
import org.itxtech.synapseapi.multiprotocol.protocol11.protocol.ProtocolInfo;

/**
 * @author CreeperFace
 */
public class ChunkCompressor implements ChunkCacher {

    public void init() {
        Level.setChunkCacher(this);
    }

    @Override
    public BatchPacket getData(int x, int z, byte[] payload, int protocol) {
        DataPacket pk;

        if (protocol <= ProtocolInfo.CURRENT_PROTOCOL) {
            FullChunkDataPacket packet = new FullChunkDataPacket();
            packet.chunkX = x;
            packet.chunkZ = z;
            packet.data = payload;

            pk = packet;
        } else {
            cn.nukkit.network.protocol.FullChunkDataPacket packet = new cn.nukkit.network.protocol.FullChunkDataPacket();
            packet.chunkX = x;
            packet.chunkZ = z;
            packet.data = payload;

            pk = packet;
        }

        pk.encode();
        pk.isEncoded = true;

        BatchPacket batch = new BatchPacket();
        byte[][] batchPayload = new byte[2][];
        byte[] buf = pk.getBuffer();
        batchPayload[0] = Binary.writeUnsignedVarInt(buf.length);
        batchPayload[1] = buf;
        byte[] data = Binary.appendBytes(batchPayload);
        try {
            batch.payload = Zlib.deflate(data, Server.getInstance().networkCompressionLevel);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return batch;
    }
}
