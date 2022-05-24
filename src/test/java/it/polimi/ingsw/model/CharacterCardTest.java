package it.polimi.ingsw.model;



import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;

import static org.junit.jupiter.api.Assertions.*;

class CharacterCardTest {



}

class Choose1ToIslandTest{
    @Test

    public void testDoEffect(){
        Game game = new Game(2, true);

        Choose1ToIsland p = new Choose1ToIsland();
        p.setCurrentGame(game);

        EnumMap<Color,Integer> extractedFromCard= new EnumMap<>(Color.class);
        extractedFromCard.put(Color.BLUE,2);
        extractedFromCard.put(Color.YELLOW,1);
        extractedFromCard.put(Color.GREEN,1);
        p.setExtracted(extractedFromCard);

        game.getPlayers().get(game.getCurrentPlayer()).setCoins(2);

        //controlliamo se sono 4 gli studenti di extracted
        int sum=0;
        for(Color c : p.getExtracted().keySet()){
            sum= sum+ p.getExtracted().get(c);
        }
        assertEquals(4,sum);

        // se ne sceglie 1
        Color choosen;
        choosen= Color.GREEN;

        //si sceglie l'isola in cui metterlo
        int isl=1;
        //metto in x il numero di studenti del colore choosen prima del do effect
        int x= game.getIslands().get(isl).getStudents().get(choosen);


        p.doEffect(choosen, isl);
        //vediamo se è stato veramente aggiunto lo studente all'isola
        assertEquals(1 + x, game.getIslands().get(isl).getStudents().get(choosen));

        sum=0;
        for(Color c : p.getExtracted().keySet()){
            sum= sum+ p.getExtracted().get(c);
        }
        assertEquals(4,sum);

    }
    @Test
    public void doEffectShouldThrowRuntimeExceptionWhenNullTest(){
        Game game = new Game(2, true);

        Choose1ToIsland p = new Choose1ToIsland();
        p.setCurrentGame(game);
        EnumMap<Color, Integer> extractedFromBag= new EnumMap<>(Color.class);
        extractedFromBag.put(Color.BLUE,4);
        Color choosen= Color.YELLOW;
        int isl=1;

        Assertions.assertThrows(IllegalArgumentException.class , () -> {p.doEffect(choosen,isl);});
        System.out.println("AssertThrows");


    }
    @Test
    public void testInit(){
        Game game = new Game(2, true);
        Choose1ToIsland p = new Choose1ToIsland();
        p.setCurrentGame(game);
        p.init();

        int sum=0;
        for(Color c : p.getExtracted().keySet()){
            sum= sum+ p.getExtracted().get(c);
        }
        assertEquals(4,sum);

    }


}




class TwoAdditionalMovesTest {
    @Test
    public void testDoEffect() {
        Game game = new Game(2, true);
        TwoAdditionalMoves p= new TwoAdditionalMoves();
        p.setCurrentGame(game);
        game.getPlayers().get(game.getCurrentPlayer()).setCoins(3);
        int x= game.getPlayers().get(game.getCurrentPlayer()).getMaxSteps();
        p.doEffect();
        assertEquals(x+2,game.getPlayers().get(game.getCurrentPlayer()).getMaxSteps());
    }

}
class NoEntryIslandTest{
    @Test
    public void testDoEffect(){
        Game game = new Game(2, true);
        NoEntryIsland p=new NoEntryIsland();
        p.setCurrentGame(game);
        game.getPlayers().get(game.getCurrentPlayer()).setCoins(3);
        int islNumb=1;

        assertEquals(2,p.cost);

        assertEquals(false,game.getIslands().get(islNumb).isNoEntryIsland());
        p.doEffect(islNumb);
        assertEquals(true,game.getIslands().get(islNumb).isNoEntryIsland());

        assertEquals(3,p.cost);

    }


}

