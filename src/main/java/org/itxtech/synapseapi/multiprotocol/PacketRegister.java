package org.itxtech.synapseapi.multiprotocol;

import cn.nukkit.Server;
import cn.nukkit.network.protocol.BatchPacket;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.utils.Binary;
import cn.nukkit.utils.BinaryStream;
import cn.nukkit.utils.MainLogger;
import org.itxtech.synapseapi.multiprotocol.protocol11.protocol.*;
import org.itxtech.synapseapi.multiprotocol.protocol12.protocol.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author CreeperFace
 */
public class PacketRegister {

    private static Class<? extends DataPacket>[] packetPool11 = new Class[256];

    /**
     * nukkit packet -> 1.1 packet
     */
    private static Map<Class<? extends DataPacket>, Class<? extends DataPacket>> replacements = new HashMap<>();

    /**
     * 1.2.10 and above packets
     */
    private static Class<? extends PacketDecoder<? extends DataPacket>>[] decoders = new Class[256];

    public static void init() {
        registerPacket11(ProtocolInfo.ADD_ENTITY_PACKET, AddEntityPacket.class);
        registerPacket11(ProtocolInfo.ADD_HANGING_ENTITY_PACKET, AddHangingEntityPacket.class);
        registerPacket11(ProtocolInfo.ADD_ITEM_ENTITY_PACKET, AddItemEntityPacket.class);
        registerPacket11(ProtocolInfo.ADD_PAINTING_PACKET, AddPaintingPacket.class);
        registerPacket11(ProtocolInfo.ADD_PLAYER_PACKET, AddPlayerPacket.class);
        registerPacket11(ProtocolInfo.ADVENTURE_SETTINGS_PACKET, AdventureSettingsPacket.class);
        registerPacket11(ProtocolInfo.ANIMATE_PACKET, AnimatePacket.class);
        registerPacket11(ProtocolInfo.AVAILABLE_COMMANDS_PACKET, AvailableCommandsPacket.class);
        registerPacket11(ProtocolInfo.BATCH_PACKET, cn.nukkit.network.protocol.BatchPacket.class);
        registerPacket11(ProtocolInfo.BLOCK_ENTITY_DATA_PACKET, BlockEntityDataPacket.class);
        registerPacket11(ProtocolInfo.BLOCK_EVENT_PACKET, BlockEventPacket.class);
        registerPacket11(ProtocolInfo.BLOCK_PICK_REQUEST_PACKET, BlockPickRequestPacket.class);
        registerPacket11(ProtocolInfo.BOSS_EVENT_PACKET, BossEventPacket.class);
        registerPacket11(ProtocolInfo.CHANGE_DIMENSION_PACKET, ChangeDimensionPacket.class);
        registerPacket11(ProtocolInfo.CHUNK_RADIUS_UPDATED_PACKET, ChunkRadiusUpdatedPacket.class);
        registerPacket11(ProtocolInfo.CLIENTBOUND_MAP_ITEM_DATA_PACKET, ClientboundMapItemDataPacket.class);
        registerPacket11(ProtocolInfo.COMMAND_STEP_PACKET, CommandStepPacket.class);
        registerPacket11(ProtocolInfo.CONTAINER_CLOSE_PACKET, ContainerClosePacket.class);
        registerPacket11(ProtocolInfo.CONTAINER_OPEN_PACKET, ContainerOpenPacket.class);
        registerPacket11(ProtocolInfo.CONTAINER_SET_CONTENT_PACKET, ContainerSetContentPacket.class);
        registerPacket11(ProtocolInfo.CONTAINER_SET_DATA_PACKET, ContainerSetDataPacket.class);
        registerPacket11(ProtocolInfo.CONTAINER_SET_SLOT_PACKET, ContainerSetSlotPacket.class);
        registerPacket11(ProtocolInfo.CRAFTING_DATA_PACKET, CraftingDataPacket.class);
        registerPacket11(ProtocolInfo.CRAFTING_EVENT_PACKET, CraftingEventPacket.class);
        registerPacket11(ProtocolInfo.DISCONNECT_PACKET, DisconnectPacket.class);
        registerPacket11(ProtocolInfo.DROP_ITEM_PACKET, DropItemPacket.class);
        registerPacket11(ProtocolInfo.ENTITY_EVENT_PACKET, EntityEventPacket.class);
        registerPacket11(ProtocolInfo.ENTITY_FALL_PACKET, EntityFallPacket.class);
        registerPacket11(ProtocolInfo.EXPLODE_PACKET, ExplodePacket.class);
        registerPacket11(ProtocolInfo.FULL_CHUNK_DATA_PACKET, FullChunkDataPacket.class);
        registerPacket11(ProtocolInfo.GAME_RULES_CHANGED_PACKET, GameRulesChangedPacket.class);
        registerPacket11(ProtocolInfo.HURT_ARMOR_PACKET, HurtArmorPacket.class);
        registerPacket11(ProtocolInfo.INTERACT_PACKET, InteractPacket.class);
        //registerPacket11(ProtocolInfo.INVENTORY_ACTION_PACKET, InventoryActionPacket.class);
        registerPacket11(ProtocolInfo.ITEM_FRAME_DROP_ITEM_PACKET, ItemFrameDropItemPacket.class);
        registerPacket11(ProtocolInfo.LEVEL_EVENT_PACKET, LevelEventPacket.class);
        registerPacket11(ProtocolInfo.LEVEL_SOUND_EVENT_PACKET, LevelSoundEventPacket.class);
        registerPacket11(ProtocolInfo.LOGIN_PACKET, LoginPacket.class);
        registerPacket11(ProtocolInfo.MAP_INFO_REQUEST_PACKET, MapInfoRequestPacket.class);
        registerPacket11(ProtocolInfo.MOB_ARMOR_EQUIPMENT_PACKET, MobArmorEquipmentPacket.class);
        registerPacket11(ProtocolInfo.MOB_EQUIPMENT_PACKET, MobEquipmentPacket.class);
        registerPacket11(ProtocolInfo.MOVE_ENTITY_PACKET, MoveEntityPacket.class);
        registerPacket11(ProtocolInfo.MOVE_PLAYER_PACKET, MovePlayerPacket.class);
        registerPacket11(ProtocolInfo.PLAYER_ACTION_PACKET, PlayerActionPacket.class);
        registerPacket11(ProtocolInfo.PLAYER_INPUT_PACKET, PlayerInputPacket.class);
        registerPacket11(ProtocolInfo.PLAYER_LIST_PACKET, PlayerListPacket.class);
        registerPacket11(ProtocolInfo.PLAY_SOUND_PACKET, PlaySoundPacket.class);
        registerPacket11(ProtocolInfo.PLAY_STATUS_PACKET, PlayStatusPacket.class);
        registerPacket11(ProtocolInfo.REMOVE_BLOCK_PACKET, RemoveBlockPacket.class);
        registerPacket11(ProtocolInfo.REMOVE_ENTITY_PACKET, RemoveEntityPacket.class);
        registerPacket11(ProtocolInfo.REPLACE_ITEM_IN_SLOT_PACKET, ReplaceItemInSlotPacket.class);
        registerPacket11(ProtocolInfo.REQUEST_CHUNK_RADIUS_PACKET, RequestChunkRadiusPacket.class);
        registerPacket11(ProtocolInfo.RESOURCE_PACKS_INFO_PACKET, ResourcePacksInfoPacket.class);
        registerPacket11(ProtocolInfo.RESOURCE_PACK_STACK_PACKET, ResourcePackStackPacket.class);
        registerPacket11(ProtocolInfo.RESOURCE_PACK_CLIENT_RESPONSE_PACKET, ResourcePackClientResponsePacket.class);
        registerPacket11(ProtocolInfo.RESOURCE_PACK_DATA_INFO_PACKET, ResourcePackDataInfoPacket.class);
        registerPacket11(ProtocolInfo.RESOURCE_PACK_CHUNK_DATA_PACKET, ResourcePackChunkDataPacket.class);
        registerPacket11(ProtocolInfo.RESOURCE_PACK_CHUNK_REQUEST_PACKET, ResourcePackChunkRequestPacket.class);
        registerPacket11(ProtocolInfo.RESPAWN_PACKET, RespawnPacket.class);
        registerPacket11(ProtocolInfo.RIDER_JUMP_PACKET, RiderJumpPacket.class);
        registerPacket11(ProtocolInfo.SET_COMMANDS_ENABLED_PACKET, SetCommandsEnabledPacket.class);
        registerPacket11(ProtocolInfo.SET_DIFFICULTY_PACKET, SetDifficultyPacket.class);
        registerPacket11(ProtocolInfo.SET_ENTITY_DATA_PACKET, SetEntityDataPacket.class);
        registerPacket11(ProtocolInfo.SET_ENTITY_LINK_PACKET, SetEntityLinkPacket.class);
        registerPacket11(ProtocolInfo.SET_ENTITY_MOTION_PACKET, SetEntityMotionPacket.class);
        registerPacket11(ProtocolInfo.SET_HEALTH_PACKET, SetHealthPacket.class);
        registerPacket11(ProtocolInfo.SET_PLAYER_GAME_TYPE_PACKET, SetPlayerGameTypePacket.class);
        registerPacket11(ProtocolInfo.SET_SPAWN_POSITION_PACKET, SetSpawnPositionPacket.class);
        registerPacket11(ProtocolInfo.SET_TITLE_PACKET, SetTitlePacket.class);
        registerPacket11(ProtocolInfo.SET_TIME_PACKET, SetTimePacket.class);
        registerPacket11(ProtocolInfo.SHOW_CREDITS_PACKET, ShowCreditsPacket.class);
        registerPacket11(ProtocolInfo.SPAWN_EXPERIENCE_ORB_PACKET, SpawnExperienceOrbPacket.class);
        registerPacket11(ProtocolInfo.START_GAME_PACKET, StartGamePacket.class);
        registerPacket11(ProtocolInfo.TAKE_ITEM_ENTITY_PACKET, TakeItemEntityPacket.class);
        registerPacket11(ProtocolInfo.TEXT_PACKET, TextPacket.class);
        registerPacket11(ProtocolInfo.UPDATE_BLOCK_PACKET, UpdateBlockPacket.class);
        registerPacket11(ProtocolInfo.USE_ITEM_PACKET, UseItemPacket.class);
        registerPacket11(ProtocolInfo.UPDATE_TRADE_PACKET, UpdateTradePacket.class);
        registerPacket11(ProtocolInfo.UPDATE_ATTRIBUTES_PACKET, UpdateAttributesPacket.class);

        //1.2.10 packet decoders
        decoders[AddItemEntity.NETWORK_ID & 0xff] = AddItemEntity.class;
        decoders[AddPlayer.NETWORK_ID & 0xff] = AddPlayer.class;
        decoders[PlayerList.NETWORK_ID & 0xff] = PlayerList.class;
        //decoders[cn.nukkit.network.protocol.ProtocolInfo.PLAYER_SKIN_PACKET & 0xff] = PlayerSkin.class;
        decoders[StartGame.NETWORK_ID] = StartGame.class;
        decoders[cn.nukkit.network.protocol.TextPacket.NETWORK_ID & 0xff] = Text.class;
        decoders[cn.nukkit.network.protocol.UpdateBlockPacket.NETWORK_ID & 0xff] = UpdateBlock.class;
        decoders[cn.nukkit.network.protocol.AddEntityPacket.NETWORK_ID & 0xff] = AddEntity.class;
        decoders[cn.nukkit.network.protocol.SetEntityDataPacket.NETWORK_ID & 0xff] = SetEntityData.class;
        decoders[cn.nukkit.network.protocol.ProtocolInfo.PLAYER_HOTBAR_PACKET & 0xff] = PlayerHotbar.class;
        decoders[cn.nukkit.network.protocol.ProtocolInfo.LEVEL_EVENT_PACKET & 0xff] = LevelEvent.class;
        decoders[cn.nukkit.network.protocol.ProtocolInfo.AVAILABLE_COMMANDS_PACKET & 0xff] = AvailableCommands.class;
        decoders[cn.nukkit.network.protocol.ProtocolInfo.MOVE_ENTITY_PACKET & 0xff] = MoveEntity.class;
        decoders[cn.nukkit.network.protocol.ProtocolInfo.RESOURCE_PACKS_INFO_PACKET & 0xff] = ResourcePacksInfo.class;
        decoders[cn.nukkit.network.protocol.ProtocolInfo.CRAFTING_DATA_PACKET & 0xff] = CraftingData.class;
    }

