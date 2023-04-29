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
package ladysnake.satinrenderlayer;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import ladysnake.satin.api.event.EntitiesPreRenderCallback;
import ladysnake.satin.api.event.ShaderEffectRenderCallback;
import ladysnake.satin.api.managed.ManagedCoreShader;
import ladysnake.satin.api.managed.ManagedFramebuffer;
import ladysnake.satin.api.managed.ManagedShaderEffect;
import ladysnake.satin.api.managed.ShaderEffectManager;
import ladysnake.satin.api.managed.uniform.Uniform1f;
import ladysnake.satin.api.util.RenderLayerHelper;
import ladysnake.satintestcore.SatinTestCore;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class SatinRenderLayerTest {
    public static final DeferredRegister<EntityType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.ENTITIES, SatinTestCore.MOD_ID);


    /* * * * ManagedShaderEffect-based RenderLayer entity rendering * * * */

    public static final RegistryObject<EntityType<IronGolemEntity>> ILLUSION_GOLEM = REGISTER.register(
            "illusion_golem",
            () -> EntityType.Builder.create(IronGolemEntity::new, SpawnGroup.CREATURE)
                    .setDimensions(EntityType.IRON_GOLEM.getWidth(), EntityType.IRON_GOLEM.getHeight()).build("illusion_golem")
    );

    public static final ManagedShaderEffect illusionEffect = ShaderEffectManager.getInstance().manage(new Identifier("satinrenderlayer", "shaders/post/illusion.json"),
            effect -> effect.setUniformValue("ColorModulate", 1.2f, 0.7f, 0.2f, 1.0f));
    public static final ManagedFramebuffer illusionBuffer = illusionEffect.getTarget("final");

    /* * * * ManagedShaderProgram-based RenderLayer entity rendering * * * */

    public static final RegistryObject<EntityType<WitherEntity>> RAINBOW_WITHER = REGISTER.register(
            "rainbow_wither",
            () -> EntityType.Builder.create((EntityType<WitherEntity> entityType, World world) -> {
                        WitherEntity witherEntity = new WitherEntity(entityType, world);
                        witherEntity.setAiDisabled(true);
                        return witherEntity;
                    }, SpawnGroup.CREATURE)
                    .setDimensions(EntityType.WITHER.getWidth(), EntityType.WITHER.getHeight()).build("rainbow_wither")
    );

    public static final ManagedCoreShader rainbow = ShaderEffectManager.getInstance().manageCoreShader(new Identifier("satinrenderlayer", "rainbow"));
    private static final Uniform1f uniformSTime = rainbow.findUniform1f("STime");

    private static int ticks;

    public SatinRenderLayerTest(IEventBus bus) {
        REGISTER.register(bus);

        MinecraftForge.EVENT_BUS.register(this);

        bus.addListener(this::onRegisterRenderers);
        bus.addListener(this::onRegisterModelLayer);

        RenderLayer blockRenderLayer = illusionBuffer.getRenderLayer(RenderLayer.getTranslucent());
        RenderLayerHelper.registerBlockRenderLayer(blockRenderLayer);

        // BlockRenderLayerMap.INSTANCE.putBlock(SatinTestBlocks.DEBUG_BLOCK, blockRenderLayer);
    }

    @SubscribeEvent
    public void onRegisterModelLayer(EntityAttributeCreationEvent event) {
        event.put(ILLUSION_GOLEM.get(), IronGolemEntity.createIronGolemAttributes().build());
        event.put(RAINBOW_WITHER.get(), WitherEntity.createWitherAttributes().build());
    }

    @SubscribeEvent
    public void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ILLUSION_GOLEM.get(), IllusionGolemEntityRenderer::new);
        event.registerEntityRenderer(RAINBOW_WITHER.get(), RainbowWitherEntityRenderer::new);
    }

    @SubscribeEvent
    public void onClientTickEvent(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) ticks++;
    }

    @SubscribeEvent
    public void onEntitiesPreRender(EntitiesPreRenderCallback event) {
        uniformSTime.set((ticks + event.tickDelta) * 0.05f);
    }

    @SubscribeEvent
    public void onShaderEffectRender(ShaderEffectRenderCallback event) {
        MinecraftClient client = MinecraftClient.getInstance();
        illusionEffect.render(event.tickDelta);
        client.getFramebuffer().beginWrite(true);
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SrcFactor.ZERO, GlStateManager.DstFactor.ONE);
        illusionBuffer.draw(client.getWindow().getFramebufferWidth(), client.getWindow().getFramebufferHeight(), false);
        illusionBuffer.clear();
        client.getFramebuffer().beginWrite(true);
        RenderSystem.disableBlend();
    }
}
