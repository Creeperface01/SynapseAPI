package org.itxtech.synapseapi.multiprotocol.protocol11.protocol;

import cn.nukkit.network.protocol.DataPacket;
import org.itxtech.synapseapi.utils.ClassUtils;

// A wild TransferPacket appeared!
public class TransferPacket extends Packet11 {
    public static final byte NETWORK_ID = ProtocolInfo.TRANSFER_PACKET;

    public String address; // Server address
    public int port = 19132; // Server port

    @Override
    public void decode() {
        this.address = this.getString();
        this.port = (short) this.getLShort();
    }

    @Override
    public void encode() {
        this.reset();
        this.putString(address);
        this.putLShort(port);
    }

    @Override
    public byte pid() {
        return ProtocolInfo.TRANSFER_PACKET;
    }

    @Override
    public DataPacket toDefault() {
        cn.nukkit.network.protocol.TransferPacket pk = new cn.nukkit.network.protocol.TransferPacket();
        pk.address = this.address;
        pk.port = this.port;

        return pk;
    }

    @Override
    public DataPacket fromDefault(DataPacket pk) {
        ClassUtils.requireInstance(pk, cn.nukkit.network.protocol.TransferPacket.class);

        cn.nukkit.network.protocol.TransferPacket packet = (cn.nukkit.network.protocol.TransferPacket) pk;
        this.address = packet.address;
        this.port = packet.port;

        return this;
    }

    public static Class<? extends DataPacket> getDefaultPacket() {
        return cn.nukkit.network.protocol.TransferPacket.class;
    }
}
