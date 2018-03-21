package org.itxtech.synapseapi.multiprotocol.protocol11.protocol;

import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.item.Item;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.utils.Binary;
import org.itxtech.synapseapi.utils.ClassUtils;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class AddItemEntityPacket extends Packet11 {
    public static final byte NETWORK_ID = ProtocolInfo.ADD_ITEM_ENTITY_PACKET;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    public long entityUniqueId;
    public long entityRuntimeId;
    public Item item;
    public float x;
    public float y;
    public float z;
    public float speedX;
    public float speedY;
    public float speedZ;
    public EntityMetadata metadata = new EntityMetadata();

    @Override
    public void decode() {

    }

    @Override
    public void encode() {
        this.reset();
        this.putVarLong(this.entityUniqueId);
        this.putVarLong(this.entityRuntimeId);
        this.putSlot(this.item);
        this.putVector3f(this.x, this.y, this.z);
        this.putVector3f(this.speedX, this.speedY, this.speedZ);
        this.put(Binary.writeMetadata(metadata));
    }

    @Override
    public AddItemEntityPacket fromDefault(DataPacket pk) {
        ClassUtils.requireInstance(pk, cn.nukkit.network.protocol.AddItemEntityPacket.class);

        cn.nukkit.network.protocol.AddItemEntityPacket pkk = (cn.nukkit.network.protocol.AddItemEntityPacket) pk;
        this.x = pkk.x;
        this.y = pkk.y;
        this.z = pkk.z;
        this.entityRuntimeId = pkk.entityRuntimeId;
        this.entityUniqueId = pkk.entityUniqueId;
        this.item = pkk.item;
        this.speedX = pkk.speedX;
        this.speedY = pkk.speedY;
        this.speedZ = pkk.speedZ;
        this.metadata = pkk.metadata;
        return this;
    }

    @Override
    public cn.nukkit.network.protocol.AddItemEntityPacket toDefault() {
        cn.nukkit.network.protocol.AddItemEntityPacket pkk = new cn.nukkit.network.protocol.AddItemEntityPacket();
        pkk.x = x;
        pkk.y = y;
        pkk.z = z;
        pkk.entityRuntimeId = entityRuntimeId;
        pkk.entityUniqueId = entityUniqueId;
        pkk.speedX = speedX;
        pkk.speedY = speedY;
        pkk.speedZ = speedZ;
        pkk.metadata = metadata;
        return pkk;
    }

    public static Class<? extends DataPacket> getDefaultPacket() {
        return cn.nukkit.network.protocol.AddItemEntityPacket.class;
    }
}
