package dev.cammiescorner.arcanuscontinuum.common.packets.c2s;

import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.api.entities.ArcanusEntityAttributes;
import dev.cammiescorner.arcanuscontinuum.api.spells.Pattern;
import dev.cammiescorner.arcanuscontinuum.api.spells.Spell;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellComponent;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellGroup;
import dev.cammiescorner.arcanuscontinuum.common.items.StaffItem;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusComponents;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusTags;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SyncPatternPacket {
	public static final ResourceLocation ID = Arcanus.id("sync_pattern");

	public static void send(List<Pattern> pattern) {
		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());

		buf.writeInt(pattern.size());

		for(int i = 0; i < pattern.size(); i++)
			buf.writeInt(pattern.get(i).ordinal());

		ClientPlayNetworking.send(ID, buf);
	}

	public static void handler(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf, PacketSender sender) {
		List<Pattern> pattern = new ArrayList<>();
		int listSize = buf.readInt();

		for(int i = 0; i < listSize; i++)
			pattern.add(Pattern.values()[buf.readInt()]);

		server.execute(() -> {
			ArcanusComponents.setPattern(player, pattern);

			if(pattern.size() >= 3) {
				ItemStack stack = player.getMainHandItem();
				CompoundTag tag = stack.getOrCreateTagElement(Arcanus.MOD_ID);
				int index = Arcanus.getSpellIndex(pattern);

				if(stack.getItem() instanceof StaffItem staff) {
					ListTag list = tag.getList("Spells", Tag.TAG_COMPOUND);

					if(!list.isEmpty() && player.getCooldowns().getCooldownPercent(staff, 1F) == 0) {
						Spell spell = Spell.fromNbt(list.getCompound(index));

						if(spell.getComponentGroups().stream().flatMap(SpellGroup::getAllComponents).mapToInt(SpellComponent::getMinLevel).max().orElse(1) > ArcanusComponents.WIZARD_LEVEL_COMPONENT.get(player).getLevel()) {
							player.displayClientMessage(Arcanus.translate("spell", "too_low_level"), true);
							return;
						}

						if(spell.getComponentGroups().stream().flatMap(SpellGroup::getAllComponents).count() > ArcanusComponents.maxSpellSize(player)) {
							player.displayClientMessage(Arcanus.translate("spell", "too_many_components"), true);
							return;
						}

						if(!ArcanusComponents.drainMana(player, spell.getManaCost(), player.isCreative())) {
							player.displayClientMessage(Arcanus.translate("spell", "not_enough_mana"), true);
							return;
						}

						ArcanusComponents.setPattern(player, Arcanus.getSpellPattern(index));
						ArcanusComponents.setLastCastTime(player, player.level().getGameTime());
						spell.cast(player, player.serverLevel(), stack);
						player.displayClientMessage(Component.translatable(spell.getName()).withStyle(ChatFormatting.GREEN), true);

						for(Holder<Item> holder : BuiltInRegistries.ITEM.getTagOrEmpty(ArcanusTags.STAVES))
							player.getCooldowns().addCooldown(holder.value(), (int) (spell.getCoolDown() * player.getAttributeValue(ArcanusEntityAttributes.SPELL_COOL_DOWN.get())));
					}
				}
			}
		});
	}
}
