package org.figuramc.figura.payload;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.figuramc.figura.FiguraMod;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record UuidPayload(UUID uuid) implements CustomPacketPayload {
    public static final Type<UuidPayload> TYPE = new Type<>(FiguraMod.resUuid);
    public UuidPayload(final FriendlyByteBuf buffer) {
        this(buffer.readUUID());
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
