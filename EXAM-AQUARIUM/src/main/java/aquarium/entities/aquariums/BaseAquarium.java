package aquarium.entities.aquariums;

import aquarium.entities.decorations.Decoration;
import aquarium.entities.fish.Fish;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import static aquarium.common.ConstantMessages.*;
import static aquarium.common.ExceptionMessages.*;

public abstract class BaseAquarium implements Aquarium {
    private String name;
    private int capacity;
    private Collection<Decoration> decorations;
    private Collection<Fish> fishes;

    protected BaseAquarium(String name, int capacity) {
        this.setName(name);
        this.capacity = capacity;
        this.decorations = new ArrayList<>();
        this.fishes = new ArrayList<>();
    }

    private void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new NullPointerException(AQUARIUM_NAME_NULL_OR_EMPTY);
        }
        this.name = name;
    }

    @Override
    public int calculateComfort() {
        return decorations.stream().mapToInt(Decoration::getComfort).sum();
    }

    @Override
    public void addFish(Fish fish) {
        if (fishes.size() >= capacity) {
            throw new IllegalStateException(NOT_ENOUGH_CAPACITY);
        }
        String fishWaterType = fish.getClass().getSimpleName().replaceAll("Fish", "");

        if (!this.getClass().getSimpleName().contains(fishWaterType)){
            throw new IllegalStateException(WATER_NOT_SUITABLE);
        }
        this.fishes.add(fish);

    }

    @Override
    public void removeFish(Fish fish) {
        fishes.remove(fish);
    }

    @Override
    public void addDecoration(Decoration decoration) {
       decorations.add(decoration);
    }

    @Override
    public void feed() {
        fishes.forEach(Fish::eat);
    }

    @Override
    public String getInfo() {
        String fishOutput = fishes.isEmpty()
                ? "none"
                : fishes.stream().map(Fish::getName).collect(Collectors.joining(" "));

        return String.format("%s (%s):%n" +
                        "Fish: %s%n" +
                        "Decorations: %d%n" +
                        "Comfort: %d",
                name, this.getClass().getSimpleName(),
                fishOutput,
                decorations.size(),
                calculateComfort());
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Collection<Fish> getFish() {
        return this.fishes;
    }

    @Override
    public Collection<Decoration> getDecorations() {
        return this.decorations;
    }

}
