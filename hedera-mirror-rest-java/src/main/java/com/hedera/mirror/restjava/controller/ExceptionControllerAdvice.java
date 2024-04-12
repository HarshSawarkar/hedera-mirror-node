/*
 * Copyright (C) 2024 Hedera Hashgraph, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hedera.mirror.restjava.controller;

import com.hedera.mirror.common.exception.InvalidEntityException;
import com.hedera.mirror.rest.model.Error;
import com.hedera.mirror.rest.model.ErrorStatus;
import com.hedera.mirror.rest.model.ErrorStatusMessagesInner;
import jakarta.persistence.EntityNotFoundException;
import lombok.CustomLog;
import org.springframework.boot.context.properties.bind.BindException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;

@CustomLog
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ExceptionControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler()
    private ResponseEntity<Error> notFound(final EntityNotFoundException e) {
        return errorResponse(e.getMessage(),NOT_FOUND);
    }
    @ExceptionHandler()
    private ResponseEntity<Error> inputValidationError(final InvalidEntityException e) {
        return errorResponse(e.getMessage(), BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<Error> inputValidationError(final IllegalArgumentException e) {
        return errorResponse(e.getMessage(), BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<Error> bindError(final BindException e) {
        return errorResponse(e.getMessage(), BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<Error> queryTimeout(final QueryTimeoutException e) {
        log.error("Query timed out: {}", e.getMessage());
        return errorResponse(SERVICE_UNAVAILABLE.getReasonPhrase(), SERVICE_UNAVAILABLE);
    }

    private ResponseEntity<Error> errorResponse(final String e, HttpStatus statusCode) {
        var errorMessage = new ErrorStatusMessagesInner();
        errorMessage.setMessage(e);
        var errorStatus = new ErrorStatus().addMessagesItem(errorMessage);
        var error = new Error();
        return  new ResponseEntity<>(error.status(errorStatus), statusCode);
    }
}
