package org.itxtech.synapseapi.multiprotocol.protocol11.protocol;

import cn.nukkit.network.protocol.DataPacket;
import org.itxtech.synapseapi.utils.ClassUtils;

/**
 * Created by CreeperFace on 30. 10. 2016.
 */
public class BossEventPacket extends Packet11 {

    public static final byte NETWORK_ID = ProtocolInfo.BOSS_EVENT_PACKET;

    public long eid;
    public int type;

    public static final int ADD = 0;
    public static final int UPDATE = 1;
    public static final int REMOVE = 2;

    public BossEventPacket fromDefault(DataPacket pkk) {
        ClassUtils.requireInstance(pkk, cn.nukkit.network.protocol.BossEventPacket.class);

        cn.nukkit.network.protocol.BossEventPacket pk = (cn.nukkit.network.protocol.BossEventPacket) pkk;
        this.eid = pk.bossEid;
        this.type = pk.type;
        return this;
    }

    public cn.nukkit.network.protocol.BossEventPacket toDefault() {
        cn.nukkit.network.protocol.BossEventPacket pk = new cn.nukkit.network.protocol.BossEventPacket();
        pk.bossEid = eid;
        pk.type = type;
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
        this.putEntityRuntimeId(this.eid);
        this.putUnsignedVarInt(this.type);
    }

    public static Class<? extends DataPacket> getDefaultPacket() {
        return cn.nukkit.network.protocol.BossEventPacket.class;
    }
}
