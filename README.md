LibGDX Action Pack
==================

Useful actions not found in LibGDX and action related utilities.

### `GravityAction`
Action for simulating gravity without the overhead of a physics engine.

### `Actions2`
Similar to `Actions` in LibGDX in that it acquires and initializes pooled actions provided by this library.

### `FiniteDurationAction`
Alternative to `TemporalAction` where you need to create a custom action with a duration that is not specified by the user but instead derived from the action's parameters.

### `ActionList`
Accumulates actions for multiple actors over time and executes them simultaneously and, when done, executes a callback as a `RunnableExecution` on the provided `Stage`.
