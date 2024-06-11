package org.simulation.predatorpreyevolution;

import java.util.ArrayList;

public abstract sealed class Animal extends Entity permits Carnivore, Herbivore {
    protected float directionAngle;

    protected final Specie specie;
    protected final int death_time;
    protected final boolean isMale;

    protected float speed;
    protected float fieldOfView;
    protected float viewArea;

    protected float nextMoveDirectionAngle;
    private int reproductoryTimeOut;
    private static final int bornReproductoryTimeout = 30;
    private static final int regularReproductoryTimeout = 10;

    private int reproductionTime;


    public Animal(Point startingPosition, Specie specie) {
        super(startingPosition);
        Environment.addAnimalToAdditionList(this);
        this.isMale = Math.random() < 0.5;
        this.specie = specie;
        this.death_time = Environment.getTime() + this.specie.getRandomLifeDuration();
        this.speed = this.specie.getStartingSpeed();
        this.fieldOfView = this.specie.getStartingFieldOfView();
        this.viewArea = this.specie.getStartingViewArea();
        this.reproductoryTimeOut = Environment.getTime() + Animal.bornReproductoryTimeout;
        this.directionAngle = (float) Math.random() * Point.FULL_ANGLE;
        this.energy = this.getEnergyUsage() * bornReproductoryTimeout;
        this.reproductionTime = Environment.getTime() + this.specie.getRandomLifeDuration() / 2;
    }
    public Animal(Point startingPosition, Specie specie, float baseSpeed, float baseFieldOfView, float baseViewArea) {
        super(startingPosition);
        Environment.addAnimalToAdditionList(this);
        this.isMale = Math.random() < 0.5;
        this.specie = specie;
        this.death_time = Environment.getTime() + this.specie.getRandomLifeDuration();
        this.speed = this.specie.getEvolvedSpeed(baseSpeed);
        this.fieldOfView = this.specie.getEvolvedFieldOfView(baseFieldOfView);
        this.viewArea = this.specie.getEvolvedViewArea(baseViewArea);
        this.reproductoryTimeOut = Environment.getTime() + 20;
        this.directionAngle = (float) Math.random() * Point.FULL_ANGLE;
        this.energy = this.getEnergyUsage() * bornReproductoryTimeout;
        this.reproductionTime = Environment.getTime() + this.specie.getRandomLifeDuration() / 2;
    }

    public void predictNextMove() {
        ArrayList<Entity> targets = this.getTargets();
        ArrayList<Animal> enemies = this.getEnemies();
        ArrayList<Animal> reproductoryTargets = this.getReproductoryTargets();

        if (targets.isEmpty() && enemies.isEmpty() && reproductoryTargets.isEmpty()) {
            this.nextMoveDirectionAngle = this.directionAngle + ((float) Math.random() * 0.1f - 0.05f + Point.FULL_ANGLE);
            this.nextMoveDirectionAngle %= Point.FULL_ANGLE;
            return;
        }

        double maxScore = calculateScore(this.directionAngle, targets, enemies, reproductoryTargets);

        for (double angle = 0; angle < 2 * Math.PI; angle += Math.PI / 90) {
            double score = calculateScore(angle, targets, enemies, reproductoryTargets);
            if (score > maxScore) {
                maxScore = score;
                this.nextMoveDirectionAngle = (float) angle;
            }
        }
//        if (!targets.isEmpty() || !enemies.isEmpty() || !reproductoryTargets.isEmpty()) {
//            System.out.println("t: " + targets.size() + " e: " + enemies.size() + " r: " + reproductoryTargets.size());
//            System.out.println("b: " + this.nextMoveDirectionAngle + " d: " + maxScore);
//        }
    }

    private double calculateScore(double angle, ArrayList<Entity> targets, ArrayList<Animal> enemies, ArrayList<Animal> reproductoryTargets) {
        double score = 0.0;

        // Calculate the new position if the animal moves in this direction
        float newX = this.position.x + (float) Math.cos(angle) * this.speed;
        float newY = this.position.y + (float) Math.sin(angle) * this.speed;

        // Penalize if new position is out of boundaries
        if (newX < 0 || newX > Environment.getSize().x || newY < 0 || newY > Environment.getSize().y) {
            return Double.NEGATIVE_INFINITY;
        }

        // Score based on targets
        for (Entity target : targets) {
            double distance = target.getPosition().calculateDistance(this.position);
            double alignment = Math.cos(Point.getAngleDifference((float) angle, this.position.getAngle(target.getPosition())));
            double energyGain = target.getConsumptionGainedEnergy() * alignment / distance;
            score += energyGain;
        }

        // Penalty based on enemies
        for (Animal enemy : enemies) {
            double distance = enemy.getPosition().calculateDistance(this.position);
            double alignment = Math.cos(Point.getAngleDifference((float) angle, this.position.getAngle(enemy.getPosition())));
            double danger = enemy.getChanceOfWinning(this) * alignment / distance;
            score -= danger;
        }

        if (this.reproductoryTimeOut - Environment.getTime() > 4 || this.specie.isReproduceByDivision())
            return score;
        // Score based on reproductory targets
        for (Animal reproductoryTarget : reproductoryTargets) {
            double distance = reproductoryTarget.getPosition().calculateDistance(this.position);
            double alignment = Math.cos(Point.getAngleDifference((float) angle, this.position.getAngle(reproductoryTarget.getPosition())));
            double reproductionValue = alignment / distance;
            score += reproductionValue;
        }

        return score;
    }

