package com.redick.support.graphql;

import graphql.ExecutionResult;
import graphql.execution.instrumentation.ExecutionStrategyInstrumentationContext;
import graphql.execution.instrumentation.Instrumentation;
import graphql.execution.instrumentation.InstrumentationContext;
import graphql.execution.instrumentation.parameters.InstrumentationExecuteOperationParameters;
import graphql.execution.instrumentation.parameters.InstrumentationExecutionParameters;
import graphql.execution.instrumentation.parameters.InstrumentationExecutionStrategyParameters;
import graphql.execution.instrumentation.parameters.InstrumentationFieldFetchParameters;
import graphql.execution.instrumentation.parameters.InstrumentationFieldParameters;
import graphql.execution.instrumentation.parameters.InstrumentationValidationParameters;
import graphql.language.Document;
import graphql.validation.ValidationError;
import java.util.List;

/**
 * @author Redick01
 */
public class TracingInstrumentation implements Instrumentation {

    @Override
    public InstrumentationContext<ExecutionResult> beginExecution(
            InstrumentationExecutionParameters instrumentationExecutionParameters ) {
        return null;
    }

    @Override
    public InstrumentationContext<Document> beginParse(
            InstrumentationExecutionParameters instrumentationExecutionParameters ) {
        return null;
    }

    @Override
    public InstrumentationContext<List<ValidationError>> beginValidation(
            InstrumentationValidationParameters instrumentationValidationParameters ) {
        return null;
    }

    @Override
    public InstrumentationContext<ExecutionResult> beginExecuteOperation(
            InstrumentationExecuteOperationParameters instrumentationExecuteOperationParameters ) {
        return null;
    }

    @Override
    public ExecutionStrategyInstrumentationContext beginExecutionStrategy(
            InstrumentationExecutionStrategyParameters instrumentationExecutionStrategyParameters ) {
        return null;
    }

    @Override
    public InstrumentationContext<ExecutionResult> beginField(
            InstrumentationFieldParameters instrumentationFieldParameters ) {
        return null;
    }

    @Override
    public InstrumentationContext<Object> beginFieldFetch(
            InstrumentationFieldFetchParameters instrumentationFieldFetchParameters ) {
        return null;
    }
}
