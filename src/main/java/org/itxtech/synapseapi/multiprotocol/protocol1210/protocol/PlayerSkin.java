package org.itxtech.synapseapi.multiprotocol.protocol1210.protocol;

import cn.nukkit.entity.data.Skin;
import cn.nukkit.network.protocol.PlayerSkinPacket;
import cn.nukkit.network.protocol.ProtocolInfo;
import org.itxtech.synapseapi.multiprotocol.PacketDecoder;
import org.itxtech.synapseapi.multiprotocol.ProtocolGroup;

/**
 * @author CreeperFace
 */
public class PlayerSkin extends PacketDecoder<PlayerSkinPacket> {

    @Override
    public byte pid() {
        return 0x5d;
    }

    @Override
    public byte[] encode(ProtocolGroup group, PlayerSkinPacket pk) {
        this.reset();
        this.putUUID(pk.uuid);
        this.putString(pk.skin.getModel());
        this.putString(pk.skinName);
        this.putString(pk.serializeName);

        if (group.ordinal() >= ProtocolGroup.PROTOCOL_1210.ordinal()) {
            this.putInt(1);
            this.putInt(pk.skin.getData().length);
        }

        this.putByteArray(pk.skin.getData());

        if (group.ordinal() >= ProtocolGroup.PROTOCOL_1210.ordinal()) {
            putInt(pk.skin.getCape().getData().length > 0 ? 1 : 0);
            putInt(pk.skin.getCape().getData().length);
        }
        this.putByteArray(pk.skin.getCape().getData());
        this.putString(pk.geometryModel);
        this.putString(pk.geometryData);

        pk.setBuffer(getBuffer());
        return getBuffer();
    }

    @Override
    public void decode(ProtocolGroup group, PlayerSkinPacket pk) {
        pk.uuid = this.getUUID();
        String skinId = this.getString();
        pk.skinName = this.getString();
        pk.serializeName = this.getString();
        byte[] data = this.getByteArray();
        byte[] cape = this.getByteArray();

        pk.skin = new Skin(data, skinId);
        pk.skin.setCape(pk.skin.new Cape(cape));

        pk.geometryModel = this.getString();
        pk.geometryData = this.getString();
    }
}
