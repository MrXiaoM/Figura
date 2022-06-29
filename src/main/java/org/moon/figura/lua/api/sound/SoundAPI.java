package org.moon.figura.lua.api.sound;

import com.mojang.blaze3d.audio.SoundBuffer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import org.moon.figura.avatars.Avatar;
import org.moon.figura.lua.LuaNotNil;
import org.moon.figura.lua.LuaWhitelist;
import org.moon.figura.lua.docs.LuaFunctionOverload;
import org.moon.figura.lua.docs.LuaMethodDoc;
import org.moon.figura.lua.docs.LuaTypeDoc;
import org.moon.figura.math.vector.FiguraVec3;
import org.moon.figura.trust.TrustContainer;
import org.moon.figura.trust.TrustManager;
import org.moon.figura.utils.LuaUtils;
import org.terasology.jnlua.LuaRuntimeException;

@LuaWhitelist
@LuaTypeDoc(
        name = "SoundAPI",
        description = "sound"
)
public class SoundAPI {

    private final Avatar owner;

    public SoundAPI(Avatar owner) {
        this.owner = owner;
    }

    @LuaWhitelist
    @LuaMethodDoc(
            overloads = {
                    @LuaFunctionOverload(
                            argumentTypes = {SoundAPI.class, String.class, FiguraVec3.class},
                            argumentNames = {"api", "sound", "pos"}
                    ),
                    @LuaFunctionOverload(
                            argumentTypes = {SoundAPI.class, String.class, Double.class, Double.class, Double.class},
                            argumentNames = {"api", "sound", "posX", "posY", "posZ"}
                    ),
                    @LuaFunctionOverload(
                            argumentTypes = {SoundAPI.class, String.class, FiguraVec3.class, Double.class, Double.class},
                            argumentNames = {"api", "sound", "pos", "volume", "pitch"}
                    ),
                    @LuaFunctionOverload(
                            argumentTypes = {SoundAPI.class, String.class, Double.class, Double.class, Double.class, Double.class, Double.class},
                            argumentNames = {"api", "sound", "posX", "posY", "posZ", "volume", "pitch"}
                    )
            },
            description = "sound.play_sound"
    )
    public static void playSound(@LuaNotNil SoundAPI api, @LuaNotNil String id, Object x, Double y, Double z, Double w, Double t) {
        if (!api.owner.soundsRemaining.use())
            return;

        FiguraVec3 pos;
        double volume = 1.0;
        double pitch = 1.0;

        if (x instanceof FiguraVec3) {
            pos = ((FiguraVec3) x).copy();
            if (y != null) volume = y;
            if (z != null) pitch = z;
        } else if (x == null || x instanceof Double) {
            pos = LuaUtils.parseVec3("playSound", x, y, z);
            if (w != null) volume = w;
            if (t != null) pitch = t;
        } else {
            throw new LuaRuntimeException("Illegal argument to playSound(): " + x);
        }

        //get and play the sound
        SoundBuffer buffer = api.owner.customSounds.get(id);
        if (buffer != null && TrustManager.get(api.owner.owner).get(TrustContainer.Trust.CUSTOM_SOUNDS) == 1) {
            FiguraChannel.getInstance().playSound(api.owner.owner, id, buffer, pos.x, pos.y, pos.z, (float) volume, (float) pitch, false);
        } else {
            SoundEvent event = new SoundEvent(new ResourceLocation(id));
            FiguraChannel.getInstance().playSound(api.owner.owner, id, event, pos.x, pos.y, pos.z, (float) volume, (float) pitch, false);
        }

        pos.free();
    }

    @LuaWhitelist
    @LuaMethodDoc(
            overloads = {
                    @LuaFunctionOverload(
                            argumentTypes = SoundAPI.class,
                            argumentNames = "api"
                    ),
                    @LuaFunctionOverload(
                            argumentTypes = {SoundAPI.class, String.class},
                            argumentNames = {"api", "id"}
                    )
            },
            description = "sound.stop_sound"
    )
    public static void stopSound(@LuaNotNil SoundAPI api, String id) {
        FiguraChannel.getInstance().stopSound(api.owner.owner, id);
    }

    @Override
    public String toString() {
        return "SoundAPI";
    }
}