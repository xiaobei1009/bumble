package org.bumble.test.model;

public class DeepCopyOutter {
	private DeepCopyInner inner;
	public DeepCopyInner getInner() {
		return inner;
	}
	public void setInner(DeepCopyInner inner) {
		this.inner = inner;
	}
}
