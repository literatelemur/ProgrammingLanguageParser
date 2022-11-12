package Project.com.project;

import org.parboiled.Parboiled;
import org.parboiled.parserunners.RecoveringParseRunner;
import org.parboiled.common.StringUtils;
import static org.parboiled.support.ParseTreeUtils.printNodeTree;
import org.parboiled.support.ParsingResult;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Parser parser = Parboiled.createParser(Parser.class);
        
        while (true) {
            System.out.print("Enter a name followed by an address followed by a phone number.\n");
            Scanner input = new Scanner(System.in);
            String input1 = input.nextLine();
            String input2 = input.nextLine();
            String input3 = input.nextLine();
            if (StringUtils.isEmpty(input3)) break;

            String allinput = input1 + "\n" + input2 + "\n" + input3;  
            ParsingResult<?> result = new RecoveringParseRunner(parser.Form()).run(allinput);

            System.out.println(input + " = " + result.parseTreeRoot.getValue() + '\n');
            System.out.println(printNodeTree(result) + '\n');

            if (!result.matched) {
                System.out.println(StringUtils.join(result.parseErrors, "---\n"));
            }
        }
    }

}