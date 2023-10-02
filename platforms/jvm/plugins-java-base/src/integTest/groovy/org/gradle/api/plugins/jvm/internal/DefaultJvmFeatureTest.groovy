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

package org.gradle.api.plugins.jvm.internal

import org.gradle.api.InvalidUserDataException
import org.gradle.api.plugins.JavaBasePlugin
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.test.fixtures.AbstractProjectBuilderSpec

class DefaultJvmFeatureTest extends AbstractProjectBuilderSpec {
    // TODO: This test definitely isn't appropriate for Component any more, so I've moved it here.
    // Should features create the sourcesets they are going to use?  If they are being passed in
    // like this, this is just testing the SourceSets container, not the feature.
    def "cannot create multiple feature instances with the same source set"() {
        given:
        project.plugins.apply(JavaBasePlugin)
        def ext = project.getExtensions().getByType(JavaPluginExtension)

        when:
        new DefaultJvmFeature("feature1", ext.getSourceSets().create("feature"), Collections.emptyList(), project, false, false)
        new DefaultJvmFeature("feature2", ext.getSourceSets().create("feature"), Collections.emptyList(), project, false, false)

        then:
        def e = thrown(InvalidUserDataException)
        e.message == "Cannot add a SourceSet with name 'feature' as a SourceSet with that name already exists."
    }
}
