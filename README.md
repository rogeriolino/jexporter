jexporter
=========

Java data exporter

Usage
---------

Exporting all table data (SELECT * FROM table)

```java
    MySqlConnection conn = new MySqlConnection();
    conn.connect("localhost", 3306, "test", "root", "123456");
    TableExporter exporter = new TableExporter(conn);
    Output out = new CsvOutput("~/output.csv");
    exporter.exportAll("my_table", out);
    conn.close();
```

Custom SQL

```java
    MySqlConnection conn = new MySqlConnection();
    conn.connect("localhost", 3306, "test", "root", "123456");
    TableExporter exporter = new TableExporter(conn);
    Output out = new CsvOutput("~/output.csv");
    exporter.sql("SELECT name, email FROM my_table WHERE email LIKE '%@gmail.com'").export(out);
    conn.close();
```

Table column hydrating

```java
    MySqlConnection conn = new MySqlConnection();
    conn.connect("localhost", 3306, "test", "root", "123456");
    TableExporter exporter = new TableExporter(conn);
    exporter.setHydrator(new Hydrator() {

        @Override
        public String hydrate(String columnName) {
            // column name hidrating
            return columnName.replace("_", " ");
        }

        @Override
        public String hydrate(String columnName, String columnValue) {
            // column value hidrating
            if (columnName.equals("price")) {
                try {
                    BigDecimal decimal = new BigDecimal(columnValue);
                    NumberFormat nf = NumberFormat.getCurrencyInstance();
                    return nf.format(decimal);
                } catch (NumberFormatException e) {
                }
            }
            return columnValue;
        }

    });
    Output out = new CsvOutput("~/output.csv");
    exporter.exportAll(out);
    conn.close();
```
