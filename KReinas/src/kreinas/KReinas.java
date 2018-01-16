/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kreinas;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Arrays;

/**
 *
 * @author katthyren
 */
public class KReinas {
       static int k=128;
       static int evaluations=50000;
       static int repeat=30;
       static double[] time= new double[repeat];
       static double[] iterations= new double[repeat];
       static double[] values= new double[repeat];
       static int[][] solutions= new int[repeat][];
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        EC problem= new EC(k, evaluations);
        for (int i=0; i<repeat;i++)
        {
            double[] stats =problem.Start();
            time[i]=stats[1];
            iterations[i]=stats[0];
            values[i]=stats[2];
            solutions[i]=problem.population[problem.bestQueen].board;
        }
        estadisticos( k);
    }
     public static void estadisticos(int k) {

        
                
        FileWriter fichero = null;
        PrintWriter pw = null;
        try
        {
            String name=String.valueOf(k);
            fichero = new FileWriter("experimentos/"+ name+".txt");
            pw = new PrintWriter(fichero);
            double promTime=0;
            double varianzaT = 0;
            double desviacionT;
            double promIt=0;
            double varianzaI=0;
            double desviacionI;
            double promValue=0;
            double varianzaV=0;
            double desviacionV;
            pw.println("Resultados\n");
            pw.println("indice: timpo(ms) iteraciones valor  solucion");
            for(int i=0;i<repeat;i++)
            {
                promTime+=time[i];
                promIt+= iterations[i];
                promValue+=values[i];
                pw.println(i +": "+time[i]+",\t"+iterations[i] + ",\t"+values[i]+ ",\t    "+Arrays.toString(solutions[i]));
            }
            promTime/=repeat;
            promIt/=repeat;
            promValue/=repeat;
                for(int i = 0 ; i<repeat; i++)
                {   
                    double rango=0;
                    
                    rango=0;
                    rango = Math.pow(iterations[i]-promIt,2f);
                    varianzaI+= rango;
                    rango=0;
                    rango = Math.pow(values[i]-promValue,2f);
                    varianzaV+= rango;
                    rango=0;
                    rango = Math.pow(time[i]-promTime,2f);
                     varianzaT= rango;
                    
               }
               
               varianzaT/=repeat;
               varianzaI/=repeat;
               varianzaV/=repeat;
               desviacionT = Math.sqrt(varianzaT);
               desviacionI = Math.sqrt(varianzaI);
               desviacionV = Math.sqrt(varianzaV);
            
            pw.println("Promedio:  "+promTime+",\t "+promIt+",\t "+promValue);
            pw.println("Varianza:  "+varianzaT+",\t "+varianzaI +",\t "+varianzaV);
            pw.println("Desviación estandar:  "+desviacionT+",\t "+desviacionI +",\t "+desviacionV);
            
            
            
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        finally {
           try {
           // Nuevamente aprovechamos el finally para 
           // asegurarnos que se cierra el fichero.
           if (null != fichero)
              fichero.close();
           } catch (Exception e2) {
              e2.printStackTrace();
           }
        }
    }
    
}
