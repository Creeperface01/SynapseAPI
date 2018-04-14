package org.itxtech.synapseapi.multiprotocol.protocol11.protocol;

import cn.nukkit.entity.data.Skin;
import cn.nukkit.network.protocol.DataPacket;
import org.itxtech.synapseapi.utils.ClassUtils;

import java.util.UUID;

/**
 * @author Nukkit Project Team
 */
public class PlayerListPacket extends Packet11 {

    public static final byte NETWORK_ID = ProtocolInfo.PLAYER_LIST_PACKET;

    public static final byte TYPE_ADD = 0;
    public static final byte TYPE_REMOVE = 1;

    public byte type;
    public Entry[] entries = new Entry[0];

    public PlayerListPacket fromDefault(DataPacket pkk) {
        ClassUtils.requireInstance(pkk, cn.nukkit.network.protocol.PlayerListPacket.class);

        cn.nukkit.network.protocol.PlayerListPacket pk = (cn.nukkit.network.protocol.PlayerListPacket) pkk;
        this.type = pk.type;
        this.entries = new Entry[pk.entries.length];
        for (int i = 0; i < this.entries.length; i++) {
            cn.nukkit.network.protocol.PlayerListPacket.Entry e = pk.entries[i];

            Entry entry = new Entry(e.uuid, e.entityId, e.name, e.skin);
            this.entries[i] = entry;
        }
        return this;
    }

    public cn.nukkit.network.protocol.PlayerListPacket toDefault() {
        cn.nukkit.network.protocol.PlayerListPacket pk = new cn.nukkit.network.protocol.PlayerListPacket();
        pk.type = type;
        pk.entries = new cn.nukkit.network.protocol.PlayerListPacket.Entry[entries.length];

        for (int i = 0; i < this.entries.length; i++) {
            Entry e = this.entries[i];

            cn.nukkit.network.protocol.PlayerListPacket.Entry entry = new cn.nukkit.network.protocol.PlayerListPacket.Entry(e.uuid, e.entityId, e.name, e.skin);
            pk.entries[i] = entry;
        }
        return pk;
    }

    @Override
    public void decode() {

    }

    @Override
    public void encode() {
        this.reset();
        this.putByte(this.type);
        this.putUnsignedVarInt(this.entries.length);
        for (Entry entry : this.entries) {
            this.putUUID(entry.uuid);

            if (type == TYPE_ADD) {
                this.putEntityUniqueId(entry.entityId);
                this.putString(entry.name);

                this.putString("Standard_Custom");
                this.putByteArray(entry.skin.getData());
            }
        }

    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    public static class Entry {

        public final UUID uuid;
        public long entityId = 0;
        public String name = "";
        public Skin skin;

        public Entry(UUID uuid) {
            this.uuid = uuid;
        }

        public Entry(UUID uuid, long entityId, String name, Skin skin) {
            this.uuid = uuid;
            this.entityId = entityId;
            this.name = name;
            this.skin = skin;
        }
    }

    public static Class<? extends DataPacket> getDefaultPacket() {
        return cn.nukkit.network.protocol.PlayerListPacket.class;
    }
}
