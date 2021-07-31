package com.akari.ppx.common.preference;

import android.content.Context;
import android.util.AttributeSet;

public class TestEditPreference extends EditPreference {
	public TestButton testButton;
	private boolean isArray;

	public TestEditPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TestButton getTestButton() {
		return testButton;
	}

	public void setTestButton(TestButton testButton) {
		this.testButton = testButton;
	}

	public boolean isArray() {
		return isArray;
	}

	public void setIsArray(boolean isArray) {
		this.isArray = isArray;
	}
}