class BlockTowerTest{
    @Test
    public void testDoEffect(){
        Game game = new Game(2, true);
        BlockTower p=new BlockTower();
        p.setCurrentGame(game);
        game.getPlayers().get(game.getCurrentPlayer()).setCoins(3);

        assertEquals(3,p.cost);

        assertEquals(false,game.getIslands().get(game.getMotherNaturePosition()).isBlockTower_CC());

        p.doEffect();

        assertEquals(true,game.getIslands().get(game.getMotherNaturePosition()).isBlockTower_CC());

        assertEquals(4,p.cost);

    }

}

class Plus2InfluenceTest{
    @Test
    public void testDoEffect(){
        Game game = new Game(2, true);
        Plus2Influence p= new Plus2Influence();
        p.setCurrentGame(game);
        game.getPlayers().get(game.getCurrentPlayer()).setCoins(3);

        assertEquals(2,p.cost);
        assertEquals(false,game.getIslands().get(game.getMotherNaturePosition()).isPlus2Influence_CC());
        p.doEffect();
        assertEquals(true,game.getIslands().get(game.getMotherNaturePosition()).isPlus2Influence_CC());
        assertEquals(3,p.cost);

    }
}

class BlockColorOnceTest{
    @Test
    public void testDoEffect(){
        Game game = new Game(2, true);
        BlockColorOnce p= new BlockColorOnce();
        p.setCurrentGame(game);
        game.getPlayers().get(game.getCurrentPlayer()).setCoins(3);
        Color choosen=Color.YELLOW;

        assertEquals(3,p.cost);
        assertEquals(false,game.getIslands().get(game.getMotherNaturePosition()).isBlockColorOnce_CC());
        assertEquals(null,game.getIslands().get(game.getMotherNaturePosition()).getBlockedColor());

        p.doEffect(choosen);

        assertEquals(true,game.getIslands().get(game.getMotherNaturePosition()).isBlockColorOnce_CC());
        assertEquals(choosen,game.getIslands().get(game.getMotherNaturePosition()).getBlockedColor());

        assertEquals(4,p.cost);

    }
}
class Exchange2StudentsTest{
    @Test
    public void testDoEffect() {
        Game game = new Game(2, true);
        game.getPlayers().get(game.getCurrentPlayer()).getSchoolDashboard().getEntrance().clear();
        Exchange2Students p=new Exchange2Students();
        p.setCurrentGame(game);
        game.getPlayers().get(game.getCurrentPlayer()).setCoins(3);

        game.getPlayers().get(game.getCurrentPlayer()).getSchoolDashboard().addStudentToEntrance(Color.YELLOW);
        game.getPlayers().get(game.getCurrentPlayer()).getSchoolDashboard().addStudentToEntrance(Color.YELLOW);
        game.getPlayers().get(game.getCurrentPlayer()).getSchoolDashboard().addStudentToEntrance(Color.BLUE);

        game.getPlayers().get(game.getCurrentPlayer()).getSchoolDashboard().addStudentToDiningRoom(Color.RED);
        game.getPlayers().get(game.getCurrentPlayer()).getSchoolDashboard().addStudentToDiningRoom(Color.GREEN);

        //controllo numero di studenti in entrance
        int sumDiningRoom1=0;
        for(Color c: game.getPlayers().get(game.getCurrentPlayer()).getSchoolDashboard().getDiningRoom().keySet()){
            sumDiningRoom1 = sumDiningRoom1 +game.getPlayers().get(game.getCurrentPlayer()).getSchoolDashboard().getDiningRoom().get(c);
        }
        //controllo numero di studenti in diningRoom
        int sumEntrance1=0;
        for(Color c: game.getPlayers().get(game.getCurrentPlayer()).getSchoolDashboard().getEntrance().keySet()){
            sumEntrance1 = sumEntrance1 +game.getPlayers().get(game.getCurrentPlayer()).getSchoolDashboard().getEntrance().get(c);
        }



        //inizializzo enum map ChoosenFromEntrance

        EnumMap<Color,Integer> choosenFromEntrance = new EnumMap<>(Color.class);
        choosenFromEntrance.put(Color.YELLOW,1);
        choosenFromEntrance.put(Color.BLUE,1);

        EnumMap<Color,Integer> choosenFromDiningRoom = new EnumMap<>(Color.class);
        choosenFromDiningRoom.put(Color.GREEN,1);
        choosenFromDiningRoom.put(Color.RED,1);

        //eseguo effetto
        p.doEffect(choosenFromEntrance,choosenFromDiningRoom);

        //controllo numero di studenti in entrance
        int sumDiningRoom2=0;
        for(Color c: game.getPlayers().get(game.getCurrentPlayer()).getSchoolDashboard().getDiningRoom().keySet()){
            sumDiningRoom2 = sumDiningRoom2 +game.getPlayers().get(game.getCurrentPlayer()).getSchoolDashboard().getDiningRoom().get(c);
        }
        //controllo numero di studenti in diningRoom
        int sumEntrance2=0;
        for(Color c: game.getPlayers().get(game.getCurrentPlayer()).getSchoolDashboard().getEntrance().keySet()){
            sumEntrance2 = sumEntrance2 +game.getPlayers().get(game.getCurrentPlayer()).getSchoolDashboard().getEntrance().get(c);
        }

        assertEquals(sumDiningRoom1,sumDiningRoom2);
        assertEquals(sumEntrance1,sumEntrance2);
        assertEquals(0,game.getPlayers().get(game.getCurrentPlayer()).getSchoolDashboard().getEntrance().get(Color.BLUE));
        assertEquals(1,game.getPlayers().get(game.getCurrentPlayer()).getSchoolDashboard().getEntrance().get(Color.YELLOW));

        assertEquals(1,game.getPlayers().get(game.getCurrentPlayer()).getSchoolDashboard().getDiningRoom().get(Color.YELLOW));
        assertEquals(1,game.getPlayers().get(game.getCurrentPlayer()).getSchoolDashboard().getDiningRoom().get(Color.BLUE));
    }

}


