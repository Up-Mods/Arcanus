package dev.cammiescorner.arcanuscontinuum.common.spell_components.effects.utility;

import dev.cammiescorner.arcanuscontinuum.api.spells.SpellEffect;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellType;
import dev.cammiescorner.arcanuscontinuum.api.spells.Weight;
import dev.cammiescorner.arcanuscontinuum.common.packets.s2c.SyncScalePacket;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusComponents;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusSpellComponents;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SizeChangeSpellEffect extends SpellEffect {
	public SizeChangeSpellEffect(boolean isEnabled, SpellType type, Weight weight, double manaCost, int coolDown, int minLevel) {
		super(isEnabled, type, weight, manaCost, coolDown, minLevel);
	}

	@Override
	public void effect(@Nullable LivingEntity caster, @Nullable Entity sourceEntity, Level world, HitResult target, List<SpellEffect> effects, ItemStack stack, double potency) {
		if(target.getType() == HitResult.Type.ENTITY) {
			EntityHitResult entityHit = (EntityHitResult) target;
			Entity entity = entityHit.getEntity();

			if(ArcanusSpellComponents.SHRINK.is(this))
				ArcanusComponents.setScale(entity, this, effects.stream().filter(ArcanusSpellComponents.SHRINK::is).count() * potency);
			else if(ArcanusSpellComponents.ENLARGE.is(this))
				ArcanusComponents.setScale(entity, this, effects.stream().filter(ArcanusSpellComponents.ENLARGE::is).count() * potency);

			if (!entity.level().isClientSide()) {
				RegistrySupplier<SpellEffect> supplier = ArcanusSpellComponents.SHRINK.is(this) ? ArcanusSpellComponents.SHRINK : ArcanusSpellComponents.ENLARGE;

				var strength = effects.stream().filter(supplier::is).count() * potency;
				for (ServerPlayer lookup : PlayerLookup.tracking(entity)) {
					SyncScalePacket.send(lookup, entity, this, strength);
				}
				if (entity instanceof ServerPlayer serverPlayer) {
					SyncScalePacket.send(serverPlayer, entity, this, strength);
				}
			}
		}
	}
}
