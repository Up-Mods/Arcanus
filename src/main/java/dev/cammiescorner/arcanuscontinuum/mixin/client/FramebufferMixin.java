package dev.cammiescorner.arcanuscontinuum.mixin.client;

import com.mojang.blaze3d.pipeline.RenderTarget;
import dev.cammiescorner.arcanuscontinuum.client.utils.StencilBuffer;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.ARBFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30C;
import org.lwjgl.opengl.GL31;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.Objects;

@Mixin(RenderTarget.class)
public abstract class FramebufferMixin implements StencilBuffer {
	@Unique private boolean isStencilBufferEnabled;
	@Shadow public int width;
	@Shadow public int height;
	@Shadow public abstract void resize(int width, int height, boolean clearError);

	@Inject(method = "<init>", at = @At("RETURN"))
	private void arcanuscontinuum$init(boolean useDepth, CallbackInfo info) {
		isStencilBufferEnabled = false;
	}

	@ModifyArgs(method = "createBuffers", at = @At(value = "INVOKE",
			target = "Lcom/mojang/blaze3d/platform/GlStateManager;_texImage2D(IIIIIIIILjava/nio/IntBuffer;)V",
			remap = false
	))
	private void arcanuscontinuum$modifyTexImage2D(Args args) {
		if(Objects.equals(args.get(2), GL11.GL_DEPTH_COMPONENT) && isStencilBufferEnabled) {
			args.set(2, GL31.GL_DEPTH24_STENCIL8);
			args.set(6, ARBFramebufferObject.GL_DEPTH_STENCIL);
			args.set(7, GL31.GL_UNSIGNED_INT_24_8);
		}
	}

	@ModifyArgs(method = "createBuffers", at = @At(value = "INVOKE",
			target = "Lcom/mojang/blaze3d/platform/GlStateManager;_glFramebufferTexture2D(IIIII)V",
			remap = false
	))
	private void arcanuscontinuum$modifyFrameBufferTexture2D(Args args) {
		if(Objects.equals(args.get(1), GL30C.GL_DEPTH_ATTACHMENT))
			if(isStencilBufferEnabled)
				args.set(1, GL31.GL_DEPTH_STENCIL_ATTACHMENT);
	}

	@Override
	public boolean arcanuscontinuum$isStencilBufferEnabled() {
		return isStencilBufferEnabled;
	}

	@Override
	public void arcanuscontinuum$enableStencilBufferAndReload(boolean cond) {
		if(isStencilBufferEnabled != cond) {
			isStencilBufferEnabled = cond;
			resize(width, height, Minecraft.ON_OSX);
		}
	}
}
