/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.myfaces.cdi;

import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.AnnotatedType;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.inject.spi.BeforeBeanDiscovery;
import jakarta.enterprise.inject.spi.Extension;
import jakarta.faces.event.AfterPhase;
import jakarta.faces.event.BeforePhase;

public class PhaseEventBroadcasterExtension implements Extension
{
    void beforeBeanDiscovery(@Observes BeforeBeanDiscovery event, BeanManager beanManager)
    {
        event.addQualifier(AfterPhase.class);
        event.addQualifier(BeforePhase.class);

        AnnotatedType<PhaseEventBroadcasterPhaseListener.PhaseEventBroadcaster> annotatedType =
                beanManager.createAnnotatedType(PhaseEventBroadcasterPhaseListener.PhaseEventBroadcaster.class);
        event.addAnnotatedType(annotatedType, annotatedType.getJavaClass().getName());
    }
}
