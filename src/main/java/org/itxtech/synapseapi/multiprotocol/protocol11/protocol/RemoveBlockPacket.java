package org.itxtech.synapseapi.multiprotocol.protocol11.protocol;

import cn.nukkit.math.BlockVector3;

/**
 * @author Nukkit Project Team
 */
public class RemoveBlockPacket extends Packet11 {

    public static final byte NETWORK_ID = ProtocolInfo.REMOVE_BLOCK_PACKET;

    public int x;
    public int y;
    public int z;

    @Override
    public void decode() {
        BlockVector3 v = this.getBlockVector3();
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
    }

    @Override
    public void encode() {
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }
}
