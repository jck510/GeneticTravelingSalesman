//Joshua Knudsen


//REPORT WHEN TESTING
//The algorithm cycles through 10 times (generations)
//When running this algorithm over 10 times I noticed that the lowest number found was 35
//Even when testing with much larger population sizes and larger generation sizes, I still found 35 to be the best solution the genetic algorithm found
//I left the population size at 1000 because it was a value that would finish running at a reasonable time and found the optimal solution using the genetic algorithm
//In my findings I found that there were less optimal solutions found by the algorithm with smaller population sizes which makes sense because each generation
//provides another possibility for the most optimal individual to be conceived
//In conclusion, I found that the population with 1000 individuals reproducing over 10 generations was able to find the same optimal solution consistently as larger populations


//HOW TO RUN
// In order to run this program, all you need to do is compile and run.
// It may take a few minutes due to the high amounts of computation and memory allocation but the algorithm will finish and work correctly
// NOTE: The higher the population size the longer it takes the finish running. (Could be several minutes depending on if the population size is large enough)
// There is a main method to run the program


//DETAILS ABOUT THE FUNCTIONS BELOW (RandomSelection, FitnessFunction, Reproduce (Crossover), Mutation, Encoding Scheme, MakePopulation)

//The random selection function splits each individual into 3 different categories depending on their fitness value. If they are very fit they have a
// 50% chance of being selected, if they are moderately fit they have a 30% chance of being selected. If they are unfit they have a 20% chance of being selected

//The fitness function will evaluate the given individual in the population and return the fitness value that is found by the distance between all of the spaces visited in order + going back to the first position

//The crossover function I use in this program is called (Reproduce to follow the pseudocode of the genetic algorithm from the book
//This crossover function is inspired off of the example/suggestion provided by Professor Stalica in the assignment,
//However I modified it to use both parents and even choose the subset from either one of the parents randomly at a random position in their encoding
//if the parent who is dominant is chosen, they will have more representation over the nondominant parent
//the nondominant parent will have the 3 indexes from their subset represented in the child

//The mutation function is called Mutate and it is a small probability that it will occur (1 in 200 chance in this case by the way I implemented)
//When the mutation function is called it will swap two random elements in the child's encoding
//so while the mutation isnt severe it affects the child's fitness function by swapping two elements
// the severity can depend on which random elements get swapped as it can make the fitness value for that child a bit bigger

//The encoding scheme for making the initial population can be found in the MakePopulation method
//This method has a helper function which takes the previous individual and swaps two of their values to make them different from the previous individual
//I made the population size 1000

//Genetic Algorithm Pseudocode pasted below

/*

function GENETIC-ALGORITHM(population,FITNESS-FN) returns an individual
 inputs: population, a set of individuals
    FITNESS-FN, a function that measures the fitness of an individual

 repeat
   new_population ← empty set
   for i = 1 to SIZE(population) do
     x ← RANDOM-SELECTION(population,FITNESS-FN)
     y ← RANDOM-SELECTION(population,FITNESS-FN)
     child ← REPRODUCE(x,y)
     if (small random probability) then child ← MUTATE(child)
     add child to new_population
   population ← new_population
 until some individual is fit enough, or enough time has elapsed
 return the best individual in population, according to FITNESS-FN




 */

import java.util.Vector;

public class GeneticTravelingSalesman {

