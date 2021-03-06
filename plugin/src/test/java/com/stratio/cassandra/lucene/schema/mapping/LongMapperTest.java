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
package com.stratio.cassandra.lucene.schema.mapping;

import com.stratio.cassandra.lucene.schema.Schema;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.DocValuesType;
import org.apache.lucene.search.SortField;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class LongMapperTest {

    @Test
    public void testConstructorWithoutArgs() {
        LongMapper mapper = new LongMapper("field", null, null, null);
        assertEquals(Mapper.DEFAULT_INDEXED, mapper.isIndexed());
        assertEquals(Mapper.DEFAULT_SORTED, mapper.isSorted());
        assertEquals(DoubleMapper.DEFAULT_BOOST, mapper.getBoost(), 1);
    }

    @Test
    public void testConstructorWithAllArgs() {
        LongMapper mapper = new LongMapper("field", false, true, 2.3f);
        assertFalse(mapper.isIndexed());
        assertTrue(mapper.isSorted());
        assertEquals(2.3f, mapper.getBoost(), 1);
    }

    @Test()
    public void testSortField() {
        LongMapper mapper = new LongMapper("field", null, null, 2.3f);
        SortField sortField = mapper.sortField(true);
        assertNotNull(sortField);
        assertTrue(sortField.getReverse());
    }

    @Test
    public void testValueNull() {
        LongMapper mapper = new LongMapper("field", true, true, 1f);
        Long parsed = mapper.base("test", null);
        assertNull(parsed);
    }

    @Test()
    public void testValueString() {
        LongMapper mapper = new LongMapper("field", null, null, 1f);
        Long parsed = mapper.base("test", "3");
        assertEquals(Long.valueOf(3), parsed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueStringInvalid() {
        LongMapper mapper = new LongMapper("field", null, null, 1f);
        mapper.base("test", "error");
    }

    @Test
    public void testValueInteger() {
        LongMapper mapper = new LongMapper("field", true, true, 1f);
        Long parsed = mapper.base("test", 3);
        assertEquals(Long.valueOf(3), parsed);
    }

    @Test
    public void testValueLong() {
        LongMapper mapper = new LongMapper("field", true, true, 1f);
        Long parsed = mapper.base("test", 3l);
        assertEquals(Long.valueOf(3), parsed);
    }

    @Test
    public void testValueFloatWithoutDecimal() {
        LongMapper mapper = new LongMapper("field", true, true, 1f);
        Long parsed = mapper.base("test", 3f);
        assertEquals(Long.valueOf(3), parsed);
    }

    @Test
    public void testValueFloatWithDecimalFloor() {
        LongMapper mapper = new LongMapper("field", true, true, 1f);
        Long parsed = mapper.base("test", 3.5f);
        assertEquals(Long.valueOf(3), parsed);

    }

    @Test
    public void testValueFloatWithDecimalCeil() {
        LongMapper mapper = new LongMapper("field", true, true, 1f);
        Long parsed = mapper.base("test", 3.6f);
        assertEquals(Long.valueOf(3), parsed);

    }

    @Test
    public void testValueDoubleWithoutDecimal() {
        LongMapper mapper = new LongMapper("field", true, true, 1f);
        Long parsed = mapper.base("test", 3d);
        assertEquals(Long.valueOf(3), parsed);
    }

    @Test
    public void testValueDoubleWithDecimalFloor() {
        LongMapper mapper = new LongMapper("field", true, true, 1f);
        Long parsed = mapper.base("test", 3.5d);
        assertEquals(Long.valueOf(3), parsed);

    }

    @Test
    public void testValueDoubleWithDecimalCeil() {
        LongMapper mapper = new LongMapper("field", true, true, 1f);
        Long parsed = mapper.base("test", 3.6d);
        assertEquals(Long.valueOf(3), parsed);

    }

    @Test
    public void testValueStringWithoutDecimal() {
        LongMapper mapper = new LongMapper("field", true, true, 1f);
        Long parsed = mapper.base("test", "3");
        assertEquals(Long.valueOf(3), parsed);
    }

    @Test
    public void testValueStringWithDecimalFloor() {
        LongMapper mapper = new LongMapper("field", true, true, 1f);
        Long parsed = mapper.base("test", "3.2");
        assertEquals(Long.valueOf(3), parsed);

    }

    @Test
    public void testValueStringWithDecimalCeil() {
        LongMapper mapper = new LongMapper("field", true, true, 1f);
        Long parsed = mapper.base("test", "3.2");
        assertEquals(Long.valueOf(3), parsed);
    }

    @Test
    public void testIndexedField() {
        LongMapper mapper = new LongMapper("field", true, true, 1f);
        Field field = mapper.indexedField("name", 3L);
        assertNotNull(field);
        assertEquals(3L, field.numericValue());
        assertEquals("name", field.name());
        assertEquals(false, field.fieldType().stored());
    }

    @Test
    public void testSortedField() {
        LongMapper mapper = new LongMapper("field", true, true, 1f);
        Field field = mapper.sortedField("name", 3L, false);
        assertNotNull(field);
        assertEquals(DocValuesType.NUMERIC, field.fieldType().docValuesType());
    }

    @Test
    public void testSortedFieldCollection() {
        LongMapper mapper = new LongMapper("field", true, true, 1f);
        Field field = mapper.sortedField("name", 3L, true);
        assertNotNull(field);
        assertEquals(DocValuesType.NUMERIC, field.fieldType().docValuesType());
    }

    @Test
    public void testExtractAnalyzers() {
        LongMapper mapper = new LongMapper("field", true, true, 1f);
        String analyzer = mapper.getAnalyzer();
        assertEquals(Mapper.KEYWORD_ANALYZER, analyzer);
    }

    @Test
    public void testParseJSONWithoutArgs() throws IOException {
        String json = "{fields:{age:{type:\"long\"}}}";
        Schema schema = Schema.fromJson(json);
        Mapper mapper = schema.getMapper("age");
        assertNotNull(mapper);
        assertEquals(LongMapper.class, mapper.getClass());
        assertEquals(Mapper.DEFAULT_INDEXED, mapper.isIndexed());
        assertEquals(Mapper.DEFAULT_SORTED, mapper.isSorted());
        assertEquals(LongMapper.DEFAULT_BOOST, ((LongMapper) mapper).getBoost(), 1);
    }

    @Test
    public void testParseJSONWithAllArgs() throws IOException {
        String json = "{fields:{age:{type:\"long\", indexed:\"false\", sorted:\"true\", boost:\"5\"}}}";
        Schema schema = Schema.fromJson(json);
        Mapper mapper = schema.getMapper("age");
        assertNotNull(mapper);
        assertEquals(LongMapper.class, mapper.getClass());
        assertFalse(mapper.isIndexed());
        assertTrue(mapper.isSorted());
        assertEquals(5, ((LongMapper) mapper).getBoost(), 1);
    }

    @Test
    public void testParseJSONEmpty() throws IOException {
        String json = "{fields:{}}";
        Schema schema = Schema.fromJson(json);
        Mapper mapper = schema.getMapper("age");
        assertNull(mapper);
    }

    @Test(expected = IOException.class)
    public void testParseJSONInvalid() throws IOException {
        String json = "{fields:{age:{}}";
        Schema.fromJson(json);
    }

    @Test
    public void testToString() {
        LongMapper mapper = new LongMapper("field", false, false, 0.3f);
        assertEquals("LongMapper{indexed=false, sorted=false, boost=0.3}", mapper.toString());
    }
}
