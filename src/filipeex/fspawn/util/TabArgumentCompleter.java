package filipeex.fspawn.util;

import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TabArgumentCompleter {

    public static List<String> getCompletions(List<String> suggestions, String argSoFar) {
        if (!argSoFar.equalsIgnoreCase("")) {
            List<String> completions = new ArrayList<String>();
            StringUtil.copyPartialMatches(argSoFar, suggestions, completions);
            Collections.sort(completions);
            if (completions.size() == 1) {
                if (completions.get(0).equalsIgnoreCase(argSoFar)) {
                    completions.remove(0);
                }
            }
            return completions;
        } else {
            return suggestions;
        }
    }

}
