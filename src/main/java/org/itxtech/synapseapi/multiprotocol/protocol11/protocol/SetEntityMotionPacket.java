package org.itxtech.synapseapi.multiprotocol.protocol11.protocol;

import cn.nukkit.network.protocol.DataPacket;
import org.itxtech.synapseapi.utils.ClassUtils;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class SetEntityMotionPacket extends Packet11 {
    public static final byte NETWORK_ID = ProtocolInfo.SET_ENTITY_MOTION_PACKET;

    public long eid;
    public float motionX;
    public float motionY;
    public float motionZ;

    public SetEntityMotionPacket fromDefault(DataPacket pkk) {
        ClassUtils.requireInstance(pkk, cn.nukkit.network.protocol.SetEntityMotionPacket.class);

        cn.nukkit.network.protocol.SetEntityMotionPacket pk = (cn.nukkit.network.protocol.SetEntityMotionPacket) pkk;
        this.eid = pk.eid;
        this.motionX = pk.motionX;
        this.motionY = pk.motionY;
        this.motionZ = pk.motionZ;
        return this;
    }

    public cn.nukkit.network.protocol.SetEntityMotionPacket toDefault() {
        cn.nukkit.network.protocol.SetEntityMotionPacket pk = new cn.nukkit.network.protocol.SetEntityMotionPacket();
        pk.eid = eid;
        pk.motionX = motionX;
        pk.motionY = motionY;
        this.motionZ = pk.motionZ;
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
        this.putVarLong(this.eid);
        this.putVector3f(this.motionX, this.motionY, this.motionZ);
    }

    @Override
    public boolean needConversion() {
        return false;
    }

    public static Class<? extends DataPacket> getDefaultPacket() {
        return cn.nukkit.network.protocol.SetEntityMotionPacket.class;
    }
}
