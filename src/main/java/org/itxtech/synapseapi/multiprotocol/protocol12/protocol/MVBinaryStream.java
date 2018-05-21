package org.itxtech.synapseapi.multiprotocol.protocol12.protocol;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.*;
import cn.nukkit.utils.BinaryStream;
import org.itxtech.synapseapi.multiprotocol.ProtocolGroup;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author CreeperFace
 */
public class MVBinaryStream extends BinaryStream {

    public void putSkin(Skin skin, ProtocolGroup protocol) {
        this.putString(skin.getModel());
        this.putByteArray(skin.getData());
        this.putByteArray(skin.getCape().getData());
    }

    public void putMetadata(EntityMetadata data, ProtocolGroup protocol) {
        Map<Integer, EntityData> dataMap = data.getMap();

        if (protocol.ordinal() >= ProtocolGroup.PROTOCOL_1213.ordinal()) {
            Map<Integer, EntityData> newData = new HashMap<>();

            for (Entry<Integer, EntityData> entry : dataMap.entrySet()) {
                int id = entry.getKey();

                if (id >= 24) {
                    if (id == 24) //24 doesn't exist in 1.2.13
                        continue;

                    if (id == 29) {
                        id = 26;
                    } else {
                        id--;
                    }
                }

                newData.put(id, entry.getValue());
            }

            if (newData.containsKey(Entity.DATA_FLAGS)) {
                long flags = data.getLong(Entity.DATA_FLAGS);

                long firstPart = flags & 0b1111111111111111111111;
                long midPart = ((flags >> 22) & 0b111111111111111111111) << (22 + 1); //22 flags and 1 offset

                flags = (flags >> 43 << 43) | firstPart | midPart;

                if (((flags >> 46) & 1) > 0) { //gravity
                    flags ^= 1L << 46;
                    flags ^= 1L << 47;
                }

                newData.put(Entity.DATA_FLAGS, new LongEntityData(Entity.DATA_FLAGS, flags));
            }

            dataMap = newData;

            /*MainLogger.getLogger().info("SENDING");
            for(Entry<Integer, EntityData> entry : dataMap.entrySet()) {
                MainLogger.getLogger().info(entry.getValue().getClass().getSimpleName()+" ("+entry.getKey()+"):"+entry.getValue().getType());
            }*/
        }

        putUnsignedVarInt(dataMap.size());
        for (int id : dataMap.keySet()) {
            EntityData d = dataMap.get(id);
            putUnsignedVarInt(id);
            putUnsignedVarInt(d.getType());
            switch (d.getType()) {
                case Entity.DATA_TYPE_BYTE:
                    putByte(((ByteEntityData) d).getData().byteValue());
                    break;
                case Entity.DATA_TYPE_SHORT:
                    putLShort(((ShortEntityData) d).getData());
                    break;
                case Entity.DATA_TYPE_INT:
                    putVarInt(((IntEntityData) d).getData());
                    break;
                case Entity.DATA_TYPE_FLOAT:
                    putLFloat(((FloatEntityData) d).getData());
                    break;
                case Entity.DATA_TYPE_STRING:
                    String s = ((StringEntityData) d).getData();
                    putUnsignedVarInt(s.getBytes(StandardCharsets.UTF_8).length);
                    put(s.getBytes(StandardCharsets.UTF_8));
                    break;
                case Entity.DATA_TYPE_SLOT:
                    SlotEntityData slot = (SlotEntityData) d;
                    putSlot(slot.getData());
                    break;
                case Entity.DATA_TYPE_POS:
                    IntPositionEntityData pos = (IntPositionEntityData) d;
                    putVarInt(pos.x);
                    putVarInt(pos.y);
                    putVarInt(pos.z);
                    break;
                case Entity.DATA_TYPE_LONG:
                    putVarLong(((LongEntityData) d).getData());
                    break;
                case Entity.DATA_TYPE_VECTOR3F:
                    Vector3fEntityData v3data = (Vector3fEntityData) d;
                    putLFloat(v3data.x);
                    putLFloat(v3data.y);
                    putLFloat(v3data.z);
                    break;
            }
        }
    }
}
