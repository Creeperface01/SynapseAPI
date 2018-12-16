package org.itxtech.synapseapi.multiprotocol.protocol12.entity;

import cn.nukkit.entity.item.*;
import cn.nukkit.entity.mob.*;
import cn.nukkit.entity.passive.*;
import cn.nukkit.entity.projectile.EntityArrow;
import cn.nukkit.entity.projectile.EntityEgg;
import cn.nukkit.entity.projectile.EntityEnderPearl;
import cn.nukkit.entity.projectile.EntitySnowball;
import cn.nukkit.entity.weather.EntityLightning;
import com.google.common.collect.ImmutableMap;
import lombok.experimental.UtilityClass;

/**
 * @author CreeperFace
 */
@UtilityClass
public final class EntityUtils {

    private static ImmutableMap<Integer, String> LEGACY_IDS = ImmutableMap.<Integer, String>builder()
            .put(51, "minecraft:npc")
            .put(63, "minecraft:player")
            .put(EntityWitherSkeleton.NETWORK_ID, "minecraft:wither_skeleton")
            .put(EntityHusk.NETWORK_ID, "minecraft:husk")
            .put(EntityStray.NETWORK_ID, "minecraft:stray")
            .put(EntityWitch.NETWORK_ID, "minecraft:witch")
            .put(EntityZombieVillager.NETWORK_ID, "minecraft:zombie_villager")
            .put(EntityBlaze.NETWORK_ID, "minecraft:blaze")
            .put(EntityMagmaCube.NETWORK_ID, "minecraft:magma_cube")
            .put(EntityGhast.NETWORK_ID, "minecraft:ghast")
            .put(EntityCaveSpider.NETWORK_ID, "minecraft:cave_spider")
            .put(EntitySilverfish.NETWORK_ID, "minecraft:silverfish")
            .put(EntityEnderman.NETWORK_ID, "minecraft:enderman")
            .put(EntitySlime.NETWORK_ID, "minecraft:slime")
            .put(EntityZombiePigman.NETWORK_ID, "minecraft:zombie_pigman")
            .put(EntitySpider.NETWORK_ID, "minecraft:spider")
            .put(EntitySkeleton.NETWORK_ID, "minecraft:skeleton")
            .put(EntityCreeper.NETWORK_ID, "minecraft:creeper")
            .put(EntityZombie.NETWORK_ID, "minecraft:zombie")
            .put(EntitySkeletonHorse.NETWORK_ID, "minecraft:skeleton_horse")
            .put(EntityMule.NETWORK_ID, "minecraft:mule")
            .put(EntityDonkey.NETWORK_ID, "minecraft:donkey")
            .put(31, "minecraft:dolphin")
            .put(111, "minecraft:tropicalfish")
            .put(EntityWolf.NETWORK_ID, "minecraft:wolf")
            .put(EntitySquid.NETWORK_ID, "minecraft:squid")
            .put(110, "minecraft:drowned")
            .put(EntitySheep.NETWORK_ID, "minecraft:sheep")
            .put(EntityMooshroom.NETWORK_ID, "minecraft:mooshroom")
            .put(113, "minecraft:panda")
            .put(109, "minecraft:salmon")
            .put(EntityPig.NETWORK_ID, "minecraft:pig")
            .put(EntityVillager.NETWORK_ID, "minecraft:villager")
            .put(112, "minecraft:cod")
            .put(108, "minecraft:pufferfish")
            .put(EntityCow.NETWORK_ID, "minecraft:cow")
            .put(EntityChicken.NETWORK_ID, "minecraft:chicken")
            .put(107, "minecraft:balloon")
            .put(EntityLlama.NETWORK_ID, "minecraft:llama")
            .put(20, "minecraft:iron_golem")
            .put(EntityRabbit.NETWORK_ID, "minecraft:rabbit")
            .put(21, "minecraft:snow_golem")
            .put(EntityBat.NETWORK_ID, "minecraft:bat")
            .put(EntityOcelot.NETWORK_ID, "minecraft:ocelot")
            .put(EntityHorse.NETWORK_ID, "minecraft:horse")
            .put(75, "minecraft:cat")
            .put(EntityPolarBear.NETWORK_ID, "minecraft:polar_bear")
            .put(EntityZombieHorse.NETWORK_ID, "minecraft:zombie_horse")
            .put(74, "minecraft:turtle")
            .put(30, "minecraft:parrot")
            .put(EntityGuardian.NETWORK_ID, "minecraft:guardian")
            .put(EntityElderGuardian.NETWORK_ID, "minecraft:elder_guardian")
            .put(57, "minecraft:vindicator")
            .put(EntityWither.NETWORK_ID, "minecraft:wither")
            .put(EntityEnderDragon.NETWORK_ID, "minecraft:ender_dragon")
            .put(EntityShulker.NETWORK_ID, "minecraft:shulker")
            .put(EntityEndermite.NETWORK_ID, "minecraft:endermite")
            .put(EntityMinecartEmpty.NETWORK_ID, "minecraft:minecart")
            .put(EntityMinecartHopper.NETWORK_ID, "minecraft:hopper_minecart")
            .put(EntityMinecartTNT.NETWORK_ID, "minecraft:tnt_minecart")
            .put(EntityMinecartChest.NETWORK_ID, "minecraft:chest_minecart")
            .put(100, "minecraft:command_block_minecart")
            .put(61, "minecraft:armor_stand")
            .put(EntityItem.NETWORK_ID, "minecraft:item")
            .put(EntityPrimedTNT.NETWORK_ID, "minecraft:tnt")
            .put(EntityFallingBlock.NETWORK_ID, "minecraft:falling_block")
            .put(EntityExpBottle.NETWORK_ID, "minecraft:xp_bottle")
            .put(EntityXPOrb.NETWORK_ID, "minecraft:xp_orb")
            .put(70, "minecraft:eye_of_ender_signal")
            .put(71, "minecraft:ender_crystal")
            .put(76, "minecraft:shulker_bullet")
            .put(77, "minecraft:fishing_hook")
            .put(79, "minecraft:dragon_fireball")
            .put(EntityArrow.NETWORK_ID, "minecraft:arrow")
            .put(EntitySnowball.NETWORK_ID, "minecraft:snowball")
            .put(EntityEgg.NETWORK_ID, "minecraft:egg")
            .put(EntityPainting.NETWORK_ID, "minecraft:painting")
            .put(73, "minecraft:thrown_trident")
            .put(85, "minecraft:fireball")
            .put(EntityPotion.NETWORK_ID, "minecraft:splash_potion")
            .put(EntityEnderPearl.NETWORK_ID, "minecraft:ender_pearl")
            .put(88, "minecraft:leash_knot")
            .put(89, "minecraft:wither_skull")
            .put(91, "minecraft:wither_skull_dangerous")
            .put(EntityBoat.NETWORK_ID, "minecraft:boat")
            .put(EntityLightning.NETWORK_ID, "minecraft:lightning_bolt")
            .put(94, "minecraft:small_fireball")
            .put(102, "minecraft:llama_spit")
            .put(95, "minecraft:area_effect_cloud")
            .put(101, "minecraft:lingering_potion")
            .put(EntityFirework.NETWORK_ID, "minecraft:fireworks_rocket")
            .put(103, "minecraft:evocation_fang")
            .put(104, "minecraft:evocation_illager")
            .put(105, "minecraft:vex")
            .put(56, "minecraft:agent")
            .put(106, "minecraft:ice_bomb")
            .put(58, "minecraft:phantom")
            .put(62, "minecraft:tripod_camera")
            .build();

    static {

    }

    public static String getEntityLegacyId(int id) {
        return LEGACY_IDS.get(id);
    }
}
