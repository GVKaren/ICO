/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kreinas;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author katthyren
 */
public class EC {
    
    
    double rP=1;     //Probabilidad de recombinacion
    double mP=.8;    //Probabilidad de mutacion
    int size=100;    //Tamano de la poblacion
    int offspring=2; //Numero de hijos
    int randSel=5;   //Cantidad de elementos aleatorios
    int k=8;
    int worstValue;
    int worstQueen;
    int worstValue2;
    int worstQueen2;
    int bestValue;
    int bestQueen;
    FileWriter fichero = null;
    PrintWriter pw = null;
    
    
    Queen[] population;
    /**
     * Default constructor with default values
     * @param queens Queens quantity
     */
    public EC(int queens)
    {
        k=queens;
        
    }
    
    /**
     * 
     * @param r recombination probability
     * @param m mutation probability
     * @param s population size
     * @param o offspring size
     * @param k Queens quantity
     */
    public EC(double r, double m, int s,int o, int k)
    {
        rP=r;
        mP=m;
        size=s;
        offspring=o;
        
       
    }
    
    public Queen Evolutionary()
    {
        Initiation();
        
        return new Queen(1);
    }
    
    public void Initiation()
    {
        population = new Queen[size];
        population[0]=new Queen(k);
        population[0].evaluate();
        worstValue=population[0].fitness;
        worstQueen=bestQueen=0;
        bestValue=population[0].fitness;
        for  (int i = 1; i < size; i++) 
        {
           population[i]=new Queen(k);
          /* population[i].evaluate();
           if (population[i].fitness<worstValue)
           {
               worstValue=population[i].fitness;
               worstQueen=i;
           }
           else if (population[i].fitness>bestValue)
           {
               bestValue=population[i].fitness;
               bestQueen=i;
           }*/
           
        }
        Evaluate();
    }
    public void Recombine(Queen Mom, Queen Dad)
    {
         Random random = new Random(); 
         int pivot=random.nextInt(k-2)+1;
         int[] sonBase= new int[k];
         int[] daughterBase = new int[k];
         
         for (int i=0; i<pivot;i++)
         {
             sonBase[i]=Mom.board[i];
             daughterBase[i]=Dad.board[i];
         }
         
         int value=0;
         
         int g=pivot;//Reinas colocadas en la hija
         
         int b=pivot;//Reinas colocados en el hijo
         for (int i=pivot; i<k;i++)
         {
             value = Dad.board[i];
             //Debido a que la base fue hecha con la mama, el resto se toman del padre
             if ( !Exist(sonBase,value))
             {
                 sonBase[b]=Dad.board[i];
                 b++;
             }

             value = Mom.board[i];
             if ( !Exist(daughterBase,value))
             {
                 daughterBase[g]=Mom.board[i];
                 g++;
             }
         }
         if (b<k)
         ///AJUSTE PARA BUSCAR EN EL MENOR LUGAR los repetidos
         
//         if (pivot<(k/2))//Buscar en la primera mitad
         {
             //Al hijo le conviene buscar en el papa
             int i=0;
             while (b<k)
             {
                 value=Dad.board[i];
                 if ( !Exist(sonBase,value))
                 {
                    sonBase[b]=Dad.board[i];
                    b++;
                    
                 }
                 i++;
             }
             //A la hija le conviene buscar en la mama
             i=0;
             while (g<k)
             {
                 value=Mom.board[i];
                 
                 if ( !Exist(daughterBase,value))
                 {
                    daughterBase[g]=Mom.board[i];
                    g++;
                    
                 }
                 i++;
             }
         }
         ///Se selecciona el mejor hijo para que sea el nuevo posible 
         ///miembro de la poblacion
       /* Queen princess = new Queen(k);
        if (princess.evaluate(sonBase)>princess.evaluate(daughterBase))
            princess=new Queen(sonBase);
        else
            princess= new Queen(daughterBase);
        princess.evaluate();
        
        /*System.out.println( pivot);
        System.out.println(Arrays.toString(sonBase));
        System.out.println(Arrays.toString(daughterBase));
        System.out.print("   Princess aptitude: ");
        System.out.println(princess.fitness);
        return princess;*/
        Queen son, daughter;
        son = new Queen (sonBase);
        daughter= new Queen(daughterBase);
        Mutate(son);
        son.evaluate();
        if (son.fitness>worstValue2)
        {
            population[worstQueen2]=son;
            //worstValue=princess.fitness;
            //System.out.println("   Princess is better");

        }
        else if(son.fitness>worstValue)
            population[worstQueen]=son;
        Mutate(daughter);
        daughter.evaluate();
        if (daughter.fitness>worstValue2)
        {
            population[worstQueen2]=daughter;

        }
        else if(daughter.fitness>worstValue)
            population[worstQueen]=daughter;
        
         
    }
    boolean Exist(int[] array, int value)
    {
        int i=0;
       while (i<k)
       {
           if (array[i]==value)
               return true;
           i++;
       }
       return false;
    }
    public void Mutate(Queen queen)
    {
        Random random = new Random();
        float  mutate= random.nextFloat();
        if (mutate<mP)
        {
           int position1= random.nextInt(k);
           int position2= random.nextInt(k);
           int temp;
           
           temp=queen.board[position1];
           queen.board[position1]=queen.board[position2];
           queen.board[position2]=temp;
           
           
        }
    }
    public void Selection()
    {
        //Se prepara el arreglo de los elegidos
        Queen[] Selected = new Queen[randSel];
        Random random = new Random();
        int chosen, limit, best1=-(k*k), best2=-(k*k); //Inidice del individuo seleecionado y
                           //el tamano de las secciones
        //Para evitar repetidos, se escogera un aletorio por secciones
        limit= (int)(size/randSel);
        Queen Mom = new Queen(k);
        Queen Dad = new Queen(k);
        for (int i=0; i<randSel; i++)
        {
            chosen= random.nextInt(limit)+limit*i;
            Selected[i]=population[chosen];
            if (Selected[i].fitness>best1)
            {
                best1=Selected[i].fitness;
                Mom=Selected[i];
            }   
            else if (Selected[i].fitness>best2)
            {
                best2=Selected[i].fitness;
                Dad=Selected[i];
            }   
        }
        /*System.out.print("Best");
        System.out.println(bestValue);
        System.out.print("Worst");
        System.out.println(worstValue);
        System.out.println(Arrays.toString(population[bestQueen].board));*/
        
        if (random.nextFloat()<=rP)
            Offsring(Mom,Dad);
        
    }
    public void Evaluate()
    {
        for  (int i = 0; i < size; i++) 
        {
           population[i].evaluate();
           if (population[i].fitness<worstValue2)
           {
               worstValue2=population[i].fitness;
               worstQueen2=i;
           }else if (population[i].fitness<worstValue)
           {
               worstValue=population[i].fitness;
               worstQueen=i;
           }else
           if (population[i].fitness>bestValue)
           {
               bestValue=population[i].fitness;
               bestQueen=i;
               /*System.out.print("Best queen: ");
               System.out.println(Arrays.toString(population[i].board));*/
           }
        }
        
    }
    public void Iterate()
    {
        for(int i =0; i<10000; i++)
        {
            Selection();
            Evaluate();
        }
        
    }
    public void Start()
    {
       Initiation();
       Iterate();
       Evaluate();
       System.out.print("Best of all:   ");
       
        System.out.println(Arrays.toString(population[bestQueen].board));
        System.out.print("fitness:   ");
       
        System.out.println((population[bestQueen].fitness));
       
    }
    /**
     * Creates and mutates a new chest board (princess). If its better, repleace
     * @param Mom
     * @param Dad 
     */
    private void Offsring(Queen Mom, Queen Dad) {
        
       Queen princess;
       Recombine(Mom,Dad);
        

    }
    
}
