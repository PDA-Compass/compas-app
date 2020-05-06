import org.testng.Assert;
import org.testng.annotations.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class UtilTest{
    class Util {
        private String acum = "";
        public ArrayList<String> result = new ArrayList<String>();

        public void add(String value) {
            if (acum.length() == 0){
                acum = value;
            }
            else
            {
                acum += value;
            }

            var parts = acum.split("\\|");
            char last = acum.charAt(acum.length() - 1);
            int count = 0;
            if (last == '|') {
                acum = "";
                count = parts.length;
            }
            else
            {
                acum = parts[parts.length - 1];
                count = parts.length - 1;
            }

            for (var i = 0; i < count; i++) {
                String line = parts[i];
                var lines = line.split("\\.");
                if (lines.length == 3) {
                    result.add(parts[i]);
                }
            }
        }
    }

    @Test
    public void FirstTest(){
        Util u = new Util();
        u.add("te|1.1.1|1.5.4|");
        Assert.assertEquals(u.result.size(), 2);
        u.add("|rt.ee.ee|ee|");
        Assert.assertEquals(u.result.size(), 3);
    }

    @Test
    public void SecondTest(){
        Util u = new Util();
        u.add("te||1.1.1||1.5");
        Assert.assertEquals(u.result.size(), 1);
        u.add(".4||rt.ee.ee||ee|");
        Assert.assertEquals(u.result.size(), 3);
    }
}