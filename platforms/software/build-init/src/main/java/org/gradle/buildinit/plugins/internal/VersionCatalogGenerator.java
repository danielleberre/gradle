/*
 * Copyright 2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.buildinit.plugins.internal;

import org.gradle.api.UncheckedIOException;
import org.gradle.api.file.Directory;
import org.gradle.util.internal.GFileUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.Collection;

/**
 * Generates version catalogs based on versions, libraries and plugins tracked in the BuildContentGenerationContext's VersionCatalogDependencyRegistry.
 */
public class VersionCatalogGenerator {
    final Directory target;

    private VersionCatalogGenerator(Directory target) {
        this.target = target;
    }

    public static VersionCatalogGenerator create(Directory target) {
        return new VersionCatalogGenerator(target);
    }

    public void generate(BuildContentGenerationContext buildContentGenerationContext) {
        VersionCatalogDependencyRegistry versionCatalogDependencyRegistry = buildContentGenerationContext.getVersionCatalogDependencyRegistry();
        Path gradleDirectory = target.getAsFile().toPath().resolve("gradle");
        GFileUtils.mkdirs(gradleDirectory.toFile());
        try (PrintWriter writer = new PrintWriter(new FileWriter(gradleDirectory.resolve("libs.versions.toml").toFile()))) {
            writer.println("# This file was generated by the Gradle 'init' task.");
            writer.println("# https://docs.gradle.org/current/userguide/platforms.html#sub::toml-dependencies-format");

            Collection<VersionCatalogDependencyRegistry.VersionEntry> versions = versionCatalogDependencyRegistry.getVersions();
            if (!versions.isEmpty()) {
                writer.println();
                writer.println("[versions]");
                for (VersionCatalogDependencyRegistry.VersionEntry v : versions) {
                    writer.println(String.format("%s = \"%s\"", v.alias, v.version));
                }
            }

            Collection<VersionCatalogDependencyRegistry.LibraryEntry> libraries = versionCatalogDependencyRegistry.getLibraries();
            if (!libraries.isEmpty()) {
                writer.println();
                writer.println("[libraries]");
                for (VersionCatalogDependencyRegistry.LibraryEntry l : versionCatalogDependencyRegistry.getLibraries()) {
                    writer.println(String.format("%s = { module = \"%s\", version.ref = \"%s\" }", l.alias, l.module, l.versionRef));
                }
            }

            Collection<VersionCatalogDependencyRegistry.PluginEntry> plugins = versionCatalogDependencyRegistry.getPlugins();
            if (!plugins.isEmpty()) {
                writer.println();
                writer.println("[plugins]");
                for (VersionCatalogDependencyRegistry.PluginEntry p : plugins) {
                    writer.println(String.format("%s = { id = \"%s\", version = \"%s\" }", p.alias, p.pluginId, p.version));
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
