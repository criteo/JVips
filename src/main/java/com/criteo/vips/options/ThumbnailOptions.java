/*
  Copyright (c) 2021 Criteo

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

package com.criteo.vips.options;

import com.criteo.vips.enums.VipsIntent;
import com.criteo.vips.enums.VipsInteresting;

public class ThumbnailOptions {

	private boolean scale;
	private boolean noRotate;
	private int crop = -1;
	private boolean linear;
	private String importProfile;
	private String exportProfile;
	private int intent = -1;

	public boolean isScale() {
		return scale;
	}

	public void setScale(boolean scale) {
		this.scale = scale;
	}

	public ThumbnailOptions scale(boolean scale) {
		setScale(scale);
		return this;
	}

	public boolean isNoRotate() {
		return noRotate;
	}

	public void setNoRotate(boolean noRotate) {
		this.noRotate = noRotate;
	}

	public ThumbnailOptions noRotate(boolean noRotate) {
		setNoRotate(noRotate);
		return this;
	}

	public VipsInteresting getCrop() {
		if (crop != -1) {
			return VipsInteresting.valueOf(crop);
		} else {
			return null;
		}
	}

	public void setCrop(VipsInteresting crop) {
		if (crop != null) {
			this.crop = crop.getValue();
		} else {
			this.crop = -1;
		}
	}

	public ThumbnailOptions crop(VipsInteresting crop) {
		setCrop(crop);
		return this;
	}

	public boolean isLinear() {
		return linear;
	}

	public void setLinear(boolean linear) {
		this.linear = linear;
	}

	public ThumbnailOptions linear(boolean linear) {
		setLinear(linear);
		return this;
	}

	public String getImportProfile() {
		return importProfile;
	}

	public void setImportProfile(String importProfile) {
		this.importProfile = importProfile;
	}

	public ThumbnailOptions importProfile(String importProfile) {
		setImportProfile(importProfile);
		return this;
	}

	public String getExportProfile() {
		return exportProfile;
	}

	public void setExportProfile(String exportProfile) {
		this.exportProfile = exportProfile;
	}

	public ThumbnailOptions exportProfile(String exportProfile) {
		setExportProfile(exportProfile);
		return this;
	}

	public VipsIntent getIntent() {
		if (intent != -1) {
			return VipsIntent.valueOf(intent);
		} else {
			return null;
		}
	}

	public void setIntent(VipsIntent intent) {
		if (intent != null) {
			this.intent = intent.getValue();
		} else {
			this.intent = -1;
		}
	}

	public ThumbnailOptions intent(VipsIntent intent) {
		setIntent(intent);
		return this;
	}

}
