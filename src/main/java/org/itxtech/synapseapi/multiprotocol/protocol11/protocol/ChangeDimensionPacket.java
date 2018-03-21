package org.itxtech.synapseapi.multiprotocol.protocol11.protocol;

import cn.nukkit.network.protocol.DataPacket;
import org.itxtech.synapseapi.utils.ClassUtils;

/**
 * Created on 2016/1/5 by xtypr.
 * Package cn.nukkit.network.protocol in project nukkit .
 */
public class ChangeDimensionPacket extends Packet11 {

    public static final byte NETWORK_ID = ProtocolInfo.CHANGE_DIMENSION_PACKET;

    public int dimension;

    public float x;
    public float y;
    public float z;

    public boolean unknown;

    public ChangeDimensionPacket fromDefault(DataPacket pkk) {
        ClassUtils.requireInstance(pkk, cn.nukkit.network.protocol.ChangeDimensionPacket.class);

        cn.nukkit.network.protocol.ChangeDimensionPacket pk = (cn.nukkit.network.protocol.ChangeDimensionPacket) pkk;
        this.dimension = pk.dimension;
        this.x = pk.x;
        this.y = pk.y;
        this.z = pk.z;
        this.unknown = pk.respawn;
        return this;
    }

    public cn.nukkit.network.protocol.ChangeDimensionPacket toDefault() {
        cn.nukkit.network.protocol.ChangeDimensionPacket pk = new cn.nukkit.network.protocol.ChangeDimensionPacket();
        pk.dimension = dimension;
        pk.x = x;
        pk.y = y;
        pk.z = z;
        pk.respawn = unknown;
        return pk;
    }

    @Override
    public void decode() {

    }

    @Override
    public void encode() {
        this.reset();
        this.putVarInt(this.dimension);
        this.putVector3f(this.x, this.y, this.z);
        this.putBoolean(this.unknown);
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    public static Class<? extends DataPacket> getDefaultPacket() {
        return cn.nukkit.network.protocol.ChangeDimensionPacket.class;
    }
}
