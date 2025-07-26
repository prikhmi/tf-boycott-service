package com.ibm.boycott.engine;

import javax.inject.Inject;

import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract class for Drools rule execution logic.
 */
public abstract class BaseRuleExecutor {

    @Inject
    protected KieContainer kieContainer;

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseRuleExecutor.class);

    protected KieSession createSession(String sessionName) {
        try {
            return kieContainer.newKieSession(sessionName);
        } catch (Exception e) {
            LOGGER.error("Failed to create KieSession with name '{}'", sessionName, e);
            throw new RuntimeException("KieSession creation failed: " + sessionName, e);
        }
    }

    protected void fire(KieSession kieSession) {
        try {
            kieSession.fireAllRules();
        } finally {
            kieSession.dispose();
            LOGGER.debug("KieSession disposed");
        }
    }
}
