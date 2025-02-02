package org.figuramc.figura.forge;

import net.minecraft.client.KeyMapping;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.network.ChannelBuilder;
import org.figuramc.figura.FiguraMod;
import org.figuramc.figura.config.ConfigManager;
import org.figuramc.figura.config.forge.ModConfig;
import org.figuramc.figura.utils.forge.FiguraResourceListenerImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = FiguraMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class FiguraModClientForge extends FiguraMod {
    // keybinds stored here
    public static List<KeyMapping> KEYBINDS = new ArrayList<>();

    @SubscribeEvent
    public static void onInitializeClient(FMLClientSetupEvent event) {
        onClientInit();
        ModConfig.registerConfigScreen();
        ChannelBuilder.named(FiguraMod.resReconnect)
                .eventNetworkChannel()
                .addListener(e -> FiguraMod.reconnect());
        ChannelBuilder.named(FiguraMod.resUuid)
                .eventNetworkChannel()
                .addListener(e -> {
                    FriendlyByteBuf payload = e.getPayload();
                    payload.resetReaderIndex();
                    long most = payload.readLong();
                    long least = payload.readLong();
                    UUID uuid = new UUID(most, least);
                    FiguraMod.updateLocalUUID(uuid);
                });
        ChannelBuilder.named(FiguraMod.resWardrobe)
                .eventNetworkChannel()
                .addListener(e -> FiguraMod.openWardrobe());
    }

    @SubscribeEvent
    public static void registerResourceListener(RegisterClientReloadListenersEvent event) {
        getResourceListeners().forEach(figuraResourceListener -> event.registerReloadListener((FiguraResourceListenerImpl)figuraResourceListener));
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
