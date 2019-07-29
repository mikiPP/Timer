package es.sm2baleares.base.service.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Service errors catalog
 */
@Getter
@AllArgsConstructor
public enum ServiceErrorCatalog {

    GENERIC_SERVICE_ERROR("APP-SER-01"),
    EXPECTED_ELEMENT_NOT_FOUND("APP-SER-02");

    /**
     * The Error Code.
     */
    private String code;

}