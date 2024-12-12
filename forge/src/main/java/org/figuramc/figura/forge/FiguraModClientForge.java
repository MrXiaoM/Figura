package org.figuramc.figura.forge;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.SimpleReloadableResourceManager;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.network.ChannelBuilder;
import org.figuramc.figura.FiguraMod;
import org.figuramc.figura.avatar.Avatar;
import org.figuramc.figura.avatar.AvatarManager;
import org.figuramc.figura.backend2.NetworkStuff;
import org.figuramc.figura.config.ConfigManager;
import org.figuramc.figura.config.forge.ModConfig;
import org.figuramc.figura.gui.FiguraGui;
import org.figuramc.figura.utils.forge.FiguraResourceListenerImpl;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mod.EventBusSubscriber(modid = FiguraMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class FiguraModClientForge extends FiguraMod {
    // keybinds stored here
    public static List<KeyMapping> KEYBINDS = new ArrayList<>();

    @SubscribeEvent
    public static void onInitializeClient(FMLClientSetupEvent event) {
        NetworkStuff.initializeHttpClient();
        onClientInit();
        ModConfig.registerConfigScreen();
        vanillaOverlays.addAll(Arrays.asList(RenderGameOverlayEvent.ElementType.values()));

        ChannelBuilder.named(FiguraMod.resReconnect)
                .eventNetworkChannel()
                .addListener(e -> FiguraMod.reconnect());
        ChannelBuilder.named(FiguraMod.resWardrobe)
                .eventNetworkChannel()
                .addListener(e -> FiguraMod.openWardrobe());
    }


    public static void registerResourceListeners() {
        FiguraMod.getResourceListeners().forEach(figuraResourceListener -> ((SimpleReloadableResourceManager)Minecraft.getInstance().getResourceManager()).registerReloadListener((PreparableReloadListener) figuraResourceListener));
    }

    private static final List<RenderGameOverlayEvent.ElementType> vanillaOverlays = new ArrayList<>();

    public static void cancelVanillaOverlays(RenderGameOverlayEvent.Pre event) {
        if (vanillaOverlays.contains(event.getType())) {
            Entity entity = Minecraft.getInstance().getCameraEntity();
            Avatar avatar = entity == null ? null : AvatarManager.getAvatar(entity);
            if (avatar != null && avatar.luaRuntime != null && !avatar.luaRuntime.renderer.renderHUD) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void registerKeyBinding(FMLClientSetupEvent event) {
        // Config has to be initialized here, so that the keybinds exist on time
        ConfigManager.init();
        for (KeyMapping value : KEYBINDS) {
            if(value != null)
                ClientRegistry.registerKeyBinding(value);
        }
    }
}
