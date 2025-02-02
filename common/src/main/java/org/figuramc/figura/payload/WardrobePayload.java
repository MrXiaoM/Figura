package org.figuramc.figura.payload;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.figuramc.figura.FiguraMod;
import org.jetbrains.annotations.NotNull;

public record WardrobePayload() implements CustomPacketPayload {
    public static final Type<WardrobePayload> TYPE = new Type<>(FiguraMod.resWardrobe);
    public WardrobePayload(final FriendlyByteBuf buffer) {
        this();
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