    public static void registerPacket11(byte id, Class<? extends DataPacket> clazz) {
        packetPool11[id & 0xff] = clazz;

        if (Packet11.class.isAssignableFrom(clazz)) {
            try {
                Method method;

                try {
                    method = clazz.getDeclaredMethod("getDefaultPacket");
                } catch (NoSuchMethodException ex) {
                    return;
                }

                if (method != null) {
                    Class c = (Class<? extends DataPacket>) method.invoke(null);
                    if (c != null) {
                        replacements.put(c, clazz);
                    }
                }
            } catch (Exception e) {
                MainLogger.getLogger().logException(e);
            }
        }
    }

    public static DataPacket getPacket(byte id, ProtocolGroup protocol) {
        if (protocol == ProtocolGroup.PROTOCOL_11) {
            try {
                Class<? extends DataPacket> clazz = packetPool11[id & 0xff];

                if (clazz != null) {
                    return clazz.newInstance();
                }
            } catch (Exception e) {
                MainLogger.getLogger().logException(e);
                return null;
            }
        }

        return Server.getInstance().getNetwork().getPacket(id);
    }

    public static DataPacket getFullPacket(byte[] data, ProtocolGroup protocol) {
        byte pid = data[0] == (byte) 0xfe ? (byte) 0xff : data[0];

        byte start = (byte) protocol.getBufferOffset();

        DataPacket pk = getPacket(pid, protocol);
        if (pk == null) {
            Server.getInstance().getLogger().notice("C -> S null packet with PID: " + pid);
            return null;
        }

        pk.setBuffer(data, start);
        //pk.decode();

        decodePacket(pk, protocol);
        return pk;
    }

