package org.itxtech.synapseapi.multiprotocol.protocol11.protocol;

import cn.nukkit.math.Vector3f;
import cn.nukkit.network.protocol.DataPacket;
import org.itxtech.synapseapi.utils.ClassUtils;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class MoveEntityPacket extends Packet11 {
    public static final byte NETWORK_ID = ProtocolInfo.MOVE_ENTITY_PACKET;

    public long eid;
    public double x;
    public double y;
    public double z;
    public double yaw;
    public double headYaw;
    public double pitch;
    public boolean onGround;
    public boolean teleport;

    public MoveEntityPacket fromDefault(DataPacket pkk) {
        ClassUtils.requireInstance(pkk, cn.nukkit.network.protocol.MoveEntityPacket.class);

        cn.nukkit.network.protocol.MoveEntityPacket pk = (cn.nukkit.network.protocol.MoveEntityPacket) pkk;
        this.eid = pk.eid;
        this.x = pk.x;
        this.y = pk.y;
        this.z = pk.z;
        this.yaw = pk.yaw;
        this.headYaw = pk.headYaw;
        this.pitch = pk.pitch;
        this.onGround = pk.onGround;
        this.teleport = pk.teleport;
        return this;
    }

    public cn.nukkit.network.protocol.MoveEntityPacket toDefault() {
        cn.nukkit.network.protocol.MoveEntityPacket pk = new cn.nukkit.network.protocol.MoveEntityPacket();
        pk.eid = eid;
        pk.x = x;
        pk.y = y;
        pk.z = z;
        pk.yaw = yaw;
        pk.pitch = pitch;
        pk.headYaw = headYaw;
        pk.onGround = onGround;
        pk.teleport = teleport;
        return pk;
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public void decode() {
        this.eid = getEntityRuntimeId();
        Vector3f v = this.getVector3f();
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        this.pitch = this.getByte() * (360d / 256d);
        this.headYaw = this.getByte() * (360d / 256d);
        this.yaw = this.getByte() * (360d / 256d);
        this.onGround = this.getBoolean();
        this.teleport = this.getBoolean();
    }

    @Override
    public void encode() {
        this.reset();
        this.putEntityRuntimeId(this.eid);
        this.putVector3f((float) this.x, (float) this.y, (float) this.z);
        this.putByte((byte) (this.pitch / (360d / 256d)));
        this.putByte((byte) (this.headYaw / (360d / 256d)));
        this.putByte((byte) (this.yaw / (360d / 256d)));
        this.putBoolean(this.onGround);
        this.putBoolean(this.teleport);
    }

    public static Class<? extends DataPacket> getDefaultPacket() {
        return cn.nukkit.network.protocol.MoveEntityPacket.class;
    }
}
