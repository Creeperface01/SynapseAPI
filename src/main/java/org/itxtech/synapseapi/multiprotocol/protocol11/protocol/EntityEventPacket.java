package org.itxtech.synapseapi.multiprotocol.protocol11.protocol;

import cn.nukkit.network.protocol.DataPacket;
import org.itxtech.synapseapi.utils.ClassUtils;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class EntityEventPacket extends Packet11 {
    public static final byte NETWORK_ID = ProtocolInfo.ENTITY_EVENT_PACKET;


    public static final byte HURT_ANIMATION = 2;
    public static final byte DEATH_ANIMATION = 3;

    public static final byte TAME_FAIL = 6;
    public static final byte TAME_SUCCESS = 7;
    public static final byte SHAKE_WET = 8;
    public static final byte USE_ITEM = 9;
    public static final byte EAT_GRASS_ANIMATION = 10;
    public static final byte FISH_HOOK_BUBBLE = 11;
    public static final byte FISH_HOOK_POSITION = 12;
    public static final byte FISH_HOOK_HOOK = 13;
    public static final byte FISH_HOOK_TEASE = 14;
    public static final byte SQUID_INK_CLOUD = 15;
    public static final byte AMBIENT_SOUND = 16;
    public static final byte RESPAWN = 17;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    public long eid;
    public byte event;
    public long unknown;

    public EntityEventPacket fromDefault(DataPacket pkk) {
        ClassUtils.requireInstance(pkk, cn.nukkit.network.protocol.EntityEventPacket.class);

        cn.nukkit.network.protocol.EntityEventPacket pk = (cn.nukkit.network.protocol.EntityEventPacket) pkk;
        this.eid = pk.eid;
        this.event = (byte) pk.event;
        this.unknown = pk.data;
        return this;
    }

    public cn.nukkit.network.protocol.EntityEventPacket toDefault() {
        cn.nukkit.network.protocol.EntityEventPacket pk = new cn.nukkit.network.protocol.EntityEventPacket();
        pk.eid = eid;
        pk.event = event;
        pk.data = (int) unknown;
        return pk;
    }

    @Override
    public void decode() {
        this.eid = getEntityRuntimeId();
        this.event = (byte) this.getByte();
        this.unknown = this.getUnsignedVarInt();
    }

    @Override
    public void encode() {
        this.reset();
        this.putEntityRuntimeId(this.eid);
        this.putByte(this.event);
        this.putUnsignedVarInt(this.unknown);
    }

    public static Class<? extends DataPacket> getDefaultPacket() {
        return cn.nukkit.network.protocol.EntityEventPacket.class;
    }
}
