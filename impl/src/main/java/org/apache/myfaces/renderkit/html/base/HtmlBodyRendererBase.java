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
package org.apache.myfaces.renderkit.html.base;

import org.apache.myfaces.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.renderkit.html.util.ClientBehaviorRendererUtils;
import org.apache.myfaces.renderkit.html.util.CommonHtmlAttributesUtil;
import org.apache.myfaces.renderkit.html.util.CommonHtmlEventsUtil;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import jakarta.faces.application.ProjectStage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.UIViewRoot;
import jakarta.faces.component.behavior.ClientBehavior;
import jakarta.faces.component.behavior.ClientBehaviorHolder;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.ResponseWriter;

import org.apache.myfaces.renderkit.ClientBehaviorEvents;
import org.apache.myfaces.renderkit.html.util.ResourceUtils;
import org.apache.myfaces.renderkit.html.util.HTML;

/**
 * Renderer used by h:body component
 * 
 * @since 2.0
 */
public class HtmlBodyRendererBase extends HtmlRenderer
{

    @Override
    public void decode(FacesContext context, UIComponent component)
    {
        // check for npe
        super.decode(context, component);
        
        ClientBehaviorRendererUtils.decodeClientBehaviors(context, component);
    }

    @Override
    public void encodeBegin(FacesContext facesContext, UIComponent component) throws IOException
    {
        super.encodeBegin(facesContext, component); //check for NP

        ResponseWriter writer = facesContext.getResponseWriter();
        if (component instanceof ClientBehaviorHolder holder)
        {
            Map<String, List<ClientBehavior>> behaviors = holder.getClientBehaviors();
            if (!behaviors.isEmpty())
            {
                ResourceUtils.renderDefaultJsfJsInlineIfNecessary(facesContext, writer);
            }

            writer.startElement(HTML.BODY_ELEM, component);
            if (!behaviors.isEmpty())
            {
                // Note "name" does not exists as attribute in <body> tag.
                writer.writeAttribute(HTML.ID_ATTR, component.getClientId(facesContext),null);
            }
            else
            {
                if (HtmlRendererUtils.isOutputHtml5Doctype(facesContext))
                {
                    HtmlRendererUtils.writeIdIfNecessary(writer, component, facesContext);
                }
            }
            if (behaviors.isEmpty() && isCommonPropertiesOptimizationEnabled(facesContext))
            {
                CommonHtmlAttributesUtil.renderEventProperties(writer, 
                        CommonHtmlAttributesUtil.getMarkedAttributes(component), component);
            }
            else
            {
                if (isCommonEventsOptimizationEnabled(facesContext))
                {
                    CommonHtmlEventsUtil.renderBehaviorizedEventHandlers(facesContext, writer, 
                           CommonHtmlAttributesUtil.getMarkedAttributes(component),
                           CommonHtmlEventsUtil.getMarkedEvents(component), component, behaviors);
                }
                else
                {
                    HtmlRendererUtils.renderBehaviorizedEventHandlers(facesContext, writer, component, behaviors);
                }
            }
            HtmlRendererUtils.renderBehaviorizedAttribute(facesContext, writer, HTML.ONLOAD_ATTR, component,
                    ClientBehaviorEvents.LOAD, behaviors, HTML.ONLOAD_ATTR);
            HtmlRendererUtils.renderBehaviorizedAttribute(facesContext, writer, HTML.ONUNLOAD_ATTR, component,
                    ClientBehaviorEvents.UNLOAD, behaviors, HTML.ONUNLOAD_ATTR);
            if (isCommonPropertiesOptimizationEnabled(facesContext))
            {
                HtmlRendererUtils.renderHTMLAttributes(writer, component,
                        HTML.BODY_ATTRIBUTES_WITHOUT_EVENTS);
                CommonHtmlAttributesUtil.renderCommonPassthroughPropertiesWithoutEvents(writer, 
                        CommonHtmlAttributesUtil.getMarkedAttributes(component), component);
            }
            else
            {
                HtmlRendererUtils.renderHTMLAttributes(writer, component,
                        HTML.BODY_PASSTHROUGH_ATTRIBUTES_WITHOUT_EVENTS);
            }
            HtmlRendererUtils.renderHTMLStringAttribute(writer, component, HTML.XMLNS_ATTR , HTML.XMLNS_ATTR);
            
        }
        else
        {
            writer.startElement(HTML.BODY_ELEM, component);
            HtmlRendererUtils.writeIdIfNecessary(writer, component, facesContext);
            if (isCommonPropertiesOptimizationEnabled(facesContext))
            {
                HtmlRendererUtils.renderHTMLAttributes(writer, component,
                        HTML.BODY_ATTRIBUTES);
                CommonHtmlAttributesUtil.renderCommonPassthroughProperties(writer, 
                        CommonHtmlAttributesUtil.getMarkedAttributes(component), component);
            }
            else
            {
                HtmlRendererUtils.renderHTMLAttributes(writer, component,
                        HTML.BODY_PASSTHROUGH_ATTRIBUTES);
            }
            HtmlRendererUtils.renderHTMLStringAttribute(writer, component, HTML.XMLNS_ATTR , HTML.XMLNS_ATTR);
        }
    }

    @Override
    public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException
    {
        super.encodeEnd(facesContext, component); //check for NP

        ResponseWriter writer = facesContext.getResponseWriter();
        UIViewRoot root = facesContext.getViewRoot();
        // Perf: use indexes for iteration over children,
        // componentResources are jakarta.faces.component._ComponentChildrenList._ComponentChildrenList(UIComponent)  
        List<UIComponent> componentResources = root.getComponentResources(facesContext, HTML.BODY_TARGET);
        int childrenCount = componentResources.size();
        if (childrenCount > 0)
        {
            for (int i = 0; i < childrenCount; i++)
            {
                UIComponent child = componentResources.get(i);
                child.encodeAll(facesContext);
            }
        }
        
        // render all unhandled FacesMessages when ProjectStage is Development
        if (facesContext.isProjectStage(ProjectStage.Development))
        {
            HtmlRendererUtils.renderUnhandledFacesMessages(facesContext);
        }
        
        writer.endElement(HTML.BODY_ELEM);
    }
}
