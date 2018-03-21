package org.itxtech.synapseapi.multiprotocol.protocol11.protocol;

import cn.nukkit.network.protocol.DataPacket;
import org.itxtech.synapseapi.utils.ClassUtils;

public class StopSoundPacket extends Packet11 {

    public static final byte NETWORK_ID = ProtocolInfo.STOP_SOUND_PACKET;

    public String name;
    public boolean stopAll;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public void decode() {

    }

    @Override
    public void encode() {
        this.putString(this.name);
        this.putBoolean(this.stopAll);
    }

    @Override
    public DataPacket fromDefault(DataPacket pk) {
        ClassUtils.requireInstance(pk, cn.nukkit.network.protocol.StopSoundPacket.class);
        cn.nukkit.network.protocol.StopSoundPacket packet = (cn.nukkit.network.protocol.StopSoundPacket) pk;

        this.name = packet.name;
        this.stopAll = packet.stopAll;
        return this;
    }

    @Override
    public DataPacket toDefault() {
        cn.nukkit.network.protocol.StopSoundPacket pk = new cn.nukkit.network.protocol.StopSoundPacket();
        pk.name = this.name;
        pk.stopAll = this.stopAll;

        return pk;
    }

    public static Class<? extends DataPacket> getDefaultPacket() {
        return cn.nukkit.network.protocol.StopSoundPacket.class;
    }
}
