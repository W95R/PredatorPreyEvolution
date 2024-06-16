package org.simulation.predatorpreyevolution;

import java.util.ArrayList;

public abstract sealed class Animal extends Entity permits Carnivore, Herbivore {
    protected float directionAngle;

    protected final Specie specie;
    protected final int death_time;
    private int reproductionTime;   // Used only with reproduction by division
    protected final boolean isMale;

    protected final float speed;
    protected final float fieldOfView;
    protected final float viewArea;

    protected float nextMoveDirectionAngle;
    private int reproductoryTimeOut;

    private static final int bornReproductoryTimeout = 30;
    private static final int regularReproductoryTimeout = 10;
    private static final int interactionDistanceSquared = 25;
    private static final int consumptionGainedEnergyMultiplier = 5;


    /**
     * Creates animal with starting speed, fieldOfView and viewArea values from specie
     * @param startingPosition animal's staring position
     * @param specie specie to which animal belongs
     */
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
        this.reproductionTime = Environment.getTime() + this.specie.getRandomLifeDuration() / 3;
    }

    /**
     * Creates animal with speed, fieldOfView and viewArea values mutated based on mutation rate from specie
     * @param startingPosition animal's staring position
     * @param specie specie to which animal belongs
     * @param baseSpeed speed to be mutated
     * @param baseFieldOfView fieldOfView to be mutated
     * @param baseViewArea viewArea to be mutated
     */
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
        this.reproductionTime = Environment.getTime() + this.specie.getRandomLifeDuration() / 3;
    }

    /**
     * Calculates best next move and saves it in nextMoveDirectionAngle
     */
    public void predictNextMove() {
        ArrayList<Entity> targets = this.getTargets();
        ArrayList<Animal> enemies = this.getEnemies();
        ArrayList<Animal> reproductoryTargets = this.getReproductoryTargets();

        if (targets.isEmpty() && enemies.isEmpty() && reproductoryTargets.isEmpty()) {
            this.nextMoveDirectionAngle = Point.getRandomAngleInRange(this.directionAngle, 0.1f);
            return;
        }

        double maxScore = calculateScore(this.directionAngle, targets, enemies, reproductoryTargets);
        for (double angle = 0; angle < Point.FULL_ANGLE; angle += Math.PI / 90) {
            double score = calculateScore(angle, targets, enemies, reproductoryTargets);
            if (score < maxScore) continue;
            maxScore = score;
            this.nextMoveDirectionAngle = (float) angle;
        }
    }

    /**
     * Calculates score for given angle based on visible entities
     * @param angle angle tested
     * @param targets visible targets
     * @param enemies visible enemies
     * @param reproductoryTargets visible reproductory targets
     * @return calculated score
     */
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

    /**
     * Updates animal position and energy, eats targets in range, reproduces, and kills animal if needed
     */
    @Override
    public void update() {
        this.directionAngle = this.nextMoveDirectionAngle;
        this.energy -= this.getEnergyUsage();
        this.move();

        ArrayList<Entity> entitiesInRange = this.getEntitiesInRange();
        this.reproductionCycle(entitiesInRange);
        this.eatingCycle(entitiesInRange);

        if (death_time < Environment.getTime()) this.kill();
        if (energy <= 0) this.kill();
    }
    @Override public float getChanceOfWinning(Animal animal) {
        if (this instanceof Herbivore) return 0f;
        if (this instanceof Carnivore && animal instanceof Herbivore) return 1f;
        return this.energy / (this.energy + animal.getEnergy());
    }
    @Override public float getConsumptionGainedEnergy() { return this.energy * Animal.consumptionGainedEnergyMultiplier; }

    /**
     * Checks if given entity is a possible target (is consumable by this animal)
     * @param entity checked entity
     * @return is entity a target
     */
    protected abstract boolean isTarget(Entity entity);
    /**
     * Checks if given entity is an enemy for this animal
     * @param animal checked animal
     * @return is animal an enemy
     */
    protected abstract boolean isEnemy(Animal animal);
    /**
     * Checks if given entity is a possible reproductory target for this animal
     * @param animal checked animal
     * @return is animal a reproductory target
     */
    protected boolean isReproductoryTarget(Animal animal) {
        if (this.specie.isReproduceByDivision()) return false;
        return animal.getSpecie().equals(this.specie) && animal.isMale != this.isMale;
    }
    /**
     * Checks if entity is visible by this animal
     * @param entity checked entity
     * @return is visible
     */
    protected boolean isEntityVisible(Entity entity) {
        return this.position.checkIfVisible(
                entity.getPosition(),
                this.directionAngle,
                this.fieldOfView,
                this.getVisibilityDistance()
        );
    }

    /**
     * Returns list of visible targets (entities consumable by this animal)
     * @return list of targets
     */
    protected abstract ArrayList<Entity> getTargets();
    /**
     * Returns list of visible enemies
     * @return list of visible enemies
     */
    protected ArrayList<Animal> getEnemies() {
        ArrayList<Animal> enemies = new ArrayList<>();
        for (Animal animal : Environment.getAnimals())
            if (this.isEnemy(animal) && this.isEntityVisible(animal))
                enemies.add(animal);
        return enemies;
    }
    /**
     * Returns list of visible reproductory targets
     * @return list of visible reproductory targets
     */
    protected ArrayList<Animal> getReproductoryTargets() {
        ArrayList<Animal> targets = new ArrayList<>();
        for (Animal animal : Environment.getAnimals())
            if (this.isReproductoryTarget(animal) && this.isEntityVisible(animal))
                targets.add(animal);
        return targets;
    }
    /**
     * Get entities in consumption/reproduction range
     * @return list of entities
     */
    protected ArrayList<Entity> getEntitiesInRange() {
        ArrayList<Entity> entities = new ArrayList<>();
        for (Entity entity: Environment.getPlants())
            if (Environment.isAlive(entity) && entity.getPosition().calculateDistanceSquared(this.position) < Animal.interactionDistanceSquared)
                entities.add(entity);
        for (Entity entity: Environment.getAnimals())
            if (Environment.isAlive(entity) && entity.getPosition().calculateDistanceSquared(this.position) < Animal.interactionDistanceSquared)
                entities.add(entity);
        return entities;
    }

    /**
     * Eats consumable entities from entities in range
     * @param entitiesInRange entities in range
     */
    protected void eatingCycle(ArrayList<Entity> entitiesInRange) {
        for (Entity entity: entitiesInRange)
            if (this.isTarget(entity))
                this.eat(entity);
    }
    /**
     * Eats provided entity of dies trying
     * @param target entity to be eaten
     */
    protected void eat(Entity target) {
        if (target.getChanceOfWinning(this) < (float) Math.random()) {
            this.energy += target.getConsumptionGainedEnergy();
            target.kill();
        } else {
            target.energy += this.getConsumptionGainedEnergy();
            this.kill();
        }
    }

    /**
     * Reproduces either with reproductory targets in range or by division
     * @param entitiesInRange entities in range
     */
    protected void reproductionCycle(ArrayList<Entity> entitiesInRange) {
        if (this.specie.isReproduceByDivision()) {
            if (reproductionTime < Environment.getTime())
                reproduceByDivision();
        }
        else {
            if(!this.isMale || this.reproductoryTimeOut > Environment.getTime()) return;
            for (Entity entity: entitiesInRange)
                if (entity instanceof Animal)
                    if (this.isReproductoryTarget((Animal)entity) )
                        this.reproduce((Animal)entity);
        }
    }
    /**
     * Reproduces with target animal
     * @param target reproductory target
     */
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
        }
        this.reproductoryTimeOut = Environment.getTime() + Animal.regularReproductoryTimeout;
    }
    /**
     * Reproduces by division
     */
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

    /**
     * Moves animal based on current direction angle
     */
    protected void move() {
        this.position.x += (float) Math.cos(this.directionAngle) * this.speed;
        this.position.y += (float) Math.sin(this.directionAngle) * this.speed;
        if (this.position.x < 0) this.position.x += Environment.getSize().x;
        if (this.position.y < 0) this.position.y += Environment.getSize().y;
        if (this.position.x > Environment.getSize().x) this.position.x -= Environment.getSize().x;
        if (this.position.y > Environment.getSize().y) this.position.y -= Environment.getSize().y;
    }

    /**
     * Returns energy usage per simulation frame
     * @return energy usage per simulation frame
     */
    protected float getEnergyUsage(){
        return (this.speed * this.speed + this.viewArea / 150)/5;
    }

    /**
     * Calculates visibility distance based on animal's characteristics
     * @return visibility distance
     */
    protected float getVisibilityDistance() {
        return (float) Math.sqrt(2 * this.viewArea / this.fieldOfView);
    }

    @Override
    public void kill() { Environment.addAnimalToRemovalList(this); }

    /**
     * Returns specie with which this animal is associated with
     * @return specie
     */
    public Specie getSpecie() { return this.specie; }
}
