package org.itxtech.synapseapi.multiprotocol.protocol11.protocol;

import cn.nukkit.network.protocol.DataPacket;
import org.itxtech.synapseapi.utils.ClassUtils;

public class EntityFallPacket extends Packet11 {
    public static final byte NETWORK_ID = ProtocolInfo.ENTITY_FALL_PACKET;

    public long eid;
    public float fallDistance;
    public boolean unknown;

    public EntityFallPacket fromDefault(DataPacket pkk) {
        ClassUtils.requireInstance(pkk, cn.nukkit.network.protocol.EntityFallPacket.class);

        cn.nukkit.network.protocol.EntityFallPacket pk = (cn.nukkit.network.protocol.EntityFallPacket) pkk;
        this.eid = pk.eid;
        this.fallDistance = pk.fallDistance;
        this.unknown = pk.unknown;
        return this;
    }

    public cn.nukkit.network.protocol.EntityFallPacket toDefault() {
        cn.nukkit.network.protocol.EntityFallPacket pk = new cn.nukkit.network.protocol.EntityFallPacket();
        pk.eid = eid;
        pk.fallDistance = fallDistance;
        pk.unknown = unknown;
        return pk;
    }

    @Override
    public void decode() {
        this.eid = this.getVarLong();
        this.fallDistance = this.getLFloat();
        this.unknown = this.getBoolean();
    }

    @Override
    public void encode() {

    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    public static Class<? extends DataPacket> getDefaultPacket() {
        return cn.nukkit.network.protocol.EntityFallPacket.class;
    }
}
