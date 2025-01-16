package dev.cammiescorner.arcanus.fabric;

import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class ArcanusMixinConfig implements IMixinConfigPlugin {
	private static final String COMPAT_MIXIN_PACKAGE = "dev.cammiescorner.arcanus.mixin.compat";
	private static final Logger LOGGER = LogManager.getLogger(ArcanusMixinConfig.class);

	@Override
	public void onLoad(String mixinPackage) {
		// NO-OP
	}

	@Override
	public String getRefMapperConfig() {
		return null;
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		if(mixinClassName.startsWith(COMPAT_MIXIN_PACKAGE)) {
			var sub = mixinClassName.substring(COMPAT_MIXIN_PACKAGE.length() + 1);
			var modid = sub.substring(0, sub.indexOf("."));
			if(!FabricLoader.getInstance().isModLoaded(modid)) {
				LOGGER.debug("Skipping integration {} because mod {} is not loaded", mixinClassName, modid);
				return false;
			}
		}
		return true;
	}

	@Override
	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
		// NO-OP
	}

	@Override
	public List<String> getMixins() {
		return null;
	}

	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
		// NO-OP
	}

	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
		// NO-OP
	}
}
