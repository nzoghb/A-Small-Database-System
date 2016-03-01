package db61b;
import org.junit.Test;
import static org.junit.Assert.*;

public class Tests {

    @Test
    public void testRow() {
        Row row = new Row(new String[]{"blah, ", "bleh ", "bloh."});
        Row row1 = new Row(new String[]{"help! ", "me"});
        assertEquals(3, row.size());
        assertEquals("bloh.", row.get(2));
        assertEquals(true, row.equals(row));
        assertEquals(false, row.equals(row1));
    }

    @Test
    public void testTable() {
        String[] titles = {"Pop", "Rock", "HipHop"};
        Table table = new Table("artists", titles);
        String[] wannabees = {"Lady Gaga", "Oasis", "Meek Mill"};
        Row first = new Row(wannabees);
        assertEquals(3, table.numColumns());
        assertEquals("artists", table.name());
        assertEquals(-1, table.columnIndex("country"));
        assertEquals(2, table.columnIndex("HipHop"));
        assertEquals(0, table.size());
        table.add(first);
        assertEquals(1, table.size());
        assertEquals(false, table.add(first));
    }

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(Tests.class));
    }
}
