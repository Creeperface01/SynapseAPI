package org.itxtech.synapseapi.multiprotocol.protocol11.protocol;

import cn.nukkit.math.Vector3f;
import cn.nukkit.network.protocol.DataPacket;
import org.itxtech.synapseapi.utils.ClassUtils;

/**
 * Created on 15-10-14.
 */
public class MovePlayerPacket extends Packet11 {

    public static final byte NETWORK_ID = ProtocolInfo.MOVE_PLAYER_PACKET;

    public static final byte MODE_NORMAL = 0;
    public static final byte MODE_RESET = 1;
    public static final byte MODE_TELEPORT = 2;
    public static final byte MODE_PITCH = 3; //facepalm Mojang

    public long eid;
    public float x;
    public float y;
    public float z;
    public float yaw;
    public float headYaw;
    public float pitch;
    public byte mode = MODE_NORMAL;
    public boolean onGround;
    public long ridingEid;
    public int int1 = 0;
    public int int2 = 0;

    public MovePlayerPacket fromDefault(DataPacket pkk) {
        ClassUtils.requireInstance(pkk, cn.nukkit.network.protocol.MovePlayerPacket.class);

        cn.nukkit.network.protocol.MovePlayerPacket pk = (cn.nukkit.network.protocol.MovePlayerPacket) pkk;
        this.eid = pk.eid;
        this.x = pk.x;
        this.y = pk.y;
        this.z = pk.z;
        this.yaw = pk.yaw;
        this.headYaw = pk.headYaw;
        this.pitch = pk.pitch;
        this.onGround = pk.onGround;
        this.mode = (byte) pk.mode;
        this.ridingEid = pk.ridingEid;
        this.int1 = pk.int1;
        this.int2 = pk.int2;
        return this;
    }

    public cn.nukkit.network.protocol.MovePlayerPacket toDefault() {
        cn.nukkit.network.protocol.MovePlayerPacket pk = new cn.nukkit.network.protocol.MovePlayerPacket();
        pk.eid = eid;
        pk.x = x;
        pk.y = y;
        pk.z = z;
        pk.yaw = yaw;
        pk.pitch = pitch;
        pk.headYaw = headYaw;
        pk.onGround = onGround;
        pk.mode = mode;
        pk.ridingEid = ridingEid;
        pk.int1 = int1;
        pk.int2 = int2;
        return pk;
    }

    @Override
    public void decode() {
        this.eid = getEntityRuntimeId();
        Vector3f v = this.getVector3f();
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        this.pitch = this.getLFloat();
        this.headYaw = this.getLFloat();
        this.yaw = this.getLFloat();
        this.mode = (byte) this.getByte();
        this.onGround = this.getBoolean();
        this.ridingEid = getEntityRuntimeId();
        if (this.mode == MODE_TELEPORT) {
            this.int1 = this.getLInt();
            this.int2 = this.getLInt();
        }
    }

    @Override
    public void encode() {
        this.reset();
        this.putEntityRuntimeId(this.eid);
        this.putVector3f(this.x, this.y, this.z);
        this.putLFloat(this.pitch);
        this.putLFloat(this.yaw);
        this.putLFloat(this.headYaw);
        this.putByte(this.mode);
        this.putBoolean(this.onGround);
        this.putEntityRuntimeId(this.ridingEid);
        if (this.mode == MODE_TELEPORT) {
            this.putLInt(this.int1);
            this.putLInt(this.int2);
        }
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    public static Class<? extends DataPacket> getDefaultPacket() {
        return cn.nukkit.network.protocol.MovePlayerPacket.class;
    }
}
