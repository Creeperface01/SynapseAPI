package org.itxtech.synapseapi.multiprotocol.protocol11;

import cn.nukkit.AdventureSettings;
import cn.nukkit.Player;
import org.itxtech.synapseapi.multiprotocol.protocol11.protocol.AdventureSettingsPacket;

/**
 * @author CreeperFace
 */
public class AdventureSettings11 extends AdventureSettings {

    public static final int PERMISSION_NORMAL = 0;
    public static final int PERMISSION_OPERATOR = 1;
    public static final int PERMISSION_HOST = 2;
    public static final int PERMISSION_AUTOMATION = 3;
    public static final int PERMISSION_ADMIN = 4;

    public Player player;

    public AdventureSettings11(Player p) {
        super(p);
        this.player = p;
    }

    public void setCanDestroyBlock(boolean canDestroyBlock) {
        this.get(Type.WORLD_IMMUTABLE);
    }

    public void setAutoJump(boolean autoJump) {
        set(Type.AUTO_JUMP, autoJump);
    }

    public void setCanFly(boolean canFly) {
        set(Type.ALLOW_FLIGHT, canFly);
    }

    public void setFlying(boolean flying) {
        set(Type.FLYING, flying);
    }

    public void setNoclip(boolean noclip) {
        set(Type.NO_CLIP, noclip);
    }

    public void setNoPvp(boolean noPvp) {
        set(Type.ATTACK_PLAYERS, !noPvp);
    }

    public boolean canDestroyBlock() {
        return get(Type.WORLD_IMMUTABLE);
    }

    public boolean isAutoJumpEnabled() {
        return get(Type.AUTO_JUMP);
    }

    public boolean canFly() {
        return get(Type.ALLOW_FLIGHT);
    }

    public boolean isFlying() {
        return get(Type.FLYING);
    }

    public boolean isNoclipEnabled() {
        return get(Type.NO_CLIP);
    }

    public boolean isMuted() {
        return get(Type.MUTED);
    }

    public void update() {
        AdventureSettingsPacket pk = new AdventureSettingsPacket();
        pk.flags = 0;
        pk.worldImmutable = get(Type.WORLD_IMMUTABLE);
        pk.autoJump = get(Type.AUTO_JUMP);
        pk.allowFlight = get(Type.ALLOW_FLIGHT);
        pk.noClip = get(Type.NO_CLIP);
        pk.isFlying = get(Type.FLYING);
        pk.noPvp = get(Type.ATTACK_PLAYERS);
        pk.noPvm = get(Type.ATTACK_MOBS);
        pk.noMvp = false;
        pk.muted = get(Type.MUTED);
        pk.userPermission = (this.player.isOp() ? PERMISSION_OPERATOR : PERMISSION_NORMAL);

        player.dataPacket(pk);

        player.resetInAirTicks();
    }

    public static AdventureSettings11 from(Player p, AdventureSettings settings) {
        AdventureSettings11 s = new AdventureSettings11(p);

        for (Type t : Type.values()) {
            s.set(t, settings.get(t));
        }

        return s;
    }

    public static class Builder {
        private final AdventureSettings11 settings;

        public Builder(Player player) {
            if (player == null) {
                throw new IllegalArgumentException("Player can not be null.");
            }

            settings = new AdventureSettings11(player);
        }

        public Builder canFly(boolean can) {
            settings.setCanFly(can);
            return this;
        }

        public Builder noclip(boolean noclip) {
            settings.setNoclip(noclip);
            return this;
        }

        public Builder canDestroyBlock(boolean can) {
            settings.setCanDestroyBlock(can);
            return this;
        }

        public Builder autoJump(boolean autoJump) {
            settings.setAutoJump(autoJump);
            return this;
        }

        public AdventureSettings build() {
            return this.settings;
        }
    }
}