class Choose1DiningRoomTest{
    @Test
    public void testDoEffect(){
        Game game= new Game(2, true);
        Choose1DiningRoom p=new Choose1DiningRoom();
        p.setCurrentGame(game);
        game.getPlayers().get(game.getCurrentPlayer()).setCoins(3);
        game.getPlayers().get(0).setCurrentGame(game);
        game.getPlayers().get(0).getSchoolDashboard().setCurrentGame(game);
        game.getPlayers().get(1).setCurrentGame(game);
        game.getPlayers().get(1).getSchoolDashboard().setCurrentGame(game);

        p.getExtracted().put(Color.YELLOW,2);
        p.getExtracted().put(Color.RED,2);

        Color choosen= Color.YELLOW;
        //verifico che il colore non sia presente nella sala
        assertEquals(0,game.getPlayers().get(game.getCurrentPlayer()).getSchoolDashboard().getDiningRoom().get(choosen));


        p.doEffect(choosen);
        assertEquals(1,game.getPlayers().get(game.getCurrentPlayer()).getSchoolDashboard().getDiningRoom().get(choosen));


    }
    @Test
    public void testDoEffectShouldThrowRuntimeExceptionWhenColorIsAbsent(){
        Game game= new Game(2, true);
        Choose1DiningRoom p=new Choose1DiningRoom();
        p.setCurrentGame(game);

        Color choosen= Color.RED;

        p.getExtracted().put(Color.YELLOW,2);
        p.getExtracted().put(Color.RED,2);

        Assertions.assertThrows(IllegalArgumentException.class , () -> {p.doEffect(choosen);});

    }
    @Test
    public void testInit(){
        Game game= new Game(2, true);
        Choose1DiningRoom p=new Choose1DiningRoom();
        p.setCurrentGame(game);
        p.init();
        int sum=0;
        for(Color c: p.getExtracted().keySet()){
            sum=sum + p.getExtracted().get(c);
        }
        assertEquals(4,sum);

    }

}

