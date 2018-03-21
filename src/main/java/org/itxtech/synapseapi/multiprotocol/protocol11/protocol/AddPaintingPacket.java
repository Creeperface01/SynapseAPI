package org.itxtech.synapseapi.multiprotocol.protocol11.protocol;

import cn.nukkit.network.protocol.DataPacket;
import org.itxtech.synapseapi.utils.ClassUtils;

/**
 * @author Nukkit Project Team
 */
public class AddPaintingPacket extends Packet11 {

    public static final byte NETWORK_ID = ProtocolInfo.ADD_PAINTING_PACKET;

    public long entityUniqueId;
    public long entityRuntimeId;
    public int x;
    public int y;
    public int z;
    public int direction;
    public String title;

    @Override
    public void decode() {

    }

    @Override
    public void encode() {
        this.reset();
        this.putVarLong(this.entityUniqueId);
        this.putVarLong(this.entityRuntimeId);
        this.putBlockVector3(this.x, this.y, this.z);
        this.putVarInt(this.direction);
        this.putString(this.title);
    }

    @Override
    public AddPaintingPacket fromDefault(DataPacket pk) {
        ClassUtils.requireInstance(pk, cn.nukkit.network.protocol.AddPaintingPacket.class);

        cn.nukkit.network.protocol.AddPaintingPacket pkk = (cn.nukkit.network.protocol.AddPaintingPacket) pk;
        this.x = pkk.x;
        this.y = pkk.y;
        this.z = pkk.z;
        this.entityRuntimeId = pkk.entityRuntimeId;
        this.entityUniqueId = pkk.entityUniqueId;
        this.direction = pkk.direction;
        this.title = pkk.title;
        return this;
    }

    @Override
    public cn.nukkit.network.protocol.AddPaintingPacket toDefault() {
        cn.nukkit.network.protocol.AddPaintingPacket pkk = new cn.nukkit.network.protocol.AddPaintingPacket();
        pkk.x = x;
        pkk.y = y;
        pkk.z = z;
        pkk.entityRuntimeId = entityRuntimeId;
        pkk.entityUniqueId = entityUniqueId;
        pkk.direction = direction;
        pkk.title = title;
        return pkk;
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    public static Class<? extends DataPacket> getDefaultPacket() {
        return cn.nukkit.network.protocol.AddPaintingPacket.class;
    }
}
