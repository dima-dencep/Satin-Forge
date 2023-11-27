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
package ladysnake.satin.mixin.client.blockrenderlayer;

import com.google.common.collect.ImmutableList;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import ladysnake.satin.impl.BlockRenderLayerRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(RenderLayer.class)
public abstract class RenderLayerMixin extends RenderPhase {
    private RenderLayerMixin() {
        super(null, null, null);
    }

    @ModifyReturnValue(
            method = "getBlockLayers",
            at = @At("RETURN")
    )
    private static List<RenderLayer> getBlockLayers(List<RenderLayer> info) {
        return ImmutableList.<RenderLayer>builder()
                .addAll(info)
                .addAll(BlockRenderLayerRegistry.INSTANCE.getLayers())
                .build();
    }
}
