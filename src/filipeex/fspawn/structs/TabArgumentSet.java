package filipeex.fspawn.structs;

import java.util.ArrayList;
import java.util.List;

public class TabArgumentSet {

    public ArrayList<TabArgument> tabArguments = new ArrayList<TabArgument>();

    public TabArgumentSet(ArrayList<String> strings) {

        for (String s : strings)
            tabArguments.add(new TabArgument(s));

    }

    public TabArgumentSet(String[] args) {

        for (String s : args) {
            tabArguments.add(new TabArgument(s));
        }

    }

    public void addArgument(TabArgument arg) {
        tabArguments.add(arg);
    }

    public void addArgument(String arg) {
        tabArguments.add(new TabArgument(arg));
    }

}
