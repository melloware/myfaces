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
package org.apache.myfaces.flow.cdi;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.faces.bean.ApplicationScoped;
import javax.faces.flow.Flow;
import javax.faces.flow.builder.FlowBuilder;
import javax.faces.flow.builder.FlowBuilderParameter;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.myfaces.flow.builder.FlowBuilderImpl;

/**
 * This bean is used later by CDI to process flow definitions
 *
 * @author Leonardo Uribe
 */
@Named(FlowBuilderFactoryBean.FLOW_BUILDER_FACTORY_BEAN_NAME)
@ApplicationScoped
public class FlowBuilderFactoryBean
{
    public static final String FLOW_BUILDER_FACTORY_BEAN_NAME = 
        "oam_FLOW_BUILDER_FACTORY_BEAN_NAME";
    
    /**
     * In this point two things are important:
     * 
     * 1. Initialize flows in a lazy way (triggered by JSF),
     * 2. Get multiple Flow instances.
     * 
     */
    @Inject 
    @Any
    private Instance<Flow> flowDefinitions;

    public FlowBuilderFactoryBean()
    {
    }
    
    @Produces
    @FlowBuilderParameter
    public FlowBuilder createFlowBuilderInstance()
    {
        return new FlowBuilderImpl();
    }
    
    /**
     * @return the flowDefinitions
     */
    public Instance<Flow> getFlowDefinitions()
    {
        // Pass the @FlowDefinition qualifier, so only all producer methods involving
        // this annotation will be taken into account.
        return flowDefinitions.select(
                new FlowDefinitionQualifier());
    }
    
}
