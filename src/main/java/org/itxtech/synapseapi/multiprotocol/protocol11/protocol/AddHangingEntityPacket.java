package org.itxtech.synapseapi.multiprotocol.protocol11.protocol;

import cn.nukkit.math.BlockVector3;
import cn.nukkit.network.protocol.DataPacket;
import org.itxtech.synapseapi.utils.ClassUtils;

public class AddHangingEntityPacket extends Packet11 {
    public static final byte NETWORK_ID = ProtocolInfo.ADD_HANGING_ENTITY_PACKET;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    public long entityUniqueId;
    public long entityRuntimeId;
    public int x;
    public int y;
    public int z;
    public int unknown;

    @Override
    public void decode() {
        this.entityUniqueId = this.getVarLong();
        this.entityRuntimeId = getEntityRuntimeId();
        BlockVector3 v3 = this.getBlockVector3();
        this.x = v3.x;
        this.y = v3.y;
        this.z = v3.z;
        this.unknown = this.getVarInt();
    }

    @Override
    public void encode() {
        this.reset();
        this.putVarLong(this.entityUniqueId);
        this.putEntityRuntimeId(this.entityRuntimeId);
        this.putBlockVector3(this.x, this.y, this.z);
        this.putVarInt(this.unknown);
    }

    @Override
    public AddHangingEntityPacket fromDefault(DataPacket pk) {
        ClassUtils.requireInstance(pk, cn.nukkit.network.protocol.AddPaintingPacket.class);

        cn.nukkit.network.protocol.AddPaintingPacket pkk = (cn.nukkit.network.protocol.AddPaintingPacket) pk;
        this.x = pkk.x;
        this.y = pkk.y;
        this.z = pkk.z;
        this.entityUniqueId = pkk.entityUniqueId;
        this.entityRuntimeId = pkk.entityRuntimeId;
        this.unknown = pkk.direction;
        return this;
    }

    @Override
    public cn.nukkit.network.protocol.AddPaintingPacket toDefault() {
        cn.nukkit.network.protocol.AddPaintingPacket pkk = new cn.nukkit.network.protocol.AddPaintingPacket();
        pkk.x = x;
        pkk.y = y;
        pkk.z = z;
        pkk.entityUniqueId = entityUniqueId;
        pkk.entityRuntimeId = entityRuntimeId;
        pkk.direction = this.unknown;
        return pkk;
    }

    public static Class<? extends DataPacket> getDefaultPacket() {
        return cn.nukkit.network.protocol.AddPaintingPacket.class;
    }
}
