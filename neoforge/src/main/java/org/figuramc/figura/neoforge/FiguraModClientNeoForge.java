package org.figuramc.figura.neoforge;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.figuramc.figura.FiguraMod;
import org.figuramc.figura.avatar.Avatar;
import org.figuramc.figura.avatar.AvatarManager;
import org.figuramc.figura.config.ConfigManager;
import org.figuramc.figura.config.neoforge.ModConfig;
import org.figuramc.figura.gui.neoforge.GuiOverlay;
import org.figuramc.figura.gui.neoforge.GuiUnderlay;
import org.figuramc.figura.payload.ReconnectPayload;
import org.figuramc.figura.payload.UuidPayload;
import org.figuramc.figura.payload.WardrobePayload;
import org.figuramc.figura.utils.neoforge.FiguraResourceListenerImpl;

import java.util.ArrayList;
import java.util.List;

import static org.figuramc.figura.payload.Decoder.decoder;

@EventBusSubscriber(modid = FiguraMod.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class FiguraModClientNeoForge extends FiguraMod {
    // keybinds stored here
    public static List<KeyMapping> KEYBINDS = new ArrayList<>();

    @SubscribeEvent
    public static void onInitializeClient(FMLClientSetupEvent event) {
        onClientInit();
        ModConfig.registerConfigScreen();
    }

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar;
        registrar = event.registrar(FiguraMod.resReconnect.getNamespace()).optional();
        registrar.playToClient(ReconnectPayload.TYPE, decoder(ReconnectPayload::new),
                (data, context) -> FiguraMod.reconnect());
        registrar = event.registrar(FiguraMod.resUuid.getNamespace()).optional();
        registrar.playToClient(UuidPayload.TYPE, decoder(UuidPayload::new),
                (data, context) -> FiguraMod.updateLocalUUID(data.uuid()));
        registrar = event.registrar(FiguraMod.resWardrobe.getNamespace()).optional();
        registrar.playToClient(WardrobePayload.TYPE, decoder(WardrobePayload::new),
                (data, context) -> FiguraMod.openWardrobe());
    }

    @SubscribeEvent
    public static void registerResourceListener(RegisterClientReloadListenersEvent event) {
        getResourceListeners().forEach(figuraResourceListener -> event.registerReloadListener((FiguraResourceListenerImpl)figuraResourceListener));
    }

    @SubscribeEvent
    public static void registerOverlays(RegisterGuiLayersEvent event) {
        event.registerAboveAll(new ResourceLocation(FiguraMod.MOD_ID, "figura_overlay"), new GuiOverlay());
        event.registerBelowAll(new ResourceLocation(FiguraMod.MOD_ID, "figura_underlay"), new GuiUnderlay());
    }

    private static final List<ResourceLocation> vanillaOverlays = new ArrayList<>();

    public static void cancelVanillaOverlays(RenderGuiLayerEvent.Pre event) {
        if (event.getName().getNamespace().equals("minecraft")) {
            Entity entity = Minecraft.getInstance().getCameraEntity();
            Avatar avatar = entity == null ? null : AvatarManager.getAvatar(entity);
            if (avatar != null && avatar.luaRuntime != null && !avatar.luaRuntime.renderer.renderHUD) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void registerKeyBinding(RegisterKeyMappingsEvent event) {
        // Config has to be initialized here, so that the keybinds exist on time
        ConfigManager.init();
        for (KeyMapping value : KEYBINDS) {
            if(value != null)
                event.register(value);
        }
    }
}
