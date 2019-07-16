package org.itxtech.synapseapi.multiprotocol.protocol12.protocol;

import cn.nukkit.inventory.FurnaceRecipe;
import cn.nukkit.inventory.ShapedRecipe;
import cn.nukkit.inventory.ShapelessRecipe;
import cn.nukkit.item.Item;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.item.enchantment.EnchantmentEntry;
import cn.nukkit.item.enchantment.EnchantmentList;
import cn.nukkit.network.protocol.CraftingDataPacket;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.BinaryStream;
import org.itxtech.synapseapi.multiprotocol.PacketDecoder;
import org.itxtech.synapseapi.multiprotocol.ProtocolGroup;

public class CraftingData extends PacketDecoder<CraftingDataPacket> {

    public static final String CRAFTING_TAG_CRAFTING_TABLE = "crafting_table";
    public static final String CRAFTING_TAG_CARTOGRAPHY_TABLE = "cartography_table";
    public static final String CRAFTING_TAG_STONECUTTER = "stonecutter";
    public static final String CRAFTING_TAG_FURNACE = "furnace";
    public static final String CRAFTING_TAG_CAMPFIRE = "campfire";
    public static final String CRAFTING_TAG_BLAST_FURNACE = "blast_furnace";
    public static final String CRAFTING_TAG_SMOKER = "smoker";

    @Override
    public byte pid() {
        return ProtocolInfo.CRAFTING_DATA_PACKET;
    }

    @Override
    public void decode(ProtocolGroup group, CraftingDataPacket pk) {

    }

    @Override
    public byte[] encode(ProtocolGroup group, CraftingDataPacket pk) {
        reset();
        putUnsignedVarInt(pk.entries.size());

        BinaryStream writer = new BinaryStream();

        for (Object entry : pk.entries) {
            int entryType = writeEntry(entry, writer, group);
            if (entryType >= 0) {
                this.putVarInt(entryType);
                this.put(writer.getBuffer());
            } else {
                this.putVarInt(-1);
            }

            writer.reset();
        }

        this.putBoolean(pk.cleanRecipes);

        pk.setBuffer(getBuffer());
        return new byte[0];
    }

    private static int writeEntry(Object entry, BinaryStream stream, ProtocolGroup protocol) {
        if (entry instanceof ShapelessRecipe) {
            return writeShapelessRecipe(((ShapelessRecipe) entry), stream, protocol);
        } else if (entry instanceof ShapedRecipe) {
            return writeShapedRecipe(((ShapedRecipe) entry), stream, protocol);
        } else if (entry instanceof FurnaceRecipe) {
            return writeFurnaceRecipe(((FurnaceRecipe) entry), stream, protocol);
        } else if (entry instanceof EnchantmentList) {
            return writeEnchantList(((EnchantmentList) entry), stream, protocol);
        }
        return -1;
    }

    private static int writeShapelessRecipe(ShapelessRecipe recipe, BinaryStream stream, ProtocolGroup protocol) {
        stream.putUnsignedVarInt(recipe.getIngredientCount());

        for (Item item : recipe.getIngredientList()) {
            stream.putSlot(item);
        }

        stream.putUnsignedVarInt(1);
        stream.putSlot(recipe.getResult());
        stream.putUUID(recipe.getId());

        if(protocol.ordinal() >= ProtocolGroup.PROTOCOL_111.ordinal()) {
            stream.putString(CRAFTING_TAG_CRAFTING_TABLE);
        }

        return CraftingDataPacket.ENTRY_SHAPELESS;
    }

    private static int writeShapedRecipe(ShapedRecipe recipe, BinaryStream stream, ProtocolGroup protocol) {
        stream.putVarInt(recipe.getWidth());
        stream.putVarInt(recipe.getHeight());

        for (int z = 0; z < recipe.getHeight(); ++z) {
            for (int x = 0; x < recipe.getWidth(); ++x) {
                stream.putSlot(recipe.getIngredient(x, z));
            }
        }

        stream.putUnsignedVarInt(1);
        stream.putSlot(recipe.getResult());

        stream.putUUID(recipe.getId());
        if(protocol.ordinal() >= ProtocolGroup.PROTOCOL_111.ordinal()) {
            stream.putString(CRAFTING_TAG_CRAFTING_TABLE);
        }

        return CraftingDataPacket.ENTRY_SHAPED;
    }

    private static int writeFurnaceRecipe(FurnaceRecipe recipe, BinaryStream stream, ProtocolGroup protocol) {
        if (recipe.getInput().hasMeta()) { //Data recipe
            stream.putVarInt(recipe.getInput().getId());
            stream.putVarInt(recipe.getInput().getDamage());
            stream.putSlot(recipe.getResult());

            if(protocol.ordinal() >= ProtocolGroup.PROTOCOL_111.ordinal()) {
                stream.putString(CRAFTING_TAG_FURNACE);
            }

            return CraftingDataPacket.ENTRY_FURNACE_DATA;
        } else {
            stream.putVarInt(recipe.getInput().getId());
            stream.putSlot(recipe.getResult());
            if(protocol.ordinal() >= ProtocolGroup.PROTOCOL_111.ordinal()) {
                stream.putString(CRAFTING_TAG_FURNACE);
            }

            return CraftingDataPacket.ENTRY_FURNACE;
        }
    }

    private static int writeEnchantList(EnchantmentList list, BinaryStream stream, ProtocolGroup protocol) {
        stream.putByte((byte) list.getSize());
        for (int i = 0; i < list.getSize(); ++i) {
            EnchantmentEntry entry = list.getSlot(i);
            stream.putUnsignedVarInt(entry.getCost());
            stream.putUnsignedVarInt(entry.getEnchantments().length);
            for (Enchantment enchantment : entry.getEnchantments()) {
                stream.putUnsignedVarInt(enchantment.getId());
                stream.putUnsignedVarInt(enchantment.getLevel());
            }
            stream.putString(entry.getRandomName());
        }
        return CraftingDataPacket.ENTRY_ENCHANT_LIST;
    }
}
