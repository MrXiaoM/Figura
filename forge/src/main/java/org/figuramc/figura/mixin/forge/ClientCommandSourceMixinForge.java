package org.figuramc.figura.mixin.forge;

import com.mojang.text2speech.Narrator;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import org.figuramc.figura.utils.FiguraClientCommandSource;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CommandSourceStack.class)
abstract class ClientCommandSourceMixinForge implements FiguraClientCommandSource {

    @Override
    public void figura$sendFeedback(Component message) {
        Minecraft.getInstance().gui.getChat().addMessage(message);
        Narrator.getNarrator().say(message.getString(), true);
    }

    @Override
    public void figura$sendError(Component message) {
        figura$sendFeedback(new TextComponent("").append(message).withStyle(ChatFormatting.RED));
    }

    @Override
    public Minecraft figura$getClient() {
        return Minecraft.getInstance();
    }

    @Override
    public LocalPlayer figura$getPlayer() {
        return Minecraft.getInstance().player;
    }

    @Override
    public ClientLevel figura$getWorld() {
        return Minecraft.getInstance().level;
    }
}