/*
 * Copyright 2020 by OLTPBenchmark Project
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
 *
 */

package org.dbiir.tristar.benchmarks.distributions;

/**
 * An expression that generates a sequence of string values, following some distribution (Uniform, Zipfian, Sequential, etc.)
 */
public abstract class Generator {
    /**
     * Generate the next string in the distribution.
     */
    public abstract String nextString();

    /**
     * Return the previous string generated by the distribution; e.g., returned from the last nextString() call.
     * Calling lastString() should not advance the distribution or have any side effects. If nextString() has not yet
     * been called, lastString() should return something reasonable.
     */
    public abstract String lastString();
}

