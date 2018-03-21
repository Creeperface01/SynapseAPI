package org.itxtech.synapseapi.multiprotocol.protocol11.protocol;

import cn.nukkit.math.BlockVector3;
import cn.nukkit.network.protocol.DataPacket;
import org.itxtech.synapseapi.utils.ClassUtils;

public class BlockPickRequestPacket extends Packet11 {

    public static final byte NETWORK_ID = ProtocolInfo.BLOCK_PICK_REQUEST_PACKET;

    public int x;
    public int y;
    public int z;
    public int selectedSlot;

    public BlockPickRequestPacket fromDefault(DataPacket pkk) {
        ClassUtils.requireInstance(pkk, cn.nukkit.network.protocol.BlockPickRequestPacket.class);

        cn.nukkit.network.protocol.BlockPickRequestPacket pk = (cn.nukkit.network.protocol.BlockPickRequestPacket) pkk;
        this.x = pk.x;
        this.y = pk.y;
        this.z = pk.z;
        this.selectedSlot = pk.selectedSlot;
        return this;
    }

    public cn.nukkit.network.protocol.BlockPickRequestPacket toDefault() {
        cn.nukkit.network.protocol.BlockPickRequestPacket pk = new cn.nukkit.network.protocol.BlockPickRequestPacket();
        pk.x = x;
        pk.y = y;
        pk.z = z;
        pk.selectedSlot = selectedSlot;
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
        this.selectedSlot = this.getByte();
    }

    @Override
    public void encode() {

    }

    public static Class<? extends DataPacket> getDefaultPacket() {
        return cn.nukkit.network.protocol.BlockPickRequestPacket.class;
    }
}
