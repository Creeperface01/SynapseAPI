package org.itxtech.synapseapi.multiprotocol.protocol11.protocol;

import cn.nukkit.math.BlockVector3;
import cn.nukkit.network.protocol.DataPacket;
import org.itxtech.synapseapi.utils.ClassUtils;

/**
 * @author Nukkit Project Team
 */
public class PlayerActionPacket extends Packet11 {

    public static final byte NETWORK_ID = ProtocolInfo.PLAYER_ACTION_PACKET;

    public static final byte ACTION_START_BREAK = 0;
    public static final byte ACTION_ABORT_BREAK = 1;
    public static final byte ACTION_STOP_BREAK = 2;

    public static final byte ACTION_RELEASE_ITEM = 5;
    public static final byte ACTION_STOP_SLEEPING = 6;
    public static final byte ACTION_RESPAWN = 7;
    public static final byte ACTION_JUMP = 8;
    public static final byte ACTION_START_SPRINT = 9;
    public static final byte ACTION_STOP_SPRINT = 10;
    public static final byte ACTION_START_SNEAK = 11;
    public static final byte ACTION_STOP_SNEAK = 12;
    public static final byte ACTION_DIMENSION_CHANGE = 13;
    public static final byte ACTION_ABORT_DIMENSION_CHANGE = 14;
    public static final byte ACTION_START_GLIDE = 15;
    public static final byte ACTION_STOP_GLIDE = 16;
    public static final byte ACTION_WORLD_IMMUTABLE = 17;
    public static final byte ACTION_CONTINUE_BREAK = 18;

    public long entityId;
    public int action;
    public int x;
    public int y;
    public int z;
    public int face;

    public PlayerActionPacket fromDefault(DataPacket pkk) {
        ClassUtils.requireInstance(pkk, cn.nukkit.network.protocol.PlayerActionPacket.class);

        cn.nukkit.network.protocol.PlayerActionPacket pk = (cn.nukkit.network.protocol.PlayerActionPacket) pkk;
        this.x = pk.x;
        this.y = pk.y;
        this.z = pk.z;
        this.entityId = pk.entityId;
        this.face = pk.face;
        this.action = pk.action;
        return this;
    }

    public cn.nukkit.network.protocol.PlayerActionPacket toDefault() {
        cn.nukkit.network.protocol.PlayerActionPacket pk = new cn.nukkit.network.protocol.PlayerActionPacket();
        pk.x = x;
        pk.y = y;
        pk.z = z;
        pk.entityId = entityId;
        pk.face = face;
        pk.action = action;
        return pk;
    }


    @Override
    public void decode() {
        this.entityId = getEntityRuntimeId();
        this.action = this.getVarInt();
        BlockVector3 v = this.getBlockVector3();
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        this.face = this.getVarInt();
    }

    @Override
    public void encode() {
        this.reset();
        this.putEntityRuntimeId(this.entityId);
        this.putVarInt(this.action);
        this.putBlockVector3(this.x, this.y, this.z);
        this.putVarInt(this.face);
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    public static Class<? extends DataPacket> getDefaultPacket() {
        return cn.nukkit.network.protocol.PlayerActionPacket.class;
    }
}
