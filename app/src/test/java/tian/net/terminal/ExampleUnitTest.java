package tian.net.terminal;

import android.util.Log;

import org.junit.Test;

import es.dmoral.prefs.Prefs;
import tian.net.terminal.model.Login;
import tian.net.terminal.util.DateUtil;
import tian.net.terminal.util.StringUtil;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }


    @Test
    public void test() {
        String s = "#";
        System.out.println("str2HexStr: " + StringUtil.str2HexStr(s));

        String h = "31";
        System.out.println("h = " + StringUtil.hexStr2Str(h));


        String vin = "LSVFA49J232037048";
        System.out.println(" Vin Lenght = " + vin.getBytes().length);


        byte[] magic = "##".getBytes();
        System.out.println(" # = " + magic[0] + magic[1]);

        byte mgc = 0x23;

        System.out.println(" mgc = " + mgc);


        byte ff = (byte)0xfe;
        System.out.println(" ff = "  + ff);

        String str = "1234567890";
        byte[] strbb = str.getBytes();
        System.out.println(" 123 = " + strbb[0] + " " + strbb[1]);

        byte[] bytes = new byte[]{35, 35, 7, 0, 1, 0, 0};
        System.out.println(" bytes = " +new String(bytes));
    }


    @Test
    public void testDate() {
        System.out.println(" date = " + DateUtil.getByteDateTime());
    }

    @Test
    public void test2() {
        String dates = " , , , , , , ";
        int crtDate = 170701;
        for(int i=0; i<9; i++) {
            crtDate += 1;
            String temps = crtDate + "," + dates.substring(0, dates.lastIndexOf(","));
            dates = temps;
            System.out.println("-----------------------------");
            System.out.println(dates);
            System.out.println("invalid = " + temps.split(",")[6]);
        }


    }
}