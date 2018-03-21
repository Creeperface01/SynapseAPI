package org.itxtech.synapseapi.multiprotocol.protocol11.protocol;

import cn.nukkit.network.protocol.DataPacket;
import org.itxtech.synapseapi.utils.ClassUtils;

public class SetCommandsEnabledPacket extends Packet11 {

    public static final byte NETWORK_ID = ProtocolInfo.SET_COMMANDS_ENABLED_PACKET;

    public boolean enabled;

    public SetCommandsEnabledPacket fromDefault(DataPacket pkk) {
        ClassUtils.requireInstance(pkk, cn.nukkit.network.protocol.SetCommandsEnabledPacket.class);

        cn.nukkit.network.protocol.SetCommandsEnabledPacket pk = (cn.nukkit.network.protocol.SetCommandsEnabledPacket) pkk;
        this.enabled = pk.enabled;
        return this;
    }

    public cn.nukkit.network.protocol.SetCommandsEnabledPacket toDefault() {
        cn.nukkit.network.protocol.SetCommandsEnabledPacket pk = new cn.nukkit.network.protocol.SetCommandsEnabledPacket();
        pk.enabled = enabled;
        return pk;
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public void decode() {

    }

    @Override
    public void encode() {
        this.reset();
        this.putBoolean(this.enabled);
    }

    public static Class<? extends DataPacket> getDefaultPacket() {
        return cn.nukkit.network.protocol.SetCommandsEnabledPacket.class;
    }
}
