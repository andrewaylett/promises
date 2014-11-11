/*
 * Copyright 2014 Andrew Aylett
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package eu.aylett.promises;

import java.util.function.Function;

/**
 * A promise -- represents a future value.
 */
public class Promise<T> {
    private final Deferred<T> deferred;

    protected Promise(Deferred<T> deferred) {
        this.deferred = deferred;
    }

    public <U> Promise<U> then(Function<T, U> then) {
        Deferred<U> next = new Deferred<>();
        deferred.register(value -> next.resolve(then.apply(value)));
        return next.promise;
    }

    public <U> Promise<U> then(FunctionReturningPromise<T, U> then) {
        Deferred<U> next = new Deferred<>();
        deferred.register(value -> next.chain(then.apply(value).deferred));
        return next.promise;
    }
}
