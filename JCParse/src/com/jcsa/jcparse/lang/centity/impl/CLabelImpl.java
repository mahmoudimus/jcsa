package com.jcsa.jcparse.lang.centity.impl;

import com.jcsa.jcparse.lang.centity.CLabel;

public class CLabelImpl implements CLabel {

	protected long id;

	protected CLabelImpl(long id) {
		this.id = id;
	}

	@Override
	public long get_label_id() {
		return id;
	}

	@Override
	public String toString() {
		return "[" + id + "]";
	}
}
