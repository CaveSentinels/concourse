/*
 * Copyright (c) 2013-2015 Cinchapi, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.cinchapi.concourse.http;

import java.util.Map;
import java.util.Map.Entry;

import org.cinchapi.concourse.Timestamp;
import org.cinchapi.concourse.util.TestData;
import org.junit.Assert;
import org.junit.Test;

import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Response;

/**
 * Unit tests for the audit functionality in the REST API.
 * 
 * @author Jeff Nelson
 */
public class RestAuditTest extends RestTest {
    
    @Test
    public void testAuditRecord(){
        long record = TestData.getLong();
        int count = TestData.getScaleCount();
        for(int i = 0; i < count; i++){
            client.add(TestData.getSimpleString(), count, record);
        }
        Map<Long, String> resp = bodyAsJava(get("/{0}/audit", record), new TypeToken<Map<Long, String>>(){});
        Map<Timestamp, String> expected = client.audit(record);
        for(Entry<Timestamp, String> entry : expected.entrySet()){
            long timestamp = entry.getKey().getMicros();
            Assert.assertEquals(entry.getValue(), resp.get(timestamp));
        }
    }
    
    @Test
    public void testAuditKeyReturns400Error(){
        Response resp = get("/foo/audit");
        Assert.assertEquals(400, resp.code());
    }

}