    public static void main(String[] args){

        GeneticTravelingSalesman salesManProblem = new GeneticTravelingSalesman(); //creates a new object of the genetictravelingsalesman class in order to use the methods


        Vector<Vector> population = salesManProblem.MakePopulation(); //makes the population to pass into the Genetic Algorithm

        System.out.println("Population Size: " + population.size()); //prints the population size

        System.out.println("Generating Best Solution... This may take a few moments!"); //will display so the grader doesn't assume my program is broken :)

        Vector<Integer> bestIndividual = salesManProblem.GENETICALGORITHM(population);  //will get the best individual returned from the function

        System.out.println("Best Solution Found by the Genetic Algorithm: ");

        System.out.println("Starting at city " + bestIndividual.firstElement() + ":");

        String order = ""; //empty string to hold the order

        for(int i = 0; i < bestIndividual.size(); i++){
            order = order + bestIndividual.get(i) + " --> ";
        }

        order = order + bestIndividual.firstElement();

        System.out.println("Order: " + order); //prints out the order in the same format as the program description

        for(int j = 0; j < bestIndividual.size() - 1; j++){
            System.out.println(bestIndividual.get(j) + " to " + bestIndividual.get(j+1) + " is " + salesManProblem.distanceBetween(bestIndividual.get(j),bestIndividual.get(j+1)) + " units");
        }
        System.out.println(bestIndividual.lastElement() + " to " + bestIndividual.firstElement() + " is " + salesManProblem.distanceBetween(bestIndividual.lastElement(),bestIndividual.firstElement()) + " units");

        System.out.println("For a total distance of " + salesManProblem.FitnessFunction(bestIndividual) + " units");

    }


    // each individual will be a vector of numbers
    public Vector<Integer> GENETICALGORITHM(Vector<Vector> population){
        int time = 0; //holds the amount of time run (in generations) For this assignment I have specified that this will run for 10 generations

        Vector<Integer> bestIndividual; //holds the best individual with the best fitness value

        while(time < 10){ //while less than 10 generations (pseudocode says or when some individual is fit enough but I decided to go based off of time so the algorithm runs 10 times as stated in the program rubric and description, otherwise I could have set the cap to 35 to possibly find the solution faster)
            Vector<Vector> newPopulation = new Vector<Vector>(); //empty set

            for(int i = 0; i < population.size();i++){
                Vector<Integer> x = RandomSelection(population);
                Vector<Integer> y = RandomSelection(population);

                Vector<Integer> child = Reproduce(x,y); //parents will reproduce (this is where the crossover between the two gets called)
                int mutateNum = (int) (Math.random() * 200 + 1); //picks a random number between one and 200 to determine the probability of a mutation (meaning that there is a .5% chance of a mutation)
                if(mutateNum == 5){ //if the number picked was 5 then a mutation occurs
                    Mutate(child); //calls the mutate function
                }
                newPopulation.add(child); //adds the child to the new population

            }

            population = newPopulation; //the population becomes the newPopulation


            time++; //time gets incremented
        }

        System.out.println("Number of Generations: " + time); //displays the number of generations that the algorithm went through

        bestIndividual = FindBestIndividual(population); //will set the best individual according to the updated population after 10 generations

        return bestIndividual; //returns the bestIndividual found


    }

    //this function will make the initial population
    public Vector<Vector> MakePopulation(){
        Vector<Vector> population = new Vector<Vector>();
        Vector<Integer> firstIndividual = new Vector<>();


        //initializes the first individual with values
        firstIndividual.add(1);
        firstIndividual.add(2);
        firstIndividual.add(3);
        firstIndividual.add(4);
        firstIndividual.add(5);
        firstIndividual.add(6);
        firstIndividual.add(7);
        firstIndividual.add(8);
        firstIndividual.add(9);

        Vector<Integer> currentIndividual = firstIndividual;

        for(int i = 0; i < 1000; i++){ //population size will be 1000
            population.add(currentIndividual); //will add the current individual to the population
            currentIndividual = makePopulationHelper(currentIndividual); //will call the helper on the current individual to change the current individual to someone slightly different from the previous one
        }


        return population;
    }

    //make population helper will take an individual and swap two random elements in the vector in order to help make the initial population
    //new individual will be returned
    //doing this will help make the population unique
    public Vector<Integer> makePopulationHelper(Vector<Integer> currentIndividual){

        Vector<Integer> newIndividual = new Vector<Integer>(currentIndividual); //copies vector of the current individual to the new individual

        int firstIndex = (int) (Math.random() * 9); //random number between 0 and 8
        int secondIndex;

        //will keep generating random numbers if they are equal and will stop when they are not equal
        do{
            secondIndex = (int) (Math.random() * 9); //random number between 0 and 8
        }while(secondIndex == firstIndex);


        newIndividual.set(firstIndex,currentIndividual.get(secondIndex)); //sets the first index with the data of the second index

        newIndividual.set(secondIndex, currentIndividual.get(firstIndex)); //sets the second index with the data from the second index

        return newIndividual; //returns the newIndividual
    }

