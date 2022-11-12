package Project.com.project;

import org.parboiled.BaseParser;
import org.parboiled.Rule;
import org.parboiled.annotations.BuildParseTree;

/**
 * Parser for very relaxed time literals. Demonstrates usage of the value stack with default values for unmatched rules.
 */
@BuildParseTree
public class Parser extends BaseParser<Object> {

	Rule Form() {
		return Sequence(FirstOf(FullName(), Name()), Address(), PhoneNum());
	}
	
	Rule PhoneNum() {
		return Sequence(
				Ch('('), Digit(), Digit(), Digit(), Ch(')'), Space(),
				Digit(), Digit(), Digit(), Ch('-'), Digit(), Digit(),
				Digit(), Digit());			
	}
	
	Rule Address() {
		return Sequence(
				StreetNum(), Space(), StreetName(),
				Com(), Space(), City(),
				Com(), Space(), State(), Com(), Space(), Country());
	}
	
	Rule StreetNum() {
		return OneOrMore(CharRange('0', '9'));
	}
	
	Rule StreetName() {
		return OneOrMore(FirstOf(CapLetter(), LowLetter(), Ch('.'), Space()));
	}
	
	Rule Com() {
		return Ch(',');
	}
	
	Rule City() {
		return Sequence(CapLetter(), OneOrMore(FirstOf(CapLetter(), LowLetter())));
	}
	
	Rule Zip() {
		return Sequence(CharRange('0', '9'), CharRange('0', '9'),
						CharRange('0', '9'), CharRange('0', '9'),
						CharRange('0', '9'));
	}
	
	Rule State() {
		return Sequence(CapLetter(), OneOrMore(FirstOf(CapLetter(), LowLetter())));
	}
	
	Rule Country() {
		return OneOrMore(CapLetter(), OneOrMore(FirstOf(CapLetter(), LowLetter(), Space())));
	}
	
	Rule Name() {
		return Sequence(FirstOrLast(), Space(), FirstOrLast());
	}

	Rule FullName() {
		return Sequence(FirstOrLast(), Space(), FirstOrLast(), Space(), FirstOrLast());
	}
	
	Rule FirstOrLast() {
		return Sequence(CapLetter(), OneOrMore(LowLetter()));
	}
	
	Rule Space() {
		return Ch(' ');
	}
	
	Rule CapLetter() {
		return CharRange('A','Z');
	}
	
	Rule LowLetter() {
		return CharRange('a','z');
	}
	
    public Rule Time() {
        return FirstOf(Time_HH_MM_SS(), Time_HHMMSS(), Time_HMM());
    }

    // h(h)?:mm(:ss)?
    Rule Time_HH_MM_SS() {
        return Sequence(
                OneOrTwoDigits(), ':',
                TwoDigits(),
                FirstOf(Sequence(':', TwoDigits()), push(0)),
                EOI,
                swap3() && push(convertToTime(popAsInt(), popAsInt(), popAsInt()))
        );
    }

    // hh(mm(ss)?)?
    Rule Time_HHMMSS() {
        return Sequence(
                TwoDigits(),
                FirstOf(
                        Sequence(
                                TwoDigits(),
                                FirstOf(TwoDigits(), push(0))
                        ),
                        pushAll(0, 0)
                ),
                EOI,
                swap3() && push(convertToTime(popAsInt(), popAsInt(), popAsInt()))
        );
    }

    // h(mm)?
    Rule Time_HMM() {
        return Sequence(
                OneDigit(),
                FirstOf(TwoDigits(), push(0)),
                EOI,
                swap() && push(convertToTime(popAsInt(), popAsInt()))
        );
    }

    Rule OneOrTwoDigits() {
        return FirstOf(TwoDigits(), OneDigit());
    }

    Rule OneDigit() {
        return Sequence(Digit(), push(Integer.parseInt(matchOrDefault("0"))));
    }

    Rule TwoDigits() {
        return Sequence(Sequence(Digit(), Digit()), push(Integer.parseInt(matchOrDefault("0"))));
    }

    Rule Digit() {
        return CharRange('0', '9');
    }

    // ************************* ACTIONS *****************************

    protected Integer popAsInt() {
        return (Integer) pop();
    }

    protected String convertToTime(Integer hours, Integer minutes) {
        return convertToTime(hours, minutes, 0);
    }

    protected String convertToTime(Integer hours, Integer minutes, Integer seconds) {
        return String.format("%s h, %s min, %s s",
                hours != null ? hours : 0,
                minutes != null ? minutes : 0,
                seconds != null ? seconds : 0);
    }

}