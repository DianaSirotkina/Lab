package diana;

import java.util.Comparator;
import java.util.Map;

import diana.montyhall.MontyHall;
import diana.montyhall.SwitchStrategy;
import org.apache.commons.cli.*;

public class Application
{

    /*
    * выводим на консоль текст помощи
    * */
    private static Options createOptions()
    {
        Options options = new Options();
        
        options.addOption("d", "doors", true, "Количество дверей, минимально 3 двери");
        options.addOption("i", "iterations", true, "Количество итераций");
        options.addOption("h", "help", false, "Печатать этот текст");
        
        return options;
    }

    /*
    * вычисляем процент выигрыша
    * */
    private static float getPercent(int total, long success)
    {
        return (success * 100.0f) / total;
    }

    /*
    * зная кол-во дверей и кол-во итераций, считываем по статистике процент выигрыша
    * */
    private static void startGame(int numDoors, int iterations)
    {
        System.out.println("Начинаем симуляцию... (двери: " + numDoors + ", итерации: " + iterations + ")");

        Map<String, Long> result = MontyHall.runSimulations(numDoors, iterations);

        System.out.println();
        System.out.println("Результаты:");

        result.entrySet().stream()
                .sorted(
                        Map.Entry.comparingByValue(
                                Comparator.reverseOrder()
                        )
                ).forEachOrdered( entry ->

                        System.out.println(
                                SwitchStrategy.valueOf(entry.getKey()).getStrategy() +
                                        ": \t\t" + getPercent(iterations,entry.getValue()) + "%"
                        )
                );
    }


    /*
    *
    * если есть параметры с командной строки кол-во дверей и итераций,
    * считаем по этим параметрам, если нет- то по умолчанию
    *
    */
    public static void main(String[] args) throws ParseException
    {
        Options opts = createOptions();
        String command = "java -jar <FILE>";
        
        CommandLineParser commandLineParser = new DefaultParser();
        CommandLine commandLine = commandLineParser.parse(opts, args);
        
        HelpFormatter helpFormatter = new HelpFormatter();
        
        int doors = Integer.parseInt(commandLine.getOptionValue("d", "10"));
        int iterations = Integer.parseInt(commandLine.getOptionValue("i", "100"));
        
        if(commandLine.hasOption("h"))
        {
            helpFormatter.printHelp(command, opts);
        }
        else if(doors < 3)
        {
            System.out.println("ОШИБКА: минимальное количество дверей может равняться 3 и больше ");
            helpFormatter.printHelp(command, opts);
        }
        else
        {
            startGame(doors, iterations);
        }
    }
}
