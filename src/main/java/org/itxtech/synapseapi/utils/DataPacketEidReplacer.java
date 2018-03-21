package org.itxtech.synapseapi.utils;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.EntityData;
import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.network.protocol.*;
import cn.nukkit.network.protocol.AddEntityPacket;
import cn.nukkit.network.protocol.AddItemEntityPacket;
import cn.nukkit.network.protocol.AddPaintingPacket;
import cn.nukkit.network.protocol.AddPlayerPacket;
import cn.nukkit.network.protocol.AdventureSettingsPacket;
import cn.nukkit.network.protocol.AnimatePacket;
import cn.nukkit.network.protocol.BossEventPacket;
import cn.nukkit.network.protocol.EntityEventPacket;
import cn.nukkit.network.protocol.MobArmorEquipmentPacket;
import cn.nukkit.network.protocol.MobEffectPacket;
import cn.nukkit.network.protocol.MobEquipmentPacket;
import cn.nukkit.network.protocol.MoveEntityPacket;
import cn.nukkit.network.protocol.MovePlayerPacket;
import cn.nukkit.network.protocol.PlayerListPacket;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.network.protocol.SetEntityDataPacket;
import cn.nukkit.network.protocol.SetEntityLinkPacket;
import cn.nukkit.network.protocol.SetEntityMotionPacket;
import cn.nukkit.network.protocol.TakeItemEntityPacket;
import cn.nukkit.network.protocol.UpdateAttributesPacket;
import com.google.common.collect.Sets;
import org.itxtech.synapseapi.multiprotocol.protocol11.protocol.*;
import org.itxtech.synapseapi.multiprotocol.protocol11.protocol.ContainerSetContentPacket;
import org.itxtech.synapseapi.multiprotocol.protocol1210.protocol.AddPlayer;
import sun.misc.Unsafe;

import java.util.Arrays;
import java.util.Map.Entry;
import java.util.Set;

/**
 * DataPacketEidReplacer
 * ===============
 * author: boybook
 * EaseCation Network Project
 * codefuncore
 * ===============
 */
public class DataPacketEidReplacer {

    private static final Set<Integer> replaceMetadata = Sets.newHashSet(Entity.DATA_OWNER_EID, Entity.DATA_LEAD_HOLDER_EID, Entity.DATA_TRADING_PLAYER_EID, Entity.DATA_TARGET_EID);

