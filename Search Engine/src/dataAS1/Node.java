package dataAS1;

public class Node {
	private Object fileName;
	private Node link;
	private int freqCount;

	public Node(Object inputFileName, int inputFreq) {
		fileName = inputFileName;
		link = null;
		freqCount = inputFreq;
	}

	public Object getFileName() {
		return fileName;
	}

	public void setFileName(Object inputFileName) {
		this.fileName = inputFileName;
	}

	public Node getLink() {
		return link;
	}

	public void setLink(Node link) {
		this.link = link;
	}

	public void setFreq(int count) {
		this.freqCount = count;
	}

	public int getFreq() {
		return freqCount;
	}

}
