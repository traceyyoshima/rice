/*
 * Copyright 2005-2007 The Kuali Foundation
 *
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.rice.ksb.messaging.serviceconnectors;

import org.kuali.rice.ksb.messaging.ServiceInfo;


/**
 * Connector (provider of client proxys) for RESTful services.
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * @since 0.9
 */
public class RESTConnector extends AbstractServiceConnector {
    
    public RESTConnector(final ServiceInfo serviceInfo) {
        super(serviceInfo);
    }

    /**
     * @return a resource facade that wraps the CXF client proxies for these resources
     * @see org.kuali.rice.ksb.messaging.serviceconnectors.ServiceConnector#getService()
     */
    public Object getService() throws Exception {
    	ResourceFacadeImpl resourceFacade = new ResourceFacadeImpl(getServiceInfo());
    	resourceFacade.setCredentialsSource(getCredentialsSource());

    	if (resourceFacade.isSingleResourceService())
    		return resourceFacade.getResource((String)null);

        return resourceFacade;
    }	

}