class AllRemoveColorTest{
    @Test
    public void testDoEffect(){
        Game game=new Game(3, true);
        AllRemoveColor p= new AllRemoveColor();
        p.setCurrentGame(game);
        game.getPlayers().get(game.getCurrentPlayer()).setCoins(3);

        for(Player g: game.getPlayers()){
            g.setCurrentGame(game);
            g.getSchoolDashboard().setCurrentGame(game);
        }
        for(int i=0;i<4;i++)
            game.getPlayers().get(0).getSchoolDashboard().addStudentToDiningRoom(Color.RED);
        for(int i=0;i<2;i++)
            game.getPlayers().get(1).getSchoolDashboard().addStudentToDiningRoom(Color.RED);
        game.getPlayers().get(2).getSchoolDashboard().addStudentToDiningRoom(Color.GREEN);


        Color choosen=Color.RED;

        assertEquals(4,game.getPlayers().get(0).getSchoolDashboard().getDiningRoom().get(choosen));
        assertEquals(2,game.getPlayers().get(1).getSchoolDashboard().getDiningRoom().get(choosen));

        p.doEffect(choosen);

        assertEquals(1,game.getPlayers().get(0).getSchoolDashboard().getDiningRoom().get(choosen));
        assertEquals(0,game.getPlayers().get(1).getSchoolDashboard().getDiningRoom().get(choosen));

        assertEquals(1,game.getPlayers().get(2).getSchoolDashboard().getDiningRoom().get(Color.GREEN));

    }

}

