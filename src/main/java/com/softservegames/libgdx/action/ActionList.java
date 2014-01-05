package com.softservegames.libgdx.action;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.DelegateAction;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * This accumulates and processes pending actions and is scoped to a {@link Stage}.
 * </p>
 * <p>
 * It is useful for executing arbitrary code after a set of otherwise unrelated
 * actions has completed.
 * </p>
 *
 * @author Steve Taylor (Soft Serve Games)
 */
public final class ActionList {
	
	/**
	 * Create an action list for the specified stage.
	 * 
	 * @param stage A stage.
	 */
	public ActionList(Stage stage) {
		this.stage = stage;
	}

	/**
	 * Get the number of actions in this list.
	 *
	 * @return The number of actions.
	 */
	public int count() {
		return actions.size();
	}

	/**
	 * Get the action at the specified index.
	 *
	 * @param index An index.
	 *
	 * @return The action at the specified index.
	 */
	public Action get(int index) {
		return actions.get(index);
	}

	/**
	 * Add an action to this list.
	 *
	 * @param action The action to add.
	 */
	public void add(Action action) {
		actions.add(action);
	}

	/**
	 * Clear this list.
	 */
	public void clear() {
		actions.clear();
	}

	/**
	 * Add all the actions to their actor and then clear all the actions.
	 * 
	 * @param done  The callback to execute as a {@link RunnableAction} on the stage as soon as all the actions have completed.
	 * @param delay The delay between completing the actions in this list and executing the callback.
	 */
	public void process(Runnable done, float delay) {
		float actionDuration = getMaxDuration();
		for (Action action : actions) {
			action.getActor().addAction(action);
		}
		actions.clear();
		stage.addAction(Actions.delay(actionDuration + delay, Actions.run(done)));
	}

	/**
	 * Get the maximum duration of the actions in this list.
	 *
	 * @return The maximum duration of all the actions.
	 */
	public float getMaxDuration() {
		return actionMaxDurations(actions);
	}
	
	/**
	 * Get the maximum duration of the specified actions.
	 * 
	 * @param actions Some actions.
	 * @return        The maximum duration of the specified actions.
	 */
	public static float actionMaxDurations(Iterable<Action> actions) {
		float maxDuration = 0;
		for (Action action : actions) {
			maxDuration = Math.max(maxDuration, actionDuration(action));
		}
		return maxDuration;
	}

	/**
	 * Get the duration of the specified action.
	 *
	 * @param <A>    The type of action.
	 * @param action An action.
	 *
	 * @return The action's duration or {@code 0} if its duration is infinite or cannot be calculated.
	 */
	public static <A extends Action> float actionDuration(A action) {
		if (action instanceof FiniteDurationAction) {
			return ((FiniteDurationAction) action).getDuration();
		}
		if (action instanceof TemporalAction) {
			return ((TemporalAction) action).getDuration();
		}
		if (action instanceof DelayAction) {
			return ((DelayAction) action).getDuration() + actionDuration(((DelayAction) action).getAction());
		}
		if (action instanceof DelegateAction) {
			return actionDuration(((DelegateAction) action).getAction());
		}
		if (action instanceof SequenceAction) {
			float duration = 0;
			for (Action subAction : ((SequenceAction) action).getActions()) {
				duration += actionDuration(subAction);
			}
			return duration;
		}
		if (action instanceof ParallelAction) {
			float duration = 0;
			for (Action subAction : ((ParallelAction) action).getActions()) {
				duration = Math.max(duration, actionDuration(subAction));
			}
			return duration;
		}
		return 0;
	}

	private final List<Action> actions = new ArrayList<>();
	private final Stage stage;
}
