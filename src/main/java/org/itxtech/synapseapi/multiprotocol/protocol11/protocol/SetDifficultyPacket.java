package org.itxtech.synapseapi.multiprotocol.protocol11.protocol;

import cn.nukkit.network.protocol.DataPacket;
import org.itxtech.synapseapi.utils.ClassUtils;

/**
 * @author Nukkit Project Team
 */
public class SetDifficultyPacket extends Packet11 {

    public static final byte NETWORK_ID = ProtocolInfo.SET_DIFFICULTY_PACKET;

    public int difficulty;

    public SetDifficultyPacket fromDefault(DataPacket pkk) {
        ClassUtils.requireInstance(pkk, SetDifficultyPacket.class);

        cn.nukkit.network.protocol.SetDifficultyPacket pk = (cn.nukkit.network.protocol.SetDifficultyPacket) pkk;
        this.difficulty = pk.difficulty;
        return this;
    }

    public cn.nukkit.network.protocol.SetDifficultyPacket toDefault() {
        cn.nukkit.network.protocol.SetDifficultyPacket pk = new cn.nukkit.network.protocol.SetDifficultyPacket();
        pk.difficulty = difficulty;
        return pk;
    }

    @Override
    public void decode() {
        this.difficulty = (int) this.getUnsignedVarInt();
    }

    @Override
    public void encode() {
        this.reset();
        this.putUnsignedVarInt(this.difficulty);
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    public static Class<? extends DataPacket> getDefaultPacket() {
        return cn.nukkit.network.protocol.SetDifficultyPacket.class;
    }
}
