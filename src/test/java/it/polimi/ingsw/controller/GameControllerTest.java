package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.IslandGroup;
import it.polimi.ingsw.model.Wizard;
import it.polimi.ingsw.model.reduced.ReducedGame;
import it.polimi.ingsw.network.ClientHandler;
import it.polimi.ingsw.network.message.*;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.VirtualView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameControllerTest {
    GameController gameController;
    String nick1, nick2;

    class CustomGame extends Game {
        public CustomGame(int players, boolean expertMode) {
            super(players, expertMode);
        }

        @Override
        public EnumMap<Color, Integer> extractFromBag(int q) {
            if (q == 7) {
                // instantiate a custom
                EnumMap<Color, Integer> customExtract = new EnumMap<>(Color.class);
                customExtract.put(Color.BLUE, 2);
                customExtract.put(Color.RED, 2);
                customExtract.put(Color.GREEN, 1);
                customExtract.put(Color.YELLOW, 1);
                customExtract.put(Color.PINK, 1);

                // remove from bag
                bag.merge(Color.BLUE,   -2, Integer::sum);
                bag.merge(Color.RED,    -2, Integer::sum);
                bag.merge(Color.GREEN,  -1, Integer::sum);
                bag.merge(Color.YELLOW, -1, Integer::sum);
                bag.merge(Color.PINK,   -1, Integer::sum);

                return customExtract;
            } else if (q == 3) {
                EnumMap<Color, Integer> customExtract = new EnumMap<>(Color.class);
                customExtract.put(Color.BLUE, 1);
                customExtract.put(Color.RED, 1);
                customExtract.put(Color.GREEN, 1);

                // remove from bag
                bag.merge(Color.BLUE,  -1, Integer::sum);
                bag.merge(Color.RED,   -1, Integer::sum);
                bag.merge(Color.GREEN, -1, Integer::sum);

                return customExtract;
            } else {
                return super.extractFromBag(q);
            }
        }
    }

    @Test
    public void setUp() {
        gameController = new GameController(2, true, 0);
        Game game = new CustomGame(2, true);
        gameController.setGame(game);

        //each ArrayList now contains the exact colors contained in the islands
        for(int i=0; i<12;i++)
        {
            game.getIslands().get(i).getStudents().clear();
        }

        game.getIslands().get(1).addStudents(Color.BLUE);
        game.getIslands().get(2).addStudents(Color.GREEN);
        game.getIslands().get(3).addStudents(Color.YELLOW);
        game.getIslands().get(4).addStudents(Color.BLUE);
        game.getIslands().get(5).addStudents(Color.BLUE);
        game.getIslands().get(7).addStudents(Color.RED);
        game.getIslands().get(8).addStudents(Color.YELLOW);
        game.getIslands().get(9).addStudents(Color.GREEN);
        game.getIslands().get(10).addStudents(Color.RED);
        game.getIslands().get(11).addStudents(Color.RED);


        nick1 = "p1";
        nick2 = "p2";

        VirtualView virtualView1 = mock(VirtualView.class);
        VirtualView virtualView2 = mock(VirtualView.class);

        doNothing().when(virtualView1).showLobby(anyList(), anyInt());
        doNothing().when(virtualView1).showStringMessage(anyString());
        doNothing().when(virtualView1).update(any(ReducedGame.class));
        doNothing().when(virtualView1).updateAvailableWizards(anyList());
        doNothing().when(virtualView1).askActionPhase3(anyList(), anyBoolean());

        doNothing().when(virtualView2).showLobby(anyList(), anyInt());
        doNothing().when(virtualView2).showStringMessage(anyString());
        doNothing().when(virtualView2).update(any(ReducedGame.class));
        doNothing().when(virtualView2).updateAvailableWizards(anyList());


        doNothing().when(virtualView1).showStringMessage(anyString());
        doNothing().when(virtualView2).showStringMessage(anyString());
        gameController.addPlayer(nick1, virtualView1);
        gameController.addPlayer(nick2, virtualView2);
        game.setCurrentPlayer(0);

        Map<Integer, Integer> h1 = new HashMap<>(game.getPlayer(nick1).get().getHand().getAsMap());

        // 1) p1 plays assistant 1
        doAnswer(invocation -> {
            Map<Integer, Integer> hand = (Map<Integer, Integer>) invocation.getArguments()[0];
            List<Integer> notPlayable = (List<Integer>) invocation.getArguments()[1];

            gameController.onMessageArrived(new PlayAssistantCard(nick1, 1));
            return null;
        }).when(virtualView1).askAssistantCard(eq(h1), anyList());

        Map<Integer, Integer> h2 = new HashMap<>(game.getPlayer(nick2).get().getHand().getAsMap());

        // 1) p2 plays assistant 4
        doAnswer(invocation -> {
            Map<Integer, Integer> hand = (Map<Integer, Integer>) invocation.getArguments()[0];
            List<Integer> notPlayable = (List<Integer>) invocation.getArguments()[1];

            assertTrue(notPlayable.contains(1));

            gameController.onMessageArrived(new PlayAssistantCard(nick2, 4));
            return null;
        }).when(virtualView2).askAssistantCard(eq(h2), anyList());


        // 1) AP1-p1 moves 2 blue students and 1 red student to DR
        doAnswer(invocation -> {
            gameController.onMessageArrived(new MoveStudentToDiningRoom(nick1, Color.BLUE));

            doNothing().when(virtualView1).askActionPhase1(eq(1), anyInt(), anyBoolean());
            return null;
        }).when(virtualView1).askActionPhase1(eq(1), anyInt(), anyBoolean());

        doAnswer(invocation -> {
            gameController.onMessageArrived(new MoveStudentToDiningRoom(nick1, Color.BLUE));
            return null;
        }).when(virtualView1).askActionPhase1(eq(2), anyInt(), anyBoolean());

        doAnswer(invocation -> {
            gameController.onMessageArrived(new MoveStudentToDiningRoom(nick1, Color.RED));
            return null;
        }).when(virtualView1).askActionPhase1(eq(3), anyInt(), anyBoolean());

        // 1) AP2-p1 moves MN 1 step
        doAnswer(invocation -> {
            assertEquals(game.getPlayer(nick1).get().getSchoolDashboard().getDiningRoom().get(Color.BLUE), 2);
            assertEquals(game.getPlayer(nick1).get().getSchoolDashboard().getDiningRoom().get(Color.RED), 1);

            assertEquals(game.getPlayer(nick1).get().getSchoolDashboard().getEntrance().get(Color.RED), 1);
            assertEquals(game.getPlayer(nick1).get().getSchoolDashboard().getEntrance().get(Color.BLUE), 0);
            assertEquals(game.getPlayer(nick1).get().getSchoolDashboard().getEntrance().get(Color.GREEN), 1);
            assertEquals(game.getPlayer(nick1).get().getSchoolDashboard().getEntrance().get(Color.YELLOW), 1);
            assertEquals(game.getPlayer(nick1).get().getSchoolDashboard().getEntrance().get(Color.PINK), 1);

            assertTrue(game.getPlayer(nick1).get().getSchoolDashboard().hasProfessor(Color.RED));
            assertTrue(game.getPlayer(nick1).get().getSchoolDashboard().hasProfessor(Color.BLUE));

            gameController.onMessageArrived(new MoveMotherNature(nick1, 1));

            assertEquals(game.getMotherNaturePosition(), 1);
            assertEquals(nick1, game.getCurrentIsland().getOccupiedBy().getNickname());

            return null;
        }).when(virtualView1).askActionPhase2(eq(1), anyBoolean());

        // 1) AP3-p1 chooses CC 0
        doAnswer(invocation -> {

            gameController.onMessageArrived(new ChooseCloudCard(nick1, 1));


            return null;
        }).when(virtualView1).askActionPhase3(any(), anyBoolean());

        // 1) AP1-p2 moves 2 RED and 1 blue student to DR
        doAnswer(invocation -> {
            assertEquals(game.getPlayer(nick1).get().getSchoolDashboard().getEntrance().get(Color.RED), 2);
            assertEquals(game.getPlayer(nick1).get().getSchoolDashboard().getEntrance().get(Color.BLUE), 1);
            assertEquals(game.getPlayer(nick1).get().getSchoolDashboard().getEntrance().get(Color.GREEN), 2);
            assertEquals(game.getPlayer(nick1).get().getSchoolDashboard().getEntrance().get(Color.YELLOW), 1);
            assertEquals(game.getPlayer(nick1).get().getSchoolDashboard().getEntrance().get(Color.PINK), 1);
            gameController.onMessageArrived(new MoveStudentToDiningRoom(nick2, Color.RED));
            return null;
        }).when(virtualView2).askActionPhase1(eq(1), anyInt(), anyBoolean());

        doAnswer(invocation -> {
            gameController.onMessageArrived(new MoveStudentToDiningRoom(nick2, Color.RED));
            return null;
        }).when(virtualView2).askActionPhase1(eq(2), anyInt(), anyBoolean());

        doAnswer(invocation -> {
            gameController.onMessageArrived(new MoveStudentToDiningRoom(nick2, Color.BLUE));

            assertEquals(game.getPlayer(nick2).get().getSchoolDashboard().getDiningRoom().get(Color.RED), 2);
            return null;
        }).when(virtualView2).askActionPhase1(eq(3), anyInt(), anyBoolean());

        // 1) AP2-p2 moves MN 4 steps
        doAnswer(invocation -> {
            gameController.onMessageArrived(new MoveMotherNature(nick2, 4));

            assertEquals(game.getMotherNaturePosition(), 5);
            return null;
        }).when(virtualView2).askActionPhase2(eq(4), anyBoolean());

        // 1) AP3-p2 chooses CC 0
        doAnswer(invocation -> {
            gameController.onMessageArrived(new ChooseCloudCard(nick2, 1));
            return null;
        }).when(virtualView2).askActionPhase3(any(), anyBoolean());


        gameController.onMessageArrived(new ChooseWizard(nick1, Wizard.PIXIE));
        gameController.onMessageArrived(new ChooseWizard(nick2, Wizard.SORCERER));
    }


}