    public int FitnessFunction(Vector<Integer> individual) {
        int fitnessValue = 0; //fitness value currently set to 0 (will get updated)
        for(int i = 0; i < individual.size()-1; i++){ //will go through every location in the order that is specified by the individual
            fitnessValue = fitnessValue + distanceBetween(individual.get(i),individual.get(i+1));
        }

        fitnessValue = fitnessValue + distanceBetween(individual.lastElement(),individual.firstElement()); //then will add the value to get back to the starting location and account for that in the fitnessvalue

        return fitnessValue; //returns the fitness value of the given individual

    }

    public Vector<Integer> FindBestIndividual(Vector<Vector> population){
        Vector<Integer> bestIndividual = population.get(0); //sets the best individual as the first individual in the population to begin with

        for(int j = 0; j < population.size(); j++){ //goes through every individual in the population
            if(FitnessFunction(population.get(j)) < FitnessFunction(bestIndividual)){
                bestIndividual = population.get(j);
            }
        }
        return bestIndividual;
    }

    //this function returns a child that has had a mutation if it is the case that the probability number lined up with the matching number
    //this mutation function results in a child having two of its values swapped with one another
    public void Mutate(Vector<Integer> child) {
        int firstPosition = (int) (Math.random() * 9); //random number between 0 and 8
        int secondPosition;

        do{
            secondPosition = (int) (Math.random() * 9); // another random number between 0 and 8
        }while(firstPosition != secondPosition);

        int temp = child.get(firstPosition); //holds the child's value in the first position in a temporary storage place
        child.set(firstPosition,child.get(secondPosition)); //sets the child's value at the first position to the second position

        child.set(secondPosition, temp); //sets the child's value at the second position to the value stored from the first position in temp

        //System.out.println("Child has been mutated!"); //print statement to see how many times the children were getting mutated during program execution

    }


    //this is the crossover function where both parents will reproduce and return a child
    public Vector<Integer> Reproduce(Vector<Integer> x, Vector<Integer> y){
        Vector<Integer> newBornChild = new Vector<Integer>();

        Vector<Integer> dominantParent;
        Vector<Integer> nonDominantParent;

        int dominantChoice = (int) (Math.random() * 2); //will select a random number between 0 and 1 in order to determine which subset from which parent to choose from

        if(dominantChoice == 0){ //if the dominantChoice is 0 then x will be the dominant parent else (1) will have y be the dominant parent
            dominantParent = x;
            nonDominantParent = y;
        }
        else{
            dominantParent = y;
            nonDominantParent = x;
        }

        int subSetIndex = (int) (Math.random() * 7); //will pick a random number between 0 and 6 to determine the index where the subset from the nonDominant parent will come from

        Vector<Integer> subSet = new Vector<Integer>(); //this vector holds the subset from the nondominant parent

        for(int i = 0; i < 3; i++){ //goes through the 3 elements found and adds them to the subset
            subSet.add(nonDominantParent.get(subSetIndex + i));
        }

        int dominantParentIndex = 0; //keeps track of the

        while(newBornChild.size() != dominantParent.size()){ //while the newBornChild gets built up to the size of its parent

            if(newBornChild.size() == subSetIndex){
                for(int k = 0; k < subSet.size(); k++){ //for every item in the subset
                    newBornChild.add(subSet.get(k));
                }
            }
            else{
                if(!subSet.contains(dominantParent.get(dominantParentIndex))){ //if the subset does not contain the current index of the dominantParent
                    newBornChild.add(dominantParent.get(dominantParentIndex));
                }
                dominantParentIndex++; //will increment the dominantParentIndex
            }

        }



        return newBornChild; //the newBornChild gets returned

    }



