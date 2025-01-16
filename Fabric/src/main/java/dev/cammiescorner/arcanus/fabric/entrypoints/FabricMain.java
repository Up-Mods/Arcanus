package dev.cammiescorner.arcanus.fabric.entrypoints;

import com.teamresourceful.resourcefulconfig.api.loader.Configurator;
import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.ArcanusConfig;
import dev.cammiescorner.arcanus.api.entities.ArcanusEntityAttributes;
import dev.cammiescorner.arcanus.api.spells.Pattern;
import dev.cammiescorner.arcanus.api.spells.SpellComponent;
import dev.cammiescorner.arcanus.fabric.common.blocks.MagicDoorBlock;
import dev.cammiescorner.arcanus.fabric.common.blocks.entities.MagicDoorBlockEntity;
import dev.cammiescorner.arcanus.fabric.common.enchantments.ManaPoolEnchantment;
import dev.cammiescorner.arcanus.fabric.common.packets.c2s.*;
import dev.cammiescorner.arcanus.fabric.common.packets.s2c.SyncConfigValuesPacket;
import dev.cammiescorner.arcanus.fabric.common.packets.s2c.SyncStatusEffectPacket;
import dev.cammiescorner.arcanus.fabric.common.registry.*;
import dev.cammiescorner.arcanus.fabric.common.util.Color;
import dev.cammiescorner.arcanus.fabric.common.util.supporters.HaloData;
import dev.cammiescorner.arcanus.fabric.common.util.supporters.WizardData;
import dev.upcraft.datasync.api.DataSyncAPI;
import dev.upcraft.datasync.api.SyncToken;
import dev.upcraft.sparkweave.api.platform.services.RegistryService;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.EntityElytraEvents;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.networking.v1.EntityTrackingEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.state.BlockState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.util.List;
import java.util.UUID;

public class FabricMain implements ModInitializer {
	public static final Configurator configurator = new Configurator(Arcanus.MOD_ID);
	public static final Logger LOGGER = LoggerFactory.getLogger(Arcanus.MOD_ID);
	public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("##,####.##");

	public static final ResourceKey<Registry<SpellComponent>> SPELL_COMPONENTS_REGISTRY_KEY = ResourceKey.createRegistryKey(Arcanus.id("spell_components"));
	public static final DefaultedRegistry<SpellComponent> SPELL_COMPONENTS = FabricRegistryBuilder.createDefaulted(SPELL_COMPONENTS_REGISTRY_KEY, Arcanus.id("empty")).buildAndRegister();
	public static final Color DEFAULT_MAGIC_COLOUR = Color.fromInt(0x68e1ff, Color.Ordering.RGB);

	public static final SyncToken<WizardData> WIZARD_DATA = DataSyncAPI.register(WizardData.class, WizardData.ID, WizardData.CODEC);
	public static final SyncToken<HaloData> HALO_DATA = DataSyncAPI.register(HaloData.class, HaloData.ID, HaloData.CODEC);
	public static final UUID SPELL_SPEED_MODIFIER_ID = UUID.fromString("e348efa3-7987-4912-b82a-03c5c75eccb1");

