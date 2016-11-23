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

package org.apache.myfaces.component.search;

import javax.faces.component.UIComponent;
import javax.faces.component.search.SearchExpressionContext;
import javax.faces.component.search.SearchKeywordContext;
import javax.faces.component.search.SearchExpressionResolver;

/**
 *
 */
public class ThisSearchExpressionResolver extends SearchExpressionResolver
{
    public static final String THIS_KEYWORD = "this";

    @Override
    public void resolve(SearchKeywordContext expressionContext, UIComponent last, String command)
    {
        if (command != null && command.equalsIgnoreCase(THIS_KEYWORD))
        {
            expressionContext.invokeContextCallback(expressionContext.getFacesContext(), last);
        }
    }

    @Override
    public boolean matchKeyword(SearchExpressionContext searchExpressionContext, String keyword)
    {
        return THIS_KEYWORD.equalsIgnoreCase(keyword);
    }

    @Override
    public boolean isPassthroughKeyword(SearchExpressionContext searchExpressionContext, String keyword)
    {
        return false;
    }

    @Override
    public boolean isLeafKeyword(SearchExpressionContext searchExpressionContext, String keyword)
    {
        return false;
    }
}