    //this function returns a random individual selected from the population based on their fitness value (the lower the better)
    //In the video explaining the assignment Professor Stalica says that we just need to come up with some way of coming up with a probability that might be the easiest
    //For this function I will be generating every individual from the population's fitness value and placing them in 1 of three categories
    // Unfit, moderately fit, and very fit
    // then the program will have a higher chance of selecting an individual from the moderately fit and very fit categories over the unfit but it is possible to get selected from the unfit
    public Vector<Integer> RandomSelection(Vector<Vector> population) {


        //vectors to hold the 3 categories of individuals (by storing the index of where they fit in the population
        Vector<Integer> unFit = new Vector<Integer>();
        Vector<Integer> moderatelyFit = new Vector<Integer>();
        Vector<Integer> veryFit = new Vector<Integer>();

        for(int i = 0; i < population.size(); i++){
            if(FitnessFunction(population.get(i)) <= 50){
                veryFit.add(i); //will add the index of the current individual from the population to the very fit pool
            }
            else if(FitnessFunction(population.get(i)) <= 80){
                moderatelyFit.add(i); //will add the index of the current individual to the moderately fit pool
            }
            else{
                unFit.add(i); //else will add the index of the current individual to the unfit pool
            }

        }

        int categorySelection = (int) (Math.random() * 10 + 1); //will pick a random number between 1 and 10

        int selectionIndex;
        int individualSelection;

        if(categorySelection >= 6){ //50% chance of getting a very fit individual
            if(veryFit.size() == 0){ //if there are no individuals in the very fit pool it will return a random individual from the population
                selectionIndex = (int) (Math.random() * population.size());
                return population.get(selectionIndex);
            }
            selectionIndex = (int) (Math.random() * veryFit.size()); //will get a random number between 0 and the size of the veryfit pool
            individualSelection = veryFit.get(selectionIndex); //will get the index within the population from the veryfit pool
        }
        else if(categorySelection >= 3){ //30% chance of getting a moderately fit individual
            if(moderatelyFit.size() == 0){ //if there are no individuals that are moderately fit then it will just pull a random individual from the population otherwise it will carry on as normal
                selectionIndex = (int) (Math.random() * population.size());
                return population.get(selectionIndex);
            }
            selectionIndex = (int) (Math.random() * moderatelyFit.size()); //will get a random number between 0 and the size of the moderatelyfit pool
            individualSelection = moderatelyFit.get(selectionIndex); // will get the index within the population from the moderatelyfit pool
        }
        else{ //20% chance of getting an unfit individual
            if(unFit.size() == 0){ //if the unfit pool has no individuals it will pull a random person from the general population
                selectionIndex = (int) (Math.random() * population.size());
                return population.get(selectionIndex);
            }
            selectionIndex = (int) (Math.random() * unFit.size()); //will get a random number between 0 and the size of the unfit pool
            individualSelection = unFit.get(selectionIndex);
        }


        return population.get(individualSelection); //will return the individual selected as long as an individual exists in the category chosen


        //old function from original submission (just took a random individual from population)
        //replaced in order to incorporate fitness function
        /*
        int randomPerson = (int) (Math.random() * population.size()); //will select a random number between 0 and the population size

        return population.get(randomPerson); //will return the random person from the population


         */
    }


