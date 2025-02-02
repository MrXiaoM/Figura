package org.figuramc.figura.payload;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public interface Decoder<V> extends StreamCodec<RegistryFriendlyByteBuf, V> {
    @Override
    default void encode(@NotNull RegistryFriendlyByteBuf object, @NotNull V object2) {
    }

    static <V> Decoder<V> decoder(Function<RegistryFriendlyByteBuf, V> block) {
        return new Decoder<V>() {
            @Override
            public @NotNull V decode(@NotNull RegistryFriendlyByteBuf object) {
                return block.apply(object);
            }
        };
    }
}
