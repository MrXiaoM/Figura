package org.figuramc.figura.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.server.packs.PackType;
import org.figuramc.figura.FiguraMod;
import org.figuramc.figura.commands.fabric.FiguraCommandsFabric;
import org.figuramc.figura.config.ConfigManager;
import org.figuramc.figura.payload.ReconnectPayload;
import org.figuramc.figura.payload.UuidPayload;
import org.figuramc.figura.payload.WardrobePayload;
import org.figuramc.figura.utils.fabric.FiguraResourceListenerImpl;

public class FiguraModFabric extends FiguraMod implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ConfigManager.init();
        onClientInit();
        FiguraCommandsFabric.init();
        // we cast here to the impl that implements synchronous as the manager wants
        // register reload listener
        ResourceManagerHelper managerHelper = ResourceManagerHelper.get(PackType.CLIENT_RESOURCES);
        getResourceListeners().forEach(figuraResourceListener -> managerHelper.registerReloadListener((FiguraResourceListenerImpl)figuraResourceListener));
        ClientPlayNetworking.registerGlobalReceiver(ReconnectPayload.TYPE, (data, handler) -> FiguraMod.reconnect());
        ClientPlayNetworking.registerGlobalReceiver(WardrobePayload.TYPE, (data, handler) -> FiguraMod.openWardrobe());
        ClientPlayNetworking.registerGlobalReceiver(UuidPayload.TYPE, (data, handler) -> FiguraMod.updateLocalUUID(data.uuid()));
    }
}
