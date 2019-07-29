package es.sm2baleares.base.model.domain.audit;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.envers.RevisionListener;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Listener used by Hibernate Envers to attach the username to the audit entries.
 */
@Slf4j
public class UserRevisionListener implements RevisionListener {

    @Override
    public void newRevision(Object revisionEntity) {

        try {

            UserRevisionEntity userRevisionEntity = (UserRevisionEntity) revisionEntity;

            // Get user (if present) and add it to the revision entity
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

            if (requestAttributes != null && ((ServletRequestAttributes) requestAttributes).getRequest() != null) {

                String username = (String) ((ServletRequestAttributes) requestAttributes).getRequest().getAttribute(
                        "username");

                userRevisionEntity.setUsername(username);
            }

        } catch (Exception e) {
            log.error("Unable to get username for revision {}", revisionEntity);
        }
    }
}