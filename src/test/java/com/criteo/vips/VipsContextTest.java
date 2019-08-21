/*
  Copyright (c) 2019 Criteo

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
*/

package com.criteo.vips;

import org.junit.Assert;
import org.junit.Test;

public class VipsContextTest {
    @Test
    public void TestSetLeak() {
        VipsContext.setLeak(true);
    }

    @Test
    public void TestSetConcurrency() {
        VipsContext.setConcurrency(8);
        Assert.assertEquals(8, VipsContext.getConcurrency());
    }

    @Test
    public void TestSetMaxCache() {
        VipsContext.setMaxCache(0);
        Assert.assertEquals(0, VipsContext.getMaxCache());
    }

    @Test
    public void TestSetMaxCacheMem() {
        VipsContext.setMaxCacheMem(1024);
        Assert.assertEquals(1024, VipsContext.getMaxCacheMem());
    }
}
