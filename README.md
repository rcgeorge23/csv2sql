# csv2sql
Command line tool for converting a CSV file into a single SQL table

Usage: java -jar csv2sql.jar OPTIONS
Options category 'Basic':
  --[no]create [-r] (a boolean; default: "false")
    Include 'create' statement
  --csvfile [-c] (a string; default: "")
    The input CSV file
  --sqlfile [-s] (a string; default: "")
    The output SQL file
  --table [-t] (a string; default: "")
    The name of the table
