package api.utilities;

import java.io.IOException;

import org.testng.annotations.DataProvider;

public class DataProviders {

	String excelFilePath = System.getProperty("user.dir") + "//testData//BookingData.xlsx";
	String sheetName = "Sheet1";
	
	@DataProvider(name="Data")
	public String[][] getData() throws IOException
	{
		ExcelUtilities xl = new ExcelUtilities(excelFilePath);
		
		int numberOfRows = xl.getRowCount(sheetName);
		int numberOfColumns = xl.getCellCount(sheetName, 1);
		
		String data[][] = new String[numberOfRows][numberOfColumns];
		
		for (int i=1; i<=numberOfRows; i++)
		{
			for (int j=0; j<numberOfColumns; j++)
			{
				data[i-1][j] = xl.getCellData(sheetName, i, j);
			}
		}
		
		return data;
	}
}
