package org.itxtech.synapseapi.multiprotocol.protocol11.protocol;

import cn.nukkit.network.protocol.DataPacket;
import org.itxtech.synapseapi.utils.ClassUtils;

/**
 * Created on 15-10-22.
 */
public class SetEntityLinkPacket extends Packet11 {

    public static final byte NETWORK_ID = ProtocolInfo.SET_ENTITY_LINK_PACKET;

    public static final byte TYPE_REMOVE = 0;
    public static final byte TYPE_RIDE = 1;
    public static final byte TYPE_PASSENGER = 2;

    public long rider;
    public long riding;
    public byte type;

    public SetEntityLinkPacket fromDefault(DataPacket pkk) {
        ClassUtils.requireInstance(pkk, cn.nukkit.network.protocol.SetEntityLinkPacket.class);

        cn.nukkit.network.protocol.SetEntityLinkPacket pk = (cn.nukkit.network.protocol.SetEntityLinkPacket) pkk;
        this.rider = pk.rider;
        this.riding = pk.riding;
        this.type = pk.type;
        return this;
    }

    public cn.nukkit.network.protocol.SetEntityLinkPacket toDefault() {
        cn.nukkit.network.protocol.SetEntityLinkPacket pk = new cn.nukkit.network.protocol.SetEntityLinkPacket();
        pk.rider = rider;
        pk.riding = riding;
        pk.type = type;
        return pk;
    }

    @Override
    public void decode() {

    }

    @Override
    public void encode() {
        this.reset();
        this.putVarLong(this.rider);
        this.putVarLong(this.riding);
        this.putByte(this.type);
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    public static Class<? extends DataPacket> getDefaultPacket() {
        return cn.nukkit.network.protocol.SetEntityLinkPacket.class;
    }
}
