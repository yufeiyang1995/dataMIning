package dataMining;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class readFile{
	public static void readFileByLines() {
		String fileName = "F:\\课程\\大三下\\数据挖掘\\作业\\作业2\\Assignment2\\dataset\\Groceries.csv";
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                //System.out.println("line " + line + ": " + tempString);
                if(line > 1)
                	splitItem(tempString,line-1);
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }
	
	public static void splitItem(String tempString,int line){
		Integer tempLine = line;
		String[] sourceStrArray = tempString.split(",\"");
		sourceStrArray[1] = sourceStrArray[1].replace("{", "");
		sourceStrArray[1] = sourceStrArray[1].replace("}", "");
		sourceStrArray[1] = sourceStrArray[1].replace("\"", "");
		
		Set itemSet = new HashSet();
		String[] s = sourceStrArray[1].split(",");
		for(int i = 0;i < s.length;i++){
			itemSet.add(s[i]);
		}
		//System.out.println(itemSet);
		controller.transaction.put(tempLine, itemSet);
	}
}