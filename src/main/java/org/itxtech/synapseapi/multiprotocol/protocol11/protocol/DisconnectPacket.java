package org.itxtech.synapseapi.multiprotocol.protocol11.protocol;

import cn.nukkit.network.protocol.DataPacket;

/**
 * Created by on 15-10-12.
 */
public class DisconnectPacket extends Packet11 {
    public static final byte NETWORK_ID = ProtocolInfo.DISCONNECT_PACKET;

    public boolean hideDisconnectionScreen = false;
    public String message;

    public DisconnectPacket fromDefault(DataPacket pkk) {
        cn.nukkit.network.protocol.DisconnectPacket pk = (cn.nukkit.network.protocol.DisconnectPacket) pkk;
        this.hideDisconnectionScreen = pk.hideDisconnectionScreen;
        this.message = pk.message;
        return this;
    }

    public cn.nukkit.network.protocol.DisconnectPacket toDefault() {
        cn.nukkit.network.protocol.DisconnectPacket pk = new cn.nukkit.network.protocol.DisconnectPacket();
        pk.hideDisconnectionScreen = hideDisconnectionScreen;
        pk.message = message;
        return pk;
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public void decode() {
        this.hideDisconnectionScreen = this.getBoolean();
        this.message = this.getString();
    }

    @Override
    public void encode() {
        this.reset();
        this.putBoolean(this.hideDisconnectionScreen);
        if (!this.hideDisconnectionScreen) {
            this.putString(this.message);
        }
    }

    public static Class<? extends DataPacket> getDefaultPacket() {
        return cn.nukkit.network.protocol.DisconnectPacket.class;
    }
}
