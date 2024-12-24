package volumefinder;


public class StateManager {


    enum States {
        DRAW,// If we are drawing an edge
        ERASE,// If we are erasing an edge
        NEUTRAL,// If we are not in any particular mode
        AXIS,// If we are creating an axis line
        DIMENSION// If we are dimensioning an object
    }

    private static States currentState = States.NEUTRAL;

    // Setters and getters for the state variable
    public States getCurrentState() {
        return currentState;
    }

    public void setCurrentState(States newState) {
        currentState = newState;
    }

    // Wrapper method so I dont have to type as much
    public void revertToNeutral() {
        currentState = States.NEUTRAL;
    }

}