class Choose3toEntranceTest{
    @Test
    public void testDoEffect() {
        Game game = new Game(2, true);
        game.getPlayers().get(game.getCurrentPlayer()).getSchoolDashboard().getEntrance().clear();
        Choose3toEntrance p = new Choose3toEntrance();
        p.setCurrentGame(game);
        game.getPlayers().get(game.getCurrentPlayer()).setCoins(3);
        game.getPlayers().get(game.getCurrentPlayer()).setCurrentGame(game);
        game.getPlayers().get(game.getCurrentPlayer()).getSchoolDashboard().setCurrentGame(game);

        p.getExtracted().put(Color.BLUE, 1);
        p.getExtracted().put(Color.RED, 2);
        p.getExtracted().put(Color.YELLOW, 1);
        p.getExtracted().put(Color.GREEN, 2);


        game.getPlayers().get(game.getCurrentPlayer()).getSchoolDashboard().addStudentToEntrance(Color.BLUE);
        game.getPlayers().get(game.getCurrentPlayer()).getSchoolDashboard().addStudentToEntrance(Color.PINK);
        game.getPlayers().get(game.getCurrentPlayer()).getSchoolDashboard().addStudentToEntrance(Color.PINK);


        EnumMap<Color, Integer> fromCard = new EnumMap<>(Color.class);
        fromCard.put(Color.GREEN, 1);
        fromCard.put(Color.YELLOW, 1);
        fromCard.put(Color.RED, 1);
        EnumMap<Color, Integer> fromEntrance = new EnumMap<>(Color.class);
        fromEntrance.put(Color.BLUE, 1);
        fromEntrance.put(Color.PINK, 2);

        EnumMap<Color, Integer> support1 = new EnumMap<>(Color.class);
        EnumMap<Color, Integer> support2 = new EnumMap<>(Color.class);
        support1 = fromCard.clone();
        support2 = fromEntrance.clone();


            int sum1 = 0;
            for (Color c : p.getExtracted().keySet())
                sum1 = sum1 + p.getExtracted().get(c);

            assertEquals(6, sum1);

            p.doEffect(fromCard, fromEntrance);

            int sum2 = 0;

            for (Color c : p.getExtracted().keySet())
                sum2 = sum2 + p.getExtracted().get(c);

            //verifica della carta
            assertEquals(6, sum2);
            assertEquals(2, p.getExtracted().get(Color.BLUE));
            assertEquals(1, p.getExtracted().get(Color.GREEN));
            assertEquals(1, p.getExtracted().get(Color.RED));
            assertEquals(2, p.getExtracted().get(Color.PINK));

            //verifica dell'ingresso
            assertEquals(1, game.getPlayers().get(game.getCurrentPlayer()).getSchoolDashboard().getEntrance().get(Color.RED));
            assertEquals(1, game.getPlayers().get(game.getCurrentPlayer()).getSchoolDashboard().getEntrance().get(Color.YELLOW));
            assertEquals(1, game.getPlayers().get(game.getCurrentPlayer()).getSchoolDashboard().getEntrance().get(Color.GREEN));

    }
    @Test
    public void testShouldThrowRuntimeExceptionWhenDifferentStudentNumber(){
        Game game = new Game(2, true);
        game.getPlayers().get(game.getCurrentPlayer()).getSchoolDashboard().getEntrance().clear();
        Choose3toEntrance p = new Choose3toEntrance();
        p.setCurrentGame(game);

        EnumMap<Color, Integer> fromCard = new EnumMap<>(Color.class);
        fromCard.put(Color.GREEN, 1);
        fromCard.put(Color.YELLOW, 1);
        fromCard.put(Color.RED, 1);
        EnumMap<Color, Integer> fromEntrance = new EnumMap<>(Color.class);
        fromEntrance.put(Color.BLUE, 1);
        fromEntrance.put(Color.PINK, 1);

        EnumMap<Color, Integer> support1 = new EnumMap<>(Color.class);
        EnumMap<Color, Integer> support2 = new EnumMap<>(Color.class);
        support1 = fromCard.clone();
        support2 = fromEntrance.clone();

        System.out.println("Throw");
        Assertions.assertThrows(IllegalArgumentException.class , () -> {p.doEffect(fromCard,fromEntrance);});



    }
    @Test
    public void testInit(){
        Game game = new Game(2, true);
        game.getPlayers().get(game.getCurrentPlayer()).getSchoolDashboard().getEntrance().clear();
        Choose3toEntrance p = new Choose3toEntrance();
        p.setCurrentGame(game);
        p.init();
        int sum=0;
        for(Color c: p.getExtracted().keySet()){
            sum=sum+p.getExtracted().get(c);
        }
        assertEquals(6,sum);


    }
}
class ChooseIslandTest{
    @Test
    public void testDoEffectOccupiedBy(){
        Game game = new Game(2, true);
        ChooseIsland p= new ChooseIsland();
        p.setCurrentGame(game);

        for(Player x : game.getPlayers()){

            x.getSchoolDashboard().setCurrentGame(game);
            x.setCurrentGame(game);
            game.getPlayers().get(0).setCoins(3);
        }
        game.setCurrentPlayer(0);

        //isola occupata 2 dal giocatore 1 decremento torri
        game.getIslands().get(2).setOccupiedBy(game.getPlayers().get(1));
        game.getPlayers().get(1).getSchoolDashboard().removeTowers(1);
        //aggiungo 1 studente rosso e 1 verde all'isola
        game.getIslands().get(2).addStudents(Color.RED,1);
        game.getIslands().get(2).addStudents(Color.GREEN,1);
        game.getIslands().get(2).addStudents(Color.YELLOW,1);
        //metto 1 studenti nella dining room di entrambi gli studenti
        game.getPlayers().get(1).getSchoolDashboard().addStudentToDiningRoom(Color.RED);
        game.getPlayers().get(0).getSchoolDashboard().addStudentToDiningRoom(Color.YELLOW);
        game.getPlayers().get(0).getSchoolDashboard().addStudentToDiningRoom(Color.GREEN);

        assertEquals(7,game.getPlayers().get(1).getSchoolDashboard().getTowers());
        assertEquals(8,game.getPlayers().get(0).getSchoolDashboard().getTowers());
        //ora il giocatore 1 ha il prof
        p.doEffect(2);

        assertEquals(game.getPlayers().get(0),game.getIslands().get(2).getOccupiedBy());
        assertEquals(7,game.getPlayers().get(0).getSchoolDashboard().getTowers());
        assertEquals(8,game.getPlayers().get(1).getSchoolDashboard().getTowers());
    }
    @Test
    public void testDoEffect(){
        Game game = new Game(2, true);
        ChooseIsland p= new ChooseIsland();
        p.setCurrentGame(game);
        for(Player x : game.getPlayers()){

            x.getSchoolDashboard().setCurrentGame(game);
            x.setCurrentGame(game);

        }
        game.getPlayers().get(0).setCoins(3);
        game.setCurrentPlayer(0);

        game.getIslands().get(2).addStudents(Color.GREEN);
        game.getPlayers().get(0).getSchoolDashboard().addStudentToDiningRoom(Color.GREEN);

        p.doEffect(2);

        assertEquals(7,game.getPlayers().get(0).getSchoolDashboard().getTowers());
        assertEquals(game.getPlayers().get(0),game.getIslands().get(2).getOccupiedBy());

    }


}

