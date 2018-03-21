package org.itxtech.synapseapi.multiprotocol.protocol11.protocol;

import cn.nukkit.math.BlockVector3;
import cn.nukkit.network.protocol.DataPacket;
import org.itxtech.synapseapi.utils.ClassUtils;

/**
 * Created by Pub4Game on 03.07.2016.
 */
public class ItemFrameDropItemPacket extends Packet11 {

    public static final byte NETWORK_ID = ProtocolInfo.ITEM_FRAME_DROP_ITEM_PACKET;

    public int x;
    public int y;
    public int z;

    public ItemFrameDropItemPacket fromDefault(DataPacket pkk) {
        ClassUtils.requireInstance(pkk, cn.nukkit.network.protocol.ItemFrameDropItemPacket.class);

        cn.nukkit.network.protocol.ItemFrameDropItemPacket pk = (cn.nukkit.network.protocol.ItemFrameDropItemPacket) pkk;
        this.x = pk.x;
        this.y = pk.y;
        this.z = pk.z;
        return this;
    }

    public cn.nukkit.network.protocol.ItemFrameDropItemPacket toDefault() {
        cn.nukkit.network.protocol.ItemFrameDropItemPacket pk = new cn.nukkit.network.protocol.ItemFrameDropItemPacket();
        pk.x = x;
        pk.y = y;
        pk.z = z;
        return pk;
    }

    @Override
    public void decode() {
        BlockVector3 v = this.getBlockVector3();
        this.z = v.z;
        this.y = v.y;
        this.x = v.x;
    }

    @Override
    public void encode() {

    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    public static Class<? extends DataPacket> getDefaultPacket() {
        return cn.nukkit.network.protocol.ItemFrameDropItemPacket.class;
    }
}