    public static DataPacket replace(DataPacket pk, long from, long to) {
        DataPacket packet = pk.clone();
        boolean change = true;

        if(packet instanceof Packet11) {

            switch (packet.pid()) {
                case org.itxtech.synapseapi.multiprotocol.protocol11.protocol.AddPlayerPacket.NETWORK_ID:
                    org.itxtech.synapseapi.multiprotocol.protocol11.protocol.AddPlayerPacket mp = (org.itxtech.synapseapi.multiprotocol.protocol11.protocol.AddPlayerPacket) packet;

                    mp.metadata = replaceMetadata(mp.metadata, from, to);
                    break;
                case org.itxtech.synapseapi.multiprotocol.protocol11.protocol.AddEntityPacket.NETWORK_ID:
                    org.itxtech.synapseapi.multiprotocol.protocol11.protocol.AddEntityPacket aep = (org.itxtech.synapseapi.multiprotocol.protocol11.protocol.AddEntityPacket) packet;

                    aep.metadata = replaceMetadata(aep.metadata, from, to);
                    break;
                case org.itxtech.synapseapi.multiprotocol.protocol11.protocol.AddItemEntityPacket.NETWORK_ID:
                    org.itxtech.synapseapi.multiprotocol.protocol11.protocol.AddItemEntityPacket aiep = (org.itxtech.synapseapi.multiprotocol.protocol11.protocol.AddItemEntityPacket) packet;

                    aiep.metadata = replaceMetadata(aiep.metadata, from, to);
                    break;
                case AnimatePacket.NETWORK_ID:
                    if (((org.itxtech.synapseapi.multiprotocol.protocol11.protocol.AnimatePacket) packet).eid == from) ((org.itxtech.synapseapi.multiprotocol.protocol11.protocol.AnimatePacket) packet).eid = to;
                    break;
                case TakeItemEntityPacket.NETWORK_ID:
                    if (((org.itxtech.synapseapi.multiprotocol.protocol11.protocol.TakeItemEntityPacket) packet).entityId == from) ((org.itxtech.synapseapi.multiprotocol.protocol11.protocol.TakeItemEntityPacket) packet).entityId = to;
                    break;
                case SetEntityMotionPacket.NETWORK_ID:
                    if (((org.itxtech.synapseapi.multiprotocol.protocol11.protocol.SetEntityMotionPacket) packet).eid == from) ((org.itxtech.synapseapi.multiprotocol.protocol11.protocol.SetEntityMotionPacket) packet).eid = to;
                    break;
                case org.itxtech.synapseapi.multiprotocol.protocol11.protocol.SetEntityLinkPacket.NETWORK_ID:
                    if (((org.itxtech.synapseapi.multiprotocol.protocol11.protocol.SetEntityLinkPacket) packet).rider == from) ((org.itxtech.synapseapi.multiprotocol.protocol11.protocol.SetEntityLinkPacket) packet).rider = to;
                    if (((org.itxtech.synapseapi.multiprotocol.protocol11.protocol.SetEntityLinkPacket) packet).riding == from) ((org.itxtech.synapseapi.multiprotocol.protocol11.protocol.SetEntityLinkPacket) packet).riding = to;
                    break;
                case org.itxtech.synapseapi.multiprotocol.protocol11.protocol.SetEntityDataPacket.NETWORK_ID:
                    org.itxtech.synapseapi.multiprotocol.protocol11.protocol.SetEntityDataPacket sedp = (org.itxtech.synapseapi.multiprotocol.protocol11.protocol.SetEntityDataPacket) packet;

                    if (sedp.eid == from) sedp.eid = to;
                    sedp.metadata = replaceMetadata(sedp.metadata, from, to);
                    break;
                case org.itxtech.synapseapi.multiprotocol.protocol11.protocol.UpdateAttributesPacket.NETWORK_ID:
                    if (((org.itxtech.synapseapi.multiprotocol.protocol11.protocol.UpdateAttributesPacket) packet).entityId == from) ((org.itxtech.synapseapi.multiprotocol.protocol11.protocol.UpdateAttributesPacket) packet).entityId = to;
                    break;
                case org.itxtech.synapseapi.multiprotocol.protocol11.protocol.EntityEventPacket.NETWORK_ID:
                    if (((org.itxtech.synapseapi.multiprotocol.protocol11.protocol.EntityEventPacket) packet).eid == from) ((org.itxtech.synapseapi.multiprotocol.protocol11.protocol.EntityEventPacket) packet).eid = to;
                    break;
                case org.itxtech.synapseapi.multiprotocol.protocol11.protocol.MovePlayerPacket.NETWORK_ID:
                    if (((org.itxtech.synapseapi.multiprotocol.protocol11.protocol.MovePlayerPacket) packet).eid == from) ((org.itxtech.synapseapi.multiprotocol.protocol11.protocol.MovePlayerPacket) packet).eid = to;
                    break;
                case org.itxtech.synapseapi.multiprotocol.protocol11.protocol.MobEquipmentPacket.NETWORK_ID:
                    if (((org.itxtech.synapseapi.multiprotocol.protocol11.protocol.MobEquipmentPacket) packet).eid == from) ((org.itxtech.synapseapi.multiprotocol.protocol11.protocol.MobEquipmentPacket) packet).eid = to;
                    break;
                case org.itxtech.synapseapi.multiprotocol.protocol11.protocol.MobEffectPacket.NETWORK_ID:
                    if (((org.itxtech.synapseapi.multiprotocol.protocol11.protocol.MobEffectPacket) packet).eid == from) ((org.itxtech.synapseapi.multiprotocol.protocol11.protocol.MobEffectPacket) packet).eid = to;
                    break;
                case org.itxtech.synapseapi.multiprotocol.protocol11.protocol.MoveEntityPacket.NETWORK_ID:
                    if (((org.itxtech.synapseapi.multiprotocol.protocol11.protocol.MoveEntityPacket) packet).eid == from) ((org.itxtech.synapseapi.multiprotocol.protocol11.protocol.MoveEntityPacket) packet).eid = to;
                    break;
                case org.itxtech.synapseapi.multiprotocol.protocol11.protocol.MobArmorEquipmentPacket.NETWORK_ID:
                    if (((org.itxtech.synapseapi.multiprotocol.protocol11.protocol.MobArmorEquipmentPacket) packet).eid == from) ((org.itxtech.synapseapi.multiprotocol.protocol11.protocol.MobArmorEquipmentPacket) packet).eid = to;
                    break;
                case org.itxtech.synapseapi.multiprotocol.protocol11.protocol.PlayerListPacket.NETWORK_ID:
                    Arrays.stream(((org.itxtech.synapseapi.multiprotocol.protocol11.protocol.PlayerListPacket) packet).entries).filter(entry -> entry.entityId == from).forEach(entry -> entry.entityId = to);
                    break;
                case org.itxtech.synapseapi.multiprotocol.protocol11.protocol.BossEventPacket.NETWORK_ID:
                    if (((org.itxtech.synapseapi.multiprotocol.protocol11.protocol.BossEventPacket) packet).eid == from) ((org.itxtech.synapseapi.multiprotocol.protocol11.protocol.BossEventPacket) packet).eid = to;
                    break;
                case ContainerSetContentPacket.NETWORK_ID:
                    if (((ContainerSetContentPacket) packet).eid == from) ((ContainerSetContentPacket) packet).eid = to;
                    break;
                default:
                    change = false;
                    break;
            }

            if(change) {
                packet.isEncoded = false;
            }

            return packet;
        }

        switch (packet.pid()) {
            case AddPlayerPacket.NETWORK_ID:
                AddPlayerPacket app = (AddPlayerPacket) packet;

                app.metadata = replaceMetadata(app.metadata, from, to);
                break;
            case AddEntityPacket.NETWORK_ID:
                AddEntityPacket aep = (AddEntityPacket) packet;

                aep.metadata = replaceMetadata(aep.metadata, from, to);
                break;
            case AddItemEntityPacket.NETWORK_ID:
                AddItemEntityPacket aiep = (AddItemEntityPacket) packet;

                aiep.metadata = replaceMetadata(aiep.metadata, from, to);
                break;
            case AnimatePacket.NETWORK_ID:
                if (((AnimatePacket) packet).eid == from) ((AnimatePacket) packet).eid = to;
                break;
            case TakeItemEntityPacket.NETWORK_ID:
                if (((TakeItemEntityPacket) packet).entityId == from) ((TakeItemEntityPacket) packet).entityId = to;
                break;
            case SetEntityMotionPacket.NETWORK_ID:
                if (((SetEntityMotionPacket) packet).eid == from) ((SetEntityMotionPacket) packet).eid = to;
                break;
            case SetEntityLinkPacket.NETWORK_ID:
                if (((SetEntityLinkPacket) packet).rider == from) ((SetEntityLinkPacket) packet).rider = to;
                if (((SetEntityLinkPacket) packet).riding == from) ((SetEntityLinkPacket) packet).riding = to;
                break;
            case SetEntityDataPacket.NETWORK_ID:
                SetEntityDataPacket sedp = (SetEntityDataPacket) packet;

                if (sedp.eid == from) sedp.eid = to;
                sedp.metadata = replaceMetadata(sedp.metadata, from, to);
                break;
            case UpdateAttributesPacket.NETWORK_ID:
                if (((UpdateAttributesPacket) packet).entityId == from) ((UpdateAttributesPacket) packet).entityId = to;
                break;
            case EntityEventPacket.NETWORK_ID:
                if (((EntityEventPacket) packet).eid == from) ((EntityEventPacket) packet).eid = to;
                break;
            case MovePlayerPacket.NETWORK_ID:
                if (((MovePlayerPacket) packet).eid == from) ((MovePlayerPacket) packet).eid = to;
                break;
            case MobEquipmentPacket.NETWORK_ID:
                if (((MobEquipmentPacket) packet).eid == from) ((MobEquipmentPacket) packet).eid = to;
                break;
            case MobEffectPacket.NETWORK_ID:
                if (((MobEffectPacket) packet).eid == from) ((MobEffectPacket) packet).eid = to;
                break;
            case MoveEntityPacket.NETWORK_ID:
                if (((MoveEntityPacket) packet).eid == from) ((MoveEntityPacket) packet).eid = to;
                break;
            case MobArmorEquipmentPacket.NETWORK_ID:
                if (((MobArmorEquipmentPacket) packet).eid == from) ((MobArmorEquipmentPacket) packet).eid = to;
                break;
            case PlayerListPacket.NETWORK_ID:
                Arrays.stream(((PlayerListPacket) packet).entries).filter(entry -> entry.entityId == from).forEach(entry -> entry.entityId = to);
                break;
            case BossEventPacket.NETWORK_ID:
                if (((BossEventPacket) packet).bossEid == from) ((BossEventPacket) packet).bossEid = to;
                break;
            case AdventureSettingsPacket.NETWORK_ID:
                if (((AdventureSettingsPacket) packet).entityUniqueId == from) ((AdventureSettingsPacket) packet).entityUniqueId = to;
                break;
            case ProtocolInfo.UPDATE_EQUIPMENT_PACKET:
                if (((UpdateEquipmentPacket) packet).eid == from) ((UpdateEquipmentPacket) packet).eid = to;
                break;
            default:
                change = false;
        }

        if(change) {
            packet.isEncoded = false;
        }

        return packet;
    }

    private static EntityMetadata replaceMetadata(EntityMetadata data, long from, long to) {
        boolean changed = false;

        for(Integer key : replaceMetadata) {
            if(data.getLong(key) == from) {
                if(!changed) {
                    data = cloneMetadata(data);
                    changed = true;
                }

                data.putLong(key, to);
            }
        }

        return data;
    }

    private static EntityMetadata cloneMetadata(EntityMetadata data) {
        EntityMetadata newData = new EntityMetadata();

        for(EntityData value : data.getMap().values()) {
            newData.put(value);
        }

        return newData;
    }
}
