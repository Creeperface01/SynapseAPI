package org.itxtech.synapseapi.multiprotocol.protocol11.protocol;

import cn.nukkit.network.protocol.DataPacket;
import org.itxtech.synapseapi.utils.ClassUtils;

/**
 * @author Nukkit Project Team
 */
public class AdventureSettingsPacket extends Packet11 {
    public static final byte NETWORK_ID = ProtocolInfo.ADVENTURE_SETTINGS_PACKET;

    public boolean worldImmutable;
    public boolean noPvp;
    public boolean noPvm;
    public boolean noMvp;

    public boolean autoJump;
    public boolean allowFlight;
    public boolean noClip;
    public boolean worldBuilder;
    public boolean isFlying;
    public boolean muted;

    /*
     bit mask | flag name
	0x00000001 world_immutable
	0x00000002 no_pvp
	0x00000004 no_pvm
	0x00000008 no_mvp
	0x00000010 ?
	0x00000020 auto_jump
	0x00000040 allow_fly
	0x00000080 noclip
	0x00000100 ?
	0x00000200 is_flying
	*/

    public int flags = 0;
    public int userPermission;

    public AdventureSettingsPacket fromDefault(DataPacket pkk) {
        ClassUtils.requireInstance(pkk, cn.nukkit.network.protocol.AdventureSettingsPacket.class);

        cn.nukkit.network.protocol.AdventureSettingsPacket pk = (cn.nukkit.network.protocol.AdventureSettingsPacket) pkk;
        this.worldImmutable = pk.getFlag(cn.nukkit.network.protocol.AdventureSettingsPacket.WORLD_IMMUTABLE);
        this.noClip = pk.getFlag(cn.nukkit.network.protocol.AdventureSettingsPacket.NO_CLIP);
        this.autoJump = pk.getFlag(cn.nukkit.network.protocol.AdventureSettingsPacket.AUTO_JUMP);
        this.allowFlight = pk.getFlag(cn.nukkit.network.protocol.AdventureSettingsPacket.ALLOW_FLIGHT);
        this.worldBuilder = pk.getFlag(cn.nukkit.network.protocol.AdventureSettingsPacket.WORLD_BUILDER);
        this.isFlying = pk.getFlag(cn.nukkit.network.protocol.AdventureSettingsPacket.FLYING);
        this.muted = pk.getFlag(cn.nukkit.network.protocol.AdventureSettingsPacket.MUTED);
        this.userPermission = (int) pk.playerPermission;
        return this;
    }

    public cn.nukkit.network.protocol.AdventureSettingsPacket toDefault() {
        cn.nukkit.network.protocol.AdventureSettingsPacket pk = new cn.nukkit.network.protocol.AdventureSettingsPacket();
        pk.setFlag(cn.nukkit.network.protocol.AdventureSettingsPacket.WORLD_IMMUTABLE, this.worldImmutable);
        pk.setFlag(cn.nukkit.network.protocol.AdventureSettingsPacket.NO_CLIP, this.noClip);
        pk.setFlag(cn.nukkit.network.protocol.AdventureSettingsPacket.ATTACK_MOBS, true);
        pk.setFlag(cn.nukkit.network.protocol.AdventureSettingsPacket.ATTACK_PLAYERS, true);
        pk.setFlag(cn.nukkit.network.protocol.AdventureSettingsPacket.AUTO_JUMP, this.autoJump);
        pk.setFlag(cn.nukkit.network.protocol.AdventureSettingsPacket.ALLOW_FLIGHT, this.allowFlight);
        pk.setFlag(cn.nukkit.network.protocol.AdventureSettingsPacket.WORLD_BUILDER, this.worldBuilder);
        pk.setFlag(cn.nukkit.network.protocol.AdventureSettingsPacket.FLYING, this.isFlying);
        pk.setFlag(cn.nukkit.network.protocol.AdventureSettingsPacket.MUTED, this.muted);
        pk.playerPermission = this.userPermission;
        pk.commandPermission = pk.playerPermission >= 2 ? cn.nukkit.network.protocol.AdventureSettingsPacket.PERMISSION_OPERATOR : cn.nukkit.network.protocol.AdventureSettingsPacket.PERMISSION_NORMAL;

        return pk;
    }

    @Override
    public void decode() {
        this.flags = (int) this.getUnsignedVarInt();
        this.userPermission = (int) this.getUnsignedVarInt();
        this.worldImmutable = (this.flags & 1) != 0;
        this.noPvp = (this.flags & (1 << 1)) != 0;
        this.noPvm = (this.flags & (1 << 2)) != 0;
        this.noMvp = (this.flags & (1 << 3)) != 0;

        this.autoJump = (this.flags & (1 << 5)) != 0;
        this.allowFlight = (this.flags & (1 << 6)) != 0;
        this.noClip = (this.flags & (1 << 7)) != 0;
        this.worldBuilder = (this.flags & (1 << 8)) != 0;
        this.isFlying = (this.flags & (1 << 9)) != 0;
        this.muted = (this.flags & (1 << 10)) != 0;
    }

    @Override
    public void encode() {
        this.reset();
        if (this.worldImmutable) this.flags |= 1;
        if (this.noPvp) this.flags |= 1 << 1;
        if (this.noPvm) this.flags |= 1 << 2;
        if (this.noMvp) this.flags |= 1 << 3;

        if (this.autoJump) this.flags |= 1 << 5;
        if (this.allowFlight) this.flags |= 1 << 6;
        if (this.noClip) this.flags |= 1 << 7;
        if (this.worldBuilder) this.flags |= 1 << 8;
        if (this.isFlying) this.flags |= 1 << 9;
        if (this.muted) this.flags |= 1 << 10;
        this.putUnsignedVarInt(this.flags);
        this.putUnsignedVarInt(this.userPermission);
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    public static Class<? extends DataPacket> getDefaultPacket() {
        return cn.nukkit.network.protocol.AdventureSettingsPacket.class;
    }
}
