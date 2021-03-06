import stripsAlg.Action;
import stripsAlg.STRIPS;
import utils.*;
import utils.enums.PlannerState;
import utils.enums.OutputFormat;
import utils.exceptions.CompleteAtStartException;
import utils.exceptions.StuckException;
import utils.exceptions.UnsolvableException;


import java.util.List;
import java.util.concurrent.*;

public class Main {

    final static Boolean PRINT_GOAL_STACK = false; // print the stack or not
    final static OutputFormat OUTPUT_FORMAT = OutputFormat.JSON; // specifying an output format
    final static long TIMEOUT_THRESHOLD = 2000; // set planner time threshold

    public static void main(String[] args) {
        List<String> names = InputReader.readNames(""); // read in all problems/situations
        List<String[]> initialStates = InputReader.readInitStates(""); // read in all initial states
        List<String[]> goalStates = InputReader.readGoalStates(""); // read in all goal states
        ResultWriter resultWriter = new ResultWriter(OUTPUT_FORMAT);

        // Run the planner
        for (int i = 0; i < names.size(); i++) {
            String name = names.get(i);
            String[] initialState = initialStates.get(i);
            String[] goalState = goalStates.get(i);

            System.out.println("Start working on task: " + name);
            try {
                STRIPS strips = new STRIPS(initialState, goalState, PRINT_GOAL_STACK, TIMEOUT_THRESHOLD);
                List<Action> plan = strips.getPlan();
                resultWriter.writeResult(plan, name, initialState, goalState, PlannerState.NORMAL);
            } catch (UnsolvableException e) {
                resultWriter.writeStringResult("UNSOLVABLE", name, initialState, goalState, PlannerState.UNSOLVABLE);
            } catch (StuckException | TimeoutException e) {
                resultWriter.writeStringResult("STRIPS_STUCK", name, initialState, goalState, PlannerState.STUCK);
            } catch (CompleteAtStartException e) {
                resultWriter.writeStringResult("NOTHING_NEEDS_TO_BE_DONE", name, initialState, goalState, PlannerState.DO_NOTHING);
            }
            System.out.println("Completed task: " + name);
        }


    }
}
