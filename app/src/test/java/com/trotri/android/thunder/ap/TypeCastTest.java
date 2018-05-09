package com.trotri.android.thunder.ap;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * TypeCastTest class file
 * 测试类型转换类
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: TypeCastTest.java 1 2017-03-03 10:00:06Z huan.song $
 * @since 1.0
 */
public class TypeCastTest {
    @Test
    public void toInt() throws Exception {
        assertEquals(11, TypeCast.toInt(11, -1));
        assertEquals(-11, TypeCast.toInt(-11, -1));
        assertEquals(11, TypeCast.toInt("11", -1));
        assertEquals(11, TypeCast.toInt("  11  ", -1));
        assertEquals(-11, TypeCast.toInt("-11", -1));
        assertEquals(-11, TypeCast.toInt("  -11  ", -1));
        assertEquals(11, TypeCast.toInt(11.11, -1));
        assertEquals(-11, TypeCast.toInt(-11.11, -1));
        assertEquals(11, TypeCast.toInt("11.11", -1));
        assertEquals(-11, TypeCast.toInt("-11.11", -1));
        assertEquals(-1, TypeCast.toInt(null, -1)); // 注意，结果：-1，非：0
        assertEquals(-1, TypeCast.toInt("", -1)); // 注意，结果：-1，非：0
        assertEquals(-1, TypeCast.toInt("  ", -1)); // 注意，结果：-1，非：0
        assertEquals(-1, TypeCast.toInt("abc", -1));
    }

    @Test
    public void toLong() throws Exception {
        assertEquals(11, TypeCast.toLong(11, -1));
        assertEquals(-11, TypeCast.toLong(-11, -1));
        assertEquals(11, TypeCast.toLong("11", -1));
        assertEquals(11, TypeCast.toLong("  11  ", -1));
        assertEquals(-11, TypeCast.toLong("-11", -1));
        assertEquals(-11, TypeCast.toLong("  -11  ", -1));
        assertEquals(11, TypeCast.toLong(11.11, -1));
        assertEquals(-11, TypeCast.toLong(-11.11, -1));
        assertEquals(11, TypeCast.toLong("11.11", -1));
        assertEquals(-11, TypeCast.toLong("-11.11", -1));
        assertEquals(-1, TypeCast.toLong(null, -1)); // 注意，结果：-1，非：0
        assertEquals(-1, TypeCast.toLong("", -1)); // 注意，结果：-1，非：0
        assertEquals(-1, TypeCast.toLong("  ", -1)); // 注意，结果：-1，非：0
        assertEquals(-1, TypeCast.toLong("abc", -1));
    }

    @Test
    public void toFloat() throws Exception {
        assertEquals(11f, TypeCast.toFloat(11, -1.0f), 0);
        assertEquals(-11f, TypeCast.toFloat(-11, -1.0f), 0);
        assertEquals(11.11f, TypeCast.toFloat(11.11, -1.0f), 0);
        assertEquals(-11.11f, TypeCast.toFloat(-11.11, -1.0f), 0);
        assertEquals(11.11f, TypeCast.toFloat("11.11", -1.0f), 0);
        assertEquals(11.11f, TypeCast.toFloat("  11.11  ", -1.0f), 0);
        assertEquals(-11.11f, TypeCast.toFloat("-11.11", -1.0f), 0);
        assertEquals(-11.11f, TypeCast.toFloat("  -11.11  ", -1.0f), 0);
        assertEquals(-1.0f, TypeCast.toFloat(null, -1.0f), 0); // 注意，结果：-1.0f，非：0
        assertEquals(-1.0f, TypeCast.toFloat("", -1.0f), 0); // 注意，结果：-1.0f，非：0
        assertEquals(-1.0f, TypeCast.toFloat("  ", -1.0f), 0); // 注意，结果：-1.0f，非：0
        assertEquals(-1.0f, TypeCast.toFloat("abc", -1.0f), 0);
    }

    @Test
    public void toDouble() throws Exception {
        assertEquals(11, TypeCast.toDouble(11, -1.0), 0);
        assertEquals(-11, TypeCast.toDouble(-11, -1.0), 0);
        assertEquals(11.11, TypeCast.toDouble(11.11, -1.0), 0);
        assertEquals(-11.11, TypeCast.toDouble(-11.11, -1.0), 0);
        assertEquals(11.11, TypeCast.toDouble("11.11", -1.0), 0);
        assertEquals(11.11, TypeCast.toDouble("  11.11  ", -1.0), 0);
        assertEquals(-11.11, TypeCast.toDouble("-11.11", -1.0), 0);
        assertEquals(-11.11, TypeCast.toDouble("  -11.11  ", -1.0), 0);
        assertEquals(-1.0, TypeCast.toDouble(null, -1.0), 0); // 注意，结果：-1.0，非：0
        assertEquals(-1.0, TypeCast.toDouble("", -1.0), 0); // 注意，结果：-1.0，非：0
        assertEquals(-1.0, TypeCast.toDouble("  ", -1.0), 0); // 注意，结果：-1.0，非：0
        assertEquals(-1.0, TypeCast.toDouble("abc", -1.0), 0);
    }

}
