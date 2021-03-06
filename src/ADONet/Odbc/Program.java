package ADONet.Odbc;

import system.Console;
import system.data.odbc.*;

public class Program {
    public static void main(String[] args) {

        // The connection string assumes that the Access
        // Northwind.mdb is located in the c:\Data folder.
        String connectionString =
                "Driver={Microsoft Access Driver (*.mdb)};"
                        + "Dbq=c:\\Data\\Northwind.mdb;Uid=Admin;Pwd=;";

        // Provide the query string with a parameter placeholder.
        String queryString =
                "SELECT ProductID, UnitPrice, ProductName from products "
                        + "WHERE UnitPrice > ? "
                        + "ORDER BY UnitPrice DESC;";

        // Specify the parameter value.
        int paramValue = 5;

        // Create and open the connection in a using block. This
        // ensures that all resources will be closed and disposed
        // when the code exits.
        OdbcConnection connection = null;
        try {
            connection = new OdbcConnection(connectionString);
            try {
                // Create the Command and Parameter objects.
                OdbcCommand command = new OdbcCommand(queryString, connection);
                OdbcParameterCollection odbcParameters = new OdbcParameterCollection(command.get_Parameters().getBaseObject());
                odbcParameters.AddWithValue("@pricePoint", paramValue);

                // Open the connection in a try/catch block.
                // Create and execute the DataReader, writing the result
                // set to the console window.
                try {
                    connection.Open();
                    OdbcDataReader reader = new OdbcDataReader(command.ExecuteReader().getBaseObject());
                    while (reader.Read()) {
                        Console.WriteLine("\t{0}\t{1}\t{2}",
                                reader.get_Item(0), reader.get_Item(1), reader.get_Item(2));
                    }
                    reader.Close();
                } catch (Exception ex) {
                    Console.WriteLine(ex.getMessage());
                }
                Console.ReadLine();

            } finally {
                connection.Dispose();
                connection.close();
            }
        } catch (Exception error) {
            error.printStackTrace();
        }
    }
}
