package com.mkyong.hashing;

import org.junit.Assert;
import org.junit.Test;

public class AppTest {

    private Long INPUT = 4L;

    @Test
    public void testResults() {
        Assert.assertEquals( new Long(16), App.square(INPUT));
    }

}