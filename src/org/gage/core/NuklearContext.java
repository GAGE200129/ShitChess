package org.gage.core;

import static org.lwjgl.nuklear.Nuklear.nk_free;
import static org.lwjgl.nuklear.Nuklear.nk_init;

import org.lwjgl.nuklear.NkAllocator;
import org.lwjgl.nuklear.NkContext;
import org.lwjgl.system.MemoryUtil;

public class NuklearContext {

	private static NkAllocator ALLOCATOR = NkAllocator.create()
			.alloc((handle, old, size) -> MemoryUtil.nmemAllocChecked(size)).mfree((handle, old) -> MemoryUtil.nmemFree(old));

	private NkContext ctx;

	public NuklearContext() {

		nk_init(ctx, ALLOCATOR, null);
	}

	public void shutdown() {
		nk_free(ctx);
	}
}
