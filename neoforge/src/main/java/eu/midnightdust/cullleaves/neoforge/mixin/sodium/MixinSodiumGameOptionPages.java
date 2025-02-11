package eu.midnightdust.cullleaves.neoforge.mixin.sodium;

import eu.midnightdust.cullleaves.CullLeavesClient;
import eu.midnightdust.cullleaves.config.CullLeavesConfig;
import net.caffeinemc.mods.sodium.client.gui.SodiumGameOptionPages;
import net.caffeinemc.mods.sodium.client.gui.options.OptionFlag;
import net.caffeinemc.mods.sodium.client.gui.options.OptionGroup;
import net.caffeinemc.mods.sodium.client.gui.options.OptionImpact;
import net.caffeinemc.mods.sodium.client.gui.options.OptionImpl;
import net.caffeinemc.mods.sodium.client.gui.options.control.TickBoxControl;
import net.caffeinemc.mods.sodium.client.gui.options.storage.SodiumOptionsStorage;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;

@Mixin(value = SodiumGameOptionPages.class, remap = false)
public class MixinSodiumGameOptionPages {

    @Shadow @Final private static SodiumOptionsStorage sodiumOpts;

    @ModifyVariable(method = "performance", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableList;copyOf(Ljava/util/Collection;)Lcom/google/common/collect/ImmutableList;"))
    private static List<OptionGroup> cullleaves$addCullLeavesOption(List<OptionGroup> groups) {
        groups.add(OptionGroup.createBuilder()
                .add(OptionImpl.createBuilder(boolean.class, sodiumOpts)
                        .setName(Text.translatable("cullleaves.midnightconfig.enabled"))
                        .setTooltip(Text.translatable("cullleaves.midnightconfig.enabled.tooltip.sodium"))
                        .setControl(TickBoxControl::new)
                        .setBinding((opts, value) -> {
                            CullLeavesConfig.enabled = value;
                            CullLeavesConfig.write(CullLeavesClient.MOD_ID);
                        }, opts -> CullLeavesConfig.enabled)
                        .setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD)
                        .setImpact(OptionImpact.MEDIUM)
                        .build()
                ).add(OptionImpl.createBuilder(boolean.class, sodiumOpts)
                        .setName(Text.translatable("cullleaves.midnightconfig.cullRoots"))
                        .setTooltip(Text.translatable("cullleaves.midnightconfig.cullRoots.tooltip.sodium"))
                        .setControl(TickBoxControl::new)
                        .setBinding((opts, value) -> {
                            CullLeavesConfig.cullRoots = value;
                            CullLeavesConfig.write(CullLeavesClient.MOD_ID);
                        }, opts -> CullLeavesConfig.cullRoots)
                        .setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD)
                        .setImpact(OptionImpact.MEDIUM)
                        .build()
                )
                .build()
        );

        return groups;
    }
}
