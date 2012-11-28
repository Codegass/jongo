/*
 * Copyright (C) 2011 Benoit GUEROUT <bguerout at gmail dot com> and Yves AMSELLEM <amsellem dot yves at gmail dot com>
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

package org.jongo.marshall.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import org.jongo.Mapper;
import org.jongo.bson.Bson;
import org.jongo.bson.BsonDocument;
import org.jongo.model.Friend;
import org.junit.Test;

import java.io.IOException;

import static org.fest.assertions.Assertions.assertThat;

public class JacksonMapperTest {

    @Test
    public void enhanceConfigAndBuildConfig() throws Exception {

        BsonDocument document = Bson.createDocument(new BasicDBObject("name", "robert"));

        MappingConfig config = JacksonMapper.enhanceConfig()
                .addDeserializer(String.class, new DoeJsonDeserializer())
                .buildConfig();
        ObjectMapper mapper = config.getObjectMapper();

        Friend friend = mapper.readValue(document.toByteArray(), Friend.class);
        assertThat(friend.getName()).isEqualTo("Doe");
    }

    @Test
    public void enhanceConfigAndBuildMapper() throws Exception {

        BsonDocument document = Bson.createDocument(new BasicDBObject("name", "robert"));

        Mapper mapper = JacksonMapper.enhanceConfig()
                .addDeserializer(String.class, new DoeJsonDeserializer())
                .buildMapper();
        Friend friend = mapper.getUnmarshaller().unmarshall(document, Friend.class);

        assertThat(friend.getName()).isEqualTo("Doe");
    }


    private static class DoeJsonDeserializer extends JsonDeserializer<String> {
        @Override
        public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            return "Doe";
        }
    }


}
