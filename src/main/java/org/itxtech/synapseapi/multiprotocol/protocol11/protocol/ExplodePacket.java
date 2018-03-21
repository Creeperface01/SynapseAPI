package org.itxtech.synapseapi.multiprotocol.protocol11.protocol;

import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.DataPacket;
import org.itxtech.synapseapi.utils.ClassUtils;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class ExplodePacket extends Packet11 {

    public static final byte NETWORK_ID = ProtocolInfo.EXPLODE_PACKET;

    public float x;
    public float y;
    public float z;
    public float radius;
    public Vector3[] records = new Vector3[0];

    public ExplodePacket fromDefault(DataPacket pkk) {
        ClassUtils.requireInstance(pkk, cn.nukkit.network.protocol.ExplodePacket.class);

        cn.nukkit.network.protocol.ExplodePacket pk = (cn.nukkit.network.protocol.ExplodePacket) pkk;
        this.x = pk.x;
        this.y = pk.y;
        this.z = pk.z;
        this.radius = pk.radius;
        this.records = pk.records;
        return this;
    }

    public cn.nukkit.network.protocol.ExplodePacket toDefault() {
        cn.nukkit.network.protocol.ExplodePacket pk = new cn.nukkit.network.protocol.ExplodePacket();
        pk.x = x;
        pk.y = y;
        pk.z = z;
        pk.radius = radius;
        pk.records = records;
        return pk;
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public DataPacket clean() {
        this.records = new Vector3[0];
        return super.clean();
    }

    @Override
    public void decode() {

    }

    @Override
    public void encode() {
        this.reset();
        this.putVector3f(this.x, this.y, this.z);
        this.putVarInt((int) (this.radius * 32));
        this.putUnsignedVarInt(this.records.length);
        if (this.records.length > 0) {
            for (Vector3 record : records) {
                this.putBlockVector3((int) record.x, (int) record.y, (int) record.z);
            }
        }
    }

    public static Class<? extends DataPacket> getDefaultPacket() {
        return cn.nukkit.network.protocol.ExplodePacket.class;
    }
}