class TempControlProfTest{
    @Test
    public void testDoEffect(){
        Game game = new Game(2, true);
        TempControlProf p= new TempControlProf();
        p.setCurrentGame(game);
        for(Player g: game.getPlayers()) {
            g.setCoins(3);
            g.setCurrentGame(game);
            g.getSchoolDashboard().setCurrentGame(game);
        }
        game.setCurrentPlayer(0);
        game.getPlayers().get(0).getSchoolDashboard().addStudentToDiningRoom(Color.RED);
        game.getPlayers().get(0).getSchoolDashboard().addStudentToDiningRoom(Color.RED);
        assertEquals(true,game.getPlayers().get(0).getSchoolDashboard().getProfessors().contains(Color.RED));

        game.getPlayers().get(1).getSchoolDashboard().addStudentToDiningRoom(Color.RED);
        game.getPlayers().get(1).getSchoolDashboard().addStudentToDiningRoom(Color.RED);
        assertEquals(false,game.getPlayers().get(1).getSchoolDashboard().getProfessors().contains(Color.RED));

        game.setCurrentPlayer(1);
        p.doEffect();

        assertEquals(false,game.getPlayers().get(0).getSchoolDashboard().getProfessors().contains(Color.RED));
        assertEquals(true,game.getPlayers().get(1).getSchoolDashboard().getProfessors().contains(Color.RED));


    }
    @Test
    public void testResetTempControlProf(){
        Game game = new Game(2, true);
        TempControlProf p= new TempControlProf();
        p.setCurrentGame(game);
        for(Player g: game.getPlayers()) {
            g.setCoins(3);
            g.setCurrentGame(game);
            g.getSchoolDashboard().setCurrentGame(game);
        }
        game.setCurrentPlayer(0);
        game.getPlayers().get(0).getSchoolDashboard().addStudentToDiningRoom(Color.RED);
        game.getPlayers().get(0).getSchoolDashboard().addStudentToDiningRoom(Color.RED);
        assertEquals(true,game.getPlayers().get(0).getSchoolDashboard().getProfessors().contains(Color.RED));

        game.getPlayers().get(1).getSchoolDashboard().addStudentToDiningRoom(Color.RED);
        game.getPlayers().get(1).getSchoolDashboard().addStudentToDiningRoom(Color.RED);
        assertEquals(false,game.getPlayers().get(1).getSchoolDashboard().getProfessors().contains(Color.RED));

        game.setCurrentPlayer(1);
        p.doEffect();

        assertEquals(false,game.getPlayers().get(0).getSchoolDashboard().getProfessors().contains(Color.RED));
        assertEquals(true,game.getPlayers().get(1).getSchoolDashboard().getProfessors().contains(Color.RED));

        p.resetTempControlProf();

        assertEquals(true,game.getPlayers().get(0).getSchoolDashboard().getProfessors().contains(Color.RED));
        assertEquals(false,game.getPlayers().get(1).getSchoolDashboard().getProfessors().contains(Color.RED));
        assertTrue(p.getPlayerModified().isEmpty());
    }


}

