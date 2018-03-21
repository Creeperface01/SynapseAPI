package org.itxtech.synapseapi.multiprotocol.protocol11.protocol;

import cn.nukkit.network.protocol.DataPacket;
import org.itxtech.synapseapi.utils.ClassUtils;

public class SpawnExperienceOrbPacket extends Packet11 {

    public static final byte NETWORK_ID = ProtocolInfo.SPAWN_EXPERIENCE_ORB_PACKET;

    public float x;
    public float y;
    public float z;
    public int amount;

    public SpawnExperienceOrbPacket fromDefault(DataPacket pkk) {
        ClassUtils.requireInstance(pkk, SpawnExperienceOrbPacket.class);

        cn.nukkit.network.protocol.SpawnExperienceOrbPacket pk = (cn.nukkit.network.protocol.SpawnExperienceOrbPacket) pkk;
        this.amount = pk.amount;
        this.y = pk.y;
        this.z = pk.z;
        this.x = pk.x;
        return this;
    }

    public cn.nukkit.network.protocol.SpawnExperienceOrbPacket toDefault() {
        cn.nukkit.network.protocol.SpawnExperienceOrbPacket pk = new cn.nukkit.network.protocol.SpawnExperienceOrbPacket();
        pk.amount = amount;
        pk.y = y;
        pk.z = z;
        pk.x = x;
        return pk;
    }

    @Override
    public void decode() {

    }

    @Override
    public void encode() {
        this.reset();
        this.putVector3f(this.x, this.y, this.z);
        this.putUnsignedVarInt(this.amount);
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    public static Class<? extends DataPacket> getDefaultPacket() {
        return cn.nukkit.network.protocol.SpawnExperienceOrbPacket.class;
    }
}
