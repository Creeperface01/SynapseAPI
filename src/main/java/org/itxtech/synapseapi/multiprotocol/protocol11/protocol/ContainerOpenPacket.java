package org.itxtech.synapseapi.multiprotocol.protocol11.protocol;

import cn.nukkit.network.protocol.DataPacket;
import org.itxtech.synapseapi.utils.ClassUtils;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class ContainerOpenPacket extends Packet11 {
    public static final byte NETWORK_ID = ProtocolInfo.CONTAINER_OPEN_PACKET;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    public byte windowid;
    public byte type;
    public int x;
    public int y;
    public int z;
    public final long entityId = -1;

    public ContainerOpenPacket fromDefault(DataPacket pkk) {
        ClassUtils.requireInstance(pkk, cn.nukkit.network.protocol.ContainerOpenPacket.class);

        cn.nukkit.network.protocol.ContainerOpenPacket pk = (cn.nukkit.network.protocol.ContainerOpenPacket) pkk;
        this.windowid = (byte) pk.windowId;
        this.type = (byte) pk.type;
        this.x = pk.x;
        this.y = pk.y;
        this.z = pk.z;
        return this;
    }

    public cn.nukkit.network.protocol.ContainerOpenPacket toDefault() {
        cn.nukkit.network.protocol.ContainerOpenPacket pk = new cn.nukkit.network.protocol.ContainerOpenPacket();
        pk.windowId = windowid;
        pk.type = type;
        pk.x = x;
        pk.y = y;
        pk.z = z;
        pk.entityId = entityId;

        return pk;
    }

    @Override
    public void decode() {

    }

    @Override
    public void encode() {
        this.reset();
        this.putByte(this.windowid);
        this.putByte(this.type);
        this.putBlockVector3(this.x, this.y, this.z);
        this.putEntityUniqueId(this.entityId);
    }

    public static Class<? extends DataPacket> getDefaultPacket() {
        return cn.nukkit.network.protocol.ContainerOpenPacket.class;
    }
}
