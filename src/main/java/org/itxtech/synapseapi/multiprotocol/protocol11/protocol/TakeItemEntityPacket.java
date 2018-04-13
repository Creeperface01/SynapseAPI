package org.itxtech.synapseapi.multiprotocol.protocol11.protocol;

import cn.nukkit.network.protocol.DataPacket;
import org.itxtech.synapseapi.utils.ClassUtils;

/**
 * Created on 15-10-14.
 */
public class TakeItemEntityPacket extends Packet11 {

    public static final byte NETWORK_ID = ProtocolInfo.TAKE_ITEM_ENTITY_PACKET;

    public long entityId;
    public long target;

    @Override
    public void decode() {
    }

    @Override
    public void encode() {
        this.reset();
        this.putEntityRuntimeId(this.target);
        this.putEntityRuntimeId(this.entityId);
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public DataPacket toDefault() {
        cn.nukkit.network.protocol.TakeItemEntityPacket pk = new cn.nukkit.network.protocol.TakeItemEntityPacket();
        pk.target = this.target;
        pk.entityId = this.entityId;
        return pk;
    }

    @Override
    public DataPacket fromDefault(DataPacket pk) {
        ClassUtils.requireInstance(pk, cn.nukkit.network.protocol.TakeItemEntityPacket.class);

        cn.nukkit.network.protocol.TakeItemEntityPacket packet = (cn.nukkit.network.protocol.TakeItemEntityPacket) pk;
        this.target = packet.target;
        this.entityId = packet.entityId;
        return this;
    }

    public static Class<? extends DataPacket> getDefaultPacket() {
        return cn.nukkit.network.protocol.TakeItemEntityPacket.class;
    }
}