    @SuppressWarnings("unchecked")
    public static boolean decodePacket(DataPacket pk, ProtocolGroup protocol) {
        if (pk.pid() == BatchPacket.NETWORK_ID) {
            pk.decode();
            return true;
        }

        if (protocol != ProtocolGroup.PROTOCOL_11) {
            try {
                Class<? extends PacketDecoder<? extends DataPacket>> decoder = decoders[pk.pid() & 0xff];

                if (decoder != null) {
                    PacketDecoder<DataPacket> dcd = ((PacketDecoder<DataPacket>) decoder.newInstance());
                    dcd.setBuffer(pk.getBuffer(), pk.getOffset());
                    dcd.decode(protocol, pk);
                    return true;
                }
            } catch (Exception e) {
                MainLogger.getLogger().logException(e);
                return false;
            }
        }

        pk.decode();
        return true;
    }

    @SuppressWarnings("unchecked")
    public static boolean encodePacket(DataPacket pk, ProtocolGroup protocol) {
        if (pk.pid() == BatchPacket.NETWORK_ID) {
            pk.encode();
            return true;
        }

        if (protocol != ProtocolGroup.PROTOCOL_11) {
            try {
                Class<? extends PacketDecoder<? extends DataPacket>> decoder = decoders[pk.pid() & 0xff];

                if (decoder != null) {
                    ((PacketDecoder<DataPacket>) decoder.newInstance()).encode(protocol, pk);

                    if (protocol.ordinal() >= ProtocolGroup.PROTOCOL_16.ordinal()) {
                        byte[] buffer = Binary.writeUnsignedVarInt(pk.pid());

                        if (pk.getBuffer().length > 0) {
                            pk.setBuffer(Binary.appendBytes(buffer, Binary.subBytes(pk.getBuffer(), 3)));
                        }
                    }
                    return true;
                }
            } catch (Exception e) {
                MainLogger.getLogger().info("PID: " + pk.pid() + "  packet: " + pk.getClass().getName());
                MainLogger.getLogger().logException(e);
                return false;
            }
        }

        pk.encode();
        if (protocol.ordinal() >= ProtocolGroup.PROTOCOL_16.ordinal()) {
            if (pk.getBuffer().length > 0) {
                byte[] buffer = Binary.writeUnsignedVarInt(pk.pid());
                pk.setBuffer(Binary.appendBytes(buffer, Binary.subBytes(pk.getBuffer(), 3)));
            }
        }
        return true;
    }

