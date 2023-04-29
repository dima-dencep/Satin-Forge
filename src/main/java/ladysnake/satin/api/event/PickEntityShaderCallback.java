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

import net.minecraft.client.gl.ShaderEffect;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraftforge.eventbus.api.Event;

import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class PickEntityShaderCallback extends Event {
    public @Nullable Entity entity;
    public Consumer<Identifier> loadShaderFunc;
    public Supplier<ShaderEffect> appliedShaderGetter;

    /**
     * Fired in {@link net.minecraft.client.render.GameRenderer#onCameraEntitySet(Entity)}
     */
    public PickEntityShaderCallback(@Nullable Entity entity, Consumer<Identifier> loadShaderFunc, Supplier<ShaderEffect> appliedShaderGetter) {
        this.entity = entity;
        this.loadShaderFunc = loadShaderFunc;
        this.appliedShaderGetter = appliedShaderGetter;
    }

}
