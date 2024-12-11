package org.figuramc.figura.mixin;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.world.level.Level;
import org.figuramc.figura.FiguraMod;
import org.figuramc.figura.avatar.Avatar;
import org.figuramc.figura.avatar.AvatarManager;
import org.figuramc.figura.avatar.local.LocalAvatarLoader;
import org.figuramc.figura.backend2.NetworkStuff;
import org.figuramc.figura.gui.widgets.lists.AvatarList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ClientPacketListener.class, priority = 999)
public abstract class ClientPacketListenerMixin {

    @Shadow public abstract ClientLevel getLevel();

    @Inject(at = @At("HEAD"), method = "sendUnsignedCommand", cancellable = true)
    private void sendUnsignedCommand(String command, CallbackInfoReturnable<Boolean> cir) {
        if (command.startsWith(FiguraMod.MOD_ID))
            cir.setReturnValue(false);
    }

    @Inject(method = "handleEntityEvent", at = @At(value = "FIELD", target = "Lnet/minecraft/core/particles/ParticleTypes;TOTEM_OF_UNDYING:Lnet/minecraft/core/particles/SimpleParticleType;"), cancellable = true)
    private void handleTotem(ClientboundEntityEventPacket packet, CallbackInfo ci) {
        Level level = getLevel();
        Avatar avatar = AvatarManager.getAvatar(packet.getEntity(level));
        if (avatar != null && avatar.totemEvent())
            ci.cancel();
    }

    @Inject(method = "handleUnknownCustomPayload", at = @At(value = "HEAD"), cancellable = true)
    private void handleUnknownCustomPayload(CustomPacketPayload payload, CallbackInfo ci) {
        if (payload.id().equals(FiguraMod.resReconnect)) {
            ci.cancel();
            AvatarManager.clearAvatars(FiguraMod.getLocalPlayerUUID());
            try {
                LocalAvatarLoader.loadAvatar(null, null);
            } catch (Exception ignored) {}
            AvatarManager.localUploaded = true;
            AvatarList.selectedEntry = null;
            NetworkStuff.auth();
        }
    }
}