    //this function will take in two cities and return the distance between the two cities
    //these values reflect what is on the distance matrix in the program description/instructions
    public int distanceBetween(int locationOne, int locationTwo){

        //break statements are commented out because they are not reachable after the return statement

        switch(locationOne){
            case 1:
                switch(locationTwo){
                    case 1:
                        return 0;
                        //break;
                    case 2:
                        return 2;
                        //break;
                    case 3:
                        return 11;
                        //break;
                    case 4:
                        return 3;
                        //break;
                    case 5:
                        return 18;
                        //break;
                    case 6:
                        return 14;
                        //break;
                    case 7:
                        return 20;
                        //break;
                    case 8:
                        return 12;
                        //break;
                    case 9:
                        return 5;
                        //break;
                }
                //break;
            case 2:
                switch(locationTwo){
                    case 1:
                        return 2;
                        //break;
                    case 2:
                        return 0;
                        //break;
                    case 3:
                        return 13;
                        //break;
                    case 4:
                        return 10;
                        //break;
                    case 5:
                        return 5;
                        //break;
                    case 6:
                        return 3;
                        //break;
                    case 7:
                        return 8;
                        //break;
                    case 8:
                        return 20;
                        //break;
                    case 9:
                        return 17;
                        //break;
                }
            case 3:
                switch(locationTwo){
                    case 1:
                        return 11;
                        //break;
                    case 2:
                        return 13;
                        //break;
                    case 3:
                        return 0;
                        //break;
                    case 4:
                    case 8:
                        return 5; //both case 4 and 8 return 5 so they are merged together
                        //break;
                    case 5:
                        return 19;
                        //break;
                    case 6:
                        return 21;
                        //break;
                    case 7:
                        return 2;
                        //break;
                    case 9:
                        return 8;
                        //break;

                }
            case 4:
                switch(locationTwo){
                    case 1:
                        return 3;
                        //break;
                    case 2:
                        return 10;
                        //break;
                    case 3:
                        return 5;
                        //break;
                    case 4:
                        return 0;
                        //break;
                    case 5:
                        return 6;
                        //break;
                    case 6:
                        return 4;
                        //break;
                    case 7:
                        return 12;
                        //break;
                    case 8:
                        return 15;
                        //break;
                    case 9:
                        return 1;
                        //break;
                }
            case 5:
                switch(locationTwo){
                    case 1:
                        return 18;
                        //break;
                    case 2:
                        return 5;
                        //break;
                    case 3:
                        return 19;
                        //break;
                    case 4:
                    case 7:
                        return 6; //both case 4 and 7 return 6
                        //break;
                    case 5:
                        return 0;
                        //break;
                    case 6:
                        return 12;
                        //break;
                    case 8:
                        return 9;
                        //break;
                    case 9:
                        return 7;
                        //break;
                }
            case 6:
                switch(locationTwo){
                    case 1:
                        return 14;
                        //break;
                    case 2:
                        return 3;
                        //break;
                    case 3:
                        return 21;
                        //break;
                    case 4:
                    case 9:
                        return 4; //both case 4 and 9 return 4
                        //break;
                    case 5:
                        return 12;
                        //break;
                    case 6:
                        return 0;
                        //break;
                    case 7:
                        return 19;
                        //break;
                    case 8:
                        return 7;
                        //break;
                }
            case 7:
                switch(locationTwo){
                    case 1:
                        return 20;
                        //break;
                    case 2:
                        return 8;
                        //break;
                    case 3:
                        return 2;
                        //break;
                    case 4:
                        return 12;
                        //break;
                    case 5:
                        return 6;
                        //break;
                    case 6:
                        return 19;
                        //break;
                    case 7:
                        return 0;
                        //break;
                    case 8:
                        return 21;
                        //break;
                    case 9:
                        return 13;
                        //break;
                }
            case 8:
                switch(locationTwo){
                    case 1:
                        return 12;
                        //break;
                    case 2:
                        return 20;
                        //break;
                    case 3:
                        return 5;
                        //break;
                    case 4:
                        return 15;
                        //break;
                    case 5:
                        return 9;
                        //break;
                    case 6:
                        return 7;
                        //break;
                    case 7:
                        return 21;
                        //break;
                    case 8:
                        return 0;
                        //break;
                    case 9:
                        return 6;
                        //break;
                }
            case 9:
                switch(locationTwo){
                    case 1:
                        return 5;
                        //break;
                    case 2:
                        return 17;
                        //break;
                    case 3:
                        return 8;
                        //break;
                    case 4:
                        return 1;
                        //break;
                    case 5:
                        return 7;
                        //break;
                    case 6:
                        return 4;
                        //break;
                    case 7:
                        return 13;
                        //break;
                    case 8:
                        return 6;
                        //break;
                    case 9:
                        return 0;
                        //break;
                }

        }

        return -1; //this is meant to keep the compiler happy
                    // the program should never get to this case
                    //but in case it does, -1 will serve as an error
                    //message in the case that I use print statements

    }

}
