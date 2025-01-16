package dev.cammiescorner.arcanus.fabric.client.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;

public class CollapseParticle extends TextureSheetParticle {
	private final SpriteSet spriteProvider;

	public CollapseParticle(ClientLevel world, double posX, double posY, double posZ, double velocityX, double velocityY, double velocityZ, SpriteSet spriteProvider) {
		super(world, posX, posY, posZ, 0, 0, 0);
		this.lifetime = 60;
		this.spriteProvider = spriteProvider;
		this.xd = velocityX;
		this.yd = velocityY;
		this.zd = velocityZ;
	}

	@Override
	public void tick() {
		setSpriteFromAge(spriteProvider);
		super.tick();
	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
	}

	@Environment(EnvType.CLIENT)
	public static class Factory implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet spriteProvider;

		public Factory(SpriteSet spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		@Override
		public Particle createParticle(SimpleParticleType defaultParticleType, ClientLevel clientWorld, double posX, double posY, double posZ, double velocityX, double velocityY, double velocityZ) {
			CollapseParticle particle = new CollapseParticle(clientWorld, posX, posY, posZ, velocityX, velocityY, velocityZ, spriteProvider);
			particle.setSpriteFromAge(spriteProvider);
			return particle;
		}
	}
}
