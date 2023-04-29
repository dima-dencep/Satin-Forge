/*
 * Satin
 * Copyright (C) 2019-2022 Ladysnake
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
package ladysnake.satintestcore.item;

import ladysnake.satintestcore.SatinTestCore;
import ladysnake.satintestcore.block.SatinTestBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SatinTestItems {
    public static final DeferredRegister<Item> REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, SatinTestCore.MOD_ID);

    public static final RegistryObject<DebugItem> DEBUG_ITEM = REGISTER.register("debug_item", () -> new DebugItem(new Item.Settings()));
    public static final RegistryObject<BlockItem> DEBUG_BLOCK = REGISTER.register("debug_block", () -> new BlockItem(SatinTestBlocks.DEBUG_BLOCK.get(), new Item.Settings()));
    // Registries.BLOCK.getId(SatinTestBlocks.DEBUG_BLOCK.get()).getPath()

    public static void init(IEventBus bus) {
        REGISTER.register(bus);
    }
}