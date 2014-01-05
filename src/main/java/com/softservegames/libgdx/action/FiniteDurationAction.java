
package com.softservegames.libgdx.action;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;

/**
 * Concrete actions of this type calculate their duration from their user specified parameters.
 * 
 * Unlike {@link TemporalAction}, concrete implementations don't require the user to specify the
 * duration itself. It is derived from the action's parameters.
 *
 * @author Steve Taylor (Soft Serve Games)
 */
public abstract class FiniteDurationAction extends Action {
	
	/**
	 * Get the duration, in seconds, for this action to execute from start to completion.
	 * 
	 * @return This action's total duration.
	 */
	public abstract float getDuration();
}
