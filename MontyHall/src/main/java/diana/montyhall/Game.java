package diana.montyhall;

import java.util.List;

/**
 * Monty Hall game implementation.
 * <p>
 * Executes a {@link GameSession} upon instantiation according to the provided
 * {@code Strategy}. A call to {@link #playerWon()} returns the result.
 */
class Game
{
    
    private final int winningDoorId;
    private final List<Door> doors;
    private int selectedDoorId = -1;
    
    Game(GameSession session, SwitchStrategy switchStrategy)
    {
        this.doors = session.getDoors();
        this.winningDoorId = session.getWinningDoorId();
        play(switchStrategy);
    }
    
    boolean playerWon()
    {
        return this.selectedDoorId == this.winningDoorId;
    }
    
    private void play(SwitchStrategy switchStrategy)
    {
        selectedDoorId = selectDoor();
        
        boolean isDone = false;
        while(!isDone)
        {
            openDoor();
            
            if(SwitchStrategy.ALWAYS_SWITCH.equals(switchStrategy))
            {
                selectedDoorId = selectDoor();
            }
            else if(SwitchStrategy.SWITCH_ON_LAST.equals(switchStrategy) && this.doors.size() == 3)
            {
                selectedDoorId = selectDoor();
            }


            isDone = this.doors.size() == 2; // no choice left when two doors are left
        }
    }
    
    private int selectDoor()
    {
        Door door = this.doors.stream()
                .filter(d -> d.getId() != this.selectedDoorId)
                .findAny().get();
        return door.getId(); // non-existent id if null
    }
    
    private void openDoor()
    {
        Door door = this.doors.stream()
                .filter(d -> d.getId() != this.winningDoorId && d.getId() != this.selectedDoorId)
                .findAny().get();
        
        this.doors.remove(door); // removal signifies opening the door
    }
}
