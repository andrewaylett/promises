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

import javax.annotation.concurrent.GuardedBy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

/**
 * Methods for creating and resolving {@link Promise}s.
 */
public final class Deferred<T> {
    public final Promise<T> promise;
    private final ReentrantLock lock = new ReentrantLock();
    @GuardedBy("lock")
    private final List<Consumer<T>> pendingOperations = new ArrayList<>();
    @GuardedBy("lock")
    private T value = null;

    public Deferred() {
        this.promise = new Promise<>(this);
    }

    void register(Consumer<T> operation) {
        lock.lock();
        try {
            if (value == null) {
                pendingOperations.add(operation);
                return;
            }
        } finally {
            lock.unlock();
        }
        operation.accept(value);
    }

    public void resolve(T value) {
        lock.lock();
        try {
            if (this.value != null) {
                throw new IllegalStateException("Tried to set Deferred twice");
            }

            this.value = value;
            for (Consumer<T> operation : pendingOperations) {
                operation.accept(value);
            }
        } finally {
            lock.unlock();
        }
    }

    public void chain(Deferred<T> deferred) {
        deferred.register(this::resolve);
    }
}
