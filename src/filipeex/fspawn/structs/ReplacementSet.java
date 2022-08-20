package filipeex.fspawn.structs;

import java.util.ArrayList;

public class ReplacementSet {

    public ArrayList<Replacement> replacements = new ArrayList<Replacement>();

    public ReplacementSet(Replacement[] replacementArray) {

        for (Replacement r : replacementArray)
            replacements.add(r);

    }

    public ReplacementSet(Replacement r) {

        replacements.add(r);

    }

}
