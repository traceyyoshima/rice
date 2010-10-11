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
package org.kuali.rice.kns.bo;


import java.util.LinkedHashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kuali.test.KNSTestCase;

/**
 * This is a description of what this class does - chang don't forget to fill this in. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class PersistableAttachmentBaseTest extends KNSTestCase {

	PersistableAttachmentBase persistableAttachmentBase;
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		persistableAttachmentBase = new PersistableAttachmentBase();
	}

	/**
	 * This method ...
	 * 
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	super.tearDown();
	persistableAttachmentBase = null;
	}
	
	@Test
	public void testAttachmentContent(){
		byte[] dummyByte = "dummy string".getBytes(); 
		persistableAttachmentBase.setAttachmentContent(dummyByte);
		assertEquals("Testing AttachmentContent in PersistableAttachmentBase.",dummyByte,persistableAttachmentBase.getAttachmentContent());
	}
	
	@Test
	public void testFileName(){
		persistableAttachmentBase.setFileName("FileName");
		assertEquals("Testing FileName in PersistableAttachmentBase.","FileName",persistableAttachmentBase.getFileName());
	}
	
	@Test
	public void testContentType(){
		persistableAttachmentBase.setContentType("contentType");
		assertEquals("Testing FileName in PersistableAttachmentBase.","contentType",persistableAttachmentBase.getContentType());
	}
	
	@Test
	public void testToStringMapper(){
		persistableAttachmentBase.setFileName("FileName");
		persistableAttachmentBase.setContentType("ContentType");
		LinkedHashMap dummyHashmap = persistableAttachmentBase.toStringMapper();
		assertEquals("Testing ToStringMapper in PersistableAttachmentBase.","FileName",dummyHashmap.get("fileName"));
		assertEquals("Testing ToStringMapper in PersistableAttachmentBase.","ContentType",dummyHashmap.get("contentType"));
	}
	
	
	
}
