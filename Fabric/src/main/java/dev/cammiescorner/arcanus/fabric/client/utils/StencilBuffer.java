package dev.cammiescorner.arcanus.fabric.client.utils;

public interface StencilBuffer {
	boolean arcanus$isStencilBufferEnabled();

	void arcanus$enableStencilBufferAndReload(boolean cond);
}
