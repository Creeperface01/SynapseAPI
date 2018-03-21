package org.itxtech.synapseapi.multiprotocol.protocol11.protocol;

import cn.nukkit.entity.Attribute;
import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.utils.Binary;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class AddEntityPacket extends Packet11 {
    public static final byte NETWORK_ID = ProtocolInfo.ADD_ENTITY_PACKET;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    public long entityUniqueId;
    public long entityRuntimeId;
    public int type;
    public float x;
    public float y;
    public float z;
    public float speedX = 0f;
    public float speedY = 0f;
    public float speedZ = 0f;
    public float yaw;
    public float pitch;
    public EntityMetadata metadata = new EntityMetadata();
    public Attribute[] attributes = new Attribute[0];
    public Object[][] links = new Object[0][3];

    @Override
    public void decode() {

    }

    @Override
    public void encode() {
        this.reset();
        this.putVarLong(this.entityUniqueId);
        this.putVarLong(this.entityRuntimeId);
        this.putUnsignedVarInt(this.type);
        this.putVector3f(this.x, this.y, this.z);
        this.putVector3f(this.speedX, this.speedY, this.speedZ);
        this.putLFloat(this.pitch);
        this.putLFloat(this.yaw);
        this.putAttributeList(this.attributes);
        this.put(Binary.writeMetadata(this.metadata));
        this.putUnsignedVarInt(this.links.length);
        for (Object[] link : this.links) {
            this.putVarLong((long) link[0]);
            this.putVarLong((long) link[1]);
            this.putByte((byte) link[2]);
        }
    }

    @Override
    public AddEntityPacket fromDefault(DataPacket pk) {
        cn.nukkit.network.protocol.AddEntityPacket apk = (cn.nukkit.network.protocol.AddEntityPacket) pk;
        this.entityRuntimeId = apk.entityRuntimeId;
        this.entityUniqueId = apk.entityUniqueId;
        this.type = apk.type;
        this.x = apk.x;
        this.y = apk.y;
        this.z = apk.z;
        this.speedX = apk.speedX;
        this.speedY = apk.speedY;
        this.speedZ = apk.speedZ;
        this.yaw = apk.yaw;
        this.pitch = apk.pitch;
        this.metadata = apk.metadata;
        this.attributes = apk.attributes;
        this.links = apk.links;
        this.attributes = apk.attributes;

        return this;
    }

    @Override
    public cn.nukkit.network.protocol.AddEntityPacket toDefault() {
        cn.nukkit.network.protocol.AddEntityPacket apk = new cn.nukkit.network.protocol.AddEntityPacket();
        apk.entityRuntimeId = entityRuntimeId;
        apk.entityUniqueId = entityUniqueId;
        apk.type = type;
        apk.x = x;
        apk.y = y;
        apk.z = z;
        apk.speedX = speedX;
        apk.speedY = speedY;
        apk.speedZ = speedZ;
        apk.yaw = yaw;
        apk.pitch = pitch;
        apk.metadata = metadata;
        apk.attributes = attributes;

        /*for(int i = 0; i < links.length; i++) {
            Object[] o = links[i];

            for(int j = 0; j < o.length; j++) {
                apk.links[i][j] = o[j];
            }
        }*/

        return apk;
    }

    public static Class<? extends DataPacket> getDefaultPacket() {
        return cn.nukkit.network.protocol.AddEntityPacket.class;
    }
}
