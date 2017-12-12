package diana.montyhall;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import java.util.stream.IntStream;

/**
 * Main Monty Hall simulation class. 
 * <p>
 * Provides static methods for running simulations based on the specified
 * strategies and returning the number of winning sessions for each strategy.
 */

public class MontyHall 
{ 
    
    public static Map<String, Long> runSimulations(int numDoors, int iterations, SwitchStrategy[] switchStrategies)
    {
        List<GameSession> sessions = IntStream.range(0, iterations)
                .mapToObj(_i -> new GameSession(numDoors))
                .collect(toList());
        
        return Arrays.stream(switchStrategies).parallel()
                .collect(
                    toMap(Enum::toString, switchStrategy -> numberOfWins(sessions, switchStrategy))
                );
    }
    
    public static Map<String, Long> runSimulations(int numDoors, int iterations)
    {
        return runSimulations(numDoors, iterations, SwitchStrategy.values());
    }
    
    private static long numberOfWins(List<GameSession> sessions, SwitchStrategy switchStrategy)
    {
        return sessions.stream()
                .map(session -> new Game(session, switchStrategy))
                .filter(Game::playerWon)
                .count();
    }
}
