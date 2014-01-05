
package com.softservegames.libgdx.action;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;

/**
 * Helpers for acquiring and initializing poolable actions.
 * 
 * @author Steve Taylor (Soft Serve Games)
 */
public class Actions2 {
	
	/**
	 * Acquire and initialize a {@link GravityAction} from the pool.
	 * 
	 * @param gravity    The gravity coefficient (must be positive).
	 * @param fromY      The initial y-coordinate (cannot be greater than {@code toY}).
	 * @param toY        The target y-coordinate (cannot be less than {@code fromY}).
	 * @param bounces    The number of times to bounce after reaching the y-coordinate for the
	 *                   first time (between 0 and 20 inclusive).
	 * @param bounciness The factor to apply to the height of the previous bounce (or the initial
	 *                   height if this is the first bounce) to determine the height of a bounce.
	 * @return           The new {@link GravityAction}.
	 * @throws IllegalArgumentException if one or more parameters are not valid.
	 */
	public static GravityAction gravity(float gravity, float fromY, float toY, int bounces, float bounciness) throws IllegalArgumentException {
		return Actions.action(GravityAction.class).init(gravity, fromY, toY, bounces, bounciness);
	}
}
