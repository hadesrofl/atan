package com.github.robocup_atan.atan.parser.player;

import com.github.robocup_atan.atan.model.enums.Flag;
import com.github.robocup_atan.atan.parser.BaseCommandFilter;
import com.github.robocup_atan.atan.parser.Filter;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collection;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.Parameterized;

/**
 * Test class for parsing the player see command.
 *
 * @author Atan
 */
@RunWith(Parameterized.class)
public class PlayerSeeBallTest {

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {

                /**
                 * Tests for seeing the goal are captured in the following
                 * array structure.
                 *   0: Server command.
                 *   1: Value of isTeamEast.
                 *   2: Expected value of distance.
                 *   3: Expected value of direction.
                 *   4: Expected value of distance change.
                 *   5: Expected value of direction change.
                 *   6: Expected value of body facing direction.
                 *   7: Expected value of head facing direction.
                 */

                // East team see ball messages.
                {"(see 123.45 ((b) 123.45 123.45 123.45 123.45 123.45 123.45))", true,  123.45, 123.45, 123.45, 123.45, 123.45, 123.45},
                {"(see 123.45 ((b) 123.45 123.45 123.45 123.45 123.45 123.45))", true,  123.45, 123.45, 123.45, 123.45, 123.45, 123.45},
                {"(see 123.45 ((b) 123.45 123.45 123.45 123.45 123.45 123.45))", true,  123.45, 123.45, 123.45, 123.45, 123.45, 123.45},
                // West team see ball messages.
                {"(see 123.45 ((b) 123.45 123.45 123.45 123.45 123.45 123.45))", false, 123.45, 123.45, 123.45, 123.45, 123.45, 123.45},
                {"(see 123.45 ((b) 123.45 123.45 123.45 123.45 123.45 123.45))", false, 123.45, 123.45, 123.45, 123.45, 123.45, 123.45},
                {"(see 123.45 ((b) 123.45 123.45 123.45 123.45 123.45 123.45))", false, 123.45, 123.45, 123.45, 123.45, 123.45, 123.45},

            });
    }

    // Global test variables.
    private CmdParserPlayer parser;
    private TestFilter      commandFilter;
    private Filter          filter;

    // Test parameters.
    private String  command;
    private boolean isTeamEast;
    private double  expectedDistance;
    private double  expectedDirection;
    private double  expectedDistChange;
    private double  expectedDirChange;
    private double  expectedBodyFacingDirection;
    private double  expectedHeadFacingDirection;

    public PlayerSeeBallTest(
        String  command,
        boolean isTeamEast,
        double  distance,
        double  direction,
        double  distChange,
        double  dirChange,
        double  bodyFacingDirection,
        double  headFacingDirection) {

        this.command                     = command;
        this.isTeamEast                  = isTeamEast;
        this.expectedDistance            = distance;
        this.expectedDirection           = direction;;
        this.expectedDistChange          = distChange;
        this.expectedDirChange           = dirChange;
        this.expectedBodyFacingDirection = bodyFacingDirection;
        this.expectedHeadFacingDirection = headFacingDirection;
    }

    @Before
    public void setUpTest() throws Exception {
        parser        = new CmdParserPlayer(new StringReader(""));
        commandFilter = new TestFilter();
        filter        = new Filter();
    }

    @Test
    public void parseCommand() {
        filter.run(command, commandFilter);
    }

    private class TestFilter extends BaseCommandFilter {

        private TestController controller = new TestController();
        private TestActions    action     = new TestActions();

        @Override
        public void seeCommand(String cmd) {
            try {
                parser.parseSeeCommand(cmd, controller, action);
            } catch (ParseException ex) {
                Assert.fail(ex.getMessage());
            }
        }
    }

    private class TestController extends BaseControllerPlayer {

        @Override
        public void infoSeeBall(
            double distance,
            double direction,
            double distChange,
            double dirChange,
            double bodyFacingDirection,
            double headFacingDirection) {

            Assert.assertEquals(expectedDistance, distance, 0);
            Assert.assertEquals(expectedDirection, direction, 0);
            Assert.assertEquals(expectedDistChange, distChange, 0);
            Assert.assertEquals(expectedDirChange, dirChange, 0);
            Assert.assertEquals(expectedBodyFacingDirection, bodyFacingDirection, 0);
            Assert.assertEquals(expectedHeadFacingDirection, headFacingDirection, 0);
        }
    }

    private class TestActions extends BaseActionsPlayer {

        @Override
        public boolean isTeamEast() {
            return isTeamEast;
        }
    }
    
    // Test logging
    @Rule
    public TestWatcher watchman = new TestWatcher() {

        @Override
        protected void succeeded(Description description) {
            System.out.print("Passed - ");
        }
        
        @Override
        protected void failed(Throwable e, Description description) {
            System.out.print("FAILED - ");
        }
        
        @Override
        protected void finished(Description description) {
            System.out.println(command);
        }
    };
}

