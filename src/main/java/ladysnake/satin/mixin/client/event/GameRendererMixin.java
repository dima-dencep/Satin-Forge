/*
 * Satin
 * Copyright (C) 2019-2023 Ladysnake
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; If not, see <https://www.gnu.org/licenses>.
 */
package ladysnake.satin.mixin.client.event;

import ladysnake.satin.api.event.PickEntityShaderCallback;
import ladysnake.satin.api.event.ShaderEffectRenderCallback;
import ladysnake.satin.impl.ReloadableShaderEffectManager;
import net.minecraft.client.gl.ShaderEffect;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.resource.ResourceFactory;
import net.minecraft.util.Identifier;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

import static org.spongepowered.asm.mixin.injection.At.Shift.AFTER;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {




    @Shadow @Nullable
    ShaderEffect shader;

    @Shadow protected abstract void loadShader(Identifier location);

    /**
     * Fires {@link ShaderEffectRenderCallback}
     */
    @Inject(
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;drawEntityOutlinesFramebuffer()V", shift = AFTER),
            method = "render"
    )
    private void hookShaderRender(float tickDelta, long nanoTime, boolean renderLevel, CallbackInfo info) {
        MinecraftForge.EVENT_BUS.post(new ShaderEffectRenderCallback(tickDelta));
    }

    /**
     * Fires {@link PickEntityShaderCallback}
     * Disabled by optifine
     */
    @Inject(method = "onCameraEntitySet", at = @At(value = "RETURN"), require = 0)
    private void useCustomEntityShader(@Nullable Entity entity, CallbackInfo info) {
        if (this.shader == null) {
            // Mixin does not like method references to shadowed methods
            //noinspection Convert2MethodRef
            MinecraftForge.EVENT_BUS.post(new PickEntityShaderCallback(entity, loc -> this.loadShader(loc), () -> this.shader));
        }
    }

    @Inject(method = "preloadShaders", at = @At(value = "RETURN"))
    private void loadSatinPrograms(ResourceFactory factory, CallbackInfo ci) {
        ReloadableShaderEffectManager.INSTANCE.reload(factory);
    }
}
