package aquarium.core;

import aquarium.entities.aquariums.Aquarium;
import aquarium.entities.aquariums.FreshwaterAquarium;
import aquarium.entities.aquariums.SaltwaterAquarium;
import aquarium.entities.decorations.Decoration;
import aquarium.entities.decorations.Ornament;
import aquarium.entities.decorations.Plant;
import aquarium.entities.fish.Fish;
import aquarium.entities.fish.FreshwaterFish;
import aquarium.entities.fish.SaltwaterFish;
import aquarium.repositories.DecorationRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import static aquarium.common.ConstantMessages.*;
import static aquarium.common.ExceptionMessages.*;

public class ControllerImpl implements Controller {
    private DecorationRepository decorations;
    private Collection<Aquarium> aquariums;

    public ControllerImpl() {
        this.decorations = new DecorationRepository();
        this.aquariums = new ArrayList<>();
    }

    @Override
    public String addAquarium(String aquariumType, String aquariumName) {
        Aquarium aquarium;
        if (!aquariumType.equals(FreshwaterAquarium.class.getSimpleName())
                && !aquariumType.equals(SaltwaterAquarium.class.getSimpleName())) {
            throw new NullPointerException(INVALID_AQUARIUM_TYPE);
        }
        if (aquariumType.equals(FreshwaterAquarium.class.getSimpleName())) {
            aquarium = new FreshwaterAquarium(aquariumName);
        } else {
            aquarium = new SaltwaterAquarium(aquariumName);
        }
        aquariums.add(aquarium);
        return String.format(SUCCESSFULLY_ADDED_AQUARIUM_TYPE, aquariumType);
    }

    @Override
    public String addDecoration(String type) {
        Decoration decoration;
        if (!type.equals(Ornament.class.getSimpleName())
                && !type.equals(Plant.class.getSimpleName())) {
            throw new IllegalArgumentException(INVALID_DECORATION_TYPE);
        }
        if (type.equals(Ornament.class.getSimpleName())) {
            decoration = new Ornament();
        } else {
            decoration = new Plant();
        }
        decorations.add(decoration);
        return String.format(SUCCESSFULLY_ADDED_DECORATION_TYPE, type);
    }

    @Override
    public String insertDecoration(String aquariumName, String decorationType) {
        Aquarium aquarium = aquariums.stream().filter(a -> a.getName().equals(aquariumName)).findFirst()
                .orElse(null);
        Decoration decoration = decorations.findByType(decorationType);
        if (decoration == null) {
            throw new IllegalArgumentException(String.format(NO_DECORATION_FOUND, decorationType));
        }
        decorations.remove(decoration);
        aquarium.addDecoration(decoration);
        return String.format(SUCCESSFULLY_ADDED_DECORATION_IN_AQUARIUM, decorationType, aquariumName);
    }

    @Override
    public String addFish(String aquariumName, String fishType, String fishName, String fishSpecies, double price) {
        Aquarium aquarium = aquariums.stream().filter(a -> a.getName().equals(aquariumName)).findFirst()
                .orElse(null);
        Fish fish;
        if (!fishType.equals(FreshwaterFish.class.getSimpleName())
                && !fishType.equals(SaltwaterFish.class.getSimpleName())) {
            throw new IllegalArgumentException(INVALID_FISH_TYPE);
        }
        if (fishType.equals(FreshwaterFish.class.getSimpleName())) {
            fish = new FreshwaterFish(fishName, fishSpecies, price);
        } else {
            fish = new SaltwaterFish(fishName, fishSpecies, price);
        }
        try {
            aquarium.addFish(fish);
        } catch (IllegalArgumentException exception) {
            System.out.println(exception.getMessage());
        }

        return String.format(SUCCESSFULLY_ADDED_FISH_IN_AQUARIUM, fishType, aquariumName);
    }


    @Override
    public String feedFish(String aquariumName) {
        Aquarium aquarium = aquariums.stream().filter(a -> a.getName().equals(aquariumName)).findFirst()
                .orElse(null);
        aquarium.feed();
        return String.format(FISH_FED, aquarium.getFish().size());
    }

    @Override
    public String calculateValue(String aquariumName) {
        Aquarium aquarium = aquariums.stream().filter(a -> a.getName().equals(aquariumName)).findFirst()
                .orElse(null);
        double fishPrice = aquarium.getFish().stream().mapToDouble(Fish::getPrice).sum();
        double decorationPrice = aquarium.getDecorations().stream()
                .mapToDouble(Decoration::getPrice).sum();
        return String.format(VALUE_AQUARIUM, aquariumName, fishPrice+decorationPrice);
    }

    @Override
    public String report() {
        String report = aquariums.stream().map(Aquarium::getInfo)
                .collect(Collectors.joining(System.lineSeparator()));
        return report;

    }
}
