/*
 * Satin
 * Copyright (C) 2019-2020 Ladysnake
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
package ladysnake.satin.impl;

import ladysnake.satin.Satin;
import ladysnake.satin.api.experimental.managed.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import org.apiguardian.api.API;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.apiguardian.api.API.Status.INTERNAL;

public abstract class ResettableManagedShaderBase<S extends AutoCloseable> implements UniformFinder {
    /**Location of the shader json definition file*/
    private final Identifier location;
    private final Map<String, ManagedUniform> managedUniforms = new HashMap<>();
    private boolean errored;
    @CheckForNull
    protected S shader;

    public ResettableManagedShaderBase(Identifier location) {
        this.location = location;
    }

    @Nullable
    protected S getShaderOrLog() {
        if (!this.isInitialized() && !this.errored) {
            this.initializeOrLog();
        }
        return this.shader;
    }

    @API(status = INTERNAL)
    public void initializeOrLog() {
        try {
            this.initialize();
        } catch (IOException e) {
            this.errored = true;
            this.logInitError(e);
        }
    }

    protected abstract void logInitError(IOException e);

    public void initialize() throws IOException {
        this.release();
        MinecraftClient mc = MinecraftClient.getInstance();
        this.shader = parseShader(mc, this.location);
        this.setup(mc.getWindow().getFramebufferWidth(), mc.getWindow().getFramebufferHeight());
    }

    protected abstract S parseShader(MinecraftClient mc, Identifier location) throws IOException;

    /**
     * {@inheritDoc}
     */
    public void release() {
        if (this.isInitialized()) {
            try {
                assert this.shader != null;
                this.shader.close();
                this.shader = null;
            } catch (Exception e) {
                throw new RuntimeException("Failed to release shader " + this.location, e);
            }
        }
        this.errored = false;
    }

    protected Collection<ManagedUniform> getManagedUniforms() {
        return this.managedUniforms.values();
    }

    protected abstract boolean setupUniform(ManagedUniform uniform, S shader);

    public boolean isInitialized() {
        return this.shader != null;
    }

    public boolean isErrored() {
        return this.errored;
    }

    public void setErrored(boolean error) {
        this.errored = error;
    }

    public Identifier getLocation() {
        return location;
    }

    protected ManagedUniform manageUniform(String uniformName) {
        return this.managedUniforms.computeIfAbsent(uniformName, name -> {
            ManagedUniform ret = new ManagedUniform(name);
            if (this.shader != null) {
                boolean found = setupUniform(ret, shader);
                if (!found) {
                    Satin.LOGGER.warn("[Satin] No uniform found with name {} in shader {}", name, this.location);
                }
            }
            return ret;
        });
    }

    @Override
    public Uniform1i findUniform1i(String uniformName) {
        return manageUniform(uniformName);
    }

    @Override
    public Uniform2i findUniform2i(String uniformName) {
        return manageUniform(uniformName);
    }

    @Override
    public Uniform3i findUniform3i(String uniformName) {
        return manageUniform(uniformName);
    }

    @Override
    public Uniform4i findUniform4i(String uniformName) {
        return manageUniform(uniformName);
    }

    @Override
    public Uniform1f findUniform1f(String uniformName) {
        return manageUniform(uniformName);
    }

    @Override
    public Uniform2f findUniform2f(String uniformName) {
        return manageUniform(uniformName);
    }

    @Override
    public Uniform3f findUniform3f(String uniformName) {
        return manageUniform(uniformName);
    }

    @Override
    public Uniform4f findUniform4f(String uniformName) {
        return manageUniform(uniformName);
    }

    @Override
    public UniformMat4 findUniformMat4(String uniformName) {
        return manageUniform(uniformName);
    }

    @API(status = INTERNAL)
    public abstract void setup(int newWidth, int newHeight);
}
