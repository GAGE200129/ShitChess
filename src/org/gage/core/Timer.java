package org.gage.core;

public class Timer {

	private double lastLoopTime;

	public void init() {
		this.lastLoopTime = getTime();
	}

	public long getTime() {
		return System.nanoTime();
	}

	public double getElapsedTime() {
		long time = getTime();
		double elapsedTime = (double) (time - lastLoopTime) / 1000000000.0;
		lastLoopTime = time;
		return elapsedTime;
	}

	public double getLastLoopTime() {
		return lastLoopTime;
	}
}
