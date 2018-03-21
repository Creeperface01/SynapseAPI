package org.itxtech.synapseapi.multiprotocol.protocol11.protocol;

import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.item.Item;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.utils.Binary;
import org.itxtech.synapseapi.utils.ClassUtils;

import java.util.UUID;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class AddPlayerPacket extends Packet11 {
    public static final byte NETWORK_ID = ProtocolInfo.ADD_PLAYER_PACKET;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    public UUID uuid;
    public String username;
    public long entityUniqueId;
    public long entityRuntimeId;
    public float x;
    public float y;
    public float z;
    public float speedX;
    public float speedY;
    public float speedZ;
    public float pitch;
    public float yaw;
    public Item item;
    public EntityMetadata metadata = new EntityMetadata();

    @Override
    public void decode() {

    }

    @Override
    public void encode() {
        this.reset();
        this.putUUID(this.uuid);
        this.putString(this.username);
        this.putVarLong(this.entityUniqueId);
        this.putVarLong(this.entityRuntimeId);
        this.putVector3f(this.x, this.y, this.z);
        this.putVector3f(this.speedX, this.speedY, this.speedZ);
        this.putLFloat(this.pitch);
        this.putLFloat(this.yaw); //TODO headrot
        this.putLFloat(this.yaw);
        this.putSlot(this.item);

        this.put(Binary.writeMetadata(this.metadata));
    }

    public AddPlayerPacket fromDefault(DataPacket pkk) {
        ClassUtils.requireInstance(pkk, cn.nukkit.network.protocol.AddPlayerPacket.class);

        cn.nukkit.network.protocol.AddPlayerPacket pk = (cn.nukkit.network.protocol.AddPlayerPacket) pkk;
        this.x = pk.x;
        this.y = pk.y;
        this.z = pk.z;
        this.uuid = pk.uuid;
        this.username = pk.username;
        this.entityRuntimeId = pk.entityRuntimeId;
        this.entityUniqueId = pk.entityUniqueId;
        this.speedX = pk.speedX;
        this.speedY = pk.speedY;
        this.speedZ = pk.speedZ;
        this.yaw = pk.yaw;
        this.pitch = pk.pitch;
        this.metadata = pk.metadata;
        return this;
    }

    public cn.nukkit.network.protocol.AddPlayerPacket toDefault() {
        cn.nukkit.network.protocol.AddPlayerPacket pk = new cn.nukkit.network.protocol.AddPlayerPacket();
        pk.x = x;
        pk.y = y;
        pk.z = z;
        pk.uuid = uuid;
        pk.username = username;
        pk.entityRuntimeId = entityRuntimeId;
        pk.entityUniqueId = entityUniqueId;
        pk.speedX = speedX;
        pk.speedY = speedY;
        pk.speedZ = speedZ;
        pk.yaw = yaw;
        pk.pitch = pitch;
        pk.metadata = metadata;
        return pk;
    }

    public static Class<? extends DataPacket> getDefaultPacket() {
        return cn.nukkit.network.protocol.AddPlayerPacket.class;
    }
}
