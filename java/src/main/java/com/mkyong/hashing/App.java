package com.mkyong.hashing;

public class App {

    private String internalValue;

    public App(String init) {
        this.internalValue = init;
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Please provide an input!");
            System.exit(0);
        }
        System.out.println(args[0]);
    }

    public static Long square(Long x) {
        if (x == null) {
            return 0L;
        }
        return x * x;
    }

    public String concatValue(Long val){
        return new StringBuilder(internalValue).append(val).toString();
    }
}
