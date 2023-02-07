package aquarium.entities.aquariums;

import aquarium.entities.decorations.Decoration;
import aquarium.entities.fish.Fish;

import java.util.Collection;

public class SaltwaterAquarium extends BaseAquarium{
    private static final int CAPACITY = 25;
    public SaltwaterAquarium(String name) {
        super(name, CAPACITY);
    }

}
