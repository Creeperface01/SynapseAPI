package org.itxtech.synapseapi.multiprotocol.protocol11.protocol;

import cn.nukkit.network.protocol.DataPacket;
import org.itxtech.synapseapi.utils.ClassUtils;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class ContainerSetDataPacket extends Packet11 {
    public static final byte NETWORK_ID = ProtocolInfo.CONTAINER_SET_DATA_PACKET;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    public byte windowid;
    public int property;
    public int value;

    public ContainerSetDataPacket fromDefault(DataPacket pkk) {
        ClassUtils.requireInstance(pkk, cn.nukkit.network.protocol.ContainerSetDataPacket.class);

        cn.nukkit.network.protocol.ContainerSetDataPacket pk = (cn.nukkit.network.protocol.ContainerSetDataPacket) pkk;
        this.windowid = (byte) pk.windowId;
        this.property = pk.property;
        this.value = pk.value;
        return this;
    }

    public cn.nukkit.network.protocol.ContainerSetDataPacket toDefault() {
        cn.nukkit.network.protocol.ContainerSetDataPacket pk = new cn.nukkit.network.protocol.ContainerSetDataPacket();
        pk.windowId = windowid;
        pk.property = property;
        pk.value = value;
        return pk;
    }

    @Override
    public void decode() {

    }

    @Override
    public void encode() {
        this.reset();
        this.putByte(this.windowid);
        this.putVarInt(this.property);
        this.putVarInt(this.value);
    }

    public static Class<? extends DataPacket> getDefaultPacket() {
        return cn.nukkit.network.protocol.ContainerSetDataPacket.class;
    }
}
