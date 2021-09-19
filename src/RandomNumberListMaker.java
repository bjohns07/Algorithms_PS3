import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class RandomNumberListMaker {

	public static void main(String[] args) {
		int numberOfNums = 0;
		if(args.length < 1) {
			System.out.println("No arguments received, exiting.");
			return;
		}
		
		try {
			numberOfNums = Integer.parseInt(args[0]);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		
		File outFile = new File(System.getProperty("user.dir") + "/RandomNumberListMaker-Generated.txt");
		FileWriter fr;
		try {
			fr = new FileWriter(outFile);
			BufferedWriter br = new BufferedWriter(fr);
			
			for(int i = 0; i < numberOfNums; i++) {
				int num = (int) (Math.random() * numberOfNums);
				br.write(num + "\n");
			}
			
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		

	}

}
