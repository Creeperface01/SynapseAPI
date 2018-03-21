package org.itxtech.synapseapi.multiprotocol.protocol1210.protocol;

import cn.nukkit.entity.data.Skin;
import cn.nukkit.utils.BinaryStream;
import org.itxtech.synapseapi.multiprotocol.ProtocolGroup;

/**
 * @author CreeperFace
 */
public class MVBinaryStream extends BinaryStream {

    public void putSkin(Skin skin, ProtocolGroup protocol) {
        this.putString(skin.getModel());

        if(protocol.ordinal() >= ProtocolGroup.PROTOCOL_1210.ordinal()) {
            this.putLInt(1);
        }
        this.putByteArray(skin.getData());
    }
}
