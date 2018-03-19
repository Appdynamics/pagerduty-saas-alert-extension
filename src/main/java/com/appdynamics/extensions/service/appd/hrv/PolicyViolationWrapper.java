/*
 * Copyright 2018. AppDynamics LLC and its affiliates.
 * All Rights Reserved.
 * This is unpublished proprietary source code of AppDynamics LLC and its affiliates.
 * The copyright notice above does not evidence any actual or intended publication of such source code.
 */

package com.appdynamics.extensions.service.appd.hrv;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "policy-violations")
public class PolicyViolationWrapper {

    @XmlElement(name="policy-violation")
    List<PolicyViolation> policyViolations;

    public List<PolicyViolation> getPolicyViolations() {
        return policyViolations;
    }

    public void setPolicyViolations(List<PolicyViolation> policyViolations) {
        this.policyViolations = policyViolations;
    }
}
