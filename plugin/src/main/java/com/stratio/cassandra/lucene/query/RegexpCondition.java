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
package com.stratio.cassandra.lucene.query;

import com.google.common.base.Objects;
import com.stratio.cassandra.lucene.schema.Schema;
import com.stratio.cassandra.lucene.schema.mapping.SingleColumnMapper;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.RegexpQuery;

/**
 * Implements the wildcard search query. Supported wildcards are {@code *}, which matches any character sequence
 * (including the empty one), and {@code ?}, which matches any single character. '\' is the escape character.
 * <p/>
 * Note this query can be slow, as it needs to iterate over many terms. In order to prevent extremely slow
 * WildcardQueries, a Wildcard term should not start with the wildcard {@code *}.
 *
 * @author Andres de la Pena <adelapena@stratio.com>
 */
public class RegexpCondition extends SingleFieldCondition {

    /** The name of the field to be matched. */
    public final String field;

    /** The wildcard expression to be matched. */
    public final String value;

    /**
     * Constructor using the field name and the value to be matched.
     *
     * @param boost The boost for this query clause. Documents matching this clause will (in addition to the normal
     *              weightings) have their score multiplied by {@code boost}. If {@code null}, then {@link
     *              #DEFAULT_BOOST} is used as default.
     * @param field The name of the field to be matched.
     * @param value The wildcard expression to be matched.
     */
    public RegexpCondition(Float boost, String field, String value) {
        super(boost, field);

        if (value == null) {
            throw new IllegalArgumentException("Field value required");
        }

        this.field = field;
        this.value = value;
    }

    /** {@inheritDoc} */
    @Override
    public Query query(Schema schema) {
        SingleColumnMapper<?> columnMapper = getMapper(schema, field);
        Class<?> clazz = columnMapper.baseClass();
        Query query;
        if (clazz == String.class) {
            Term term = new Term(field, value);
            query = new RegexpQuery(term);
        } else {
            String message = String.format("Regexp queries are not supported by %s mapper", clazz.getSimpleName());
            throw new UnsupportedOperationException(message);
        }
        query.setBoost(boost);
        return query;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return Objects.toStringHelper(this).add("boost", boost).add("field", field).add("value", value).toString();
    }
}
