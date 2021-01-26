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

public class VipsException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private String vipsErrorBuffer;

    public VipsException(String message) {
        super(message);
    }

    public VipsException(String message, String vipsErrorBuffer) {
        super(message);
        this.vipsErrorBuffer = vipsErrorBuffer;
    }

    @Override
    public String getMessage() {
        String msg = super.getMessage();
        if (vipsErrorBuffer != null) {
            return msg + '\n' + " vips error buffer contains: " + vipsErrorBuffer.trim();
        }
        return msg;
    }
}
