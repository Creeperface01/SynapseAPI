package org.itxtech.synapseapi.multiprotocol.protocol11.chunk;

import cn.nukkit.level.Level;
import cn.nukkit.level.Level.ChunkCacher;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.utils.Binary;
import org.itxtech.synapseapi.multiprotocol.ProtocolGroup;
import org.itxtech.synapseapi.multiprotocol.protocol11.protocol.FullChunkDataPacket;

import java.util.EnumMap;
import java.util.Map;

/**
 * @author CreeperFace
 */
public class ChunkCompressor implements ChunkCacher {

    public static void init() {
        Level.setChunkCacher(new ChunkCompressor());
    }

    @Override
    public ChunkResult getData(int x, int z, byte[] payload, byte[] payload11) {

        Map<ProtocolGroup, DataPacket> packets = new EnumMap<>(ProtocolGroup.class);

        {
            cn.nukkit.network.protocol.FullChunkDataPacket packet = new cn.nukkit.network.protocol.FullChunkDataPacket();
            packet.chunkX = x;
            packet.chunkZ = z;
            packet.data = payload;

            packets.put(ProtocolGroup.PROTOCOL_12, compress(packet, ProtocolGroup.PROTOCOL_12));
            packets.put(ProtocolGroup.PROTOCOL_16, compress(packet, ProtocolGroup.PROTOCOL_16));
        }

        {
            FullChunkDataPacket packet = new FullChunkDataPacket();
            packet.chunkX = x;
            packet.chunkZ = z;
            packet.data = payload11;

            packets.put(ProtocolGroup.PROTOCOL_11, compress(packet, ProtocolGroup.PROTOCOL_11));
        }

        return new ChunkResult(packets, x, z);
    }

    private DataPacket compress(DataPacket pk, ProtocolGroup protocol) {
        pk.encode();
        pk.isEncoded = true;
        pk.setBuffer(Binary.appendBytes(protocol.ordinal() < ProtocolGroup.PROTOCOL_16.ordinal() ? new byte[]{pk.pid()} : Binary.writeUnsignedVarInt(pk.pid()), Binary.subBytes(pk.getBuffer(), protocol.getBufferOffset())));

//        BatchPacket batch = new BatchPacket(); //don't need to compile if nemisys is on local network
//        byte[][] batchPayload = new byte[2][];
//        byte[] buf = pk.getBuffer();
//        batchPayload[0] = Binary.writeUnsignedVarInt(buf.length);
//        batchPayload[1] = buf;
//        byte[] data = Binary.appendBytes(batchPayload);
//        try {
//            batch.payload = Zlib.deflate(data, 4);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }

        return pk;
    }
}
