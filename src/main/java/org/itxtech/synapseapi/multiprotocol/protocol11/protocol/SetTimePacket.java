package org.itxtech.synapseapi.multiprotocol.protocol11.protocol;

import cn.nukkit.network.protocol.DataPacket;
import org.itxtech.synapseapi.utils.ClassUtils;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class SetTimePacket extends Packet11 {
    public static final byte NETWORK_ID = ProtocolInfo.SET_TIME_PACKET;

    public int time;

    public SetTimePacket fromDefault(DataPacket pkk) {
        ClassUtils.requireInstance(pkk, cn.nukkit.network.protocol.SetTimePacket.class);

        cn.nukkit.network.protocol.SetTimePacket pk = (cn.nukkit.network.protocol.SetTimePacket) pkk;
        this.time = pk.time;
        return this;
    }

    public cn.nukkit.network.protocol.SetTimePacket toDefault() {
        cn.nukkit.network.protocol.SetTimePacket pk = new cn.nukkit.network.protocol.SetTimePacket();
        pk.time = time;
        return pk;
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public void decode() {

    }

    @Override
    public void encode() {
        this.reset();
        this.putVarInt(this.time);
    }

    public static Class<? extends DataPacket> getDefaultPacket() {
        return cn.nukkit.network.protocol.SetTimePacket.class;
    }
}
