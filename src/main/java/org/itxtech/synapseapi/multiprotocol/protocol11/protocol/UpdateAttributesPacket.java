package org.itxtech.synapseapi.multiprotocol.protocol11.protocol;

import cn.nukkit.entity.Attribute;
import cn.nukkit.network.protocol.DataPacket;

/**
 * @author Nukkit Project Team
 */
public class UpdateAttributesPacket extends Packet11 {

    public static final byte NETWORK_ID = ProtocolInfo.UPDATE_ATTRIBUTES_PACKET;

    public Attribute[] entries;
    public long entityId;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    public void decode() {

    }

    public void encode() {
        this.reset();

        this.putEntityRuntimeId(this.entityId);

        if (this.entries == null) {
            this.putUnsignedVarInt(0);
        } else {
            this.putUnsignedVarInt(this.entries.length);
            for (Attribute entry : this.entries) {
                this.putLFloat(entry.getMinValue());
                this.putLFloat(entry.getMaxValue());
                this.putLFloat(entry.getValue());
                this.putLFloat(entry.getDefaultValue());
                this.putString(entry.getName());
            }
        }
    }

    @Override
    public DataPacket fromDefault(DataPacket pk) {
        cn.nukkit.network.protocol.UpdateAttributesPacket packet = (cn.nukkit.network.protocol.UpdateAttributesPacket) pk;
        this.entries = packet.entries;
        this.entityId = packet.entityId;
        return this;
    }

    @Override
    public DataPacket toDefault() {
        cn.nukkit.network.protocol.UpdateAttributesPacket pk = new cn.nukkit.network.protocol.UpdateAttributesPacket();
        pk.entityId = this.entityId;
        pk.entries = this.entries;

        return pk;
    }

    public static Class<? extends DataPacket> getDefaultPacket() {
        return cn.nukkit.network.protocol.UpdateAttributesPacket.class;
    }
}
