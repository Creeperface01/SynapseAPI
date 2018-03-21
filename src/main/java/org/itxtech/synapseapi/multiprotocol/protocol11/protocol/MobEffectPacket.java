package org.itxtech.synapseapi.multiprotocol.protocol11.protocol;

import cn.nukkit.network.protocol.DataPacket;
import org.itxtech.synapseapi.utils.ClassUtils;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class MobEffectPacket extends Packet11 {

    public static final byte NETWORK_ID = ProtocolInfo.MOB_EFFECT_PACKET;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    public static final byte EVENT_ADD = 1;
    public static final byte EVENT_MODIFY = 2;
    public static final byte EVENT_REMOVE = 3;

    public long eid;
    public int eventId;
    public int effectId;
    public int amplifier = 0;
    public boolean particles = true;
    public int duration = 0;

    @Override
    public void decode() {

    }

    @Override
    public void encode() {
        this.reset();
        this.putVarLong(this.eid);
        this.putByte((byte) this.eventId);
        this.putVarInt(this.effectId);
        this.putVarInt(this.amplifier);
        this.putBoolean(this.particles);
        this.putVarInt(this.duration);
    }

    @Override
    public DataPacket fromDefault(DataPacket pk) {
        ClassUtils.requireInstance(pk, cn.nukkit.network.protocol.MobEffectPacket.class);
        cn.nukkit.network.protocol.MobEffectPacket packet = (cn.nukkit.network.protocol.MobEffectPacket) pk;
        this.eid = packet.eid;
        this.eventId = packet.eventId;
        this.effectId = packet.effectId;
        this.amplifier = packet.amplifier;
        this.particles = packet.particles;
        this.duration = packet.duration;


        return this;
    }

    @Override
    public DataPacket toDefault() {
        cn.nukkit.network.protocol.MobEffectPacket pk = new cn.nukkit.network.protocol.MobEffectPacket();
        pk.eid = this.eid;
        pk.eventId = eventId;
        pk.effectId = effectId;
        pk.amplifier = amplifier;
        pk.particles = particles;
        pk.duration = duration;

        return pk;
    }

    public static Class<? extends DataPacket> getDefaultPacket() {
        return cn.nukkit.network.protocol.MobEffectPacket.class;
    }
}
