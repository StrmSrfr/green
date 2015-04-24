package me.cytochro.green.special.operator;

import java.io.IOException;
import java.util.Arrays;

import com.google.common.collect.ImmutableMap;

import me.cytochro.zson.Cons;
import me.cytochro.zson.List;
import me.cytochro.zson.Nil;
import me.cytochro.zson.Symbol;
import me.cytochro.zson.T;

import me.cytochro.green.Exception;
import me.cytochro.green.Function;
import me.cytochro.green.Future;
import me.cytochro.green.Green;
import me.cytochro.green.LexicalEnvironment;
import me.cytochro.green.SpecialOperator;

import me.cytochro.green.exception.ArityException;
import me.cytochro.green.exception.Unbound;

public class Lambda implements T, SpecialOperator {
    public static abstract class Guts implements Function {
        protected final T lambdaList;
        protected final List body;
        protected final LexicalEnvironment outer;
        protected final Green runtime;
        
        public static Guts newInstance(Cons guts,
                                       LexicalEnvironment outer,
                                       Green runtime) {
            final T lambdaList = guts.getCar();
            if (lambdaList instanceof Symbol) {
                return new VariableArity(guts, outer, runtime);
            } else if (lambdaList instanceof List) {
                return new FixedArity(guts, outer, runtime);
            } else {
                throw new ClassCastException("lambda list was not symbol or list");
            }
        }
        
        protected Guts(Cons guts, LexicalEnvironment outer, Green runtime) {
            this.lambdaList = guts.getCar();
            final T bodyO = guts.getCdr();
            assert (bodyO instanceof List);
            body = (List) bodyO;
            this.outer = outer;
            this.runtime = runtime;
        }

        public Future apply(Future[] arguments) {
            ArityException ae = this.checkArity(arguments);
            if (ae != null) {
                return () -> ae;
            }

            Exception ex = this.checkException(arguments);
            if (ex != null) {
                return () -> ex;
            }

            final LexicalEnvironment inner =
                buildInnerLexenv(arguments);

            T expression = body.getCar();
            return runtime.evalForFuture(expression, inner);
        }

        protected abstract ArityException checkArity(Future[] arguments);

        protected Exception checkException(Future[] arguments) {
            for (Future f : arguments) {
                final T v = f.get();
                if (v instanceof Exception) {
                    return (Exception) v;
                }
            }
            return null;
        }
        protected abstract LexicalEnvironment buildInnerLexenv(Future[] arguments);
    }

    public static class FixedArity extends Guts {
        final Symbol[] parameters;
        public FixedArity(Cons guts, LexicalEnvironment outer, Green runtime) {
            super(guts, outer, runtime);
            assert (lambdaList instanceof List);
            this.parameters = ((List) lambdaList).toArray(Symbol[]::new);
        }

        @Override
        protected ArityException checkArity(Future[] arguments) {
            if (arguments.length != parameters.length) {
                return new ArityException(this,
                                          arguments.length,
                                          parameters.length,
                                          parameters.length);
            } else {
                return null;
            }
        }

        @Override
        protected LexicalEnvironment buildInnerLexenv(Future[] arguments) {
            final ImmutableMap.Builder<Symbol, Future> b =
                new ImmutableMap.Builder<>();
            for (int i = 0; i < arguments.length; i++) {
                Symbol name = (Symbol) parameters[i]; // TODO check type
                Future value = arguments[i];
                b.put((Symbol) name, arguments[i]);
            }

            return new LexicalEnvironment(outer, b.build());
        }
    }

    public static class VariableArity extends Guts {
        final Symbol parameters;
        public VariableArity(Cons guts, LexicalEnvironment outer, Green runtime) {
            super(guts, outer, runtime);
            assert (lambdaList instanceof Symbol);
            this.parameters = (Symbol) lambdaList;
        }

        @Override
        protected ArityException checkArity(Future[] arguments) {
            return null;
        }

        @Override
        protected LexicalEnvironment buildInnerLexenv(Future[] arguments) {
            return new LexicalEnvironment(outer, parameters,
                                          () -> List.fromArray(Arrays.stream(arguments)
                                                               .parallel()
                                                               .map(Future::get)
                                                               .toArray(T[]::new)));
        }
    }
    
    @Override
    public Future eval(T expression, LexicalEnvironment lexenv) {
        return () -> {
            assert (expression instanceof Cons);
            final Cons expr = (Cons) expression;
            final T lambda = expr.getCar();
            assert (LAMBDA.equals(lambda));
            final T restO = expr.getCdr();
            assert (restO instanceof Cons);
            final Cons guts = (Cons) restO;
            return Guts.newInstance(guts, lexenv, runtime);
        };
    }

    public Symbol name() {
        return LAMBDA;
    }

    public Lambda(Green runtime) {
        this.runtime = runtime;
    }
    
    private final Symbol LAMBDA = Symbol.intern("lambda");
    private final Green runtime;
}
        