    public static DataPacket getCompatiblePacket(DataPacket packet, ProtocolGroup protocol) {
        return getCompatiblePacket(packet, protocol, false);
    }

    public static DataPacket getCompatiblePacket(DataPacket packet, ProtocolGroup protocol, boolean checkExist) {
        if (packet.pid() == BatchPacket.NETWORK_ID) {
            return packet;
        }

        if (protocol == ProtocolGroup.PROTOCOL_11 && !(packet instanceof Packet11)) {
            try {
                /*if(checkExist && !(getPacket(packet.pid(), protocol) instanceof Packet11)) {
                    return null;
                }*/

                Class<? extends DataPacket> clazz = replacements.get(packet.getClass());
                if (clazz == null) {
                    return checkExist ? null : packet;
                }

                DataPacket replaced = clazz.newInstance();
                if (replaced == null) {
                    return packet;
                }

                ((Packet11) replaced).fromDefault(packet);

                return replaced;
            } catch (Exception e) {
                MainLogger.getLogger().logException(e);
            }
        } else if (protocol != ProtocolGroup.PROTOCOL_11 && packet instanceof Packet11) {
            return ((Packet11) packet).toDefault();
        }

        return packet;
    }

    public static DataPacket[] decodeBatch(BatchPacket batchPacket) {
        /*byte[][] payload = new byte[packets.length * 2][];
        for (int i = 0; i < packets.length; i++) {
            DataPacket p = packets[i];
            if (!p.isEncoded) {
                p.encode();
            }
            byte[] buf = p.getBuffer();
            payload[i * 2] = Binary.writeUnsignedVarInt(buf.length);
            payload[i * 2 + 1] = buf;
        }
        byte[] data;
        data = Binary.appendBytes(payload);*/

        List<DataPacket> packets = new ArrayList<>();

        byte[] payload = batchPacket.payload;
        BinaryStream stream = new BinaryStream(payload);
        while (!stream.feof()) {
            int len = (int) stream.getUnsignedVarInt();
            byte[] buffer = stream.get(len);

            DataPacket pk = getFullPacket(buffer, ProtocolGroup.PROTOCOL_11);
            packets.add(pk);
        }

        return packets.stream().toArray(DataPacket[]::new);
    }
}