    @Override
    public void update() {
        this.directionAngle = this.nextMoveDirectionAngle;
        this.move();
        ArrayList<Entity> entitiesInRange = this.getEntitiesInRange();
        if (!this.specie.isReproduceByDivision() && this.isMale && this.reproductoryTimeOut < Environment.getTime())
            for (Entity entity: entitiesInRange)
                if (entity instanceof Animal)
                    if (this.isReproductoryTarget((Animal)entity) )
                        this.reproduce((Animal)entity);
        for (Entity entity: entitiesInRange)
            if (this.isTarget(entity))
                this.eat(entity);
        if (death_time < Environment.getTime()) this.kill();
        if (reproductionTime < Environment.getTime()) reproduceByDivision();
        this.energy -= this.getEnergyUsage();
        if (energy <= 0) this.kill();
    }
    @Override public float getChanceOfWinning(Animal animal) {
        if (this instanceof Herbivore) return 0f;
        if (this instanceof Carnivore && animal instanceof Herbivore) return 1f;
        return this.energy / (this.energy + animal.getEnergy());
    }
    @Override public float getConsumptionGainedEnergy() { return this.energy * 5; }

    protected abstract boolean isTarget(Entity entity);
    protected abstract boolean isEnemy(Animal animal);
    protected boolean isReproductoryTarget(Animal animal) {
        return animal.getSpecie().equals(this.specie) && animal.isMale != this.isMale;
    }
    protected boolean isEntityVisible(Entity entity) {
        return this.position.checkIfVisible(
                entity.getPosition(),
                this.directionAngle,
                this.fieldOfView,
                this.getVisibilityDistance()
        );
    }

    protected abstract ArrayList<Entity> getTargets();
    protected ArrayList<Animal> getEnemies() {
        ArrayList<Animal> enemies = new ArrayList<>();
        for (Animal animal : Environment.getAnimals())
            if (this.isEnemy(animal) && this.isEntityVisible(animal))
                enemies.add(animal);
        return enemies;
    }
    protected ArrayList<Animal> getReproductoryTargets() {
        ArrayList<Animal> targets = new ArrayList<>();
        // if (!this.isMale) return targets;
        for (Animal animal : Environment.getAnimals())
            if (this.isReproductoryTarget(animal) && this.isEntityVisible(animal))
                targets.add(animal);
        return targets;
    }
    protected ArrayList<Entity> getEntitiesInRange() {
        ArrayList<Entity> entities = new ArrayList<>();
        for (Entity entity: Environment.getPlants())
            if (Environment.isAlive(entity) && entity.getPosition().calculateDistanceSquared(this.position) < 25)
                entities.add(entity);
        for (Entity entity: Environment.getAnimals())
            if (Environment.isAlive(entity) && entity.getPosition().calculateDistanceSquared(this.position) < 25)
                entities.add(entity);
        return entities;
    }

    protected float getDirection() {
        return directionAngle;
    }
    protected void eat(Entity target) {
        if (target.getChanceOfWinning(this) < (float) Math.random()) {
            this.energy += target.getConsumptionGainedEnergy();
            target.kill();
        } else {
            target.energy += this.getConsumptionGainedEnergy();
            this.kill();
        }
    }
    protected void reproduce(Animal target) {
        for (int i = this.specie.getReproductionRate(); i > 0; i--) {
            if (this instanceof Carnivore)
                new Carnivore(
                        this.position.getRandomPointInRadius(50),
                        this.specie,
                        (target.speed + this.speed) / 2,
                        (target.fieldOfView + this.fieldOfView) / 2,
                        (target.viewArea + this.viewArea) / 2
                );
            else
                new Herbivore(
                        this.position.getRandomPointInRadius(50),
                        this.specie,
                        (target.speed + this.speed) / 2,
                        (target.fieldOfView + this.fieldOfView) / 2,
                        (target.viewArea + this.viewArea) / 2
                );
            this.reproductoryTimeOut = Environment.getTime() + Animal.regularReproductoryTimeout;
        }
    }
    protected void reproduceByDivision() {
        for (int i = this.specie.getReproductionRate(); i > 0; i--) {
            if (this instanceof Carnivore)
                new Carnivore(
                        this.position.getRandomPointInRadius(50), this.specie, this.speed, this.fieldOfView, this.viewArea
                );
            else
                new Herbivore(
                        this.position.getRandomPointInRadius(50), this.specie, this.speed, this.fieldOfView, this.viewArea
                );
            this.reproductionTime = this.death_time;
        }
    }
    protected void move() {
        this.position.x += (float) Math.cos(this.directionAngle) * this.speed;
        this.position.y += (float) Math.sin(this.directionAngle) * this.speed;
        if (this.position.x < 0) this.position.x += Environment.getSize().x;
        if (this.position.y < 0) this.position.y += Environment.getSize().y;
        if (this.position.x > Environment.getSize().x) this.position.x -= Environment.getSize().x;
        if (this.position.y > Environment.getSize().y) this.position.y -= Environment.getSize().y;
    }
    protected float getEnergyUsage(){
        return (this.speed * this.speed + this.viewArea / 150)/5;
    }
    protected float getVisibilityDistance() {
        return (float) Math.sqrt(2 * this.viewArea / this.fieldOfView);
    }

    @Override
    public void kill() { Environment.addAnimalToRemovalList(this); }

    public Specie getSpecie() { return this.specie; }
}
