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
package ladysnake.satin.api.event;

import net.minecraft.client.option.GraphicsMode;
import net.minecraft.client.render.Camera;
import net.minecraftforge.eventbus.api.Event;

/**
 * @see PostWorldRenderCallbackV2
 */
public class PostWorldRenderCallback extends Event {
    public Camera camera;
    public float tickDelta;
    public long nanoTime;

    /**
     * Fired after Minecraft has rendered everything in the world, before it renders hands, HUDs and GUIs.
     *
     * <p>{@link net.minecraft.client.gl.PostEffectProcessor}s <strong>must not</strong> be rendered in this callback, as they will prevent
     * {@link GraphicsMode#FABULOUS fabulous graphics} and other effects from working properly.
     */
    public PostWorldRenderCallback(Camera camera, float tickDelta, long nanoTime) {
        this.camera = camera;
        this.tickDelta = tickDelta;
        this.nanoTime = nanoTime;
    }

}
