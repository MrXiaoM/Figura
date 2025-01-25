package org.figuramc.figura.neoforge;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record UuidPayload(UUID uuid) implements CustomPacketPayload {
    public UuidPayload(final FriendlyByteBuf buffer) {
        this(buffer.readUUID());
    }

    @Override
    public void write(@NotNull FriendlyByteBuf buf) {
    }

    @Override
    public @NotNull ResourceLocation id() {
        return FiguraModClientNeoForge.resUuid;
    }
}
