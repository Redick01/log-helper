package com.redick.support.graphql;

import graphql.ExecutionResult;
import graphql.ExecutionResultImpl;
import graphql.execution.instrumentation.InstrumentationContext;
import graphql.execution.instrumentation.InstrumentationState;
import graphql.execution.instrumentation.SimpleInstrumentation;
import graphql.execution.instrumentation.SimpleInstrumentationContext;
import graphql.execution.instrumentation.parameters.InstrumentationExecutionParameters;
import graphql.execution.instrumentation.parameters.InstrumentationFieldFetchParameters;
import graphql.execution.instrumentation.parameters.InstrumentationValidationParameters;
import graphql.execution.instrumentation.tracing.TracingSupport;
import graphql.language.Document;
import graphql.validation.ValidationError;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author Redick01
 */
@Slf4j
public class TracingInstrumentation extends SimpleInstrumentation {

    private final Options options;

    public TracingInstrumentation() {
        this(Options.newOptions());
    }

    public TracingInstrumentation(Options options) {
        this.options = options;
    }

    public InstrumentationState createState() {
        return new TracingSupport(this.options.includeTrivialDataFetchers);
    }

    public CompletableFuture<ExecutionResult> instrumentExecutionResult(ExecutionResult executionResult, InstrumentationExecutionParameters parameters) {
        Map<Object, Object> currentExt = executionResult.getExtensions();
        TracingSupport tracingSupport = (TracingSupport)parameters.getInstrumentationState();
        Map<Object, Object> withTracingExt = new LinkedHashMap(currentExt == null ? Collections.emptyMap() : currentExt);
        withTracingExt.put("tracing", tracingSupport.snapshotTracingData());
        return CompletableFuture.completedFuture(new ExecutionResultImpl(executionResult.getData(), executionResult.getErrors(), withTracingExt));
    }

    public InstrumentationContext<Object> beginFieldFetch(InstrumentationFieldFetchParameters parameters) {
        TracingSupport tracingSupport = (TracingSupport)parameters.getInstrumentationState();
        TracingSupport.TracingContext ctx = tracingSupport.beginField(parameters.getEnvironment(), parameters.isTrivialDataFetcher());
        return SimpleInstrumentationContext.whenCompleted((result, t) -> {
            ctx.onEnd();
        });
    }

    public InstrumentationContext<Document> beginParse(InstrumentationExecutionParameters parameters) {
        TracingSupport tracingSupport = (TracingSupport)parameters.getInstrumentationState();
        TracingSupport.TracingContext ctx = tracingSupport.beginParse();
        return SimpleInstrumentationContext.whenCompleted((result, t) -> {
            ctx.onEnd();
        });
    }

    public InstrumentationContext<List<ValidationError>> beginValidation(InstrumentationValidationParameters parameters) {
        TracingSupport tracingSupport = (TracingSupport)parameters.getInstrumentationState();
        TracingSupport.TracingContext ctx = tracingSupport.beginValidation();
        return SimpleInstrumentationContext.whenCompleted((result, t) -> {
            ctx.onEnd();
        });
    }

    public static class Options {
        private final boolean includeTrivialDataFetchers;

        private Options(boolean includeTrivialDataFetchers) {
            this.includeTrivialDataFetchers = includeTrivialDataFetchers;
        }

        public boolean isIncludeTrivialDataFetchers() {
            return this.includeTrivialDataFetchers;
        }

        public Options includeTrivialDataFetchers(boolean flag) {
            return new Options(flag);
        }

        public static Options newOptions() {
            return new Options(true);
        }
    }
}