	@Override
	public void onInitialize() {
		configurator.register(ArcanusConfig.class);

		RegistryService registryService = RegistryService.get();
		ArcanusEntityAttributes.registerAll();
		ArcanusEntities.ENTITY_TYPES.accept(registryService);
		ArcanusEnchantments.ENCHANTMENTS.accept(registryService);
		ArcanusBlocks.BLOCKS.accept(registryService);
		ArcanusItems.ITEM_GROUPS.accept(registryService);
		ArcanusItems.ITEMS.accept(registryService);
		ArcanusBlockEntities.BLOCK_ENTITY_TYPES.accept(registryService);
		ArcanusParticles.PARTICLE_TYPES.accept(registryService);
		ArcanusPointsOfInterest.register();
		ArcanusRecipes.RECIPE_SERIALIZERS.accept(registryService);
		ArcanusScreenHandlers.SCREEN_HANDLERS.accept(registryService);
		ArcanusSpellComponents.SPELL_COMPONENTS.accept(registryService);
		ArcanusMobEffects.MOB_EFFECTS.accept(registryService);
		ArcanusStructureProcessorTypes.STRUCTURE_PROCESSORS.accept(registryService);

		ArcanusCriteriaTriggers.register();

		ServerPlayNetworking.registerGlobalReceiver(CastSpellPacket.ID, CastSpellPacket::handler);
		ServerPlayNetworking.registerGlobalReceiver(SetCastingPacket.ID, SetCastingPacket::handler);
		ServerPlayNetworking.registerGlobalReceiver(SaveBookDataPacket.ID, SaveBookDataPacket::handler);
		ServerPlayNetworking.registerGlobalReceiver(SyncPatternPacket.ID, SyncPatternPacket::handler);
		ServerPlayNetworking.registerGlobalReceiver(ShootOrbsPacket.ID, ShootOrbsPacket::handler);

		CommandRegistrationCallback.EVENT.register(ArcanusCommands::init);

		ModifyItemAttributeModifiersCallback.EVENT.register((stack, slot, attributeModifiers) -> {
			if(ArcanusConfig.Enchantments.ManaPool.maxLevel <= 0)
				return;

			int manaPoolLevel = EnchantmentHelper.getItemEnchantmentLevel(ArcanusEnchantments.MANA_POOL.get(), stack);

			if(slot.isArmor() && manaPoolLevel > 0) {
				if(stack.getItem() instanceof ArmorItem armorItem && armorItem.getEquipmentSlot() != slot)
					return;

				AttributeModifier maxManaModifier = new AttributeModifier(ManaPoolEnchantment.getUuidForSlot(slot), "Mana Pool Max Mana Modifier", ArcanusConfig.Enchantments.ManaPool.manaPerLevel * manaPoolLevel, ArcanusConfig.Enchantments.ManaPool.manaModifierOperation);

				attributeModifiers.put(ArcanusEntityAttributes.MAX_MANA.get(), maxManaModifier);
			}
		});

		EntityElytraEvents.CUSTOM.register((entity, tickElytra) -> entity.hasEffect(ArcanusMobEffects.MANA_WINGS.get()));

		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			var hostProfile = server.getSingleplayerProfile();
			if(hostProfile == null || !hostProfile.getId().equals(handler.player.getGameProfile().getId())) {
				SyncConfigValuesPacket.send(handler.player);
			}

			SyncStatusEffectPacket.sendToAll(handler.player, ArcanusMobEffects.ANONYMITY.get(), handler.player.hasEffect(ArcanusMobEffects.ANONYMITY.get()));
		});

		ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
			if(ArcanusComponents.areUpdatesBlocked(handler.player))
				ArcanusComponents.setBlockUpdates(handler.player, false);
		});

		EntityTrackingEvents.START_TRACKING.register((trackedEntity, player) -> {
			if(trackedEntity instanceof ServerPlayer playerEntity)
				SyncStatusEffectPacket.sendTo(player, playerEntity, ArcanusMobEffects.ANONYMITY.get(), playerEntity.hasEffect(ArcanusMobEffects.ANONYMITY.get()));

			// FIXME temporal dilation no worky
//			if(trackedEntity instanceof LivingEntity livingEntity)
//				SyncStatusEffectPacket.sendTo(player, livingEntity, ArcanusStatusEffects.TEMPORAL_DILATION.get(), livingEntity.hasStatusEffect(ArcanusStatusEffects.TEMPORAL_DILATION.get()));
		});

		EntitySleepEvents.STOP_SLEEPING.register((entity, sleepingPos) -> {
			if(!entity.level().isClientSide() && entity.level().getDayTime() == 24000) {
				MobEffectInstance copperCurse = entity.getEffect(ArcanusMobEffects.COPPER_CURSE.get());

				if(copperCurse != null) {
					entity.removeEffect(ArcanusMobEffects.COPPER_CURSE.get());

					if(copperCurse.getDuration() > 24000)
						entity.addEffect(new MobEffectInstance(ArcanusMobEffects.COPPER_CURSE.get(), copperCurse.getDuration() - 24000, 0, true, false));
				}
			}
		});

		UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
			ItemStack stack = player.getItemInHand(hand);
			BlockPos pos = hitResult.getBlockPos();
			BlockState state = world.getBlockState(pos);

			if(!world.isClientSide() && player.isShiftKeyDown() && stack.is(Items.NAME_TAG) && stack.hasCustomHoverName()) {
				MagicDoorBlockEntity door = MagicDoorBlock.getBlockEntity(world, state, pos);

				if(door != null && door.getOwner() == player) {
					door.setPassword(stack.getHoverName().getString());

					if(!player.isCreative())
						stack.shrink(1);

					return InteractionResult.SUCCESS;
				}
			}

			if(ArcanusComponents.isBlockWarded(world, pos) && !ArcanusComponents.isOwnerOfBlock(player, pos)) {
				UseOnContext ctx = new BlockPlaceContext(world, player, hand, stack, hitResult);
				InteractionResult result = stack.useOn(ctx);

				if(!result.consumesAction()) {
					player.displayClientMessage(Component.translatable("text.arcanus.block_is_warded").withStyle(ChatFormatting.RED), true);
					player.swing(hand);

					return InteractionResult.FAIL;
				}

				return result;
			}

			return InteractionResult.PASS;
		});

		// FIXME temporal dilation no worky
