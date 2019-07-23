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
import org.junit.Assume;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

@RunWith(Theories.class)
public class VipsInterpretationTest {

    @Theory
    public void TestShouldReturnValidValue(VipsInterpretation interpretation) {
        Assume.assumeTrue(interpretation != VipsInterpretation.ERROR && interpretation != VipsInterpretation.LAST);
        int i = interpretation.getValue();

        Assert.assertEquals(VipsInterpretation.valueOf(i), interpretation);
    }
}
