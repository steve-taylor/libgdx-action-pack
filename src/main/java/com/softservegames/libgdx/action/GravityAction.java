package com.softservegames.libgdx.action;

import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * LibGDX action that simulates gravity by altering the {@link Actor}'s y-coordinate without the
 * overhead of a physics engine.
 *
 * @author Steve Taylor (Soft Serve Games)
 */
public class GravityAction extends FiniteDurationAction {
	
	public GravityAction() {
		restart();
	}
	
	/**
	 * <p>
	 * Set this action's properties.
	 * </p>
	 * <p>
	 * It is recommended not to call this directly. Instead, use
	 * {@link Actions2#gravity(float, float, float, int, float)} to obtain an instance from the
	 * pool and initialize it.
	 * </p>
	 * 
	 * @param gravity    The gravity coefficient (must be positive).
	 * @param fromY      The initial y-coordinate (cannot be greater than {@code toY}).
	 * @param toY        The target y-coordinate (cannot be less than {@code fromY}).
	 * @param bounces    The number of times to bounce after reaching the y-coordinate for the
	 *                   first time (between 0 and 20 inclusive).
	 * @param bounciness The factor to apply to the height of the previous bounce (or the initial
	 *                   height if this is the first bounce) to determine the height of a bounce.
	 * @return           This action.
	 * @throws IllegalArgumentException if one or more parameters are not valid.
	 */
	public final GravityAction init(float gravity, float fromY, float toY, int bounces, float bounciness) throws IllegalArgumentException {
		if (gravity <= 0) {
			throw new IllegalArgumentException("gravity must be positive");
		}
		if (fromY > toY) {
			throw new IllegalArgumentException("fromY cannot be > toY");
		}
		if (bounces < 0 || bounces > 20) {
			throw new IllegalArgumentException("bounces should be between 0 and 20");
		}
		if (bounciness < 0f || bounciness > 1f) {
			throw new IllegalArgumentException("bounciness should be between 0 and 1");
		}
		this.gravity = gravity;
		this.toY = toY;

		// Calculate durations and heights.
		{
			intervals = 1 + 2 * bounces;
			float distance = toY - fromY;
			heights[0] = fromY;
			durations[0] = calculateDuration(distance, gravity);
			for (int i = 1; i < intervals; i += 2) {
				distance *= bounciness;
				heights[i] = heights[i + 1] = toY - distance;
				durations[i] = durations[i + 1] = calculateDuration(distance, gravity);
			}
		}
		// Calculate total durations.
		{
			float runningTotalDuration = 0;
			for (int i = 0; i < intervals; ++i) {
				totalDurations[i] = runningTotalDuration += durations[i];
			}
			duration = runningTotalDuration;
		}

		elapsedTime = 0;
		
		return this;
	}

	@Override
	public float getDuration() {
		return duration;
	}

	@Override
	public boolean act(float delta) {
		elapsedTime += delta;
		float y = calculateY(elapsedTime);
		getActor().setY(y);
		return elapsedTime >= duration;
	}
	
	/**
	 * Calculate the y-coordinate for the specified elapsed time.
	 * 
	 * @param elapsedTime The total amount of time that has elapsed since this action starting.
	 * @return            The y-coordinate for the elapsed time.
	 */
	private float calculateY(float elapsedTime) {
		if (elapsedTime >= duration) {
			return toY;
		}
		int index = indexOfDelta(elapsedTime);
		float time = index % 2 != 0
					 ? totalDurations[index] - elapsedTime // bouncing up
					 : index > 0
					   ? elapsedTime - totalDurations[index - 1] // falling from bounce
					   : elapsedTime; // initial fall
		return heights[index] + calculateDistance(time, gravity);
	}

	/**
	 * Get the lowest index into the total durations array where the specified delta
	 * is less than the total duration.
	 *
	 * @param delta The elapsed time, in seconds, since the action started.
	 *
	 * @return
	 */
	private int indexOfDelta(float delta) {
		for (int i = 0; i < intervals; ++i) {
			if (delta < totalDurations[i]) {
				return i;
			}
		}
		return intervals - 1;
	}

	@Override
	public final void restart() {
		elapsedTime = 0;
		gravity = 3000;
		toY = 0;
		intervals = 1;
		duration = 0;
	}
	
	public static float calculateDuration(float distance, float gravity) {
		return (float) Math.sqrt(2 * distance / gravity);
	}
	
	public static float calculateDistance(float time, float gravity) {
		return 0.5f * gravity * time * time;
	}

	private static final int MAX_BOUNCES = 20;

	private float elapsedTime = 0;
	private float gravity;
	private float toY;
	private int intervals;
	private float duration;
	private final float[] durations = new float[1 + 2 * MAX_BOUNCES];
	private final float[] totalDurations = new float[1 + 2 * MAX_BOUNCES];
	private final float[] heights = new float[1 + 2 * MAX_BOUNCES];
}
