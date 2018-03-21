package org.itxtech.synapseapi.multiprotocol.protocol11.protocol;

import cn.nukkit.network.protocol.DataPacket;
import org.itxtech.synapseapi.utils.ClassUtils;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class BlockEventPacket extends Packet11 {
    public static final byte NETWORK_ID = ProtocolInfo.BLOCK_EVENT_PACKET;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    public int x;
    public int y;
    public int z;
    public int case1;
    public int case2;

    public BlockEventPacket fromDefault(DataPacket pkk) {
        ClassUtils.requireInstance(pkk, cn.nukkit.network.protocol.BlockEventPacket.class);

        cn.nukkit.network.protocol.BlockEventPacket pk = (cn.nukkit.network.protocol.BlockEventPacket) pkk;
        this.x = pk.x;
        this.y = pk.y;
        this.z = pk.z;
        this.case1 = pk.case1;
        this.case2 = pk.case2;
        return this;
    }

    public cn.nukkit.network.protocol.BlockEventPacket toDefault() {
        cn.nukkit.network.protocol.BlockEventPacket pk = new cn.nukkit.network.protocol.BlockEventPacket();
        pk.x = x;
        pk.y = y;
        pk.z = z;
        pk.case1 = case1;
        pk.case2 = case2;
        return pk;
    }

    @Override
    public void decode() {

    }

    @Override
    public void encode() {
        this.reset();
        this.putBlockVector3(this.x, this.y, this.z);
        this.putVarInt(this.case1);
        this.putVarInt(this.case2);
    }

    public static Class<? extends DataPacket> getDefaultPacket() {
        return cn.nukkit.network.protocol.BlockEventPacket.class;
    }
}
