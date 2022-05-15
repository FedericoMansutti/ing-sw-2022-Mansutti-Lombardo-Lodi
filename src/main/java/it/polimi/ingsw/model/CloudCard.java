package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.EnumMap;

public class CloudCard {
    private EnumMap<Color, Integer> students;
    private boolean full;
    private Game currentGame;

    public void setUp()
    {
        students = currentGame.extractFromBag(3);
    }

    public EnumMap<Color, Integer> getStudents()
    {
        return students;
    }
    public void transferStudents()
    {
        for(Color color : students.keySet())
        {
            currentGame.getPlayers().get(currentGame.getCurrentPlayer()).getSchoolDashboard().getEntrance().put(color, currentGame.getPlayers().get(currentGame.getCurrentPlayer()).getSchoolDashboard().getEntrance().get(color)+ students.get(color));
        }
        students=new EnumMap<>(Color.class);
    }

    public void setCurrentGame(Game game) {
        this.currentGame=game;
    }
}


