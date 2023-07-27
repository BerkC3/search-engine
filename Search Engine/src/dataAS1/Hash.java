package dataAS1;

public class Hash {
	String strToHash;
	int hashResult;
	int hashConstant = 31;
	char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();

	public int ssf(String inputText) {
		strToHash = inputText.toLowerCase();
		int sum = 0;
		for (int i = 0; i < strToHash.length(); i++) {
			for (int j = 0; j < alphabet.length; j++) {
				if (strToHash.charAt(i) == alphabet[j]) {
					sum += j + 1;
				}
			}
		}
		hashResult = sum;

		return hashResult;
	}

	public int paf(String inputText) {
		strToHash = inputText.toLowerCase();
		int sum = 0;
		for (int i = 0; i < strToHash.length(); i++) {
			for (int j = 0; j < alphabet.length; j++) {
				if (strToHash.charAt(i) == alphabet[j]) {
					sum += (j + 1) * Math.pow(hashConstant, strToHash.length() - (i + 1));
				}
			}
		}
		hashResult = sum;

		return hashResult;
	}

	public int ssf() {
		strToHash = strToHash.toLowerCase();
		int sum = 0;
		for (int i = 0; i < strToHash.length(); i++) {
			for (int j = 0; j < alphabet.length; j++) {
				if (strToHash.charAt(i) == alphabet[j]) {
					sum += j + 1;
				}
			}
		}
		hashResult = sum;

		return hashResult;
	}

	public int paf() {
		strToHash = strToHash.toLowerCase();
		int sum = 0;
		for (int i = 0; i < strToHash.length(); i++) {
			for (int j = 0; j < alphabet.length; j++) {
				if (strToHash.charAt(i) == alphabet[j]) {
					sum += (j + 1) * Math.pow(hashConstant, strToHash.length() - (i + 1));
				}
			}
		}
		hashResult = sum;

		return hashResult;
	}

}
