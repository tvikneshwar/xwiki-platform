/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.xwiki.edit;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.script.ScriptContext;

import org.xwiki.script.ScriptContextManager;
import org.xwiki.stability.Unstable;

/**
 * Base class for editors that take their input from the {@link ScriptContext}.
 * 
 * @param <D> the type of data that can be edited by this editor
 * @version $Id$
 * @since 8.2RC1
 */
@Unstable
public abstract class AbstractEditor<D> implements Editor<D>
{
    /**
     * The key used to access the edit information from the script context.
     */
    private static final String EDIT_CONTEXT_KEY = "edit";

    @Inject
    private ScriptContextManager scripts;

    @Override
    @SuppressWarnings("unchecked")
    public String render(D data, Map<String, Object> parameters) throws EditException
    {
        ScriptContext scriptContext = this.scripts.getScriptContext();
        // DefaultVelocityManager doesn't overwrite the existing keys when copying the script context bindings so we
        // have to reuse the existing binding for the edit context.
        Object editContext = scriptContext.getAttribute(EDIT_CONTEXT_KEY, ScriptContext.ENGINE_SCOPE);
        Map<String, Object> editContextMap;
        if (editContext instanceof Map) {
            editContextMap = (Map<String, Object>) editContext;
            editContextMap.clear();
        } else {
            editContextMap = new HashMap<>();
            scriptContext.setAttribute(EDIT_CONTEXT_KEY, editContextMap, ScriptContext.ENGINE_SCOPE);
        }
        editContextMap.putAll(getEditContext(data, parameters));
        return render();
    }

    protected Map<String, Object> getEditContext(D data, Map<String, Object> parameters)
    {
        Map<String, Object> editContext = new HashMap<>();
        editContext.put("data", data);
        editContext.put("parameters", parameters);
        return editContext;
    }

    protected abstract String render() throws EditException;
}
