package org.itxtech.synapseapi.multiprotocol.protocol11.chunk;

import cn.nukkit.level.format.ChunkRequestTask;
import cn.nukkit.network.protocol.DataPacket;
import lombok.Getter;
import org.itxtech.synapseapi.multiprotocol.ProtocolGroup;

import java.util.Map;

/**
 * @author CreeperFace
 */
public class ChunkResult extends ChunkRequestTask.Result {

    @Getter
    private Map<ProtocolGroup, DataPacket> payloads;

    public ChunkResult(DataPacket payload, DataPacket payload11, int x, int z) {
        super(payload, payload11, x, z);
        throw new UnsupportedOperationException("This class can't be initialized with this constructor");
    }

    public ChunkResult(Map<ProtocolGroup, DataPacket> payloads, int x, int z) {
        super(null, null, x, z);
        this.payloads = payloads;
    }
}
