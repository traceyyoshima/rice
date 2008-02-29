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
package org.kuali.rice.kcb.service;

import org.kuali.rice.kcb.exception.MessageDeliveryException;
import org.kuali.rice.kcb.exception.MessageDismissalException;
import org.kuali.rice.kcb.vo.MessageVO;

/**
 * The KCB MessagingService provides an API to deliver messages
 * to arbitrary multiple endpoints. 
 * 
 * @author Kuali Rice Team (kuali-rice@googlegroups.com)
 */
public interface MessagingService {
    /**
     * Delivers a message
     * 
     * @param message message to deliver
     * @return identifier for the message
     */
    public long deliver(MessageVO message) throws MessageDeliveryException;
    /**
     * Removes a specific message and all deliveries
     * 
     * @param messageId id of the message to remove
     * @param user the user under which the action was taken
     * @param cause the cause or action taken to remove the message 
     */
    public void remove(long messageId, String user, String cause) throws MessageDismissalException;
    
    /**
     * Removes a specific message and all deliveries
     * 
     * @param originId origin id of the message to remove
     * @param user the user under which the action was taken
     * @param cause the cause or action taken to remove the message 
     */
    public void removeByOriginId(String originId, String user, String cause) throws MessageDismissalException;
}