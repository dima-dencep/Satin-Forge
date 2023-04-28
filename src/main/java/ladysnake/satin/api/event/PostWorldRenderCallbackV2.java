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

import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraftforge.eventbus.api.Event;

public class PostWorldRenderCallbackV2 extends Event {
    public MatrixStack matrices;
    public Camera camera;
    public float tickDelta;
    public long nanoTime;

    /**
     * Fired after Minecraft has rendered everything in the world, before it renders hands, HUDs and GUIs.
     */
    public PostWorldRenderCallbackV2(MatrixStack matrices, Camera camera, float tickDelta, long nanoTime) {
        this.matrices = matrices;
        this.camera = camera;
        this.tickDelta = tickDelta;
        this.nanoTime = nanoTime;
    }

}
