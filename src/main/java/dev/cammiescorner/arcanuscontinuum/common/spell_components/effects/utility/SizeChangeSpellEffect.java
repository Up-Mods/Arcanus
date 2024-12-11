package dev.cammiescorner.arcanuscontinuum.common.spell_components.effects.utility;

import dev.cammiescorner.arcanuscontinuum.api.spells.SpellEffect;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellType;
import dev.cammiescorner.arcanuscontinuum.api.spells.Weight;
import dev.cammiescorner.arcanuscontinuum.common.packets.s2c.SyncScalePacket;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusComponents;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusSpellComponents;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.qsl.networking.api.PlayerLookup;

import java.util.List;

public class SizeChangeSpellEffect extends SpellEffect {
	public SizeChangeSpellEffect(boolean isEnabled, SpellType type, Weight weight, double manaCost, int coolDown, int minLevel) {
		super(isEnabled, type, weight, manaCost, coolDown, minLevel);
	}

	@Override
	public void effect(@Nullable LivingEntity caster, @Nullable Entity sourceEntity, World world, HitResult target, List<SpellEffect> effects, ItemStack stack, double potency) {
		if(target.getType() == HitResult.Type.ENTITY) {
			EntityHitResult entityHit = (EntityHitResult) target;
			Entity entity = entityHit.getEntity();

			if(ArcanusSpellComponents.SHRINK.is(this))
				ArcanusComponents.setScale(entity, this, effects.stream().filter(ArcanusSpellComponents.SHRINK::is).count() * potency);
			else if(ArcanusSpellComponents.ENLARGE.is(this))
				ArcanusComponents.setScale(entity, this, effects.stream().filter(ArcanusSpellComponents.ENLARGE::is).count() * potency);

			if (!entity.getWorld().isClient()) {
				RegistrySupplier<SpellEffect> supplier = ArcanusSpellComponents.SHRINK.is(this) ? ArcanusSpellComponents.SHRINK : ArcanusSpellComponents.ENLARGE;
				for (ServerPlayerEntity lookup : PlayerLookup.tracking(entity))
					SyncScalePacket.send(lookup, entity, this, effects.stream().filter(supplier::is).count() * potency);
				if (entity instanceof ServerPlayerEntity serverPlayer)
					SyncScalePacket.send(serverPlayer, entity, this, effects.stream().filter(supplier::is).count() * potency);
			}
		}
	}
}
