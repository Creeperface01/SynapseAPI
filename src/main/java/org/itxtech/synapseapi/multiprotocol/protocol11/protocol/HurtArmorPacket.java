package org.itxtech.synapseapi.multiprotocol.protocol11.protocol;

import cn.nukkit.network.protocol.DataPacket;
import org.itxtech.synapseapi.utils.ClassUtils;

/**
 * @author Nukkit Project Team
 */
public class HurtArmorPacket extends Packet11 {

    public static final byte NETWORK_ID = ProtocolInfo.HURT_ARMOR_PACKET;

    public int health;

    public HurtArmorPacket fromDefault(DataPacket pkk) {
        ClassUtils.requireInstance(pkk, cn.nukkit.network.protocol.HurtArmorPacket.class);

        cn.nukkit.network.protocol.HurtArmorPacket pk = (cn.nukkit.network.protocol.HurtArmorPacket) pkk;
        this.health = pk.health;
        return this;
    }

    public cn.nukkit.network.protocol.HurtArmorPacket toDefault() {
        cn.nukkit.network.protocol.HurtArmorPacket pk = new cn.nukkit.network.protocol.HurtArmorPacket();
        pk.health = health;
        return pk;
    }

    @Override
    public void decode() {

    }

    @Override
    public void encode() {
        this.reset();
        this.putVarInt(this.health);
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    public static Class<? extends DataPacket> getDefaultPacket() {
        return cn.nukkit.network.protocol.HurtArmorPacket.class;
    }
}
