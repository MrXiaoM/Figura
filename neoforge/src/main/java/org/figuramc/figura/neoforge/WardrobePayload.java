package org.figuramc.figura.neoforge;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record WardrobePayload() implements CustomPacketPayload {
    public WardrobePayload(final FriendlyByteBuf buffer) {
        this();
    }
    @Override
    public void write(@NotNull FriendlyByteBuf buf) {
    }

    @Override
    public @NotNull ResourceLocation id() {
        return FiguraModClientNeoForge.resWardrobe;
    }
}