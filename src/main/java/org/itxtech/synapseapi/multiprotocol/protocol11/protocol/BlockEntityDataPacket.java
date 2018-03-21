package org.itxtech.synapseapi.multiprotocol.protocol11.protocol;

import cn.nukkit.math.BlockVector3;
import cn.nukkit.network.protocol.DataPacket;
import org.itxtech.synapseapi.utils.ClassUtils;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class BlockEntityDataPacket extends Packet11 {
    public static final byte NETWORK_ID = ProtocolInfo.BLOCK_ENTITY_DATA_PACKET;

    public int x;
    public int y;
    public int z;
    public byte[] namedTag;

    public BlockEntityDataPacket fromDefault(DataPacket pkk) {
        ClassUtils.requireInstance(pkk, cn.nukkit.network.protocol.BlockEntityDataPacket.class);

        cn.nukkit.network.protocol.BlockEntityDataPacket pk = (cn.nukkit.network.protocol.BlockEntityDataPacket) pkk;
        this.x = pk.x;
        this.y = pk.y;
        this.z = pk.z;
        this.namedTag = pk.namedTag;
        return this;
    }

    public cn.nukkit.network.protocol.BlockEntityDataPacket toDefault() {
        cn.nukkit.network.protocol.BlockEntityDataPacket pk = new cn.nukkit.network.protocol.BlockEntityDataPacket();
        pk.x = x;
        pk.y = y;
        pk.z = z;
        pk.namedTag = namedTag;
        return pk;
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public void decode() {
        BlockVector3 v = this.getBlockVector3();
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        this.namedTag = this.get();
    }

    @Override
    public void encode() {
        this.reset();
        this.putBlockVector3(this.x, this.y, this.z);
        this.put(this.namedTag);
    }

    public static Class<? extends DataPacket> getDefaultPacket() {
        return cn.nukkit.network.protocol.BlockEntityDataPacket.class;
    }
}