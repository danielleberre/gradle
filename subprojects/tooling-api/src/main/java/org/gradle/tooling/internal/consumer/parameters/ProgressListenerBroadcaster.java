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

package org.gradle.tooling.internal.consumer.parameters;

import org.gradle.api.NonNullApi;
import org.gradle.internal.event.ListenerBroadcast;
import org.gradle.tooling.events.ProgressListener;
import org.gradle.tooling.internal.protocol.events.InternalProgressEvent;

@NonNullApi
interface ProgressListenerBroadcaster {
    boolean canHandle(Class<?> eventType);

    void broadCast(Object progressEvent);

    boolean canHandleProgressEvent(Class<?> progressEventType);

    ListenerBroadcast<ProgressListener> getListeners();

    boolean canHandleDescriptor(Class<?> aClass);

    void broadCastInterProgressEvent(InternalProgressEvent progressEvent);
}
