package org.itxtech.synapseapi.multiprotocol.protocol11.protocol;

import cn.nukkit.network.protocol.DataPacket;
import org.itxtech.synapseapi.utils.ClassUtils;

public class ShowCreditsPacket extends Packet11 {

    public static final byte NETWORK_ID = ProtocolInfo.SHOW_CREDITS_PACKET;

    public static final int STATUS_START_CREDITS = 0;
    public static final int STATUS_END_CREDITS = 1;

    public long eid;
    public int status;

    public ShowCreditsPacket fromDefault(DataPacket pkk) {
        ClassUtils.requireInstance(pkk, cn.nukkit.network.protocol.ShowCreditsPacket.class);

        cn.nukkit.network.protocol.ShowCreditsPacket pk = (cn.nukkit.network.protocol.ShowCreditsPacket) pkk;
        this.eid = pk.eid;
        this.status = pk.status;
        return this;
    }

    public cn.nukkit.network.protocol.ShowCreditsPacket toDefault() {
        cn.nukkit.network.protocol.ShowCreditsPacket pk = new cn.nukkit.network.protocol.ShowCreditsPacket();
        pk.eid = eid;
        pk.status = status;
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
        this.putEntityRuntimeId(this.eid);
        this.putVarInt(this.status);
    }

    public static Class<? extends DataPacket> getDefaultPacket() {
        return cn.nukkit.network.protocol.ShowCreditsPacket.class;
    }
}
