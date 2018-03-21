package org.itxtech.synapseapi.multiprotocol.protocol11.protocol;

import cn.nukkit.math.Vector3f;
import cn.nukkit.network.protocol.DataPacket;
import org.itxtech.synapseapi.utils.ClassUtils;

/**
 * @author Nukkit Project Team
 */
public class RespawnPacket extends Packet11 {

    public static final byte NETWORK_ID = ProtocolInfo.RESPAWN_PACKET;

    public float x;
    public float y;
    public float z;

    public RespawnPacket fromDefault(DataPacket pkk) {
        ClassUtils.requireInstance(pkk, cn.nukkit.network.protocol.RespawnPacket.class);

        cn.nukkit.network.protocol.RespawnPacket pk = (cn.nukkit.network.protocol.RespawnPacket) pkk;
        this.x = pk.x;
        this.y = pk.y;
        this.z = pk.z;
        return this;
    }

    public cn.nukkit.network.protocol.RespawnPacket toDefault() {
        cn.nukkit.network.protocol.RespawnPacket pk = new cn.nukkit.network.protocol.RespawnPacket();
        pk.x = x;
        pk.y = y;
        pk.z = z;
        return pk;
    }

    @Override
    public void decode() {
        Vector3f v = this.getVector3f();
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
    }

    @Override
    public void encode() {
        this.reset();
        this.putVector3f(this.x, this.y, this.z);
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    public static Class<? extends DataPacket> getDefaultPacket() {
        return cn.nukkit.network.protocol.RespawnPacket.class;
    }
}
