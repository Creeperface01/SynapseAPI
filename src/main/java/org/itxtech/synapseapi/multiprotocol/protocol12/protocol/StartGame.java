package org.itxtech.synapseapi.multiprotocol.protocol12.protocol;

import cn.nukkit.network.protocol.StartGamePacket;
import cn.nukkit.utils.RuleData;
import org.itxtech.synapseapi.multiprotocol.PacketDecoder;
import org.itxtech.synapseapi.multiprotocol.ProtocolGroup;

/**
 * @author CreeperFace
 */
public class StartGame extends PacketDecoder<StartGamePacket> {

    public static final int NETWORK_ID = 0x0b;

    @Override
    public byte pid() {
        return StartGamePacket.NETWORK_ID;
    }

    @Override
    public byte[] encode(ProtocolGroup group, StartGamePacket pk) {
        this.reset();
        this.putEntityUniqueId(pk.entityUniqueId);
        this.putEntityRuntimeId(pk.entityRuntimeId);
        this.putVarInt(pk.playerGamemode);
        this.putVector3f(pk.x, pk.y, pk.z);
        this.putLFloat(pk.yaw);
        this.putLFloat(pk.pitch);
        this.putVarInt(pk.seed);
        this.putVarInt(pk.dimension);
        this.putVarInt(pk.generator);
        this.putVarInt(pk.worldGamemode);
        this.putVarInt(pk.difficulty);
        this.putBlockVector3(pk.spawnX, pk.spawnY, pk.spawnZ);
        this.putBoolean(pk.hasAchievementsDisabled);
        this.putVarInt(pk.dayCycleStopTime);
        this.putBoolean(pk.eduMode);

        if (group.ordinal() >= ProtocolGroup.PROTOCOL_14.ordinal()) {
            this.putBoolean(false); //edu features
        }

        this.putLFloat(pk.rainLevel);
        this.putLFloat(pk.lightningLevel);
        this.putBoolean(pk.multiplayerGame);
        this.putBoolean(pk.broadcastToLAN);
        this.putBoolean(pk.broadcastToXboxLive);
        this.putBoolean(pk.commandsEnabled);
        this.putBoolean(pk.isTexturePacksRequired);
        this.putUnsignedVarInt(pk.ruleDatas.length);
        for (RuleData rule : pk.ruleDatas) {
            this.putRuleData(rule);
        }
        this.putBoolean(pk.bonusChest);

        if (group.ordinal() >= ProtocolGroup.PROTOCOL_14.ordinal()) {
            putBoolean(false); //start with map enabled
        }

        this.putBoolean(pk.trustPlayers);
        this.putVarInt(pk.permissionLevel);
        this.putVarInt(pk.gamePublish);

        if (group.ordinal() >= ProtocolGroup.PROTOCOL_1210.ordinal()) {
            putLInt(0); //chunk tick range
        }

        if (group.ordinal() >= ProtocolGroup.PROTOCOL_1213.ordinal()) {
            putBoolean(false); //broadcast to platform
            putVarInt(0); //platform broadcast mode
            putBoolean(false); //xbl broadcast intent
        }

        if (group.ordinal() >= ProtocolGroup.PROTOCOL_14.ordinal()) {
            putBoolean(false); //locked behavior pack
            putBoolean(false); //locked resource pack
            putBoolean(false); //from locked world template
        }

        this.putString(pk.levelId);
        this.putString(pk.worldName);
        this.putString(pk.premiumWorldTemplateId);
        this.putBoolean(pk.unknown); //is trial
        this.putLLong(pk.currentTick);
        this.putVarInt(pk.enchantmentSeed);

        pk.setBuffer(getBuffer());
        return getBuffer();
    }

    @Override
    public void decode(ProtocolGroup group, StartGamePacket pk) {
        pk.decode();
    }
}
