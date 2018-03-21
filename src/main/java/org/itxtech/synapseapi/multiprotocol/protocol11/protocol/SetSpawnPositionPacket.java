package org.itxtech.synapseapi.multiprotocol.protocol11.protocol;

import cn.nukkit.network.protocol.DataPacket;
import org.itxtech.synapseapi.utils.ClassUtils;

/**
 * @author Nukkit Project Team
 */
public class SetSpawnPositionPacket extends Packet11 {

    public static final byte NETWORK_ID = ProtocolInfo.SET_SPAWN_POSITION_PACKET;

    public static final int TYPE_PLAYER_SPAWN = 0;
    public static final int TYPE_WORLD_SPAWN = 1;

    public int spawnType;
    public int y;
    public int z;
    public int x;
    public boolean spawnForced = false;

    public SetSpawnPositionPacket fromDefault(DataPacket pkk) {
        ClassUtils.requireInstance(pkk, cn.nukkit.network.protocol.SetSpawnPositionPacket.class);

        cn.nukkit.network.protocol.SetSpawnPositionPacket pk = (cn.nukkit.network.protocol.SetSpawnPositionPacket) pkk;
        this.spawnType = pk.spawnType;
        this.y = pk.y;
        this.z = pk.z;
        this.x = pk.x;
        this.spawnForced = pk.spawnForced;
        return this;
    }

    public cn.nukkit.network.protocol.SetSpawnPositionPacket toDefault() {
        cn.nukkit.network.protocol.SetSpawnPositionPacket pk = new cn.nukkit.network.protocol.SetSpawnPositionPacket();
        pk.spawnType = spawnType;
        pk.y = y;
        pk.z = z;
        pk.x = x;
        pk.spawnForced = spawnForced;
        return pk;
    }

    @Override
    public void decode() {

    }

    @Override
    public void encode() {
        this.reset();
        this.putVarInt(this.spawnType);
        this.putBlockVector3(this.x, this.y, this.z);
        this.putBoolean(this.spawnForced);
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    public static Class<? extends DataPacket> getDefaultPacket() {
        return cn.nukkit.network.protocol.SetSpawnPositionPacket.class;
    }
}
