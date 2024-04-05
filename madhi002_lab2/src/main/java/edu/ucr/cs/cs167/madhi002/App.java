package edu.ucr.cs.cs167.madhi002;

import java.util.Arrays;
import java.util.function.Function;

public class App 
{

    /** Parts I & II functions:
     * public static void printEvenNumbers(int from, int to){
     *    System.out.printf("Printing numbers in the range [%d,%d]\n", from, to);
     *    for(int i = from; i <= to; i++){
     *        if (i%2 == 0){
     *           System.out.println(i);
     *       }
     *   }
     * }

     * public static void printNumbersDivisibleByThree(int from, int to){
     *   System.out.printf("Printing numbers in the range [%d,%d]\n", from, to);
     *   for(int i = from; i <= to; i++){
     *       if (i%3 == 0){
     *           System.out.println(i);
     *       }
     *     }
     *   }

     * public static void printNumbers(int from, int to, int base ){
     *    if (base == 2){
     *       printEvenNumbers(from, to);
     *   }
     *    else if (base == 3){
     *       printNumbersDivisibleByThree(from, to);
     *   }
     *  }

     * public static void main (String[] args){
     *    if(args.length < 3){
     *    System.err.println("Error: At least three parameters expected, from, to, and base.");
     *    System.exit(1);
     *    }

     *    int from = Integer.parseInt(args[0]);
     *    int to = Integer.parseInt(args[1]);
     *    int base = Integer.parseInt(args[2]);
     *    printNumbers(from, to, base);
     * }
     /*

    /*---------------------------------------------------------------------------*/
    /** part III & IV
     * static class IsEven implements Function<Integer, Boolean> {
     *   @Override
     *   public Boolean apply(Integer x) {
     *       return x % 2 == 0;
     *   }
     * }

    * static class IsDivisibleByThree implements Function<Integer, Boolean> {
    *    @Override
    *    public Boolean apply(Integer x) {
    *        return x % 3 == 0;
    *       }
    * }
     *
     *  public static void printNumbers(int from, int to, Function<Integer, Boolean> filter){
     *         System.out.printf("Printing numbers in the range [%d,%d]\n", from, to);
     *         for (int i = from; i <= to; i++){
     *             if (filter.apply(i)){
     *                 System.out.println(i);
     *             }
     *         }
     *     }

    * public static void main (String[] args){
    * if(args.length < 3){
    *    System.err.println("Error: At least three parameters expected, from, to, and base.");
    *    System.exit(1);
    * }

    * int from = Integer.parseInt(args[0]);
    * int to = Integer.parseInt(args[1]);
    * int base = Integer.parseInt(args[2]);


    * Part III test for Q1:
    * boolean result  = new IsEven().apply(5);
    * System.out.println(result);

    * Part IV here:
     *
    * Function<Integer, Boolean> divisibleByFive = new Function<Integer, Boolean>() {
    * @Override
    * public Boolean apply(Integer x) {
    *    return x % 5 == 0;
    *    }
    * };

    * Function<Integer, Boolean> divisibleByTen = x -> x % 10 == 0;

    * Function<Integer, Boolean> filter = null;
    * if (base == 2){
    *     filter = new IsEven();
    * } else if (base == 3){
    *    filter = new IsDivisibleByThree();
    * } else if (base == 5){
    *    filter = divisibleByFive;
    * } else if (base == 10){
    *     filter = divisibleByTen;
    * }

    * printNumbers(from,to,filter);
    * }
     */

    /* ---------------------------------------------------------------------------*/
    /** Part V:
     * public static void main (String[] args){
     *         if(args.length < 3){
     *             System.err.println("Error: At least three parameters expected, from, to, and base.");
     *             System.exit(1);
     *         }
     *
     *         int from = Integer.parseInt(args[0]);
     *         int to = Integer.parseInt(args[1]);
     *         int base = Integer.parseInt(args[2]);
     *
     *         Function<Integer, Boolean> divisibleByBase = x -> x % base == 0;
     *
     *         printNumbers(from, to, divisibleByBase);
     *
     *     }
     */

    /** ---------------------------------------------------------------------------
     * Part VI: */
    public static void printNumbers(int from, int to, Function<Integer, Boolean> filter){
        System.out.printf("Printing numbers in the range [%d,%d]\n", from, to);
        for (int i = from; i <= to; i++){
            if (filter.apply(i)){
                System.out.println(i);
            }
        }
    }

    public static Function<Integer, Boolean> combineWithAnd(Function<Integer, Boolean> ... filters) {
        return and->{
            for(Function<Integer, Boolean> filter : filters){
                if(!filter.apply(and)){
                    return false;
                }
            }
            return true;
        };
    }

    public static Function<Integer, Boolean> combineWithOr(Function<Integer, Boolean> ... filters) {
        return or->{
            for(Function<Integer, Boolean> filter: filters){
                if(filter.apply(or)){
                    return true;
                }
            }
            return false;
        };
    }


    public static void main (String[] args){
        if(args.length < 3){
            System.err.println("Error: At least three parameters expected, from, to, and base.");
            System.exit(1);
        }

        int from = Integer.parseInt(args[0]);
        int to = Integer.parseInt(args[1]);

        String[] baseType = args[2].split("[,v]");
        int[] bases = new int[baseType.length];
        for (int i = 0; i < baseType.length; i++){
            bases[i] = Integer.parseInt(baseType[i]);
        }

        Function<Integer, Boolean>[] filters = new Function[bases.length];
        Function<Integer, Boolean> combinedFilter;

        for (int i = 0; i < baseType.length; i++){
            int base = bases[i];
            filters[i] = x -> x % base == 0;
        }

        boolean usingAnd = args[2].contains(",");
        if(usingAnd){
            combinedFilter = combineWithAnd(filters);
        } else{
            combinedFilter = combineWithOr(filters);
        }

        printNumbers(from, to, combinedFilter);
    }

}
