package com.ibm.boycott.engine;

import javax.enterprise.context.ApplicationScoped;

import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.boycott.model.DroolsRequest;
import com.ibm.boycott.model.LcEvaluationRequest;
import com.ibm.boycott.model.LcEvaluationResult;

/**
 * Executes boycott rules using the Drools engine.
 */
@ApplicationScoped
public class BoycottRuleEngine extends BaseRuleExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(BoycottRuleEngine.class);

    public LcEvaluationResult execute(LcEvaluationRequest lcRequest, DroolsRequest droolsRequest) {
        LOGGER.info("Starting rule execution for boycott category");

        LcEvaluationResult result = new LcEvaluationResult();
        KieSession kieSession = null;

        try {
            kieSession = createSession("ksession-boycott");

            LOGGER.debug("Setting global 'response' and inserting facts");
            kieSession.setGlobal("response", result);
            kieSession.insert(droolsRequest);
            kieSession.insert(lcRequest);

            LOGGER.info("Firing rules for 'ksession-boycott'");
            fire(kieSession);
            LOGGER.info("Rule execution completed successfully");

        } catch (Exception e) {
            LOGGER.error("Error during rule execution", e);
        } finally {
            if (kieSession != null) {
                kieSession.dispose(); // extra safeguard
                LOGGER.debug("KieSession disposed in finally");
            }
        }

        LOGGER.debug("Returning LcEvaluationResult: {}", result);
        return result;
    }
}
