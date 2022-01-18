package com.mkyong.hashing;

import java.sql.Time;
import java.util.Date;

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
        //System.out.println("Yo");
    }

    public static Long square(Long x) {
        if (x == null) {
            return 0L;
        }
        return x * x;
    }

    private class Timer {

        private Timer INSTANCE;
        private int TICK;
        
        private Double averageFreeCycles;
        private int nextFrame;
        private int delta;

        private Timer(int amt) 
        {
            this.TICK = amt;
        }

        public int time() 
        {
            return (Math.round((new Date()).getTime() * 1000));
        }

        public void setNextFrame() 
        {
            this.nextFrame += this.delta;
        }

        public void updateCycles(int cyclesThisFrame) 
        {
            this.averageFreeCycles = (this.averageFreeCycles + cyclesThisFrame) / 2;
        }

        public void nextFrameReady()
        {
            int cycles = 0;
            while (this.time() < this.nextFrame) { cycles++; }
            
            this.setNextFrame();
            this.updateCycles(cycles);
        }

        public Timer get() 
        {
            return get(60);
        }

        public Timer get(int amt) 
        {
            if (INSTANCE == null) 
            {
                this.INSTANCE = new Timer(amt);
            }

            return INSTANCE;
        }
    }

    public String concatValue(Long val)
    {
        return new StringBuilder(internalValue).append(val).toString();
    }
}
