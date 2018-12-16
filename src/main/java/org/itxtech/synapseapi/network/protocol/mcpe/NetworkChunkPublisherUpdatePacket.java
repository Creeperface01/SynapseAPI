package org.itxtech.synapseapi.network.protocol.mcpe;

import cn.nukkit.math.BlockVector3;
import cn.nukkit.network.protocol.DataPacket;

/**
 * @author CreeperFace
 */
public class NetworkChunkPublisherUpdatePacket extends DataPacket {

    public static final byte NETWORK_ID = 0x79;

    public BlockVector3 position;
    public int radius;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public void decode() {
        this.position = this.getSignedBlockPosition();
        this.radius = (int) this.getUnsignedVarInt();
    }

    @Override
    public void encode() {
        this.reset();
        this.putSignedBlockPosition(position);
        this.putUnsignedVarInt(radius);
    }
}
