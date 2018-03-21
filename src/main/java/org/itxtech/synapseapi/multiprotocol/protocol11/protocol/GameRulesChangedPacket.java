package org.itxtech.synapseapi.multiprotocol.protocol11.protocol;

import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.utils.RuleData;
import org.itxtech.synapseapi.utils.ClassUtils;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class GameRulesChangedPacket extends Packet11 {
    public static final byte NETWORK_ID = ProtocolInfo.GAME_RULES_CHANGED_PACKET;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    public RuleData[] ruleDatas = new RuleData[0];

    public GameRulesChangedPacket fromDefault(DataPacket pkk) {
        ClassUtils.requireInstance(pkk, cn.nukkit.network.protocol.GameRulesChangedPacket.class);

        cn.nukkit.network.protocol.GameRulesChangedPacket pk = (cn.nukkit.network.protocol.GameRulesChangedPacket) pkk;
        this.ruleDatas = pk.ruleDatas;
        return this;
    }

    public cn.nukkit.network.protocol.GameRulesChangedPacket toDefault() {
        cn.nukkit.network.protocol.GameRulesChangedPacket pk = new cn.nukkit.network.protocol.GameRulesChangedPacket();
        pk.ruleDatas = ruleDatas;
        return pk;
    }

    @Override
    public void decode() {
    }

    @Override
    public void encode() {
        this.reset();
        this.putInt(this.ruleDatas.length);
        for (RuleData rule : this.ruleDatas) {
            this.putRuleData(rule);
        }
    }

    public static Class<? extends DataPacket> getDefaultPacket() {
        return cn.nukkit.network.protocol.GameRulesChangedPacket.class;
    }
}
