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
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.QueryBuilder;

/**
 * A {@link Condition} implementation that matches documents containing a value for a field.
 *
 * @author Andres de la Pena <adelapena@stratio.com>
 */
public class MatchCondition extends SingleFieldCondition {

    /** The name of the field to be matched. */
    public final String field;

    /** The value of the field to be matched. */
    public final Object value;

    /**
     * Constructor using the field name and the value to be matched.
     *
     * @param boost The boost for this query clause. Documents matching this clause will (in addition to the normal
     *              weightings) have their score multiplied by {@code boost}. If {@code null}, then {@link
     *              #DEFAULT_BOOST} is used as default.
     * @param field The name of the field to be matched.
     * @param value The value of the field to be matched.
     */
    public MatchCondition(Float boost, String field, Object value) {
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
            String value = (String) columnMapper.base(field, this.value);
            Analyzer analyzer = schema.getAnalyzer();
            QueryBuilder queryBuilder = new QueryBuilder(analyzer);
            query = queryBuilder.createPhraseQuery(field, value, 0);
            if (query == null) query = new BooleanQuery();
        } else if (clazz == Integer.class) {
            Integer value = (Integer) columnMapper.base(field, this.value);
            query = NumericRangeQuery.newIntRange(field, value, value, true, true);
        } else if (clazz == Long.class) {
            Long value = (Long) columnMapper.base(field, this.value);
            query = NumericRangeQuery.newLongRange(field, value, value, true, true);
        } else if (clazz == Float.class) {
            Float value = (Float) columnMapper.base(field, this.value);
            query = NumericRangeQuery.newFloatRange(field, value, value, true, true);
        } else if (clazz == Double.class) {
            Double value = (Double) columnMapper.base(field, this.value);
            query = NumericRangeQuery.newDoubleRange(field, value, value, true, true);
        } else {
            String message = String.format("Match queries are not supported by %s mapper", clazz.getSimpleName());
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