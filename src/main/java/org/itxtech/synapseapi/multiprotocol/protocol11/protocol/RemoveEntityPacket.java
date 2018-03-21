package org.itxtech.synapseapi.multiprotocol.protocol11.protocol;

import cn.nukkit.network.protocol.DataPacket;
import org.itxtech.synapseapi.utils.ClassUtils;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class RemoveEntityPacket extends Packet11 {
    public static final byte NETWORK_ID = ProtocolInfo.REMOVE_ENTITY_PACKET;

    public long eid;

    public RemoveEntityPacket fromDefault(DataPacket pkk) {
        ClassUtils.requireInstance(pkk, cn.nukkit.network.protocol.RemoveEntityPacket.class);

        cn.nukkit.network.protocol.RemoveEntityPacket pk = (cn.nukkit.network.protocol.RemoveEntityPacket) pkk;
        this.eid = pk.eid;
        return this;
    }

    public cn.nukkit.network.protocol.RemoveEntityPacket toDefault() {
        cn.nukkit.network.protocol.RemoveEntityPacket pk = new cn.nukkit.network.protocol.RemoveEntityPacket();
        pk.eid = eid;
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
        this.putVarLong(this.eid);
    }

    public static Class<? extends DataPacket> getDefaultPacket() {
        return cn.nukkit.network.protocol.RemoveEntityPacket.class;
    }
}
