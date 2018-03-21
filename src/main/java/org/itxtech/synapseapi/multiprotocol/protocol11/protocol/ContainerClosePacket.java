package org.itxtech.synapseapi.multiprotocol.protocol11.protocol;

import cn.nukkit.network.protocol.DataPacket;
import org.itxtech.synapseapi.utils.ClassUtils;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class ContainerClosePacket extends Packet11 {
    public static final byte NETWORK_ID = ProtocolInfo.CONTAINER_CLOSE_PACKET;

    public ContainerClosePacket fromDefault(DataPacket pkk) {
        ClassUtils.requireInstance(pkk, cn.nukkit.network.protocol.ContainerClosePacket.class);

        cn.nukkit.network.protocol.ContainerClosePacket pk = (cn.nukkit.network.protocol.ContainerClosePacket) pkk;
        this.windowid = pk.windowId;

        return this;
    }

    public cn.nukkit.network.protocol.ContainerClosePacket toDefault() {
        cn.nukkit.network.protocol.ContainerClosePacket pk = new cn.nukkit.network.protocol.ContainerClosePacket();
        pk.windowId = this.windowid;

        return pk;
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    public int windowid;

    @Override
    public void decode() {
        this.windowid = this.getByte();
    }

    @Override
    public void encode() {
        this.reset();
        this.putByte((byte) this.windowid);
    }

    public static Class<? extends DataPacket> getDefaultPacket() {
        return cn.nukkit.network.protocol.ContainerClosePacket.class;
    }
}
