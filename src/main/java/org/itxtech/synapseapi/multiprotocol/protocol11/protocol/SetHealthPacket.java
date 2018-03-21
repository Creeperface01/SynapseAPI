package org.itxtech.synapseapi.multiprotocol.protocol11.protocol;

import cn.nukkit.network.protocol.DataPacket;
import org.itxtech.synapseapi.utils.ClassUtils;

public class SetHealthPacket extends Packet11 {

    public static final byte NETWORK_ID = ProtocolInfo.SET_HEALTH_PACKET;

    public int health;

    public SetHealthPacket fromDefault(DataPacket pkk) {
        ClassUtils.requireInstance(pkk, cn.nukkit.network.protocol.SetHealthPacket.class);

        cn.nukkit.network.protocol.SetHealthPacket pk = (cn.nukkit.network.protocol.SetHealthPacket) pkk;
        this.health = pk.health;
        return this;
    }

    public cn.nukkit.network.protocol.SetHealthPacket toDefault() {
        cn.nukkit.network.protocol.SetHealthPacket pk = new cn.nukkit.network.protocol.SetHealthPacket();
        pk.health = health;
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
        this.putUnsignedVarInt(this.health);
    }

    public static Class<? extends DataPacket> getDefaultPacket() {
        return cn.nukkit.network.protocol.SetHealthPacket.class;
    }
}
