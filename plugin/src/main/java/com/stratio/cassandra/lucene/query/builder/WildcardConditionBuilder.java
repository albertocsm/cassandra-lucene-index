/*
 * Copyright 2014, Stratio.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.stratio.cassandra.lucene.query.builder;

import com.stratio.cassandra.lucene.query.WildcardCondition;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * {@link ConditionBuilder} for building a new {@link WildcardCondition}.
 *
 * @author Andres de la Pena <adelapena@stratio.com>
 */
public class WildcardConditionBuilder extends ConditionBuilder<WildcardCondition, WildcardConditionBuilder> {

    /** The name of the field to be matched. */
    @JsonProperty("field")
    final String field;

    /** The wildcard expression to be matched. */
    @JsonProperty("value")
    final String value;

    /**
     * Creates a new {@link WildcardConditionBuilder} for the specified field and value.
     *
     * @param field The name of the field to be matched.
     * @param value The wildcard expression to be matched.
     */
    @JsonCreator
    protected WildcardConditionBuilder(@JsonProperty("field") String field, @JsonProperty("value") String value) {
        this.field = field;
        this.value = value;
    }

    /**
     * Returns the {@link WildcardCondition} represented by this builder.
     *
     * @return The {@link WildcardCondition} represented by this builder.
     */
    @Override
    public WildcardCondition build() {
        return new WildcardCondition(boost, field, value);
    }
}
