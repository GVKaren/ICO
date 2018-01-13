/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kreinas;

import static java.lang.Math.abs;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author katthyren
 */
public class Queen {
    int k;
    int[] board;
    int fitness;
    
    
    public  Queen(int k)
    {
        board = new int[k];
        
        for  (int i = 0; i < board.length; i++) 
        {
           board[i]=i+1;
        }
        shuffle(board);

    }
     public  Queen(int[] position)
    {
        board = position;

    }
    
    public void evaluate()
    {
        fitness =0;
        for  (int i = 0; i < board.length-1; i++) 
        {
            for (int j = i+1; j < board.length; j++) 
            {
               if ((j-i)==Math.abs(board[i]-board[j]))
               {
                   fitness++;
               }
            }
        }
        fitness= -fitness;
    }
    /**
     * Evaluate without create a queen
     * @param board
     * @return 
     */
    public int evaluate( int[] board)
    {
        int fitness =0;
        for  (int i = 0; i < board.length-1; i++) 
        {
            for (int j = i+1; j < board.length; j++) 
            {
                /*if(Math.abs(board[i]-board[j]) ==1 && Math.abs(j-i)==1)
                    j=j;*/
               if ((j-i)==Math.abs(board[i]-board[j]))
               {
                   fitness++;
               }
            }
        }
        return -fitness;
    }
    
    static void shuffle(int[] array)
    {
        int index, temp;
    Random random = new Random();
    for (int i = array.length - 1; i > 0; i--)
    {
        index = random.nextInt(i + 1);
        temp = array[index];
        array[index] = array[i];
        array[i] = temp;
    }
    }
    
}
