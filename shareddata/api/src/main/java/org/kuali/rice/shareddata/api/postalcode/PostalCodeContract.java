/*
 * Copyright 2006-2011 The Kuali Foundation
 *
 *  Licensed under the Educational Community License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.opensource.org/licenses/ecl2.php
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.kuali.rice.shareddata.api.postalcode;

/**
 * This is the contract for a Postal Code.  A postal code is assigned to different geographic regions
 * in order to give each region an identifier.
 *
 * Examples of postal codes are Zip Codes in the United States and FSALDU in Canada.
 */
public interface PostalCodeContract {
    /**
     * This the county code for the PostalCode.  This cannot be null or a blank string.
     *
     * @return code
     */
    String getCode();

    /**
     * This the postal country code for the PostalCode.  This cannot be null or a blank string.
     *
     * @return postal country code
     */
    String getCountryCode();

    /**
     * This the postal state code for the PostalCode.  This can be null.
     *
     * @return postal state code
     */
    String getStateCode();

    /**
     * This the postal state code for the PostalCode.  This can be null.
     *
     * @return postal state code
     */
    String getCityName();

    /**
     * This the county code for the PostalCode.  This cannot be null.
     *
     * @return postal state code
     */
    String getCountyCode();

    /**
     * This the active flag for the PostalCode.
     *
     * @return the active flag of the PostalCode
     */
    boolean isActive();
}
