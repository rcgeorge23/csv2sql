package uk.co.novinet.csv2sql;

import com.google.devtools.common.options.OptionsParser;
import com.opencsv.CSVReader;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class Csv2Sql {
    private static void printUsage(OptionsParser parser) {
        System.out.println("Usage: java -jar csv2sql.jar OPTIONS");
        System.out.println(parser.describeOptions(Collections.<String, String>emptyMap(), OptionsParser.HelpVerbosity.LONG));
    }

    public static void main(String[] args) throws IOException {
        OptionsParser parser = OptionsParser.newOptionsParser(CommandLineOptions.class);
        parser.parseAndExitUponError(args);
        CommandLineOptions options = parser.getOptions(CommandLineOptions.class);

        if (isBlank(options.csvFilename) || isBlank(options.sqlFilename)) {
            printUsage(parser);
            return;
        }

        FileReader reader = new FileReader(new File(options.csvFilename));

        CSVReader csvReader = new CSVReader(reader);
        String[] row;

        List<ColumnDescriptor> columnDescriptors = null;

        int index = 0;

        StringBuffer buf = new StringBuffer();

        while ((row = csvReader.readNext()) != null) {
            if (index == 0) {
                columnDescriptors = buildColumnDescriptors(row);
                if (options.addCreateStatement) {
                    buf.append(tableCreationSql(columnDescriptors, options.tableName));
                }
            } else {
                String commaSeparatedColumnNames = columnDescriptors.stream().map(columnDescriptor -> "`" + columnDescriptor.getDatabaseColumnName() + "`").collect(Collectors.joining(", "));
                String commaSeparatedValues = Arrays.stream(row).map(value -> "'" + escape(value) + "'").collect(Collectors.joining(", "));
                buf.append(String.format("INSERT INTO `%s` (%s) VALUES (%s);\n", options.tableName, commaSeparatedColumnNames, commaSeparatedValues));
            }

            index++;
        }

        FileUtils.write(new File(options.sqlFilename), buf.toString(), "UTF-8");

        reader.close();
        csvReader.close();
    }

    private static String escape(String value) {
        if (value != null) {
            return value.replaceAll("'", "''").replaceAll("\n", "\\n").replaceAll("\r", "\\r");
        }

        return null;
    }

    private static String tableCreationSql(List<ColumnDescriptor> columnDescriptors, String tableName) throws IOException {
        String sql = "CREATE TABLE IF NOT EXISTS `%s` (%s) ENGINE=MyISAM DEFAULT CHARSET=utf8;";

        StringBuffer buf = new StringBuffer();

        int index = 0;

        for (ColumnDescriptor columnDescriptor : columnDescriptors) {
            buf.append("`" + columnDescriptor.getDatabaseColumnName() + "` " + columnDescriptor.getDataType().name() + (columnDescriptor.getLength() == 0 ? "" : "(" + columnDescriptor.getLength() + ")"));

            if (index < columnDescriptors.size() - 1) {
                buf.append(",");
            }

            index++;
        }

        sql = String.format(sql, tableName, buf.toString());

        return sql;
    }

    private static List<ColumnDescriptor> buildColumnDescriptors(String[] row) {
        List<ColumnDescriptor> columnDescriptors = new ArrayList<>();

        for (String csvColumnName : row) {
            ColumnDescriptor columnDescriptor = new ColumnDescriptor();
            columnDescriptor.setCsvColumnName(csvColumnName);
            columnDescriptor.setDatabaseColumnName(toDatabaseColumnName(csvColumnName));
            columnDescriptor.setDataType(DataType.TEXT);
//            columnDescriptor.setLength(21844);
            columnDescriptors.add(columnDescriptor);
        }

        return columnDescriptors;
    }

    private static String toDatabaseColumnName(String csvColumnName) {
        String result = csvColumnName.toLowerCase().replaceAll("\\s+", "_").replaceAll("[^a-zA-Z0-9_]", "");

        if (result.length() > 64) {
            result = result.substring(0, 63);
        }

        if (result.length() == 0) {
            result = UUID.randomUUID().toString().replace("-", "");
        }

        return result;
    }
}
