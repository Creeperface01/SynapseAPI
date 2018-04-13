package org.itxtech.synapseapi.multiprotocol.protocol11.protocol;

import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.utils.Binary;
import org.itxtech.synapseapi.utils.ClassUtils;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class SetEntityDataPacket extends Packet11 {
    public static final byte NETWORK_ID = ProtocolInfo.SET_ENTITY_DATA_PACKET;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    public long eid;
    public EntityMetadata metadata;

    public DataPacket fromDefault(DataPacket pkk) {
        ClassUtils.requireInstance(pkk, cn.nukkit.network.protocol.SetEntityDataPacket.class);

        cn.nukkit.network.protocol.SetEntityDataPacket pk = (cn.nukkit.network.protocol.SetEntityDataPacket) pkk;
        this.eid = pk.eid;
        this.metadata = pk.metadata;
        return pkk;
    }

    public DataPacket toDefault() {
        cn.nukkit.network.protocol.SetEntityDataPacket pk = new cn.nukkit.network.protocol.SetEntityDataPacket();
        pk.eid = eid;
        pk.metadata = metadata;
        return pk;
    }

    @Override
    public void decode() {

    }

    @Override
    public void encode() {
        this.reset();
        this.putEntityRuntimeId(this.eid);
        this.put(Binary.writeMetadata(this.metadata));
    }

    @Override
    public boolean needConversion() {
        return false;
    }

    public static Class<? extends DataPacket> getDefaultPacket() {
        return cn.nukkit.network.protocol.SetEntityDataPacket.class;
    }
}
