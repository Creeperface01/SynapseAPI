package org.itxtech.synapseapi.multiprotocol.protocol11.protocol;

import cn.nukkit.network.protocol.DataPacket;
import org.itxtech.synapseapi.utils.ClassUtils;

/**
 * @author Nukkit Project Team
 */
public class AnimatePacket extends Packet11 {

    public static final byte NETWORK_ID = ProtocolInfo.ANIMATE_PACKET;

    public long eid;
    public int action;
    public float unknown;

    public AnimatePacket fromDefault(DataPacket pkk) {
        ClassUtils.requireInstance(pkk, cn.nukkit.network.protocol.AnimatePacket.class);

        cn.nukkit.network.protocol.AnimatePacket pk = (cn.nukkit.network.protocol.AnimatePacket) pkk;
        this.eid = pk.eid;
        this.action = pk.action.getId();
        this.unknown = pk.rowingTime;
        return this;
    }

    public cn.nukkit.network.protocol.AnimatePacket toDefault() {
        cn.nukkit.network.protocol.AnimatePacket pk = new cn.nukkit.network.protocol.AnimatePacket();
        pk.eid = eid;
        pk.action = cn.nukkit.network.protocol.AnimatePacket.Action.fromId(action);
        pk.rowingTime = unknown;
        return pk;
    }

    @Override
    public void decode() {
        this.action = this.getVarInt();
        this.eid = getEntityRuntimeId();
        if ((this.action & 0x80) != 0) {
            this.unknown = this.getLFloat();
        }
    }

    @Override
    public void encode() {
        this.reset();
        this.putVarInt(this.action);
        this.putEntityRuntimeId(this.eid);
        if ((this.action & 0x80) != 0) {
            this.putLFloat(this.unknown);
        }
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    public static Class<? extends DataPacket> getDefaultPacket() {
        return cn.nukkit.network.protocol.AnimatePacket.class;
    }
}
