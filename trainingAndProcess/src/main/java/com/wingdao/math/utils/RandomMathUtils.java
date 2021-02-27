package com.wingdao.math.utils;

import com.wingdao.math.pojo.Condition;
import com.wingdao.math.pojo.Constant;
import com.wingdao.math.pojo.Fraction;
import com.wingdao.math.pojo.MathNode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RandomMathUtils {
    private static ThreadLocalRandom random;

    public static int nextInt(int minIntNum,int maxIntNum) {
        random = ThreadLocalRandom.current();
        return random.nextInt(maxIntNum - minIntNum + 1) + minIntNum;
    }

    public static int nextInt(int bound) {
        random = ThreadLocalRandom.current();
        return random.nextInt(bound);
    }

    //根据整数数量比重随机生成分数或整数节点
    public static MathNode nextNum(Condition condition) {
        random = ThreadLocalRandom.current();
        if (random.nextInt(10) < condition.getIntWeight()) {
            int intNum = nextInt(condition.getMinIntNum(),condition.getMaxIntNum());
            Fraction fra = new Fraction(0, intNum, 1);
            return new MathNode(fra, intNum);
        } else {
            Fraction fra = nextFraction(condition.getMinFraNum(),condition.getMaxFraNum(),condition.getMinDenomNum(),condition.getMaxDenomNum());
            return new MathNode(fra);
        }
    }

    //根据条件生成随机符号节点
    public static MathNode nextSign(Condition condition) {
        random = ThreadLocalRandom.current();
        List<String> signList = new ArrayList<>();
        for (int i = 0; i < condition.getSigns().length; i++) {
            int signCode = condition.getSigns()[i];
            if (signCode == Constant.ADDCODE) {
                signList.add(Constant.ADDITION);
            } else if (signCode == Constant.SUBCODE) {
                signList.add(Constant.SUBTRACTION);
            } else if (signCode == Constant.MULCODE) {
                signList.add(Constant.MULTIPLICATION);
            } else {
                signList.add(Constant.DIVISION);
            }
        }
        String sign = signList.get(random.nextInt(signList.size()));
        return new MathNode(sign);
    }

    public static Fraction nextFraction(int minFraNum, int maxFraNum, int minDenomNum, int maxDenomNum) {
        random = ThreadLocalRandom.current();
        int n = 1;
        int d = 1;
        while (n % d == 0) {
            d = random.nextInt(maxDenomNum - minDenomNum + 1) + minDenomNum;
            n = random.nextInt(maxFraNum*d - minFraNum*d + 1) + minFraNum*d;
        }
        return new Fraction(0,n,d);
    }
}
