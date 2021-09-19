package com.test.viewpagerfun.toolbox;

import java.util.Optional;

public class Levenshtein {

    public static int distance(String response, String meaning) {
        //neither string is allowed to be uninitialized
        if (response == null || meaning == null) {
            throw new IllegalArgumentException();
        }

        int response_len = response.length();
        int meaning_len = meaning.length();

        //if a string is empty, it takes n operations to generate the other
        if(response_len == 0) return meaning_len;
        if(meaning_len  == 0) return response_len;

        //creates the 'field'
        int[][] dist = new int[response_len + 1][meaning_len + 1] ;

        // downwards populating the index of the first column
        for(int i=0; i<response_len+1; i++){
            dist[i][0] = i;
        }

        // populating the first row indexes.
        for(int j=0; j<meaning_len+1; j++){
            dist[0][j] = j;
        }

        //leave 0 index as is. loop starts with first letter of the string
        for (int i=1; i<response_len+1; i++){
            //leave 0 index as is. loop starts with first letter of the string
            for(int j=1; j<meaning_len+1; j++){
                //check equality of the letters. 0 on match else 1
                int cost = response.charAt(i-1) == meaning.charAt(j-1) ? 0 : 1;
                /*  assign the lowest value.
                 *  note that Math.min takes can only take two arguments,
                 *  to make it work with three, we wrap two in a separate Math.min
                 */
                dist[i][j] = Math.min(
                        Math.min(dist[i-1][j] + 1,  // deletion
                        dist[i][j-1] + 1            // insertion
                        ),
                        dist[i-1][j-1] + cost       // substitution
                );
                //transpose (change order of letters)
                if(i>1 && j>1 &&
                    response.charAt(i-1) == meaning.charAt(j-2) &&
                    response.charAt(i-2) == meaning.charAt(j-1)){
                    dist[i][j] = Math.min(dist[i][j], dist[i-2][j-2] + cost);
                }

            }
        }
        //the indices which determine the position of the distance
        return dist[response_len][meaning_len];
    }
}
