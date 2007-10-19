/*
 * Copyright 2007 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl1.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.iu.uis.eden.routetemplate;

import org.apache.bsf.BSFEngine;
import org.apache.bsf.BSFException;
import org.apache.bsf.BSFManager;
import org.codehaus.groovy.bsf.GroovyEngine;
import org.junit.Ignore;
import org.junit.Test;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;


/**
 * Tests that Groovy can be loaded via Bean Scripting Framework

 * @author Kuali Rice Team (kuali-rice@googlegroups.com)
 */
@Ignore // until groovy dependencies issue gets resolved
public class LoadEmbeddedGroovyTest {
    @Test public void testNativeGroovy() {
        Binding binding = new Binding();
        binding.setVariable("foo", new Integer(2));
        GroovyShell shell = new GroovyShell(binding);

        Object value = shell.evaluate("println 'Hello World!'; x = 123; return foo * 10");
        assert value.equals(new Integer(20));
        assert binding.getVariable("x").equals(new Integer(123));
    }

    @Test public void testBSFGroovy() throws BSFException {
        BSFManager.registerScriptingEngine(
                "groovy", 
                "org.codehaus.groovy.bsf.GroovyEngine", 
                new String[] { "groovy", "gy" }
        );
        GroovyEngine ge = new GroovyEngine();
        BSFManager manager = new BSFManager();
        BSFEngine engine = manager.loadScriptingEngine("groovy");
        manager.eval("groovy", "LoadEmbeddedGroovyTest", 0, 0, "println 'hello embedded groovy world'");
    }
}
