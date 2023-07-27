package dataAS1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Scanner;

public class Test {

	public static void display(HashedDictionary<Integer, String> dataBase) {
		Iterator<String> keyIterator = dataBase.getKeyIterator();
		Iterator<Integer> valueIterator = dataBase.getValueIterator();
		while (keyIterator.hasNext()) {
			System.out.println("Value: " + valueIterator.next() + " Key: " + keyIterator.next());
		}
	}

	public static void main(String[] args) throws IOException {
		Scanner sc = new Scanner(System.in);

		// Declare the hashTable dtb
		HashedDictionary<Integer, String> dtb = new HashedDictionary<Integer, String>();

		// Delimiters
		String DELIMITERS = "[-+=" + " " + "\r\n " + "1234567890" + "’'\"" + "(){}<>\\[\\]" + ":" + "," + "‒–—―" + "…" +

				"!" + "." + "«»" + "-‐" + "?" + "‘’“”" + ";" + "/" + "⁄" + "␠" + "·" + "&" + "@" + "*" + "\\" + "•" +

				"^" + "¤¢$€£¥₩₪" + "†‡" + "°" + "¡" + "¿" + "¬" + "#" + "№" + "%‰‱" + "¶" + "′" + "§" + "~" + "¨" + "_"
				+

				"|¦" + "⁂" + "☞" + "∴" + "‽" + "※" + "]";

		// Our hash function

		Hash myHashFunc = new Hash();

		int hashFuncType = -1; // 0 for SSF, 1 for PAF
		int collHandleType = -1; // 0 for LP, 1 for DH
		int searchType = -1; // 0 for search one word, 1 for search three words

		// Take hash function type from user
		do {
			System.out.println("Which hash function do you want to use? (SSF or PAF)");
			String hf = sc.nextLine();
			hf = hf.toLowerCase();
			if (hf.equals("ssf")) {
				System.out.println("Choosen hash function is Simple Summation Function (SSF).");
				dtb.setHashType(0);
				hashFuncType = 0;
			} else if (hf.equals("paf")) {
				System.out.println("Choosen hash function is Polynomial Accumulation Function (PAF).");
				hashFuncType = 1;
				dtb.setHashType(1);

			} else {
				System.out.println("Invalid hash function, please choose SSF or PAF.");
			}
			System.out.println("");
		} while (hashFuncType != 0 && hashFuncType != 1);

		// Take collision handling function type from user
		do {
			System.out.println(
					"Which collision handling method do you want to use? (LP for Lineer Probing or DH Double Hashing)");
			String cht = sc.nextLine();
			cht = cht.toLowerCase();
			if (cht.equals("lp")) {
				System.out.println("Choosen collision handling method is Lineer Probing (LP).");
				collHandleType = 0;
				dtb.setCollType(0);
			} else if (cht.equals("dh")) {
				System.out.println("Choosen collision handling method is Double Hashing (DH).");
				collHandleType = 1;
				dtb.setCollType(1);
			} else {
				System.out.println("Invalid collision handling method, please choose LP or DP.");
			}
			System.out.println("");

		} while (collHandleType != 0 && collHandleType != 1);

		// Directories
		File dir = new File("src/dataAS1/sport");
		String[] fileNames = dir.list();
		BufferedReader stpDir = new BufferedReader(new FileReader("src/dataAS1/stop_words_en.txt"));
		BufferedReader srchDir = new BufferedReader(new FileReader("src/dataAS1/search.txt"));

		// From search.txt to an array
		String searchText = "";
		String searchLine = srchDir.readLine();
		searchText += searchLine;
		while (searchLine != null) {
			searchLine = srchDir.readLine();
			if (searchLine != null) {
				searchText += searchLine.toLowerCase();
				searchText += " ";
			}
		}
		String srchwrds = searchText.replace("\n", "").replace("\r", "");
		String[] searchArr = srchwrds.split(" ");
		srchDir.close();

		// From stopwords.txt to an array without delimiter
		String stopText = "";
		String stpwrds = stopText.replace("\n", "").replace("\r", "");
		String stopwordsLine = stpDir.readLine();
		stopText += stopwordsLine;
		while (stopwordsLine != null) {
			stopwordsLine = stpDir.readLine();
			if (stopwordsLine != null) {
				stopText += stopwordsLine.toLowerCase();
				stopText += " ";
			}
		}
		String[] stpwrdsArr = stpwrds.split("  ");
		stpwrdsArr = stpwrds.split(DELIMITERS);
		stpDir.close();

		String[] clearedContentArr;

		long allIdxTime = 0;
		long allIdxStartTime = System.nanoTime();
		int temppp = 0;
		for (String fileName : fileNames) {

			File f = new File(dir, fileName);
			BufferedReader br = new BufferedReader(new FileReader(f));

			// All content from txt files to an array without delimiters
			String line = br.readLine().toLowerCase();
			String allText = line;
			while (line != null) {
				line = br.readLine();
				if (line != null) {
					allText += line.toLowerCase();
					allText += " ";
				}
			}
			String content = allText.replace("\n", "").replace("\r", "");
			String[] contentArr = content.split(" ");
			contentArr = content.split(DELIMITERS);
			String clearedContent = "";

			// Removing all stopwords from content array
			boolean isExist = false;
			for (int i = 0; i < contentArr.length; i++) {
				for (int j = 0; j < stpwrdsArr.length; j++) {
					if (contentArr[i].equals(stpwrdsArr[j])) {
						isExist = true;
						break;
					} else {
						isExist = false;
					}
				}
				if (isExist == false) {
					clearedContent += contentArr[i];
					clearedContent += " ";
				}
			}
			clearedContentArr = clearedContent.split(" ");
			long idxStartTime = System.nanoTime();

			if (hashFuncType == 0) {
				for (int i = 0; i < clearedContentArr.length; i++) {
					myHashFunc.strToHash = clearedContentArr[i];
					dtb.add(myHashFunc.ssf(), clearedContentArr[i], fileName);

				}
			} else if (hashFuncType == 1) {
				for (int i = 0; i < clearedContentArr.length; i++) {
					myHashFunc.strToHash = clearedContentArr[i];
					dtb.add(myHashFunc.paf(), clearedContentArr[i], fileName);
				}
			}
			long idxEndTime = System.nanoTime();
			allIdxTime += idxEndTime - idxStartTime;
			br.close();
		}
		long allIdxFinishTime = System.nanoTime();

		// Benchmarks
		// Remove the comment lines for search every words in search.txt file, and print
		// the benchmark results.
		System.out.println("All indexing time without file ops : " + allIdxTime);
		System.out.println("All indexing time with file ops : " + (allIdxFinishTime - allIdxStartTime));
		long min = 500000000;
		long max = 0;
		long start = 0;
		long finish = 0;
		long searchWordCounter = 0;
		long opTime = 0;
		long allOpTime = 0;
		for (int i = 0; i < searchArr.length; i++) {
			start = System.nanoTime();
			dtb.search(searchArr[i]);
			finish = System.nanoTime();
			opTime = finish - start;
			if (opTime > max) {
				max = opTime;
			}
			if (opTime < min) {
				min = opTime;
			}
			allOpTime += opTime;
			searchWordCounter++;

		}
		long avg = allOpTime / searchWordCounter;
		System.out.println("Min : " + min + " nano seconds.");
		System.out.println("Max : " + max + " nano seconds.");
		System.out.println("Avg : " + allOpTime + " / " + searchWordCounter + " = " + avg + " nano seconds.");
		System.out.println("Collision count are : " + dtb.getCollisions());

		boolean isEntryTypeTrue = false;

		// Choose a search method whether 1 word search or 3 words search
		do {
			System.out.println("Do you want to search 1 word or 3 words? (1 or 3)");
			String hf = sc.nextLine();
			String word = "";
			String[] threeWordSearchArr;
			if (hf.equals("1")) {
				System.out.println("Choosen search type is one word search. (1)");
				searchType = 0;

				// Check if the input for 1 word search method is valid
				do {
					System.out.println("\nWhat do you want to search? ");
					word = sc.nextLine();
					String[] tempWordArr = word.split(" ");
					if (tempWordArr.length == 1) {
						isEntryTypeTrue = true;
					} else {
						System.out.println("Invalid input format. Please write only one word.");
					}

				} while (!isEntryTypeTrue);

				// Display input word with specific method
				dtb.search(word).display();
			} else if (hf.equals("3")) {
				System.out.println("Choosen search type is three words search. (3)");
				searchType = 1;

				// Check if the input for 3 words search method is valid
				do {
					System.out.println("\nWhat do you want to search? ");
					threeWordSearchArr = sc.nextLine().split(" ");
					if (threeWordSearchArr.length == 3) {
						isEntryTypeTrue = true;
					} else {
						System.out.println("Invalid input format. Please write 'three' words.");
					}
				} while (!isEntryTypeTrue);

				// Send input words with specific method
				Object[][] ar = dtb.searchThreeWords(threeWordSearchArr);
				int maxFreq = 0;
				Object maxFreqFileName = "";
				for (int i = 0; i < ar.length; i++) {
					if (ar[i][0] != null) {
						if ((Integer) ar[i][1] >= maxFreq) {
							maxFreq = (Integer) ar[i][1];
							maxFreqFileName = ar[i][0];
						}
					} else {
						break;
					}
				}

				// Returns the txt file name and the frequency for given words if they exists.
				if (maxFreq != 0) {
					System.out.println(maxFreqFileName + " with " + maxFreq + " frequencies.");
				} else {
					System.out.println("Not exist!");
				}
			} else {
				System.out.println("Invalid search type, please choose valid search type.");
			}
			System.out.println("");
		} while (searchType != 0 && searchType != 1);
		sc.close();
	} // end main

}
