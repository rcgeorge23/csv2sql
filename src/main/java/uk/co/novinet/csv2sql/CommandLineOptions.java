package uk.co.novinet.csv2sql;

import com.google.devtools.common.options.Option;
import com.google.devtools.common.options.OptionsBase;

public class CommandLineOptions extends OptionsBase {

    @Option(
        name = "csvfile",
        abbrev = 'c',
        help = "The input CSV file",
        category = "Basic",
        defaultValue = ""
    )
    public String csvFilename;

    @Option(
            name = "create",
            abbrev = 'r',
            help = "Include 'create' statement",
            category = "Basic",
            defaultValue = "false"
    )
    public boolean addCreateStatement;

    @Option(
            name = "table",
            abbrev = 't',
            help = "The name of the table",
            category = "Basic",
            defaultValue = ""
    )
    public String tableName;


}
