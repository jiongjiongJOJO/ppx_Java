package com.akari.ppx.common.preference;

import com.akari.ppx.common.constant.Const;

public class TestButton {
	private final CharSequence text;
	private final Listener listener;

	public TestButton(final Listener listener) {
		this(Const.TEST, listener);
	}

	public TestButton(CharSequence text, final Listener listener) {
		this.text = text;
		this.listener = listener;
	}

	public CharSequence getText() {
		return text;
	}

	public CharSequence handleText(String text) {
		if (listener != null)
			return listener.handleText(text);
		return null;
	}

	public interface Listener {
		CharSequence handleText(String text);
	}
}
