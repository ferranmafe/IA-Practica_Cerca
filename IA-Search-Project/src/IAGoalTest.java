import aima.search.framework.GoalTest;

public class IAGoalTest implements GoalTest{
    public boolean isGoalState(Object aState) {
        State state= (State) aState;

        return state.isGoal();
    }
}
