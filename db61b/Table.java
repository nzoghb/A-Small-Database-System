package db61b;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import static db61b.Utils.*;

/** A single table in a database.
 *  @author Nicolas Zoghb
 */
class Table implements Iterable<Row> {
    /** A new Table named NAME whose columns are give by COLUMNTITLES,
     *  which must be distinct (else exception thrown). */
    Table(String name, String[] columnTitles) {
        _name = name;
        for (int i = 0; i < columnTitles.length; i += 1) {
            for (int j = i + 1; j < columnTitles.length; j += 1) {
                if (columnTitles[i].equals(columnTitles[j])) {
                    throw error("Table contains duplicate");
                }
            }
        }
        _titles = columnTitles;
    }

    /** A new Table named NAME whose column names are give by COLUMNTITLES. */
    Table(String name, List<String> columnTitles) {
        this(name, columnTitles.toArray(new String[columnTitles.size()]));
    }

    /** Return the number of columns in this table. */
    int numColumns() {
        return _titles.length;
    }

    /** Returns my name. */
    String name() {
        return _name;
    }

    /** Returns a TableIterator over my rows in an unspecified order. */
    TableIterator tableIterator() {
        return new TableIterator(this);
    }

    /** Returns an iterator that returns my rows in an unspecfied order. */
    @Override
    public Iterator<Row> iterator() {
        return _rows.iterator();
    }

    /** Return the title of the Kth column.  Requires 0 <= K < columns(). */
    String title(int k) {
        return _titles[k];
    }

    /** Return the number of the column whose title is TITLE, or -1 if
     *  there isn't one. */
    int columnIndex(String title) {
        if (Arrays.asList(_titles).contains(title)) {
            int i = 0;
            while (_titles[i] != title) {
                i += 1;
            }
            return i;
        }
        return -1;
    }

    /** Return the number of Rows in this table. */
    int size() {
        return _rows.size();
    }

    /** Add ROW to THIS if no equal row already exists.  Return true if anything
     *  was added, false otherwise. */
    boolean add(Row row) {
        if ((!(_rows.contains(row)))) {
            _rows.add(row);
            return true;
        }
        return false;
    }

    /** Read the contents of the file NAME.db, and return as a Table.
     *  Format errors in the .db file cause a DBException. */
    static Table readTable(String name) {
        BufferedReader input;
        Table table;
        input = null;
        table = null;
        try {
            input = new BufferedReader(new FileReader(name + ".db"));
            String header = input.readLine();
            if (header == null) {
                throw error("missing header in DB file");
            }
            String[] columnNames = header.split(",");

            table = new Table(name, columnNames);
            String line = null;
            String[] arr = null;
            line = input.readLine();
            while (line != null) {
                arr = line.split(",");
                line = input.readLine();
                table.add(new Row(arr));
            }

        } catch (FileNotFoundException e) {
            throw error("could not find %s.db", name);
        } catch (IOException e) {
            throw error("problem reading from %s.db", name);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    /* Ignore IOException */
                }
            }
        }
        return table;
    }

    /** Write the contents of TABLE into the file NAME.db. Any I/O errors
     *  cause a DBException. */
    void writeTable(String name) {
        PrintStream output;
        output = null;
        try {
            String titles;
            titles = "";
            output = new PrintStream(name + ".db");

            for (int i = 0; i < numColumns(); i += 1) {
                titles += _titles[i];
                if (i < numColumns() - 1) {
                    titles += ",";
                }
            }
            output.println(titles);
            Iterator<Row> iterRow = _rows.iterator();
            while (iterRow.hasNext()) {
                Row row = iterRow.next();
                for (int i = 0; i < _titles.length; i += 1) {
                    output.print(row.get(i));
                    if (i < _titles.length - 1) {
                        output.print(",");
                    }
                }
                output.println();
            }

        } catch (IOException e) {
            throw error("trouble writing to %s.db", name);
        } finally {
            if (output != null) {
                output.close();
            }
        }
    }

    /** Print my contents on the standard output, separated by spaces
     *  and indented by two spaces. */
    void print() {
        for (Row row : _rows) {
            System.out.print("  ");
            for (int i = 0; i < _titles.length; i += 1) {
                System.out.print(row.get(i) + " ");
            }
            System.out.println();
        }
    }

    /** My name. */
    private final String _name;
    /** My column titles. */
    private String[] _titles;
    /** */
    private HashSet<Row> _rows = new HashSet<Row>();
}

