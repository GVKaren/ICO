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
    int iterations=10000;
    int worstValue;
    int worstQueen;
    int worstValue2;
    int worstQueen2;
    int bestValue;
    int bestQueen;
    double[] stadistics= new double [3];//Guarda los valores de: cant de iteraciones, tiempo requerido, mejor aptitud
    long time;
    boolean stop;
    FileWriter fichero = null;
    PrintWriter pw = null;
    
    
    Queen[] population;
    /**
     * Default constructor with default values
     * @param queens Queens quantity
     * @param evaluations Quantity of aptitude calculus
     */
    public EC(int queens, int evaluations)
    {
        iterations=evaluations/2;
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
        worstValue=population[0].fitness;
        worstQueen=bestQueen=0;
        bestValue=population[0].fitness;
        for  (int i = 1; i < size; i++) 
        {
           population[i]=new Queen(k);
           population[i].evaluate();
           
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
         if (b<k)//Si es necesario llenar huecos
         ///AJUSTE PARA BUSCAR EN EL MENOR LUGAR los repetidos
         {
             //hijo busca en papa
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
       
        Queen son, daughter;
        son = new Queen (sonBase, k);
        daughter= new Queen(daughterBase, k);
        Mutate(son);
        son.evaluate();
        if (son.fitness>worstValue)
        {
            population[worstQueen]=son;
            worstValue=son.fitness;

        }
        else if(son.fitness>worstValue2)
        {
            population[worstQueen2]=son;
            worstValue2=son.fitness;
        }
        Mutate(daughter);
        daughter.evaluate();
        if (daughter.fitness>worstValue)
        {
            population[worstQueen]=daughter;

        }
        else if(daughter.fitness>worstValue2)
            population[worstQueen2]=daughter;
        
         
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
           queen.Mutate();
           
           
        }
    }
    public void Selection()
    {
        //Se prepara el arreglo de los elegidos
        Queen[] Selected = new Queen[randSel];
        Random random = new Random();
       // segmentedSelection(Selected);
        Selected = totalyRandomSelection(Selected);
        /*System.out.print("Best");
        System.out.println(bestValue);
        System.out.print("Worst");
        System.out.println(worstValue);
        System.out.println(Arrays.toString(population[bestQueen].board));*/
        Queen  Mom,Dad;
        Mom=Selected[0];
        Dad=Selected[1];
        if (random.nextFloat()<=rP)
            Offsring(Mom,Dad);
        
    }

    public void Evaluate()
    {
        for  (int i = 0; i < size; i++) 
        {
           if (population[i].fitness<worstValue)
           {
               worstValue2=worstValue;
               worstQueen2=worstQueen;
               worstValue=population[i].fitness;
               worstQueen=i;
           }else if (population[i].fitness<worstValue2)
           {
               worstValue2=population[i].fitness;
               worstQueen2=i;
           }else
           if (population[i].fitness>bestValue)
           {
               bestValue=population[i].fitness;
               bestQueen=i;
               
                   

           }
        }
        
    }
    public void Iterate()
    {
        long time_start, time_end;
        
        int needed=iterations;
        time_start = System.currentTimeMillis();
        for(int i =0; i<iterations; i++)
        {
            Selection();
            Evaluate();
            if (bestValue==0)
            {
                needed=i;
                break;
            }

        }
        time_end = System.currentTimeMillis();
             time= time_end - time_start;

        stadistics[0]=needed;
        stadistics[1]=time;
        
        
    }
    
    public double[] Start()
    {
       Initiation();
       Iterate();
       Evaluate();
       stadistics[2]=bestValue;
       System.out.print("Best of all:   ");
       
        System.out.println(Arrays.toString(population[bestQueen].board));
        System.out.print("fitness:   ");
       
        System.out.println((population[bestQueen].fitness));
       
        return stadistics;
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
    private Queen[] totalyRandomSelection(Queen[] Selected) 
    {
        Random random = new Random();
         int chosen; //Inidice del individuo seleecionado
        //Pueden existir repetidos
        Queen aux;

        for (int i=0; i<randSel; i++)
        {
            chosen= random.nextInt(size);
            Selected[i]=population[chosen];
            if (i>0 && Selected[i].fitness>Selected[0].fitness)
            {
                if (Selected[i].fitness<-4)
                    i=i;
                
                aux= Selected[1];
                if (i>1)
                {
                    Selected[1]=Selected[0];
                    Selected[0]=Selected[i];
                    Selected[i]=aux;
                }
                else
                {
                    Selected[1]=Selected[0];
                    Selected[0]=aux;
                }
                
            }   
            else if (i>0 && Selected[i].fitness>Selected[1].fitness)
            {
                
                aux= Selected[1];
                Selected[1]=Selected[i];
                Selected[i]=aux;
            }   
        }

            
        return Selected;
    }
    private void segmentedSelection(Queen[] Selected) 
    {
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
    }
   
}