//		ServerWorldTickEvents.END.register((server, world) -> {
//			List<Entity> loadedEntityList = new ArrayList<>();
//			world.iterateEntities().forEach(loadedEntityList::add);
//			StatusEffect statusEffect = ArcanusStatusEffects.TEMPORAL_DILATION.get();
//			float radius = 3;
//
//			for(Entity entity : loadedEntityList) {
//				if(ArcanusComponents.isTimeSlowed(entity)) {
//					List<Entity> targets = world.getOtherEntities(entity, new Box(-radius, -radius, -radius, radius, radius, radius).offset(entity.getPos()), target -> target.squaredDistanceTo(entity) <= radius * radius);
//
//					if(targets.stream().noneMatch(target -> target instanceof LivingEntity livingTarget && livingTarget.hasStatusEffect(statusEffect))) {
//						ArcanusComponents.setSlowTime(entity, false);
//						ArcanusComponents.setBlockUpdates(entity, false);
//					}
//				}
//			}
//		});
	}

	public static String format(double d) {
		return DECIMAL_FORMAT.format(d);
	}

	public static int getSpellIndex(List<Pattern> patternList) {
		String pattern = patternList.get(0).getLetter() + patternList.get(1).getLetter() + patternList.get(2).getLetter();

		return switch(pattern) {
			case "LLL" -> 0;
			case "LLR" -> 1;
			case "LRL" -> 2;
			case "LRR" -> 3;
			case "RRR" -> 4;
			case "RRL" -> 5;
			case "RLR" -> 6;
			case "RLL" -> 7;
			default -> 0;
		};
	}

	public static List<Pattern> getSpellPattern(int index) {
		return switch(index) {
			case 1 -> List.of(Pattern.LEFT, Pattern.LEFT, Pattern.RIGHT);
			case 2 -> List.of(Pattern.LEFT, Pattern.RIGHT, Pattern.LEFT);
			case 3 -> List.of(Pattern.LEFT, Pattern.RIGHT, Pattern.RIGHT);
			case 4 -> List.of(Pattern.RIGHT, Pattern.RIGHT, Pattern.RIGHT);
			case 5 -> List.of(Pattern.RIGHT, Pattern.RIGHT, Pattern.LEFT);
			case 6 -> List.of(Pattern.RIGHT, Pattern.LEFT, Pattern.RIGHT);
			case 7 -> List.of(Pattern.RIGHT, Pattern.LEFT, Pattern.LEFT);
			default -> List.of(Pattern.LEFT, Pattern.LEFT, Pattern.LEFT);
		};
	}

	public static MutableComponent getSpellPatternAsText(int index) {
		String string = switch(index) {
			case 0 -> Pattern.LEFT.getSymbol() + "-" + Pattern.LEFT.getSymbol() + "-" + Pattern.LEFT.getSymbol();
			case 1 -> Pattern.LEFT.getSymbol() + "-" + Pattern.LEFT.getSymbol() + "-" + Pattern.RIGHT.getSymbol();
			case 2 -> Pattern.LEFT.getSymbol() + "-" + Pattern.RIGHT.getSymbol() + "-" + Pattern.LEFT.getSymbol();
			case 3 -> Pattern.LEFT.getSymbol() + "-" + Pattern.RIGHT.getSymbol() + "-" + Pattern.RIGHT.getSymbol();
			case 4 -> Pattern.RIGHT.getSymbol() + "-" + Pattern.RIGHT.getSymbol() + "-" + Pattern.RIGHT.getSymbol();
			case 5 -> Pattern.RIGHT.getSymbol() + "-" + Pattern.RIGHT.getSymbol() + "-" + Pattern.LEFT.getSymbol();
			case 6 -> Pattern.RIGHT.getSymbol() + "-" + Pattern.LEFT.getSymbol() + "-" + Pattern.RIGHT.getSymbol();
			case 7 -> Pattern.RIGHT.getSymbol() + "-" + Pattern.LEFT.getSymbol() + "-" + Pattern.LEFT.getSymbol();
			default -> "ERROR";
		};

		return Component.literal(string).withStyle(style -> style.withFont(Arcanus.id("magic_symbols")));
	}
}
